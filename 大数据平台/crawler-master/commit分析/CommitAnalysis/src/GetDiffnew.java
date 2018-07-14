import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.patch.HunkHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;

import redis.clients.jedis.Jedis;

public class GetDiffnew {
	private Repository repo;
	private int project;
	private Git git;
	private RevWalk walk;
	Connection conn = null;
	Statement stmt;
	Statement stmt1;
	Jedis jedis;
	String fName = "";
	public GetDiffnew() {
		jedis = new Jedis("10.131.252.156");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://10.131.252.160:3306/fdroid?useUnicode=true&characterEncoding=UTF-8", "root","123456");
			stmt = conn.createStatement();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			
			while(jedis.llen("d_address")!= 0){
				this.project = Integer.valueOf(jedis.rpop("d_id"));
				
				String gitRepoPath = jedis.rpop("d_address");
				try {
					
					git = Git.open(new File(gitRepoPath));
					//git = Git.open(new File("C:\\Users\\jameszk\\Desktop\\clear-weather-android"));
					repo = git.getRepository();
					walk = new RevWalk(repo);
					System.out.println(this.project);
					track();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
			conn.close();
			stmt.close();
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
			try {
				conn.close();
				stmt.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		System.out.println("complete!");
		
		
		
		
	}

	// extract commits 
	public void track()
			throws RevisionSyntaxException, NoHeadException, MissingObjectException, IncorrectObjectTypeException,
			AmbiguousObjectException, GitAPIException, IOException {
		for (RevCommit commit : git.log().all().call()) {
			/////////////////////////////
			/*if(!"a3ce9c0e539818a08a1d00dbf5f337bcd2f5bf38".equals(commit.getName())){
				continue;
			}*/
			extractCommit(commit);
		}
		walk.dispose();
	}
	
	// read data from mysql to redis
	public void mysqlToRedis(){
		Jedis jedis = new Jedis("10.131.252.156");
		System.out.println("服务正在运行: "+jedis.ping());
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://10.131.252.156:3306/fdroid?useUnicode=true&characterEncoding=UTF-8", "root","root");
			stmt = conn.createStatement();
			stmt1 = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			ResultSet resultSet = stmt1.executeQuery("select local_address,repository_id from repository ");
			while(resultSet.next()){
				this.project = resultSet.getInt("repository_id");
				String gitRepoPath = resultSet.getString("local_address");
				jedis.lpush("d_address", gitRepoPath);
				jedis.lpush("d_id", String.valueOf(this.project));
				
			}
			resultSet.close();
			conn.close();
			stmt.close();
			stmt1.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
			try {
				conn.close();
				stmt.close();
				stmt1.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	// extract commits and get the information
	public void extractCommit(RevCommit commit)
			throws RevisionSyntaxException, MissingObjectException, IncorrectObjectTypeException,
			AmbiguousObjectException, IOException, GitAPIException {
		String commitId = null;
		if (commit == null || (commitId = commit.getName()) == null) {
			return;
		}

		TreeWalk treeWalk = new TreeWalk(repo);
		RevTree tree = commit.getTree();
		treeWalk.addTree(tree);
		treeWalk.setRecursive(true);


		
		RevCommit prevCommit = null;
		if (commit.getParentCount() > 0) {//--------one parent can get diff
			if(commit.getParentCount() == 1){
				prevCommit = commit.getParent(0);
				
				
				extractChange(commit, prevCommit, commitId);
				
			}
			
		}
	}

	// extract change files
	public void extractChange(RevCommit commit, RevCommit prevTargetCommit, String commitId)
			throws IncorrectObjectTypeException, IOException, GitAPIException {
		AbstractTreeIterator currentTreeParser = prepareTreeParser(commit.getName());
		AbstractTreeIterator prevTreeParser = prepareTreeParser(prevTargetCommit.getName());
		List<DiffEntry> diffs = git.diff().setNewTree(currentTreeParser).setOldTree(prevTreeParser).call();
		
		//-------------------get diff original text
		for (DiffEntry diff : diffs) {
			String newPath = diff.getNewPath();
			String oldPath = diff.getOldPath();
			String fileName = null;
			if (ChangeType.DELETE.name().equals((diff.getChangeType().name()))) {
				fileName = oldPath;
			} else if (newPath != null) {
				fileName = newPath;
			}
			if (fileName == null) {
				continue;
			}
			/*//////////////////////////////////
			if(!"app/src/main/assets/heweather-cn-city-list.json".equals(fileName)){
				continue;
			}*/
			fName = "a/aa"+(new Random()).nextInt(1000);
			while(new File(fName).exists()){
				fName = "a/aa"+(new Random()).nextInt(1000);
			}
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(fName));
			
            DiffFormatter df = new DiffFormatter(out);  
            df.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);
            df.setRepository(git.getRepository());
            df.format(diff);
            
           
            
            
            out.flush();
            out.close();
            
            //System.out.println(this.project+"    "+commitId + "    " +fileName);
            insertAllAddAndDelete(commitId,fileName,project);
            
            
          
		}
		
	}
	
	public ArrayList<Integer> getRange(String line){
		ArrayList<Integer> ret = new ArrayList<>();
		if(line.length()>2&&"@@".equals(line.substring(0, 2))){
			String pattern = "\\d+";
			Pattern rPattern = Pattern.compile(pattern);
			Matcher matcher = rPattern.matcher(line);
			while(matcher.find()){
				ret.add(Integer.valueOf(matcher.group(0)));
			}
		}
		return ret;
		
	}
	
	
	public void insertAllAddAndDelete(String commit_id, String file_name,int repo_id){
		
        try {
        	String str;
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(new FileInputStream(fName),"UTF-8"));

            ArrayList<Integer> range = new ArrayList<>();
            boolean go = false;
            
			while((str = inputStream.readLine()) != null){
				//System.out.println(str);
				
				ArrayList<Integer> tempRange = getRange(str);
				if(tempRange.size()>0){
					go = false;
					range.clear();
					range.addAll(tempRange);
					//System.out.println("begin  "+tempRange.toString());
				}else {
					if(range.size()==0){
						continue;
					}
					
					if(range.get(1)==0&&range.get(3)==0){
						
						
						go = true;
						
					}
					if(go){
						
					}else{
						
						if("+".equals(str.substring(0, 1))){
							try {
								stmt.execute("insert into change_line (repository_id,commit_id,file_name,line_number,content,type) values ( \""+repo_id+"\",\""+commit_id+"\",\""+file_name +"\",\""+range.get(2) +"\",\""+str.replace("\\", "\\\\").replace("\"", "\\\"") +"\",\""+"ADD" +"\")");
							} catch (SQLException e) {
								System.out.println("insert into change_line (repository_id,commit_id,file_name,line_number,content,type) values ( \""+repo_id+"\",\""+commit_id+"\",\""+file_name +"\",\""+range.get(2) +"\",\""+str.replace("\\", "\\\\").replace("\"", "\\\"") +"\",\""+"ADD" +"\")");
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							//System.out.println("add  "+range.get(2)+"        "+str.replace("\\", "\\\\").replace("\"", "\\\""));

							range.set(2, range.get(2)+1);
							range.set(3, range.get(3)-1);
							
						}else if("-".equals(str.substring(0, 1))){
							try {
								stmt.execute("insert into change_line (repository_id,commit_id,file_name,line_number,content,type) values ( \""+repo_id+"\",\""+commit_id+"\",\""+file_name +"\",\""+range.get(0) +"\",\""+str.replace("\\", "\\\\").replace("\"", "\\\"") +"\",\""+"DELETE" +"\")");
							} catch (SQLException e) {
								System.out.println("insert into change_line (repository_id,commit_id,file_name,line_number,content,type) values ( \""+repo_id+"\",\""+commit_id+"\",\""+file_name +"\",\""+range.get(0) +"\",\""+str.replace("\\", "\\\\").replace("\"", "\\\"") +"\",\""+"DELETE" +"\")");
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							//System.out.println("delete  "+range.get(0)+"     "+str.replace("\\", "\\\\").replace("\"", "\\\""));

							range.set(0, range.get(0)+1);
							range.set(1, range.get(1)-1);
						}
						else{
							if(range.get(1)!=0){
								range.set(0, range.get(0)+1);
								range.set(1, range.get(1)-1);
							}
							if(range.get(3)!=0){
								range.set(2, range.get(2)+1);
								range.set(3, range.get(3)-1);
							}
						}
						
					}
					
					
				}
				
			}
			inputStream.close();
			File file = new File(fName);
			file.delete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}

	public CanonicalTreeParser prepareTreeParser(String objectId)
			throws IOException, MissingObjectException, IncorrectObjectTypeException {
		// from the commit we can build the tree which allows us to construct
		// the TreeParser
		RevWalk walk = new RevWalk(repo);
		RevCommit commit = walk.parseCommit(ObjectId.fromString(objectId));
		RevTree tree = walk.parseTree(commit.getTree().getId());

		CanonicalTreeParser treeParser = new CanonicalTreeParser();
		ObjectReader oldReader = repo.newObjectReader();
		treeParser.reset(oldReader, tree.getId());
		walk.dispose();

		return treeParser;
	}
	public static void main(String[] args){
		GetDiffnew extractor = new GetDiffnew();
		//extractor.mysqlToRedis();
		System.out.println("All Finished!!");
	}
}
