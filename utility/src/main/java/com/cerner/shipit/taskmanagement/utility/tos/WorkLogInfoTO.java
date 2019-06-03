package com.cerner.shipit.taskmanagement.utility.tos;

import java.util.ArrayList;
import java.util.List;

public class WorkLogInfoTO {

	String userName;
	String password;
	List<WorkLogDetailsTO> worklogs = new ArrayList<>();

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<WorkLogDetailsTO> getWorklogs() {
		return worklogs;
	}

	public void setWorklogs(List<WorkLogDetailsTO> worklogs) {
		this.worklogs = worklogs;
	}

}
