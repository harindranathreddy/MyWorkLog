package com.cerner.shitit.taskmanagement.utility.tos;

public class JiraDetails {

	private String Id;
	private String Summery;
	private String Description;
	private String Status;
	private String Type;
	private String lastLoggedDate;

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

}
