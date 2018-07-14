package cn.edu.fudan.se.DataExtractor.repository;

public class CodeFile {
	private int fileId;
	private int releaseId;
	private String fileName;
	private String relativeAddress;

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public int getReleaseId() {
		return releaseId;
	}

	public void setReleaseId(int releaseId) {
		this.releaseId = releaseId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getRelativeAddress() {
		return relativeAddress;
	}

	public void setRelativeAddress(String relativeAddress) {
		this.relativeAddress = relativeAddress;
	}
	
}
