package cn.edu.fudan.se.DataExtractor.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Ignore;
import org.junit.Test;

import cn.edu.fudan.se.DataExtractor.mybatisDao.GitcommitDao;
import cn.edu.fudan.se.DataExtractor.mybatisDao.MybatisFactory;
import cn.edu.fudan.se.DataExtractor.repository.Gitcommit;
import cn.edu.fudan.se.DataExtractor.repository.mapper.GitcommitMapper;

public class GitcommitTest {
	@Test
	public void testSelectFile(){
		System.out.println("========================select file ===========================");
		SqlSessionFactory factory = MybatisFactory.getSqlSessionFactory();
		SqlSession sqlSession = factory.openSession();
		try{
			List<ChangeFile> rList = sqlSession.getMapper(GitcommitMapper.class).selectFile("3c3d27ced3ab3044834ed9d6718e3c462e1064bc");
			for(ChangeFile r : rList){
				System.out.println(r.getFileName()+"\t"+r.getDiffList().size()+"\t"+r.getChangeOperationList().size());
			}
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
			List<Gitcommit> rList = sqlSession.getMapper(GitcommitMapper.class).selectAllCommit(800);
			for(Gitcommit r : rList){
				System.out.println(r.getCommitId());
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
			Gitcommit r = sqlSession.getMapper(GitcommitMapper.class).selectByPrimaryKey("e5294ca132e6811fbcba50ae8a2c1ebb1e6b0e97");
			System.out.println(r.getCommitDate());
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
	}
	
	@Test
	public void testSelectByMessage(){
		System.out.println("========================select by message===========================");
		SqlSessionFactory factory = MybatisFactory.getSqlSessionFactory();
		SqlSession sqlSession = factory.openSession();
		try{
			List<Gitcommit> rList = sqlSession.getMapper(GitcommitMapper.class).selectByMessage("test");
			for(Gitcommit r : rList){
				System.out.println(r.getCommitId()+"\t"+r.getMessage());
			}
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
			long r = sqlSession.getMapper(GitcommitMapper.class).countAll();
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
			long r = sqlSession.getMapper(GitcommitMapper.class).countAllInRepository(800);
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
			List<Gitcommit> rList = sqlSession.getMapper(GitcommitMapper.class).selectInScope(0, 10, 900);
			for(Gitcommit r : rList){
				System.out.println(r.getCommitId());
			}
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
	}
	
	@Test
	public void testParent(){
		System.out.println("========================parent===========================");
		SqlSessionFactory factory = MybatisFactory.getSqlSessionFactory();
		SqlSession sqlSession = factory.openSession();
		try{
			List<Gitcommit> rList = sqlSession.getMapper(GitcommitMapper.class).getParent("00000e84c6c9e6e1a221502ad03c4d8fa16279e2");
			for(Gitcommit r : rList){
				System.out.println(r.getCommitId());
			}
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
	}
	@Ignore
	@Test
	public void testCondition(){
		System.out.println("========================conditions===========================");
		GitcommitDao dao = new GitcommitDao();
		List<Gitcommit> rList = null;
//			rList = dao.selectByConditions(null, null, "00000", 0);
//			for(Gitcommit r : rList){
//				System.out.println(r.getCommitId()+"\t"+r.getRepository().getRepositoryName());
//			}
			System.out.println("===========================np========================");
			rList = dao.selectByConditions("-android", "test", null, 0);
			for(Gitcommit r : rList){
				System.out.println(r.getCommitId());
			}
//			System.out.println("===========================p========================");
//			rList = dao.selectByConditions("android", null, null, 1);
//			for(Gitcommit r : rList){
//				System.out.println(r.getCommitId()+"\t"+r.getRepository().getRepositoryName());
//			}
//			System.out.println("===================================================");
//			rList = dao.selectByConditions(null, "test", null, 0);
//			for(Gitcommit r : rList){
//				System.out.println(r.getCommitId()+"\t"+r.getMessage());
//			}
	}
	
	@Test
	public void testRepositoryScope(){
		System.out.println("========================testRepositoryScope===========================");
		SqlSessionFactory factory = MybatisFactory.getSqlSessionFactory();
		SqlSession sqlSession = factory.openSession();
		try{
			List<Gitcommit> rList = sqlSession.getMapper(GitcommitMapper.class).selectByRepositoryScope(1000, 2000);
			System.out.println(rList.size());
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
	}
	@Test
	public void testMessageRepositoryScope(){
		System.out.println("========================testRepositoryScope===========================");
		SqlSessionFactory factory = MybatisFactory.getSqlSessionFactory();
		SqlSession sqlSession = factory.openSession();
		try{
			List<String> rList = sqlSession.getMapper(GitcommitMapper.class).selectMessageByRepositoryScope(1000, 1020);
			System.out.println(rList.size());
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
	}
	@Test
	public void testUpdateCommit(){
		GitcommitDao dao = new GitcommitDao();
		dao.updateCommitTag("t", "t", "00000e84c6c9e6e1a221502ad03c4d8fa16279e2");
	}
}
