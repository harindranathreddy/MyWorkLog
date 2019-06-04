package com.cerner.shipit.taskmanagement.service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cerner.shipit.taskmanagement.exception.TaskManagementServiceException;
import com.cerner.shipit.taskmanagement.utility.tos.JiraTO;
import com.cerner.shipit.taskmanagement.utility.tos.WorkLogInfoTO;

@Service
public interface JiraDetailsService {

	List<JiraTO> getJiraDetailsByUserId(String UserId) throws TaskManagementServiceException;
	String putJiraDetailsByUserId(WorkLogInfoTO workLogInfo) throws TaskManagementServiceException;
}
