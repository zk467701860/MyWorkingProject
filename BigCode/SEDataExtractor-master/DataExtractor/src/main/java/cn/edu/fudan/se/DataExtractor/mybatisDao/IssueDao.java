package cn.edu.fudan.se.DataExtractor.mybatisDao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Component;

import cn.edu.fudan.se.DataExtractor.repository.Issue;
import cn.edu.fudan.se.DataExtractor.repository.IssueComment;
import cn.edu.fudan.se.DataExtractor.repository.IssueEvent;
import cn.edu.fudan.se.DataExtractor.repository.mapper.IssueMapper;
@Component
public class IssueDao {
	private SqlSessionFactory sqlSessionFactory = MybatisFactory.getSqlSessionFactory();
	
	private static class SingletonHolder {  
		private static IssueDao issueDao = new IssueDao();
	}
	
	public static IssueDao getInstance(){
		return SingletonHolder.issueDao;
	}
	
	
	public long countAll(){
		long count = 0;
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try{
			count = sqlSession.getMapper(IssueMapper.class).countAll();
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
		return count;
	}
    public long countAllInRepository(int repositoryId){
    	long count = 0;
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try{
			count = sqlSession.getMapper(IssueMapper.class).countAllInRepository(repositoryId);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
		return count;
    }
    
    public Issue selectById(int id){
    	Issue issue = null;
    	SqlSession sqlSession = sqlSessionFactory.openSession();
		try{
			issue = sqlSession.getMapper(IssueMapper.class).selectByPrimaryKey(id);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
		return issue;
    }
    
    public List<Issue> selectAllIssue(int repositoryId){
    	List<Issue> issues = null;
    	SqlSession sqlSession = sqlSessionFactory.openSession();
		try{
			issues = sqlSession.getMapper(IssueMapper.class).selectAllIssue(repositoryId);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
		return issues;
    }
    
    public Issue selectByIssueId(int issueId){
    	Issue issue = null;
    	SqlSession sqlSession = sqlSessionFactory.openSession();
		try{
			issue = sqlSession.getMapper(IssueMapper.class).selectByIssueId(issueId);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
		return issue;
    }
    
    public List<Issue> selectInPage(int repositoryId, int page){
		//pages start from 1
		int inOnePage = 20;
		int start = (page-1)*inOnePage;
		SqlSession sqlSession = sqlSessionFactory.openSession();
		List<Issue> issues = null;
		try{
			issues = sqlSession.getMapper(IssueMapper.class).selectInScope(start, inOnePage, repositoryId);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
		return issues;
    }
    
    public List<Issue> selectByConditions(String repositoryName, String title, Integer index, int p){
    	String rArg=null, tArg=null;
		if(repositoryName!=null && (!"".equals(repositoryName)) && p == 0) rArg = "%"+repositoryName+"%";
		if(repositoryName!=null && (!"".equals(repositoryName)) && p == 1) rArg = repositoryName;
		if(title!=null && (!"".equals(title))) tArg = "%"+title+"%";
		SqlSession sqlSession = sqlSessionFactory.openSession();
		List<Issue> issueList = null;
		try{
			issueList = sqlSession.getMapper(IssueMapper.class).selectByConditions(rArg, tArg, index, p);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}
		return issueList;
    }
    
    public List<IssueComment> selectComment(int issueId){
    	List<IssueComment> issues = null;
    	SqlSession sqlSession = sqlSessionFactory.openSession();
		try{
			issues = sqlSession.getMapper(IssueMapper.class).selectComment(issueId);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
		return issues;
    }
    public List<IssueEvent> selectEvent(int issueId){
    	List<IssueEvent> issues = null;
    	SqlSession sqlSession = sqlSessionFactory.openSession();
		try{
			issues = sqlSession.getMapper(IssueMapper.class).selectEvent(issueId);
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
		return issues;
    }
}
