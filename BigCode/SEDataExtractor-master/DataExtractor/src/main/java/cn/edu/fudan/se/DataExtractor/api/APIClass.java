package cn.edu.fudan.se.DataExtractor.api;

public class APIClass {
	private int id;
	private String name;
	private String className;
	private String description;
	private String comment;
	private String author;
	private String  firstVersion;
	private int extendClass;
	private String type;
	private int packageId;
	private String docWebsite;
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
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getFirstVersion() {
		return firstVersion;
	}
	public void setFirstVersion(String firstVersion) {
		this.firstVersion = firstVersion;
	}
	public int getExtendClass() {
		return extendClass;
	}
	public void setExtendClass(int extendClass) {
		this.extendClass = extendClass;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getPackageId() {
		return packageId;
	}
	public void setPackageId(int packageId) {
		this.packageId = packageId;
	}
	public String getDocWebsite() {
		return docWebsite;
	}
	public void setDocWebsite(String docWebsite) {
		this.docWebsite = docWebsite;
	}
}
