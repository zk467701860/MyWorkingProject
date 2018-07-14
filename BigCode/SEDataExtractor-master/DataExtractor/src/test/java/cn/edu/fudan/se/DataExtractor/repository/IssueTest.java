package cn.edu.fudan.se.DataExtractor.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import cn.edu.fudan.se.DataExtractor.mybatisDao.IssueDao;
import cn.edu.fudan.se.DataExtractor.mybatisDao.MybatisFactory;
import cn.edu.fudan.se.DataExtractor.repository.Issue;
import cn.edu.fudan.se.DataExtractor.repository.mapper.IssueMapper;

public class IssueTest {
	@Test
	public void testSelectAll(){
		System.out.println("========================select all in ===========================");
		SqlSessionFactory factory = MybatisFactory.getSqlSessionFactory();
		SqlSession sqlSession = factory.openSession();
		try{
			List<Issue> rList = sqlSession.getMapper(IssueMapper.class).selectAllIssue(800);
			for(Issue r : rList){
				System.out.println(r.getIssueId()+"\t"+r.getTitle());
			}
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
			Issue r = sqlSession.getMapper(IssueMapper.class).selectByPrimaryKey(40);
			System.out.println(r.getIssueId()+"\t"+r.getTitle());
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
	}
	@Test
	public void testSelectByIssueId(){
		System.out.println("========================select by issue id===========================");
		SqlSessionFactory factory = MybatisFactory.getSqlSessionFactory();
		SqlSession sqlSession = factory.openSession();
		try{
			Issue r = sqlSession.getMapper(IssueMapper.class).selectByIssueId(171053246);
			System.out.println(r.getIssueId()+"\t"+r.getTitle());
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
			long r = sqlSession.getMapper(IssueMapper.class).countAll();
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
			long r = sqlSession.getMapper(IssueMapper.class).countAllInRepository(800);
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
			List<Issue> rList = sqlSession.getMapper(IssueMapper.class).selectInScope(0, 10, 800);
			for(Issue r : rList){
				System.out.println(r.getIssueId()+"\t"+r.getTitle());
			}
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
	}
	
	@Test
	public void testCondition(){
		System.out.println("========================conditions===========================");
		IssueDao dao = new IssueDao();
			List<Issue> rList = null;
//			rList = dao.selectByConditions(null, null, 12, 0);
//			for(Issue r : rList){
//				System.out.println(r.getTitle()+"\t"+r.getNumber());
//			}
			System.out.println("===========================np========================");
			rList = dao.selectByConditions("-android", "test", null, 0);
			for(Issue r : rList){
				System.out.println(r.getTitle()+"\t"+r.getNumber());
			}
			System.out.println("===========================p========================");
			rList = dao.selectByConditions("-android", null, null, 1);
			for(Issue r : rList){
				System.out.println(r.getTitle()+"\t"+r.getNumber());
			}
//			System.out.println("===================================================");
//			rList = dao.selectByConditions(null, "test", null, 0);
//			for(Issue r : rList){
//				System.out.println(r.getTitle()+"\t"+r.getNumber());
//			}
	}
}
