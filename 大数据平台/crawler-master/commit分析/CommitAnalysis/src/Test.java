import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	public static void main(String []args) {
		Connection conn = null;
		Statement stmt;
		Statement stmt1;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://10.131.252.156:3306/fdroid?useUnicode=true&characterEncoding=utf-8", "root","root");
			stmt = conn.createStatement();
			stmt1 = conn.createStatement();
			ResultSet resultSet;
			resultSet = stmt.executeQuery("select git_release_id,src_address from gitrelease where git_release_id >6068 ");
			while(resultSet.next()){
				int releaseid = resultSet.getInt("git_release_id");
				String srcaddress = resultSet.getString("src_address");
				System.out.println(releaseid);
				
				String content = "";
				//String path = srcaddress + "/README.md";
				String path = "C:\\Users\\jameszk\\Desktop\\clear-weather-android" + "\\README.md";
				File file = new File(path);
				if(file.exists()){
					BufferedReader reader = null;  
					try {  
			            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));  
			            String tempString = null;  
			            while ((tempString = reader.readLine()) != null) {  
			            	content+= tempString.replace("\\", "\\\\").replace("\"", "\\\"")+"\n";
			            }  
			            reader.close();  
			            
			            //System.out.println(content);
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
					
					stmt1.execute("update gitrelease  set  content = \""+content+"\" where git_release_id = 6068");
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
			resultSet.close();
			stmt.close();
			stmt1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
		
			
	}
}
