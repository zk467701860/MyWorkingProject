package cn.edu.fudan.se.DataExtractor.codeAnalyze.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.edu.fudan.se.DataExtractor.codeAnalyze.CodeField;

public interface CodeFieldMapper {
	public void deleteField(@Param("id")int id);
	public List<CodeField> selectFieldInClass(int classId);

}
