package cn.edu.fudan.se.DataExtractor.codeAnalyze.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.edu.fudan.se.DataExtractor.codeAnalyze.CodeParameter;

public interface CodeParameterMapper {
	public void insertParameter(CodeParameter patameter);
	public void insertParameters(List<CodeParameter> parameters);
	public List<CodeParameter> selectParameterByMethodId(@Param("id")int id);
	public void deleteParameter(@Param("id")int id);
}
