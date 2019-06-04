package com.cerner.shipit.taskmanagement.utility.tos;

public class WorkLogDetailsTO {

	String id;
	String started;
	String timeSpent;
	String comment;
	String self;
	Object author;
	Object updateAuthor;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStarted() {
		return started;
	}

	public void setStarted(String time) {
		this.started = time;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getTimeSpent() {
		return timeSpent;
	}

	public void setTimeSpent(String timeSpent) {
		this.timeSpent = timeSpent;
	}

	public String getSelf() {
		return self;
	}

	public void setSelf(String self) {
		this.self = self;
	}

	public Object getAuthor() {
		return author;
	}

	public void setAuthor(Object author) {
		this.author = author;
	}

	public Object getUpdateAuthor() {
		return updateAuthor;
	}

	public void setUpdateAuthor(Object updateAuthor) {
		this.updateAuthor = updateAuthor;
	}

	
}
