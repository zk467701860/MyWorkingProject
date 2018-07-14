
package cn.edu.fudan.se.DataExtractor.codeAnalyze.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.edu.fudan.se.DataExtractor.codeAnalyze.CodeImport;

public interface CodeImportMapper {
	public void deleteImport(@Param("id")int id);
	public List<CodeImport> selectImportInClass(int classId);

}

