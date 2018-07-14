package cn.edu.fudan.se.DataExtractor.repository;

public class Project {
	private int projectId;
	private String projectName;
	private String projectType;
	private String orgnization;
	private String mainPage;
	private String description;
	private String descriptionLocalAddress;
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	public String getOrgnization() {
		return orgnization;
	}
	public void setOrgnization(String orgnization) {
		this.orgnization = orgnization;
	}
	public String getMainPage() {
		return mainPage;
	}
	public void setMainPage(String mainPage) {
		this.mainPage = mainPage;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescriptionLocalAddress() {
		return descriptionLocalAddress;
	}
	public void setDescriptionLocalAddress(String descriptionLocalAddress) {
		this.descriptionLocalAddress = descriptionLocalAddress;
	}
	
	
}
