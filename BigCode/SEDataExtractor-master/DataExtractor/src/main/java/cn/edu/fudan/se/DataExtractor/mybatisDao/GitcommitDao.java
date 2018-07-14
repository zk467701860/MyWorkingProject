package cn.edu.fudan.se.DataExtractor.mybatisDao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Component;

import cn.edu.fudan.se.DataExtractor.repository.ChangeFile;
import cn.edu.fudan.se.DataExtractor.repository.Gitcommit;
import cn.edu.fudan.se.DataExtractor.repository.mapper.GitcommitMapper;
@Component
public class GitcommitDao {
	private SqlSessionFactory sqlSessionFactory = MybatisFactory.getSqlSessionFactory();
	private static class SingletonHolder {  
		private static GitcommitDao gitcommitDao = new GitcommitDao();
	}
	
	public static GitcommitDao getInstance(){
		return SingletonHolder.gitcommitDao;
	}
	
	public long countAll(){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		long count = 0;
		try{
			count = sqlSession.getMapper(GitcommitMapper.class).countAll();
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}
		return count;
	}
	public long countInRepository(int repositoryId){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		long count = 0;
		try{
			count = sqlSession.getMapper(GitcommitMapper.class).countAllInRepository(repositoryId);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
		return count;
	}
	
	public List<Gitcommit> selectAll(int repositoryId){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		List<Gitcommit> commitList = null;
		try{
			commitList = sqlSession.getMapper(GitcommitMapper.class).selectAllCommit(repositoryId);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}
		return commitList;
	}
	
	public Gitcommit selectById(String sha){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		Gitcommit gitcommit= null;
		try{
			gitcommit = sqlSession.getMapper(GitcommitMapper.class).selectByPrimaryKey(sha);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}
		return gitcommit;
	}
	
	public List<Gitcommit> selectByMessage(String m){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		List<Gitcommit> commitList = null;
		try{
			commitList = sqlSession.getMapper(GitcommitMapper.class).selectByMessage("%"+m+"%");
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}
		return commitList;
	}
	
	public List<Gitcommit> getParent(String sha){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		List<Gitcommit> gitcommit= null;
		try{
			gitcommit = sqlSession.getMapper(GitcommitMapper.class).getParent(sha);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}
		return gitcommit;
	}
	
	public List<ChangeFile> selectFile(String sha){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		List<ChangeFile> files= null;
		try{
			files = sqlSession.getMapper(GitcommitMapper.class).selectFile(sha);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}
		return files;
	}
	
	public List<Gitcommit> selectInPage(int repositoryId, int page){
		//pages start from 1
		int inOnePage = 40;
		int start = (page-1)*inOnePage;
		SqlSession sqlSession = sqlSessionFactory.openSession();
		List<Gitcommit> commitList = null;
		try{
			commitList = sqlSession.getMapper(GitcommitMapper.class).selectInScope(start, inOnePage, repositoryId);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}
		return commitList;
	}
	
	public List<Gitcommit> selectByConditions(String repositoryName, String log, String sha, int p){
		String rArg=null, lArg=null, sArg=null;
		if(repositoryName!=null && (!"".equals(repositoryName)) && p == 0) rArg = "%"+repositoryName+"%";
		if(repositoryName!=null && (!"".equals(repositoryName)) && p == 1) rArg = repositoryName;
		if(log!=null && (!"".equals(log))) lArg = "%"+log+"%";
		if(sha!=null && (!"".equals(sha))) sArg = "%"+sha+"%";
		SqlSession sqlSession = sqlSessionFactory.openSession();
		List<Gitcommit> commitList = null;
		try{
			commitList = sqlSession.getMapper(GitcommitMapper.class).selectByConditions(rArg, lArg, sArg, p);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}
		return commitList;
	}
	
	public List<Gitcommit> selectByRepositoryScope(int minRepositoryId, int maxRepositoryId){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		List<Gitcommit> commitList = null;
		try{
			commitList = sqlSession.getMapper(GitcommitMapper.class).selectByRepositoryScope(minRepositoryId, maxRepositoryId);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}
		return commitList;
	}
	
	public List<String> selectMessageByRepositoryScope(int minRepositoryId, int maxRepositoryId){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		List<String> commitList = null;
		try{
			commitList = sqlSession.getMapper(GitcommitMapper.class).selectMessageByRepositoryScope(minRepositoryId, maxRepositoryId);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}
		return commitList;
	}
	
	public void updateCommitTag(String simpleTag, String cosTag, String sha){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try{
			sqlSession.getMapper(GitcommitMapper.class).updateTag(simpleTag, cosTag, sha);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}
	}
}
