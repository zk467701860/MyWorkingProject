package cn.edu.fudan.se.DataExtractor.mybatisDao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Component;

import cn.edu.fudan.se.DataExtractor.repository.Gitrelease;
import cn.edu.fudan.se.DataExtractor.repository.SimpleClass;
import cn.edu.fudan.se.DataExtractor.repository.SimplePackage;
import cn.edu.fudan.se.DataExtractor.repository.SimpleRelease;
import cn.edu.fudan.se.DataExtractor.repository.mapper.GitreleaseMapper;
@Component
public class GitreleaseDao {
	private SqlSessionFactory sqlSessionFactory = MybatisFactory.getSqlSessionFactory();
	
	private static class SingletonHolder {  
		private static GitreleaseDao gitReleaseDao = new GitreleaseDao();
	}
	
	public static GitreleaseDao getInstance(){
		return SingletonHolder.gitReleaseDao;
	}
	
	
    public long countAll(){
    	long count = 0;
    	SqlSession sqlSession = sqlSessionFactory.openSession();
		try{
			count = sqlSession.getMapper(GitreleaseMapper.class).countAll();
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
		return count;
    }
    public long countllInRepository(int repositoryId){
    	long count = 0;
    	SqlSession sqlSession = sqlSessionFactory.openSession();
		try{
			count = sqlSession.getMapper(GitreleaseMapper.class).countAllInRepository(repositoryId);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
		return count;
    }
    
    public Gitrelease selectById(int id){
    	SqlSession sqlSession = sqlSessionFactory.openSession();
    	Gitrelease r = null;
		try{
			r = sqlSession.getMapper(GitreleaseMapper.class).selectByPrimaryKey(id);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
		return r;
    }
    public List<Gitrelease> selectAllInRepository(int repositoryId){
    	SqlSession sqlSession = sqlSessionFactory.openSession();
    	List<Gitrelease> rList = null;
		try{
			rList = sqlSession.getMapper(GitreleaseMapper.class).selectAllRelease(repositoryId);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}
		return rList;
    }
    public Gitrelease getLatestRelease(int repositoryId){
    	SqlSession sqlSession = sqlSessionFactory.openSession();
    	Gitrelease r = null;
		try{
			r = sqlSession.getMapper(GitreleaseMapper.class).selectLatestInRepository(repositoryId);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
		return r;
    }
    public List<Gitrelease> selectByConditions(String repositoryName, String tag, int p){
    	SqlSession sqlSession = sqlSessionFactory.openSession();
    	List<Gitrelease> rList = null;
    	String rArg=null, tArg=null;
		if(repositoryName!=null && (!"".equals(repositoryName)) && p == 0) rArg = "%"+repositoryName+"%";
		if(repositoryName!=null && (!"".equals(repositoryName)) && p == 1) rArg = repositoryName;
		if(tag!=null && (!"".equals(tag))) tArg = "%"+tag+"%";
		try{
			rList = sqlSession.getMapper(GitreleaseMapper.class).selectByConditions(rArg, tArg, p);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}
		return rList;
    }
    
    public List<SimpleRelease> selectRelease(int repositoryId){
    	SqlSession sqlSession = sqlSessionFactory.openSession();
    	List<SimpleRelease> rList = null;
		try{
			rList = sqlSession.getMapper(GitreleaseMapper.class).selectRelease(repositoryId);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}
		return rList;
    }
    public List<SimplePackage> selectPackage(int releaseId){
    	SqlSession sqlSession = sqlSessionFactory.openSession();
    	List<SimplePackage> rList = null;
		try{
			rList = sqlSession.getMapper(GitreleaseMapper.class).selectPackage(releaseId);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}
		return rList;
    }
    public List<SimpleClass> selectClass(int releaseId){
    	SqlSession sqlSession = sqlSessionFactory.openSession();
    	List<SimpleClass> rList = null;
		try{
			rList = sqlSession.getMapper(GitreleaseMapper.class).selectClass(releaseId);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}
		return rList;
    }
}
