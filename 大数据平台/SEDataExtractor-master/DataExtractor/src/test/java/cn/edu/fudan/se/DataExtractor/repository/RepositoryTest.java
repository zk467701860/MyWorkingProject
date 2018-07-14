package cn.edu.fudan.se.DataExtractor.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import cn.edu.fudan.se.DataExtractor.mybatisDao.MybatisFactory;
import cn.edu.fudan.se.DataExtractor.repository.Repository;
import cn.edu.fudan.se.DataExtractor.repository.mapper.RepositoryMapper;
import junit.framework.TestCase;

public class RepositoryTest extends TestCase {
	@Test
	public void testSelectAll(){
		System.out.println("========================select all===========================");
		SqlSessionFactory factory = MybatisFactory.getSqlSessionFactory();
		SqlSession sqlSession = factory.openSession();
		try{
			List<Repository> rList = sqlSession.getMapper(RepositoryMapper.class).selectAllRepository();
			for(Repository r : rList){
				System.out.println(r.getRepositoryName()+"\t"+r.getRepositoryId());
			}
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
	}
	
	@Test
	public void testSelectByName(){
		System.out.println("========================select by name===========================");
		SqlSessionFactory factory = MybatisFactory.getSqlSessionFactory();
		SqlSession sqlSession = factory.openSession();
		try{
			List<Repository> r = sqlSession.getMapper(RepositoryMapper.class).selectByName("android");
			System.out.println(r.size());
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
	}
	
	@Test
	public void testSelectById(){
		System.out.println("========================select by id===========================");
		SqlSessionFactory factory = MybatisFactory.getSqlSessionFactory();
		SqlSession sqlSession = factory.openSession();
		try{
			Repository r = sqlSession.getMapper(RepositoryMapper.class).selectByPrimaryKey(737);
			System.out.println(r.getRepositoryName());
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
	}
	
	@Test
	public void testCountAll(){
		System.out.println("========================count===========================");
		SqlSessionFactory factory = MybatisFactory.getSqlSessionFactory();
		SqlSession sqlSession = factory.openSession();
		try{
			long r = sqlSession.getMapper(RepositoryMapper.class).countAll();
			System.out.println(r);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
	}
	
	@Test
	public void testScope(){
		System.out.println("========================scope===========================");
		SqlSessionFactory factory = MybatisFactory.getSqlSessionFactory();
		SqlSession sqlSession = factory.openSession();
		try{
			List<Repository> rList = sqlSession.getMapper(RepositoryMapper.class).selectInScope(0, 10);
			for(Repository r : rList){
				System.out.println(r.getRepositoryName()+"\t"+r.getRepositoryId());
			}
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
	}
	
	@Test
	public void testSelectLikeName(){
		System.out.println("========================select Like name===========================");
		SqlSessionFactory factory = MybatisFactory.getSqlSessionFactory();
		SqlSession sqlSession = factory.openSession();
		try{
			List<Repository> r = sqlSession.getMapper(RepositoryMapper.class).selectLikeName("%android%");
			System.out.println(r.size());
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
	}
}
