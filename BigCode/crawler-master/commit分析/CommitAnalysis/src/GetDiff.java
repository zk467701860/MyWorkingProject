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

public class GetDiff {
	private Repository repo;
	private int project;
	private Git git;
	private RevWalk walk;
	Connection conn = null;
	Statement stmt;
	Statement stmt1;

	public GetDiff() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://10.131.252.160:3306/fdroid", "root","123456");
			stmt = conn.createStatement();
			stmt1 = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			ResultSet resultSet = stmt1.executeQuery("select local_address,repository_id from repository ");
			while(resultSet.next()){
				this.project = resultSet.getInt("repository_id");
				/*if(this.project<=2600){
					continue;
				}*/
				String gitRepoPath = resultSet.getString("local_address");
				try {
					/////////////////////////////////////////////
					//git = Git.open(new File(gitRepoPath));
					git = Git.open(new File("C:\\Users\\jameszk\\Desktop\\clear-weather-android"));
					repo = git.getRepository();
					walk = new RevWalk(repo);
					System.out.println(this.project);
					track();
					//////////////////////
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
				
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
		System.out.println("complete!");
		
		
		
		
	}

	// extract commits 
	public void track()
			throws RevisionSyntaxException, NoHeadException, MissingObjectException, IncorrectObjectTypeException,
			AmbiguousObjectException, GitAPIException, IOException {
		for (RevCommit commit : git.log().all().call()) {
			/////////////////////////////
			if(!"a3ce9c0e539818a08a1d00dbf5f337bcd2f5bf38".equals(commit.getName())){
				continue;
			}
			extractCommit(commit);
		}
		walk.dispose();
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

			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("aa"));
			
            DiffFormatter df = new DiffFormatter(out);  
            df.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);
            df.setRepository(git.getRepository());
            df.format(diff);
            
            //////////////////////////////////////////
            /*ByteArrayOutputStream out1 = new ByteArrayOutputStream();  
            DiffFormatter df1 = new DiffFormatter(out1);  
            df1.setRepository(git.getRepository());
            df1.format(diff);  
            String diffText = out1.toString("UTF-8");  
            System.out.println(diffText);
            System.out.println(prevTargetCommit.getName());*/
            
            FileHeader fileHeader = df.toFileHeader(diff);
            List<HunkHeader> hunks = (List<HunkHeader>) fileHeader.getHunks();
            
            out.flush();
            out.close();
            System.out.println(this.project+"    "+commitId + "    " +fileName);
            insertAllAddAndDelete(hunks,commitId,fileName,project);
            
            
            
            /*String diffText = out.toString("UTF-8");  
            String [] strings = diffText.split("\n");
            for(int i=0;i<strings.length;i++){
            	System.out.println("di "+i+"  "+strings[i]);
            }*/
            
            
            
			
            /*try {
				stmt.execute("update changefile set diff_text = \""+diffText.replace("\"", "\\\"").replace("\\\\\"", "\\\\\\\"")+"\" where commit_id =\""+commitId+"\" and repository_id = \""+project+"\" and  file_name = \""+fileName+"\"");
			} catch (SQLException e) {
				System.out.println("!!!!!!" + this.project+"    "+commitId+ "    " +fileName+"      error");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
            //System.out.println(this.project+"    "+commitId + "    " +fileName);
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
	public ArrayList<Integer> getEdit(ArrayList<Integer> range,List<HunkHeader> hunks){
		ArrayList<Integer> ret = new ArrayList<>();
		
        for(HunkHeader hunkHeader:hunks){
        	
        	EditList editList = hunkHeader.toEditList();
        	
        	for(Edit edit : editList){
    			int deleteB = edit.getBeginA();
    			int deleteE = edit.getEndA();
    			int insertB = edit.getBeginB();
    			int insertE = edit.getEndB();
        		
    			if((range.get(1)!=0&&deleteB >= range.get(0)-1&&deleteE<=range.get(0)+range.get(1)-1)||(range.get(3)!=0&&insertB >= range.get(2)-1&&insertE<=range.get(2)+range.get(3)-1)){
    				ret.add(deleteB);
    				ret.add(deleteE);
    				ret.add(insertB);
    				ret.add(insertE);
    				
    			}
        	}
        	
            
        	
        }
		
		
		
		return ret;
	}
	
	public void insertAllAddAndDelete(List<HunkHeader> hunks,String commit_id, String file_name,int repo_id){
		
        try {
        	String str;
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(new FileInputStream("aa"),"UTF-8"));

            ArrayList<Integer> range = new ArrayList<>();
            boolean go = false;
            ArrayList<Integer> edIntegers = new ArrayList<>();
			while((str = inputStream.readLine()) != null){
				System.out.println(str);
				
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
					
					if(edIntegers.size()==0||(range.get(0)>edIntegers.get(1) && range.get(2)>edIntegers.get(3))||(range.get(1)==0 && range.get(2)>edIntegers.get(3))||(range.get(3)==0 && range.get(0)>edIntegers.get(1))){
						edIntegers = getEdit(range, hunks);
						//System.out.println("edit  "+edIntegers.toString());
						if(edIntegers.size()==0){
							go = true;
						}
					}
					if(go || (range.get(0)<edIntegers.get(0)+1&&range.get(2)<edIntegers.get(2)+1)){
						if(range.get(1)!=0){
							range.set(0, range.get(0)+1);
							range.set(1, range.get(1)-1);
						}
						if(range.get(3)!=0){
							range.set(2, range.get(2)+1);
							range.set(3, range.get(3)-1);
						}
					}else{
						if(edIntegers.get(0)==edIntegers.get(1)&&edIntegers.get(2)==edIntegers.get(3)){
							
							
							
						}else {
							if("+".equals(str.substring(0, 1))){
								try {
									stmt.execute("insert into change_line (repository_id,commit_id,file_name,line_number,content,type) values ( \""+repo_id+"\",\""+commit_id+"\",\""+file_name +"\",\""+(edIntegers.get(2)+1) +"\",\""+str.replace("\\", "\\\\").replace("\"", "\\\"") +"\",\""+"ADD" +"\")");
								} catch (SQLException e) {
									
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								System.out.println("add  "+(edIntegers.get(2)+1)+"        "+str.replace("\\", "\\\\").replace("\"", "\\\""));
								edIntegers.set(2, edIntegers.get(2)+1);
								range.set(2, range.get(2)+1);
								range.set(3, range.get(3)-1);
								
							}else if("-".equals(str.substring(0, 1))){
								try {
									stmt.execute("insert into change_line (repository_id,commit_id,file_name,line_number,content,type) values ( \""+repo_id+"\",\""+commit_id+"\",\""+file_name +"\",\""+(edIntegers.get(0)+1) +"\",\""+str.replace("\\", "\\\\").replace("\"", "\\\"") +"\",\""+"DELETE" +"\")");
								} catch (SQLException e) {
									
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								System.out.println("delete  "+(edIntegers.get(0)+1)+"     "+str.replace("\\", "\\\\").replace("\"", "\\\""));
								edIntegers.set(0, edIntegers.get(0)+1);
								range.set(0, range.get(0)+1);
								range.set(1, range.get(1)-1);
							}
							else{
								System.out.println(str);
							}
							
						}
						
					}
					
					
				}
				
			}
			inputStream.close();
			File file = new File("aa");
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
		GetDiff extractor = new GetDiff();
		System.out.println("All Finished!!");
	}
}
