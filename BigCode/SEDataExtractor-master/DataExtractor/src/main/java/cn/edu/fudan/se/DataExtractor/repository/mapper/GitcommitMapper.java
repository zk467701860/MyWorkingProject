package cn.edu.fudan.se.DataExtractor.repository.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.edu.fudan.se.DataExtractor.repository.ChangeFile;
import cn.edu.fudan.se.DataExtractor.repository.Gitcommit;

public interface GitcommitMapper {
    public long countAll();
    public long countAllInRepository(int repositoryId);
    public List<Gitcommit> selectAllCommit(int repositoryId);
    public Gitcommit selectByPrimaryKey(String sha);
    public List<Gitcommit> selectByMessage(@Param("m")String m);

    public List<Gitcommit> selectInScope(@Param("start")int start, @Param("count")int count,@Param("repositoryId")int repositoryId);
    public List<Gitcommit> selectByConditions(@Param("repositoryName")String repositoryName, @Param("log")String log,@Param("sha")String sha, @Param("perfect")int perfect);
    public List<Gitcommit> selectByRepositoryScope(@Param("minRepositoryId")int minRepositoryId,@Param("maxRepositoryId")int maxRepositoryId);
    public List<String> selectMessageByRepositoryScope(@Param("minRepositoryId")int minRepositoryId,@Param("maxRepositoryId")int maxRepositoryId);

    public List<Gitcommit> getParent(String sha);
    
    public List<ChangeFile> selectFile(String sha);
    
    public void updateTag(@Param("simpleTag")String simpleTag, @Param("cosTag")String cosTag, @Param("sha")String sha);
}
