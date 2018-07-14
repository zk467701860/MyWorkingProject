package cn.edu.fudan.se.DataExtractor.repository;

public class ChangeOperation {
	private int changeOperationId;
	private int repositoryId;
	private String commitId;
	private String fileName;
	private String type;
	private String level;
	private String rootType;
	private String rootContent;
	private String parentType;
	private String parentContent;
	private String changedType;
	private String changedContent;
	public int getChangeOperationId() {
		return changeOperationId;
	}
	public void setChangeOperationId(int changeOperationId) {
		this.changeOperationId = changeOperationId;
	}
	public int getRepositoryId() {
		return repositoryId;
	}
	public void setRepositoryId(int repositoryId) {
		this.repositoryId = repositoryId;
	}
	public String getCommitId() {
		return commitId;
	}
	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getRootType() {
		return rootType;
	}
	public void setRootType(String rootType) {
		this.rootType = rootType;
	}
	public String getRootContent() {
		return rootContent;
	}
	public void setRootContent(String rootContent) {
		this.rootContent = rootContent;
	}
	public String getParentType() {
		return parentType;
	}
	public void setParentType(String parentType) {
		this.parentType = parentType;
	}
	public String getParentContent() {
		return parentContent;
	}
	public void setParentContent(String parentContent) {
		this.parentContent = parentContent;
	}
	public String getChangedType() {
		return changedType;
	}
	public void setChangedType(String changedType) {
		this.changedType = changedType;
	}
	public String getChangedContent() {
		return changedContent;
	}
	public void setChangedContent(String changedContent) {
		this.changedContent = changedContent;
	}
	
}
