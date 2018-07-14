

import java.util.ArrayList;
import java.util.List;

public class GitCommit {
	private String commitId;
	private String parentCommitId;
	private List<String> filePathList = new ArrayList<String>();
	private int version;

	public GitCommit() {
	}

	public GitCommit(String commitId, String parentCommitId, List<String> filePathList) {
		this.commitId = commitId;
		this.parentCommitId = parentCommitId;
		this.filePathList = filePathList;
	}

	public String getCommitId() {
		return commitId;
	}

	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}

	public String getparentCommitId() {
		return parentCommitId;
	}

	public void setparentCommitId(String parentCommitId) {
		this.parentCommitId = parentCommitId;
	}

	public List<String> getFilePathList() {
		return filePathList;
	}

	public void setFilePathList(List<String> filePathList) {
		this.filePathList = filePathList;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getVersion() {
		return this.version;
	}
}
