package cn.edu.fudan.se.DataExtractor.api;

public class APIParameter {
	private int id;
	private String name;
	private int classId;
	private int methodId;
	private int typeClass;
	private String typeString;
	private String firstVersion;
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
	public int getClassId() {
		return classId;
	}
	public void setClassId(int classId) {
		this.classId = classId;
	}
	public int getMethodId() {
		return methodId;
	}
	public void setMethodId(int methodId) {
		this.methodId = methodId;
	}
	public int getTypeClass() {
		return typeClass;
	}
	public void setTypeClass(int typeClass) {
		this.typeClass = typeClass;
	}
	public String getTypeString() {
		return typeString;
	}
	public void setTypeString(String typeString) {
		this.typeString = typeString;
	}
	public String getFirstVersion() {
		return firstVersion;
	}
	public void setFirstVersion(String firstVersion) {
		this.firstVersion = firstVersion;
	}
}
