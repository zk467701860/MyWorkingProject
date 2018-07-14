package cn.edu.fudan.se.DataExtractor.mybatisDao;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Component;

import cn.edu.fudan.se.DataExtractor.api.APIClass;
import cn.edu.fudan.se.DataExtractor.api.APILibrary;
import cn.edu.fudan.se.DataExtractor.api.APIMethod;
import cn.edu.fudan.se.DataExtractor.api.APIPackage;
import cn.edu.fudan.se.DataExtractor.api.APIParameter;
import cn.edu.fudan.se.DataExtractor.api.mapper.APIMapper;

@Component
public class APIDao {
	private SqlSessionFactory sqlSessionFactory = MybatisFactory.getSqlSessionFactory();

	private static class SingletonHolder { 
		private static APIDao singleton;	 
		static{
			 singleton = new APIDao();
		}
	}

	public static APIDao getInstance(){
		return SingletonHolder.singleton;
	}
	
	public APIPackage getAPIPackage(String packageName, int libraryId){
		APIPackage ret = new APIPackage();
		 try(SqlSession session = sqlSessionFactory.openSession()){
			 ret = session.getMapper(APIMapper.class).determinApiPackage(packageName, libraryId);
			 session.commit();
		 }
		return ret;
	}
	
	public APILibrary getAndroidApiByVersion(String version){
		List<APILibrary> ret = null;
		 try(SqlSession session = sqlSessionFactory.openSession()){
			 ret = session.getMapper(APIMapper.class).getAPILibrary("Android Platform", version);
			 session.commit();
		 }
		 if(ret!= null && ret.size()>0){
			 return ret.get(0);
		 }
		return null;
	}
	
	public List<APIClass> getApiByName(String apiName, int libraryId){
		
		 List<APIClass> ret = null;
		 try(SqlSession session = sqlSessionFactory.openSession()){
			 ret = session.getMapper(APIMapper.class).getApiByName(apiName, libraryId);
			 session.commit();
		 }
		
		return ret;
	}
	
	public List<APIClass> getApiByQualifiedName(String qualifiedName, int libraryId){
		
		 List<APIClass> ret = null;
		 try(SqlSession session = sqlSessionFactory.openSession()){
			 ret = session.getMapper(APIMapper.class).getApiByQualifiedName(qualifiedName, libraryId);
			 session.commit();
		 }
		
		return ret;
	}
	
	 public APIClass getClassByName_Packagename_Version(String name, String packageName, String version){
		 SqlSession session = null;  
		 APIClass apiclass = null;
		 try{
			 session = sqlSessionFactory.openSession();
			 int libraryId;
			 if(version.equals("O"))
				 libraryId = 26;
			 else
				 libraryId = Integer.parseInt(version);
			 apiclass = session.getMapper(APIMapper.class).getClassByName_Packagename_Version(name, packageName, libraryId);
			 session.commit();
		 }finally {
			session.close();
		}
		 return apiclass;
	 }
	 
	 public APIMethod getMethodsByName_Parameter_Classid(String name, String[] parameterType, int classId){
		 SqlSession session = null;  
		 APIMethod apimethod = null;
		 try{
			 session = sqlSessionFactory.openSession();
			 List<APIMethod> methods = session.getMapper(APIMapper.class).getMethodsByName_Parameter_Classid(name, classId);
			 if(methods.size() == 0)
				 return apimethod; 
outer:		 for(APIMethod method : methods){
				 List<APIParameter> parameters = session.getMapper(APIMapper.class).getParametersByMethodid(method.getId());
				 ArrayList<String> parametertypestring = new ArrayList<String>();
				 for (APIParameter parameter : parameters){
					 parametertypestring.add(parameter.getTypeString());
				 }
				 if(parameters.size() == parameterType.length){
					 if(parameters.size() == 0){
						 apimethod = method;
						 break;
					 }
					 else {
						 for (int i = 0; i < parameterType.length; i++){
								if(parametertypestring.contains(parameterType[i])){
									parametertypestring.remove(parametertypestring.indexOf(parameterType[i]));
									if(i == parameterType.length - 1)
									{
										apimethod = method;
										break outer;
									}
								}
								else
									break;
							}
					 }
				 }
			}
			 session.commit();
		 }finally {
			session.close();
		}
		 return apimethod; 
	 }
}
