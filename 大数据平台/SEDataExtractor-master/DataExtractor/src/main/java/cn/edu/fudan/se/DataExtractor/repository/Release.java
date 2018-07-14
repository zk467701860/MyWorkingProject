package cn.edu.fudan.se.DataExtractor.repository;

import java.util.Date;
import java.util.List;

/**
 * Created by lyk on 2017/5/8.
 */
public class Release {
    private int reLeaseId;
    private int projectId;
    private String version;
    private String remotePath;
    private String localPath;
    private String zipLocalPath;
    private Date createdDate;

    private List<CodeFile> fileList;

    public int getReLeaseId() {
        return reLeaseId;
    }

    public void setReLeaseId(int reLeaseId) {
        this.reLeaseId = reLeaseId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getZipLocalPath() {
        return zipLocalPath;
    }

    public void setZipLocalPath(String zipLocalPath) {
        this.zipLocalPath = zipLocalPath;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public List<CodeFile> getFileList() {
        return fileList;
    }

    public void setFileList(List<CodeFile> fileList) {
        this.fileList = fileList;
    }
}
