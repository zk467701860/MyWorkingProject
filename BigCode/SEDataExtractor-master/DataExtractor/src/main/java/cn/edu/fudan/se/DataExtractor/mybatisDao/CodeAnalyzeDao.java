package cn.edu.fudan.se.DataExtractor.mybatisDao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Component;

import cn.edu.fudan.se.DataExtractor.codeAnalyze.CodeClass;
import cn.edu.fudan.se.DataExtractor.codeAnalyze.CodeField;
import cn.edu.fudan.se.DataExtractor.codeAnalyze.CodeImport;
import cn.edu.fudan.se.DataExtractor.codeAnalyze.CodeMethod;
import cn.edu.fudan.se.DataExtractor.codeAnalyze.CodeParameter;
import cn.edu.fudan.se.DataExtractor.codeAnalyze.mapper.CodeClassMapper;
import cn.edu.fudan.se.DataExtractor.codeAnalyze.mapper.CodeMethodMapper;
@Component
public class CodeAnalyzeDao {
	
	private SqlSessionFactory sqlSessionFactory = MybatisFactory.getSqlSessionFactory();
	private static class SingletonHolder {  
		private static CodeAnalyzeDao singleton;
		static {
			singleton = new CodeAnalyzeDao();
		}
	}

	 
	 public static CodeAnalyzeDao getInstance(){
		 return SingletonHolder.singleton;
	 }
	 
	 public CodeClass getOldInnerClassByQualifiedName(int releaseId, String packageName, String className){
		 SqlSession session = null;  
		 CodeClass c = null;
		 try{
			 session = sqlSessionFactory.openSession();
			 List<CodeClass> list = session.getMapper(CodeClassMapper.class).getOldInnerClassByQualifiedName(releaseId, packageName, className+".java");
			 if(!list.isEmpty()){
				 c = list.get(0);
			 }
			 session.commit();
		 }finally {
			session.close();
		}
		 return c;
	 }
	 
	 public CodeClass determinOldInnerClassByPackage(int releaseId, String packageName){
		 CodeClass ret = null;
		 try(SqlSession session = sqlSessionFactory.openSession()){
			 ret = session.getMapper(CodeClassMapper.class).getOldInnerClassByPackageName(releaseId, packageName);
			 session.commit();
		 }
		 
		 return ret;
	 }
	 
	 public List<CodeMethod> getOldMethod(int classId, String methodName){
		 List<CodeMethod> ret = null;
		 try(SqlSession session = sqlSessionFactory.openSession()){
			 ret = session.getMapper(CodeMethodMapper.class).getOldMethod(classId, methodName);
			 session.commit();
		 }
		 return ret;
	 }
	 
	 public void insertNewMethod(List<CodeMethod> methodList){
		 for(CodeMethod method: methodList){
			 insertNewMethod(method);
		 }
	 }
	 
	 public void insertNewMethod(CodeMethod method){
		 try(SqlSession session = sqlSessionFactory.openSession()){
			 session.insert("cn.edu.fudan.se.DataExtractor.codeAnalyze.mapper."
			 		+ "CodeMethodMapper.insertNewMethod", method);
			 session.commit();
		 }
	 }
	 
	 public void insertField(CodeField field){
		 try(SqlSession session = sqlSessionFactory.openSession()){
			 session.insert("cn.edu.fudan.se.DataExtractor."
			 		+ "codeAnalyze.mapper.CodeFieldMapper.insertField", field);
			 session.commit();
		 }
	 }
	 
	 public void insertField(List<CodeField> fieldList){
		 try(SqlSession session = sqlSessionFactory.openSession()){
			 session.insert("cn.edu.fudan.se.DataExtractor.codeAnalyze.mapper."
			 		+ "CodeFieldMapper.insertFieldList", fieldList);
			 session.commit();
		 }	 
	 }
	 
	 public void insertParameter(CodeParameter parameter){
		 try(SqlSession session = sqlSessionFactory.openSession()){
			 session.insert("cn.edu.fudan.se.DataExtractor.codeAnalyze.mapper.CodeParameterMapper."
			 		+ "insertParameter", parameter);
			 session.commit();
		 }
	 }
	 public void insertParameters(List<CodeParameter> parameters){
		 try(SqlSession session = sqlSessionFactory.openSession()){
			 session.insert("cn.edu.fudan.se.DataExtractor.codeAnalyze.mapper."
			 		+ "CodeParameterMapper.insertParameters", parameters);
			 session.commit();
		 }
		 
	 }
	 public void insertImportNode(CodeImport codeImport){
		 try(SqlSession session = sqlSessionFactory.openSession()){
			 session.insert("cn.edu.fudan.se.DataExtractor.codeAnalyze.mapper."
			 		+ "CodeImportMapper.insertImportNode", codeImport);
			 session.commit();
		 }		 
	 }
	 
	 public void insertImportNodeList(List<CodeImport> codeImportList){
		 try(SqlSession session = sqlSessionFactory.openSession()){
			 session.insert("cn.edu.fudan.se.DataExtractor.codeAnalyze.mapper."
			 		+ "CodeImportMapper.insertImportNodeList", codeImportList);
			 session.commit();
		 }		
	 }
	 
	 public void insertCodeClass(CodeClass codeClass){
		 try(SqlSession session = sqlSessionFactory.openSession()){
			 session.insert("cn.edu.fudan.se.DataExtractor.codeAnalyze.mapper."
			 		+ "CodeClassMapper.insertOldCodeClass", codeClass);
			 session.commit();
		 }	
	 }
	 
	 public long countClass(){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		long count = 0;
		try {
			count = sqlSession.getMapper(CodeClassMapper.class).countClass();
			sqlSession.commit();
		} finally {
			sqlSession.close();
		}
		return count;
	 }
	 public long countParameter(){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		long count = 0;
		try {
			count = sqlSession.getMapper(CodeClassMapper.class).countParameter();
			sqlSession.commit();
		} finally {
			sqlSession.close();
		}
		return count;
	 }
	 public long countMethod(){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		long count = 0;
		try {
			count = sqlSession.getMapper(CodeClassMapper.class).countMethod();
			sqlSession.commit();
		} finally {
			sqlSession.close();
		}
		return count;
	 }
	 public long countVariable(){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		long count = 0;
		try {
			count = sqlSession.getMapper(CodeClassMapper.class).countVariable();
			sqlSession.commit();
		} finally {
			sqlSession.close();
		}
		return count;
	 }
}
