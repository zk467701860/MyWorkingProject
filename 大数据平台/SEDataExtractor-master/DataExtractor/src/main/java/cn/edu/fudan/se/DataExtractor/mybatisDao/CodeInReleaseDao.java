package cn.edu.fudan.se.DataExtractor.mybatisDao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Component;

import cn.edu.fudan.se.DataExtractor.codeAnalyze.CodeField;
import cn.edu.fudan.se.DataExtractor.codeAnalyze.CodeImport;
import cn.edu.fudan.se.DataExtractor.codeAnalyze.CodeMethod;
import cn.edu.fudan.se.DataExtractor.codeAnalyze.CodeParameter;
import cn.edu.fudan.se.DataExtractor.codeAnalyze.mapper.CodeClassMapper;
import cn.edu.fudan.se.DataExtractor.codeAnalyze.mapper.CodeFieldMapper;
import cn.edu.fudan.se.DataExtractor.codeAnalyze.mapper.CodeImportMapper;
import cn.edu.fudan.se.DataExtractor.codeAnalyze.mapper.CodeMethodMapper;
import cn.edu.fudan.se.DataExtractor.codeAnalyze.mapper.CodeParameterMapper;
import cn.edu.fudan.se.DataExtractor.repository.ClassInPackage;
import cn.edu.fudan.se.DataExtractor.repository.FullClass;
import cn.edu.fudan.se.DataExtractor.repository.SimpleClass;
import cn.edu.fudan.se.DataExtractor.repository.mapper.GitreleaseMapper;

@Component
public class CodeInReleaseDao {
	private SqlSessionFactory sqlSessionFactory = MybatisFactory.getSqlSessionFactory();
	
	public List<SimpleClass> selectClassInPackage(int releaseId, String packageName){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		List<SimpleClass> list = null;
		try{
			list = sqlSession.getMapper(GitreleaseMapper.class).selectClassInPackage(releaseId, packageName);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}
		return list;
	}
	
	public ClassInPackage selectClassPartInPackage(int classId){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		ClassInPackage r = null;
		try{
			r = sqlSession.getMapper(GitreleaseMapper.class).selectClassPartInPackage(classId);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}
		return r;
	}
	
	public List<CodeMethod> selectMethodInClass(int classId){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		List<CodeMethod> list = null;
		try{
			list = sqlSession.getMapper(CodeMethodMapper.class).selectMethodInClass(classId);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}
		return list;
	}
	
	public List<CodeImport> selectImportInClass(int classId){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		List<CodeImport> list = null;
		try{
			list = sqlSession.getMapper(CodeImportMapper.class).selectImportInClass(classId);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}
		return list;
	}
	
	public List<CodeField> selectFieldInClass(int classId){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		List<CodeField> list = null;
		try{
			list = sqlSession.getMapper(CodeFieldMapper.class).selectFieldInClass(classId);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}
		return list;
	}
	
	public List<CodeParameter> selectParameterInMethod(int methodId){
		List<CodeParameter> ret = null;
		try(SqlSession session = sqlSessionFactory.openSession()){
			ret = session.getMapper(CodeParameterMapper.class).selectParameterByMethodId(methodId);
		}
		
		return ret;
	}
	
	public FullClass selectOneClass(int classId){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		FullClass result = null;
		try{
			result = sqlSession.getMapper(CodeClassMapper.class).selectOneClass(classId);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}
		return result;
	}
}
