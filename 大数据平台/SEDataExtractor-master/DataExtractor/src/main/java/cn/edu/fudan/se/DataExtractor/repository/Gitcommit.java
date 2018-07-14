package cn.edu.fudan.se.DataExtractor.repository;

import java.util.Date;
import java.util.List;

public class Gitcommit{
	private String commitId;
    private Integer gitReleaseId;
    private Integer repositoryId;
    private String gitCommitId;
    private Date commitDate;
    private Integer authorId;
    private String authorName;
    private String message;
	private Integer additions;
    private Integer deletions;
    private String branchName;
    private String diff;
    
	public String getDiff() {
		return diff;
	}
	public void setDiff(String diff) {
		this.diff = diff;
	}
	public String getCommitId() {
		return commitId;
	}
	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}
	public Integer getGitReleaseId() {
		return gitReleaseId;
	}
	public void setGitReleaseId(Integer gitReleaseId) {
		this.gitReleaseId = gitReleaseId;
	}
	public Integer getRepositoryId() {
		return repositoryId;
	}
	public void setRepositoryId(Integer repositoryId) {
		this.repositoryId = repositoryId;
	}
	public String getGitCommitId() {
		return gitCommitId;
	}
	public void setGitCommitId(String gitCommitId) {
		this.gitCommitId = gitCommitId;
	}
	public Date getCommitDate() {
		return commitDate;
	}
	public void setCommitDate(Date commitDate) {
		this.commitDate = commitDate;
	}
	public Integer getAuthorId() {
		return authorId;
	}
	public void setAuthorId(Integer authorId) {
		this.authorId = authorId;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getAdditions() {
		return additions;
	}
	public void setAdditions(Integer additions) {
		this.additions = additions;
	}
	public Integer getDeletions() {
		return deletions;
	}
	public void setDeletions(Integer deletions) {
		this.deletions = deletions;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
    
	
}