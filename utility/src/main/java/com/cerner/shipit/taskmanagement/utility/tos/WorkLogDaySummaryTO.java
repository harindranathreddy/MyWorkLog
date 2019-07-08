package com.cerner.shipit.taskmanagement.utility.tos;

public class WorkLogDaySummaryTO {

	private String startDate;
	private String currentTimeSpent;
	private String loggedTime;
	private String totalTimeSpent;
	private String comments;
	private long totalSeconds;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getCurrentTimeSpent() {
		return currentTimeSpent;
	}

	public void setCurrentTimeSpent(String currentTimeSpent) {
		this.currentTimeSpent = currentTimeSpent;
	}

	public String getLoggedTime() {
		return loggedTime;
	}

	public void setLoggedTime(String loggedTime) {
		this.loggedTime = loggedTime;
	}

	public String getTotalTimeSpent() {
		return totalTimeSpent;
	}

	public void setTotalTimeSpent(String totalTimeSpent) {
		this.totalTimeSpent = totalTimeSpent;
	}

	public long getTotalSeconds() {
		return totalSeconds;
	}

	public void setTotalSeconds(long totalSeconds) {
		this.totalSeconds = totalSeconds;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	
}
