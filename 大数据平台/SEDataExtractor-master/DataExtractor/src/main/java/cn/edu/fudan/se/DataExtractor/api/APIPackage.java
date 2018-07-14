package cn.edu.fudan.se.DataExtractor.api;

public class APIPackage {
	private int id;
	private String name;
	private String firstVersion;
	private String description;
	private String docWebsite;
	private int libraryId;
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
	public String getFirstVersion() {
		return firstVersion;
	}
	public void setFirstVersion(String firstVersion) {
		this.firstVersion = firstVersion;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDocWebsite() {
		return docWebsite;
	}
	public void setDocWebsite(String docWebsite) {
		this.docWebsite = docWebsite;
	}
	public int getLibraryId() {
		return libraryId;
	}
	public void setLibraryId(int libraryId) {
		this.libraryId = libraryId;
	}
}
