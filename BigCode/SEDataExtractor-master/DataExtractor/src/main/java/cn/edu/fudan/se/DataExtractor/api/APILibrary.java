package cn.edu.fudan.se.DataExtractor.api;

public class APILibrary {
	private int id;
	private String name;
	private String orgnization;
	private String introduction;
	private String version;
	private String jdkVersion;
	private String pomFile;
	private String license;
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
	public String getOrgnization() {
		return orgnization;
	}
	public void setOrgnization(String orgnization) {
		this.orgnization = orgnization;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getJdkVersion() {
		return jdkVersion;
	}
	public void setJdkVersion(String jdkVersion) {
		this.jdkVersion = jdkVersion;
	}
	public String getPomFile() {
		return pomFile;
	}
	public void setPomFile(String pomFile) {
		this.pomFile = pomFile;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getDocWebsite() {
		return docWebsite;
	}
	public void setDocWebsite(String docWebsite) {
		this.docWebsite = docWebsite;
	}
}
