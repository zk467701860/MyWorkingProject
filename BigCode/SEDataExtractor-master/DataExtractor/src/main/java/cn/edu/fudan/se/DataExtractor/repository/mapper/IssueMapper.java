package cn.edu.fudan.se.DataExtractor.repository.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.edu.fudan.se.DataExtractor.repository.Issue;
import cn.edu.fudan.se.DataExtractor.repository.IssueComment;
import cn.edu.fudan.se.DataExtractor.repository.IssueEvent;

public interface IssueMapper {
	public long countAll();
    public long countAllInRepository(int repositoryId);
    public List<Issue> selectAllIssue(int repositoryId);
    public Issue selectByPrimaryKey(int id);
    public Issue selectByIssueId(int id);
    public List<Issue> selectInScope(@Param("start")int start, @Param("count")int count,@Param("repositoryId")int repositoryId);
    public List<Issue> selectByConditions(@Param("repositoryName")String repositoryName, @Param("title")String title, @Param("index")Integer index, @Param("perfect")int perfect);

    public List<IssueComment> selectComment(int issueId);
    public List<IssueEvent> selectEvent(int issueId);
}
