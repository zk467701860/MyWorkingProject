package cn.edu.fudan.se.DataExtractor.codeAnalyze.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import cn.edu.fudan.se.DataExtractor.codeAnalyze.CodeClass;
import cn.edu.fudan.se.DataExtractor.repository.FullClass;

public interface CodeClassMapper {
	 public long countClass();
	 public long countParameter();
	 public long countMethod();
	 public long countVariable();
    
	 public List<CodeClass> getOldInnerClassByQualifiedName(@Param("releaseId")int releaseId,
			 @Param("packageName")String packageName, @Param("className")String className);
	 public List<CodeClass> getAllOldCodeClass();
	 public CodeClass getOldInnerClassByPackageName(@Param("releaseId")int releaseId,
			 @Param("packageName")String packageName);
	 public List<CodeClass> getAllClassInRelease(@Param("releaseId")int releaseId);
	 public FullClass selectOneClass(int classId);
}

