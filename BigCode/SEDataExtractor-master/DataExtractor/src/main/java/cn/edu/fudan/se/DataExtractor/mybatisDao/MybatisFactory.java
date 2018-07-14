package cn.edu.fudan.se.DataExtractor.mybatisDao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MybatisFactory {
	
	private static class SingletonHolder {  
		private static SqlSessionFactory sqlSessionFactory;
		
		static {
			String resource = "resources/configuration.xml";
			try(InputStream inputStream = Resources.getResourceAsStream(resource)){
				if(sqlSessionFactory == null){
					sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
				}
			}catch (FileNotFoundException fileNotFoundException) {
	            fileNotFoundException.printStackTrace();
	        }
	        catch (IOException iOException) {
	            iOException.printStackTrace();
	        }
		}
	}
	

	
	public static SqlSessionFactory getSqlSessionFactory(){
		return SingletonHolder.sqlSessionFactory;
	}
}
