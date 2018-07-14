package cn.edu.fudan.se.DataExtractor.repository;

import java.util.List;

public class ClassInPackage {
	private int classId;
	private String className;
	private List<String> importList;
	private List<String> fieldList;
	private List<String> methodList;
	public int getClassId() {
		return classId;
	}
	public void setClassId(int classId) {
		this.classId = classId;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public List<String> getImportList() {
		return importList;
	}
	public void setImportList(List<String> importList) {
		this.importList = importList;
	}
	public List<String> getFieldList() {
		return fieldList;
	}
	public void setFieldList(List<String> fieldList) {
		this.fieldList = fieldList;
	}
	public List<String> getMethodList() {
		return methodList;
	}
	public void setMethodList(List<String> methodList) {
		this.methodList = methodList;
	}
	
}
