package cn.edu.fudan.se.DataExtractor.repository;

import java.util.Date;

public class Gitrelease {
    private Integer gitReleaseId;
    private String tag;
    private Date createdAt;
    private String content;
    private String releaseCommitId;
    private String zipAddress;
    private String srcAddress;
    
    Repository repository;
    
	public Repository getRepository() {
		return repository;
	}
	public void setRepository(Repository repository) {
		this.repository = repository;
	}
	public Integer getGitReleaseId() {
		return gitReleaseId;
	}
	public void setGitReleaseId(Integer gitReleaseId) {
		this.gitReleaseId = gitReleaseId;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getReleaseCommitId() {
		return releaseCommitId;
	}
	public void setReleaseCommitId(String releaseCommitId) {
		this.releaseCommitId = releaseCommitId;
	}
	public String getZipAddress() {
		return zipAddress;
	}
	public void setZipAddress(String zipAddress) {
		this.zipAddress = zipAddress;
	}
	public String getSrcAddress() {
		return srcAddress;
	}
	public void setSrcAddress(String srcAddress) {
		this.srcAddress = srcAddress;
	}
    
}
