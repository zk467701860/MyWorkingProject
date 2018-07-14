package cn.edu.fudan.se.DataExtractor.repository;

public class FullClass {
	private int classId;
	private String packageName;
	private String classPath;
	private String className;
	private String extendsName;
	private int gitReleaseId;
	public int getClassId() {
		return classId;
	}
	public void setClassId(int classId) {
		this.classId = classId;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getClassPath() {
		return classPath;
	}
	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getExtendsName() {
		return extendsName;
	}
	public void setExtendsName(String extendsName) {
		this.extendsName = extendsName;
	}
	public int getGitReleaseId() {
		return gitReleaseId;
	}
	public void setGitReleaseId(int gitReleaseId) {
		this.gitReleaseId = gitReleaseId;
	}
	
}
