package cn.edu.fudan.se.DataExtractor.repository.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.edu.fudan.se.DataExtractor.repository.ClassInPackage;
import cn.edu.fudan.se.DataExtractor.repository.Gitrelease;
import cn.edu.fudan.se.DataExtractor.repository.SimpleClass;
import cn.edu.fudan.se.DataExtractor.repository.SimpleGitrelease;
import cn.edu.fudan.se.DataExtractor.repository.SimplePackage;
import cn.edu.fudan.se.DataExtractor.repository.SimpleRelease;

public interface GitreleaseMapper {
    public long countAll();
    public long countAllInRepository(int repositoryId);
    public Gitrelease selectByPrimaryKey(int id);
    public List<Gitrelease> selectAllRelease(int repositoryId);
    public List<Gitrelease> getAllUnexpectedRelease();
    public Gitrelease selectLatestInRepository(int repositoryId);

    public List<Gitrelease> selectByConditions(@Param("repositoryName")String repositoryName, @Param("tag")String tag, @Param("perfect")int perfect);

    public List<SimpleRelease> selectRelease(int id);
    public List<SimplePackage> selectPackage(int id);
    public List<SimpleClass> selectClass(int id);
    
    
    
    public List<SimpleGitrelease> selectSimpleGitrelease(int id);
    
    //package.html
    public List<SimpleClass> selectClassInPackage(@Param("releaseId")int releaseId, @Param("packageName")String packageName);
    public ClassInPackage selectClassPartInPackage(int classId);
}
