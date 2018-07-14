package cn.edu.fudan.se.DataExtractor.api;

public class APIMethod {
	private int id;
	private String name;
	private String comment;
	private String annotation;
	private int returnClass;
	private String returnString;
	private String firstVersion;
	private int isStatic;
	private int classId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getAnnotation() {
		return annotation;
	}
	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}
	public int getReturnClass() {
		return returnClass;
	}
	public void setReturnClass(int returnClass) {
		this.returnClass = returnClass;
	}
	public String getReturnString() {
		return returnString;
	}
	public void setReturnString(String returnString) {
		this.returnString = returnString;
	}
	public String getFirstVersion() {
		return firstVersion;
	}
	public void setFirstVersion(String firstVersion) {
		this.firstVersion = firstVersion;
	}
	public int getIsStatic() {
		return isStatic;
	}
	public void setIsStatic(int isStatic) {
		this.isStatic = isStatic;
	}
	public int getClassId() {
		return classId;
	}
	public void setClassId(int classId) {
		this.classId = classId;
	}
}
