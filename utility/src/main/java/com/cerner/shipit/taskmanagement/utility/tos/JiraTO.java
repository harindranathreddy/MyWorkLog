package com.cerner.shipit.taskmanagement.utility.tos;

public class JiraTO {

	private String Id;
	private String Summery;
	private String Description;
	private String Status;
	private String Type;
	private String lastLoggedDate;
	private String jiraLink;
	private String issueIcon;
	private String statusIcon;
	private String assignedTo;

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getSummery() {
		return Summery;
	}

	public void setSummery(String summery) {
		Summery = summery;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getLastLoggedDate() {
		return lastLoggedDate;
	}

	public void setLastLoggedDate(String lastLoggedDate) {
		this.lastLoggedDate = lastLoggedDate;
	}

	public String getJiraLink() {
		return jiraLink;
	}

	public void setJiraLink(String jiraLink) {
		this.jiraLink = jiraLink;
	}

	public String getIssueIcon() {
		return issueIcon;
	}

	public void setIssueIcon(String issueIcon) {
		this.issueIcon = issueIcon;
	}

	public String getStatusIcon() {
		return statusIcon;
	}

	public void setStatusIcon(String statusIcon) {
		this.statusIcon = statusIcon;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	
	

}
