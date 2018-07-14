package cn.edu.fudan.se.DataExtractor.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import cn.edu.fudan.se.DataExtractor.mybatisDao.GitreleaseDao;
import cn.edu.fudan.se.DataExtractor.mybatisDao.MybatisFactory;
import cn.edu.fudan.se.DataExtractor.repository.Gitrelease;
import cn.edu.fudan.se.DataExtractor.repository.mapper.GitreleaseMapper;

public class ReleaseTest {

	@Test
	public void testCountAll(){
		System.out.println("========================count all===========================");
		SqlSessionFactory factory = MybatisFactory.getSqlSessionFactory();
		SqlSession sqlSession = factory.openSession();
		try{
			long r = sqlSession.getMapper(GitreleaseMapper.class).countAll();
			System.out.println(r);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
	}
	
	@Test
	public void testCountAllIn(){
		System.out.println("========================testCountAllIn===========================");
		SqlSessionFactory factory = MybatisFactory.getSqlSessionFactory();
		SqlSession sqlSession = factory.openSession();
		try{
			long r = sqlSession.getMapper(GitreleaseMapper.class).countAllInRepository(2039);
			System.out.println(r);
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
			Gitrelease r = sqlSession.getMapper(GitreleaseMapper.class).selectByPrimaryKey(189);
			System.out.println(r.getTag());
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
	}
	
	@Test
	public void testSelectAll(){
		System.out.println("========================select all in ===========================");
		SqlSessionFactory factory = MybatisFactory.getSqlSessionFactory();
		SqlSession sqlSession = factory.openSession();
		try{
			List<Gitrelease> rList = sqlSession.getMapper(GitreleaseMapper.class).selectAllRelease(2039);
			for(Gitrelease r : rList){
				System.out.println(r.getReleaseCommitId()+"\t"+r.getTag());
			}
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
	}
	@Test
	public void testSelectAllSimple(){
		System.out.println("========================select all simple in ===========================");
		SqlSessionFactory factory = MybatisFactory.getSqlSessionFactory();
		SqlSession sqlSession = factory.openSession();
		try{
			List<SimpleGitrelease> rList = sqlSession.getMapper(GitreleaseMapper.class).selectSimpleGitrelease(2039);
			for(SimpleGitrelease r : rList){
				System.out.println(r.getReleaseCommitId()+"\t"+r.getTag());
			}
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
	}
	
	@Test
	public void testSelectLatest(){
		System.out.println("========================select latest===========================");
		SqlSessionFactory factory = MybatisFactory.getSqlSessionFactory();
		SqlSession sqlSession = factory.openSession();
		try{
			Gitrelease r = sqlSession.getMapper(GitreleaseMapper.class).selectLatestInRepository(2039);
			System.out.println(r.getTag());
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
	}
	
	@Test
	public void testByConditions(){
		System.out.println("========================conditions===========================");
		GitreleaseDao dao = new GitreleaseDao();
		List<Gitrelease> rList = null;
//		rList = dao.selectByConditions(null, "v4", 0);
//		for(Gitrelease r : rList){
//			System.out.println(r.getRepository().getRepositoryName()+"\t"+r.getTag());
//		}

		System.out.println("========================np===========================");
		rList = dao.selectByConditions("-android", null, 0);
		for(Gitrelease r : rList){
			System.out.println(r.getRepository().getRepositoryName()+"\t"+r.getTag());
		}
//		System.out.println("========================p===========================");
//		rList = dao.selectByConditions("android", null, 1);
//		for(Gitrelease r : rList){
//			System.out.println(r.getRepository().getRepositoryName()+"\t"+r.getTag());
//		}
	}
	
	@Test
	public void testSelectRelease(){
		GitreleaseDao dao = new GitreleaseDao();

		List<SimpleRelease> rList = null;
		rList= dao.selectRelease(737);
		for(SimpleRelease r : rList){
			System.out.println(r.getReleaseId());
		}
	}
	@Test
	public void testSelectPackage(){
		GitreleaseDao dao = new GitreleaseDao();

		List<SimplePackage> rList = null;
		rList= dao.selectPackage(6068);
		for(SimplePackage r : rList){
			System.out.println(r.getPackageName());
		}
	}
	@Test
	public void testSelectClass(){
		GitreleaseDao dao = new GitreleaseDao();

		List<SimpleClass> rList = null;
		rList= dao.selectClass(6068);
		for(SimpleClass r : rList){
			System.out.println(r.getClassName());
		}
	}
}
