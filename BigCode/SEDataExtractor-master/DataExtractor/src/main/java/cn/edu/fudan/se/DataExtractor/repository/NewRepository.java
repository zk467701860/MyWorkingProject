package cn.edu.fudan.se.DataExtractor.repository;

import java.util.Date;

public class NewRepository {
	private int repositoryId;
	private int projectId;
	private String repositoryName;
	private String repositoryType;
	private String remoteAddress;
	private String issueAddress;
	private String licence;
	private Date lastModifiedDate;

	public int getRepositoryId() {
		return repositoryId;
	}
	public void setRepositoryId(int repositoryId) {
		this.repositoryId = repositoryId;
	}
	public String getRepositoryName() {
		return repositoryName;
	}
	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}
	public String getRepositoryType() {
		return repositoryType;
	}
	public void setRepositoryType(String repositoryType) {
		this.repositoryType = repositoryType;
	}
	public String getRemoteAddress() {
		return remoteAddress;
	}
	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}
	public String getIssueAddress() {
		return issueAddress;
	}
	public void setIssueAddress(String issueAddress) {
		this.issueAddress = issueAddress;
	}
	public String getLicence() {
		return licence;
	}
	public void setLicence(String licence) {
		this.licence = licence;
	}
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
	
}
