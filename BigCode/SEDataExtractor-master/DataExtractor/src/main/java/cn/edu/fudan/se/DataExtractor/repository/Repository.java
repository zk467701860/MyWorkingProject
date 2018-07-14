package cn.edu.fudan.se.DataExtractor.repository;

import java.util.Date;

public class Repository {
   private Integer repositoryId;
   private String repositoryName;
   private String gitAddress;
   private String issueAddress;
   private String localAddress;
   private String license;
   private Date addedDate;
   private String description;

   public Integer getRepositoryId() {
       return repositoryId;
   }
   public void setRepositoryId(Integer repositoryId) {
       this.repositoryId = repositoryId;
   }
   public String getRepositoryName() {
       return repositoryName;
   }
   public void setRepositoryName(String repositoryName) {
       this.repositoryName = repositoryName;
   }
   public String getGitAddress() {
       return gitAddress;
   }
   public void setGitAddress(String gitAddress) {
       this.gitAddress = gitAddress;
   }
   public String getIssueAddress() {
       return issueAddress;
   }
   public void setIssueAddress(String issueAddress) {
       this.issueAddress = issueAddress;
   }
   public String getLocalAddress() {
       return localAddress;
   }
   public void setLocalAddress(String localAddress) {
       this.localAddress = localAddress;
   }
   public String getLicense() {
       return license;
   }
   public void setLicense(String license) {
       this.license = license;
   }
   public Date getAddedDate() {
       return addedDate;
   }
   public void setAddedDate(Date addedDate) {
       this.addedDate = addedDate;
   }
   public String getDescription() {
       return description;
   }
   public void setDescription(String description) {
       this.description = description;
   }
}
