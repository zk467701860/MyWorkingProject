package cn.edu.fudan.se.DataExtractor.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.edu.fudan.se.DataExtractor.api.APIClass;
import cn.edu.fudan.se.DataExtractor.api.APILibrary;
import cn.edu.fudan.se.DataExtractor.api.APIMethod;
import cn.edu.fudan.se.DataExtractor.api.APIPackage;
import cn.edu.fudan.se.DataExtractor.api.APIParameter;

public interface APIMapper {
	public APIClass getClassByName_Packagename_Version(@Param("name")String name, @Param("packageName")String packageName, @Param("version")int version);
	public List<APIMethod> getMethodsByName_Parameter_Classid(@Param("name")String name, @Param("classId")int classId);
	public List<APIParameter> getParametersByMethodid(@Param("methodId")int methodId);
	public List<APIClass> getApiByName(@Param("apiName")String apiName,
			@Param("libraryId")int libraryId);
	public List<APIClass> getApiByQualifiedName(@Param("qualifiedName")String qualifiedName, 
			@Param("libraryId")int libraryId);
	public List<APILibrary> getAPILibrary(@Param("name")String name, 
			@Param("version")String version);
	public APIPackage determinApiPackage(@Param("packageName")String packageName, 
			@Param("libraryId")int libraryId);
}
