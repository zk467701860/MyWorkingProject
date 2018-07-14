package cn.edu.fudan.se.DataExtractor.mybatisDao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import cn.edu.fudan.se.DataExtractor.codeAnalyze.CodeClass;
import cn.edu.fudan.se.DataExtractor.codeAnalyze.CodeField;
import cn.edu.fudan.se.DataExtractor.codeAnalyze.CodeMethod;
import cn.edu.fudan.se.DataExtractor.codeAnalyze.CodeParameter;
import cn.edu.fudan.se.DataExtractor.codeAnalyze.mapper.CodeClassMapper;
import cn.edu.fudan.se.DataExtractor.codeAnalyze.mapper.CodeFieldMapper;
import cn.edu.fudan.se.DataExtractor.codeAnalyze.mapper.CodeImportMapper;
import cn.edu.fudan.se.DataExtractor.codeAnalyze.mapper.CodeMethodMapper;
import cn.edu.fudan.se.DataExtractor.codeAnalyze.mapper.CodeParameterMapper;
import cn.edu.fudan.se.DataExtractor.repository.Gitrelease;
import cn.edu.fudan.se.DataExtractor.repository.mapper.GitreleaseMapper;

public class DeleteDao {
	private SqlSessionFactory sqlSessionFactory = MybatisFactory.getSqlSessionFactory();
	
	public List<CodeClass> getAllClassInRelease(int releaseId){
		List<CodeClass> ret = null;
		try(SqlSession session = sqlSessionFactory.openSession()){
			 ret = session.getMapper(CodeClassMapper.class).getAllClassInRelease(releaseId);
		}
		return ret;
	}
	
	public void deleteMethod(int releaseId){
		try(SqlSession session = sqlSessionFactory.openSession()){
			
			session.getMapper(CodeMethodMapper.class).deleteMethod(releaseId);
			session.commit();
		}
	}
	
	public void deleteField(int releaseId){
		try(SqlSession session = sqlSessionFactory.openSession()){
			session.getMapper(CodeFieldMapper.class).deleteField(releaseId);
			session.commit();
		}
	}
	
	public void deleteImport(int releaseId){
		try(SqlSession session = sqlSessionFactory.openSession()){
			session.delete("deleteImport", releaseId);
			session.commit();
		}
	}
	
	public void deleteParameter(int methodId){
		try(SqlSession session = sqlSessionFactory.openSession()){
			session.getMapper(CodeParameterMapper.class).deleteParameter(methodId);
		}
	}
	
	public List<Gitrelease> getAll(){
		List<Gitrelease> ret = null;
		try(SqlSession session = sqlSessionFactory.openSession()){
			ret = session.getMapper(GitreleaseMapper.class).getAllUnexpectedRelease();
		}
		
		return ret;
	}
}
