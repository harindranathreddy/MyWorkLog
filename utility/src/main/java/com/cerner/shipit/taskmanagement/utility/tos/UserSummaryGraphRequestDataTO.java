package com.cerner.shipit.taskmanagement.utility.tos;

import java.util.List;

public class UserSummaryGraphRequestDataTO {

	int noOfDays;
	List<JiraSummaryTO> jiraSummaryDetails;
	public int getNoOfDays() {
		return noOfDays;
	}
	public void setNoOfDays(int noOfDays) {
		this.noOfDays = noOfDays;
	}
	public List<JiraSummaryTO> getJiraSummaryDetails() {
		return jiraSummaryDetails;
	}
	public void setJiraSummaryDetails(List<JiraSummaryTO> jiraSummaryDetails) {
		this.jiraSummaryDetails = jiraSummaryDetails;
	}
	
	
}
