package com.cerner.shipit.taskmanagement.service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cerner.shipit.taskmanagement.exception.TaskManagementServiceException;
import com.cerner.shipit.taskmanagement.utility.tos.GraphDataTO;
import com.cerner.shipit.taskmanagement.utility.tos.JiraSummaryTO;
import com.cerner.shipit.taskmanagement.utility.tos.JiraTO;
import com.cerner.shipit.taskmanagement.utility.tos.WorkLogInfoTO;

@Service
public interface JiraDetailsService {

	List<JiraTO> getJiraDetailsByUserId(String userId) throws TaskManagementServiceException;

	int addWorkLog(WorkLogInfoTO workLogInfo) throws TaskManagementServiceException;

	List<String> getDates(String lastLoggedDate);

	List<JiraTO> getJiraSearchDetails(String issueKey, String userId) throws TaskManagementServiceException;

	List<JiraSummaryTO> getWorkLogVerificationSummary(WorkLogInfoTO workLogInfoTo)
			throws TaskManagementServiceException;

	List<JiraSummaryTO> getUserSummary(String userId, int noOfDays) throws TaskManagementServiceException;

	GraphDataTO getUserSummaryGraphData(int noOfDays, List<JiraSummaryTO> jiraSummayDetails);

	List<JiraTO> getJiraDetailsByTeam(String teamName) throws TaskManagementServiceException;

}
