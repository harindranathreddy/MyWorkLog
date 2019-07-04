package com.cerner.shipit.taskmanagement.utility.tos;

import java.util.List;

public class JiraSummaryTO {

	private String key;
	private String totalHoursLogged;
	private String summary;
	private List<WorkLogDaySummaryTO> workLogDaySummeryTOs;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getTotalHoursLogged() {
		return totalHoursLogged;
	}

	public void setTotalHoursLogged(String totalHoursLogged) {
		this.totalHoursLogged = totalHoursLogged;
	}

	public List<WorkLogDaySummaryTO> getWorkLogDaySummeryTOs() {
		return workLogDaySummeryTOs;
	}

	public void setWorkLogDaySummeryTOs(List<WorkLogDaySummaryTO> workLogDaySummeryTOs) {
		this.workLogDaySummeryTOs = workLogDaySummeryTOs;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	

}
