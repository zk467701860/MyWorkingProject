import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class FillEmptyRelease {

	Connection conn = null;
	Statement stmt;
	Statement stmt1;
	
	public FillEmptyRelease() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://10.131.252.156:3306/fdroid?useUnicode=true&characterEncoding=utf-8", "root","root");
			stmt = conn.createStatement();
			stmt1 = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			//System.out.println("start");
			ResultSet resultSet = stmt.executeQuery("select git_release_id,src_address,repository_id from gitrelease where git_release_id >6448 ");
			while(resultSet.next()){
				int releaseid = resultSet.getInt("git_release_id");
				String srcaddress = resultSet.getString("src_address");
				int repoid = resultSet.getInt("repository_id"); 
				
				String commit_id = "";
				Timestamp createtime = new Timestamp(0);
				//---------get latest commit of repository
				System.out.println("commit");
				ResultSet resultSet1 = stmt1.executeQuery("select commit_id,commit_date  from gitcommit where commit_date in (select MAX(commit_date) as commit_date  from gitcommit where repository_id = "+repoid+")");
				while(resultSet1.next()){
					Timestamp timestamp = resultSet1.getTimestamp("commit_date");
					String c = resultSet1.getString("commit_id");
					if(c !=null){
						commit_id = c;
					}
					if(timestamp!=null){
						createtime = timestamp;
					}
					
				}
				resultSet1.close();
				
				//System.out.println("file");
				//-------get readmecontent
				String content = "";
				////////////////////////////
				String path = srcaddress + "/README.md";
				//String path = "D:\\test\\Adonai\\Man-Man" + "\\README.md";
				File file = new File(path);
				if(file.exists()){
					BufferedReader reader = null;  
					try {  
			            reader = new BufferedReader(new FileReader(file));  
			            String tempString = null;  
			            while ((tempString = reader.readLine()) != null) {  
			            	content+= tempString;
			            }  
			            reader.close();  
			        } catch (IOException e) {  
			            e.printStackTrace();  
			        } finally {  
			            if (reader != null) {  
			                try {  
			                    reader.close();  
			                } catch (IOException e1) {  
			                }  
			            }  
			        }  
				}
				try {
					content = new String(content.getBytes(),"UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
				
				//-----------fill the release
				///////////////////////
				//System.out.println("database");
				/*try {
					stmt1.execute("update gitrelease set tag = 'latest',created_at = \""+createtime+"\",content = \""+content.replace("\\", "\\\\").replace("\"", "\\\"")+"\",release_commit_id = \""+commit_id+"\" where git_release_id = "+releaseid);
				} catch (Exception e) {
					stmt1.execute("update gitrelease set tag = 'latest',created_at = \""+createtime+"\",content = \""+""+"\",release_commit_id = \""+commit_id+"\" where git_release_id = "+releaseid);
					e.printStackTrace();
				}*/
				System.out.println(repoid+"   "+commit_id+"   "+createtime);
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
	public static void main(String [] args){
		FillEmptyRelease fillEmptyRelease = new FillEmptyRelease();
	}
	
}
