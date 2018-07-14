package cn.edu.fudan.se.DataExtractor.repository;

import java.util.List;

public class ChangeFile {
	private String commitId;
	private String fileName;
	private List<Diff> diffList;
	private List<ChangeOperation> changeOperationList;
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
	public List<Diff> getDiffList() {
		return diffList;
	}
	public void setDiffList(List<Diff> diffList) {
		this.diffList = diffList;
	}
	public List<ChangeOperation> getChangeOperationList() {
		return changeOperationList;
	}
	public void setChangeOperationList(List<ChangeOperation> changeOperationList) {
		this.changeOperationList = changeOperationList;
	}
	
}
