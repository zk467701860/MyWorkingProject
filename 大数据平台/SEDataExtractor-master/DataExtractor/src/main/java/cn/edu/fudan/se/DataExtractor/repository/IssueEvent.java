package cn.edu.fudan.se.DataExtractor.repository;

import java.util.Date;

public class IssueEvent {
    private Integer id;
    private Integer repositoryId;
    private Integer issueId;
    private Integer eventId;
    private String aboutCommitId;
    private String event;
    private Date createdAt;
    private Integer actorId;
    private String actorName;
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getRepositoryId() {
		return repositoryId;
	}
	public void setRepositoryId(Integer repositoryId) {
		this.repositoryId = repositoryId;
	}
	public Integer getIssueId() {
		return issueId;
	}
	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}
	public Integer getEventId() {
		return eventId;
	}
	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}
	public String getAboutCommitId() {
		return aboutCommitId;
	}
	public void setAboutCommitId(String aboutCommitId) {
		this.aboutCommitId = aboutCommitId;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Integer getActorId() {
		return actorId;
	}
	public void setActorId(Integer actorId) {
		this.actorId = actorId;
	}
	public String getActorName() {
		return actorName;
	}
	public void setActorName(String actorName) {
		this.actorName = actorName;
	}
    
    
}
