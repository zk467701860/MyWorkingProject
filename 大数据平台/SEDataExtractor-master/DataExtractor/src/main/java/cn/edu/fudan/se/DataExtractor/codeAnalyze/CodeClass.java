package cn.edu.fudan.se.DataExtractor.codeAnalyze;

import java.util.List;

import cn.edu.fudan.se.DataExtractor.repository.CodeFile;

public class CodeClass {
	private int codeClassId;
	private String className;
	private String packageName;
	private String path;
	private int releaseId;

	private List<CodeField> fieldList;
	private List<CodeMethod> methodList;
	
	public CodeClass(){}

	public int getCodeClassId() {
		return codeClassId;
	}
	public void setCodeClassId(int codeClassId) {
		this.codeClassId = codeClassId;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<CodeField> getFieldList() {
		return fieldList;
	}

	public void setFieldList(List<CodeField> fieldList) {
		this.fieldList = fieldList;
	}

	public List<CodeMethod> getMethodList() {
		return methodList;
	}

	public void setMethodList(List<CodeMethod> methodList) {
		this.methodList = methodList;
	}

	public int getReleaseId() {
		return releaseId;
	}

	public void setReleaseId(int releaseId) {
		this.releaseId = releaseId;
	}
}
