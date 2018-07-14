

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.jws.Oneway;
import javax.lang.model.type.MirroredTypeException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;


public class GitExtractor {
	private Repository repo;
	private int project;
	private Git git;
	private RevWalk walk;
	Connection conn = null;
	Statement stmt;
	Statement stmt1;

	public GitExtractor() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://10.131.252.156:3306/fdroid", "root","root");
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
				try {
					git = Git.open(new File(gitRepoPath));
					repo = git.getRepository();
					walk = new RevWalk(repo);
					
					track();
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
		}
		
		
		
	}

	// extract commits 
	public void track()
			throws RevisionSyntaxException, NoHeadException, MissingObjectException, IncorrectObjectTypeException,
			AmbiguousObjectException, GitAPIException, IOException {
		for (RevCommit commit : git.log().all().call()) {
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

		long time = commit.getCommitTime();
		time = time * 1000;
		Timestamp commitTime = new Timestamp(time);

		String author = commit.getAuthorIdent().getName().replace("\"", "\\\"");
		String committer = commit.getCommitterIdent().getName().replace("\"", "\\\"");
		String message = commit.getFullMessage().replace("\"", "\\\"").replace("\\\\\"", "\\\\\\\"");//------------有些  \"无法处理，会报错

		
		RevCommit prevCommit = null;
		if (commit.getParentCount() > 0) {//--------one parent can get diff
			if(commit.getParentCount() == 1){
				if (   project == 1058 ||  project == 1139    ||  project == 1999  ||  project == 2309  ||  project ==  2554 ||  project ==   2990) {
					prevCommit = commit.getParent(0);
					
					String sql = "";
			
					try {
						
						ResultSet resultSet1 = stmt.executeQuery("select * from gitcommit where commit_id =\""+commitId+"\" and repository_id = \""+project+"\"");
						if(resultSet1.next()){
							sql = "update gitcommit set commit_date = \""+commitTime+ "\", committer = \""+committer+"\" where commit_id =\""+commitId+"\" and repository_id = \""+project+"\"";
						}else{
							sql = "insert into gitcommit (commit_id,repository_id,commit_date,author_name,message,committer)values(\"" + commitId + "\",\"" + project + "\",\""
									+ commitTime + "\",\"" + author +  "\",\""  + message+ "\",\""+ committer
									+   "\")";
						}
						resultSet1.close();
						//System.out.println(sql);
						stmt.execute(sql);
						System.out.println(this.project);
					} catch (Exception e) {
						System.out.println(sql);
						e.printStackTrace();
					}
				}
				
				
				/*try {
					sql = "insert into commitparent (commit_id,repository_id,parent_id)values(\"" + commitId + "\",\"" + project + "\",\""
							+ prevCommit.getName()
							+   "\")";
					//System.out.println(sql);
					stmt.execute(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				extractChange(commit, prevCommit, commitId);
				*/
			}
			else{//-------more than One parent
				
				String sql = "";
				
				try {
					
					ResultSet resultSet1 = stmt.executeQuery("select * from gitcommit where commit_id =\""+commitId+"\" and repository_id = \""+project+"\"");
					if(resultSet1.next()){
						sql = "update gitcommit set commit_date = \""+commitTime+ "\", committer = \""+committer+"\" where commit_id =\""+commitId+"\" and repository_id = \""+project+"\"";
					}else{
						sql = "insert into gitcommit (commit_id,repository_id,commit_date,author_name,message,committer)values(\"" + commitId + "\",\"" + project + "\",\""
								+ commitTime + "\",\"" + author +  "\",\""  + message+ "\",\""+ committer
								+   "\")";
					}
					resultSet1.close();
					//System.out.println(sql);
					stmt.execute(sql);
					System.out.println(this.project);
				} catch (Exception e) {
					System.out.println(sql);
					e.printStackTrace();
				}
				
				/*
				for(RevCommit revCommit : commit.getParents()){
					try {
						
						String sql = "insert into commitparent (commit_id,repository_id,parent_id)values(\"" + commitId + "\",\"" + project + "\",\""
								+ revCommit.getName()
								+   "\")";
						//System.out.println(sql);
						stmt.execute(sql);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}*/
				
			}
			
		}else{
			String sql = "";
			
			try {
				
				ResultSet resultSet1 = stmt.executeQuery("select * from gitcommit where commit_id =\""+commitId+"\" and repository_id = \""+project+"\"");
				if(resultSet1.next()){
					sql = "update gitcommit set commit_date = \""+commitTime+ "\", committer = \""+committer+"\" where commit_id =\""+commitId+"\" and repository_id = \""+project+"\"";
				}else{
					sql = "insert into gitcommit (commit_id,repository_id,commit_date,author_name,message,committer)values(\"" + commitId + "\",\"" + project + "\",\""
							+ commitTime + "\",\"" + author +  "\",\""  + message+ "\",\""+ committer
							+   "\")";
				}
				resultSet1.close();
				//System.out.println(sql);
				stmt.execute(sql);
				System.out.println(this.project);
			} catch (Exception e) {
				System.out.println(sql);
				e.printStackTrace();
			}
		}
	}

	// extract change files
	public void extractChange(RevCommit commit, RevCommit prevTargetCommit, String commitId)
			throws IncorrectObjectTypeException, IOException, GitAPIException {
		AbstractTreeIterator currentTreeParser = prepareTreeParser(commit.getName());
		AbstractTreeIterator prevTreeParser = prepareTreeParser(prevTargetCommit.getName());
		List<DiffEntry> diffs = git.diff().setNewTree(currentTreeParser).setOldTree(prevTreeParser).call();
		List<String> sqls = new ArrayList<String>();
		//System.out.println(diffs.toString());
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


			sqls.add("insert into changefile (repository_id,commit_id,file_name,type,new_path,old_path) values(\"" + project + "\",\"" + commitId + "\",\"" + fileName
						+ "\",\"" + diff.getChangeType().name() + "\",\"" + newPath + "\",\""
						+ oldPath  + "\")");
			
		}
		for (String sql : sqls) {
			try {
				stmt.execute(sql);
			} catch (Exception e) {
				System.out.println(sql);
				e.printStackTrace();
			}
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
		GitExtractor extractor = new GitExtractor();
		System.out.println("All Finished!!");
	}

}
