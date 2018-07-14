package cn.edu.fudan.se.DataExtractor.mybatisDao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Component;

import cn.edu.fudan.se.DataExtractor.repository.Repository;
import cn.edu.fudan.se.DataExtractor.repository.mapper.RepositoryMapper;
@Component
public class RepositoryDao {
	private SqlSessionFactory sqlSessionFactory = MybatisFactory.getSqlSessionFactory();
	
	private static class SingletonHolder {  
		private static RepositoryDao repostoryDao = new RepositoryDao();
	}
	
	public static RepositoryDao getInstance(){
		return SingletonHolder.repostoryDao;
	}
	
	public long countAll(){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		long count = 0;
		try{
			count = sqlSession.getMapper(RepositoryMapper.class).countAll();
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
		return count;
	}
	
	public List<Repository> selectAll(){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		List<Repository> rList = null;
		try{
			rList = sqlSession.getMapper(RepositoryMapper.class).selectAllRepository();
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
		return rList;
	}
	
	public Repository selectById(int id){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		Repository r = null;
		try{
			r = sqlSession.getMapper(RepositoryMapper.class).selectByPrimaryKey(id);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
		return r;
	}
	
	public List<Repository> selectByName(String name){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		List<Repository> r = null;
		try{
			r = sqlSession.getMapper(RepositoryMapper.class).selectByName(name);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
		return r;
	}
	
	public List<Repository> selectInPage(int page){
		//pages start from 1
		int inOnePage = 20;
		int start = (page-1)*inOnePage;
		SqlSession sqlSession = sqlSessionFactory.openSession();
		List<Repository> rList = null;
		try{
			rList = sqlSession.getMapper(RepositoryMapper.class).selectInScope(start, inOnePage);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
		return rList;
	}
	
	public List<Repository> selectLikeName(String name){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		List<Repository> r = null;
		try{
			r = sqlSession.getMapper(RepositoryMapper.class).selectLikeName("%"+name+"%");
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
		return r;
	}
}
