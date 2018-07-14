package cn.edu.fudan.se.DataExtractor.codeAnalyze.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.edu.fudan.se.DataExtractor.codeAnalyze.CodeMethod;

public interface CodeMethodMapper {
	public List<CodeMethod> getOldMethod(int classId, String methodName);
	public List<CodeMethod> selecAllMethodInClass(@Param("id")int id);
	public void deleteMethod(@Param("id")int id);
	public List<CodeMethod> selectMethodInClass(int classId);
}
