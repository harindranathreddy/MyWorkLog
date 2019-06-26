package com.cerner.shipit.taskmanagement.service.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.cerner.shipit.taskmanagement.exception.TaskManagementServiceException;
import com.cerner.shipit.taskmanagement.service.jiraapi.JiraApi;
import com.cerner.shipit.taskmanagement.service.service.JiraDetailsService;
import com.cerner.shipit.taskmanagement.utility.constant.ErrorCodes;
import com.cerner.shipit.taskmanagement.utility.constant.ErrorMessages;
import com.cerner.shipit.taskmanagement.utility.constant.GeneralConstants;
import com.cerner.shipit.taskmanagement.utility.constant.MethodConstants;
import com.cerner.shipit.taskmanagement.utility.tos.JiraTO;
import com.cerner.shipit.taskmanagement.utility.tos.WorkLogInfoTO;

@Service
@Component("jiraDetailsServiceImpl")
public class JiraDetailsServiceImpl implements JiraDetailsService {

	Logger logger = LoggerFactory.getLogger(JiraDetailsServiceImpl.class);

	@Autowired
	JiraApi jiraApi;

	@Override
	public List<JiraTO> getJiraDetailsByUserId(String userId) throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START,
				MethodConstants.GET_JIRADETAILS_BY_USERID);
		String jiraDetailsfromApi = jiraApi.getInProgressJiraDetailsByUserId(userId);
		List<JiraTO> jiraDetails = filterJiraDetails(jiraDetailsfromApi, userId);
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END,
				MethodConstants.GET_JIRADETAILS_BY_USERID);
		return jiraDetails;
	}

	private List<JiraTO> filterJiraDetails(String jiraDetailsfromApi, String userId)
			throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START, MethodConstants.FILTER_JIRADETAILS);
		final List<JiraTO> jiraDetails = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(jiraDetailsfromApi);
			String issuesObject = jsonObject.getString("issues");
			JSONArray jiraList = new JSONArray(issuesObject);
			for (int i = 0; i < jiraList.length(); i++) {
				JSONObject jiraObject = (JSONObject) jiraList.get(i);
				JiraTO jira = new JiraTO();
				jira.setId(jiraObject.getString("key"));
				jira.setJiraLink("https://jira2.cerner.com/browse/" + jiraObject.getString("key"));
				JSONObject fields = jiraObject.getJSONObject("fields");
				jira.setDescription(fields.getString("description"));
				jira.setSummery(fields.getString("summary"));
				JSONObject status = fields.getJSONObject("status");
				jira.setStatus(status.getString("name"));
				jira.setStatusIcon(status.getString("iconUrl"));
				JSONObject issueType = fields.getJSONObject("issuetype");
				jira.setType(issueType.getString("name"));
				jira.setIssueIcon(issueType.getString("iconUrl"));
				jira.setLastLoggedDate(getJiraLastWorkLogDate(jiraObject.getString("key"), userId));
				jiraDetails.add(jira);
			}
		} catch (JSONException e) {
			logger.error(ErrorCodes.E04, ErrorMessages.FAILED_DURING_FILTERING_JIRA_DETAILS);
			throw new TaskManagementServiceException(ErrorCodes.E04,
					ErrorMessages.FAILED_DURING_FILTERING_JIRA_DETAILS);
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.FILTER_JIRADETAILS);
		return jiraDetails;
	}

	public String getJiraLastWorkLogDate(String issueKey, String userId) throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START,
				MethodConstants.GET_JIRA_WORKLOG_DATE);
		String jiraWorkLog = jiraApi.getWorkLogDataByJiraId(issueKey);
		String lastLoggedDate = filterWorkLogs(jiraWorkLog, userId);
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END,
				MethodConstants.GET_JIRA_WORKLOG_DATE);
		return lastLoggedDate;
	}

	private String filterWorkLogs(String jiraWorkLog, String userId) throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START, MethodConstants.FILTER_WORKLOGS);
		String lastLoggedDate = null;
		try {
			JSONObject jsonObject = new JSONObject(jiraWorkLog);
			String workLogArray = jsonObject.getString("worklogs");
			JSONArray jiraList = new JSONArray(workLogArray);
			for (int i = jiraList.length() - 1; i >= 0; i--) {
				JSONObject latestWorkLogObject = jiraList.getJSONObject(jiraList.length() - 1);
				JSONObject author = latestWorkLogObject.getJSONObject("author");
				if ((null == userId) || userId.equalsIgnoreCase(author.getString("key"))) {
					lastLoggedDate = latestWorkLogObject.getString("started");
					break;
				}
			}
			if (null == lastLoggedDate) {
				lastLoggedDate = "0";
			}
		} catch (JSONException e) {
			logger.error(ErrorCodes.E04, ErrorMessages.FAILED_DURING_FILTER_LAST_WORKLOG_DETAILS);
			throw new TaskManagementServiceException(ErrorCodes.E04,
					ErrorMessages.FAILED_DURING_FILTER_LAST_WORKLOG_DETAILS);
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.FILTER_WORKLOGS);
		return lastLoggedDate;
	}

	@Override
	public int addWorkLog(WorkLogInfoTO workLogInfo) throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START,
				MethodConstants.GET_JIRADETAILS_BY_USERID);
		int responseStatus = jiraApi.addWorkLog(workLogInfo);
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END,
				MethodConstants.GET_JIRADETAILS_BY_USERID);
		return responseStatus;
	}

	@Override
	public List<String> getDates(String lastLoggedDate) {
		List<String> dates = new ArrayList<>();
		LocalDate currentDate = LocalDate.now();
		LocalDate loggedDate = LocalDate.parse(lastLoggedDate.substring(0, lastLoggedDate.indexOf('T')));
		if (loggedDate.isBefore(currentDate)) {
			int days = Days.daysBetween(loggedDate, currentDate).getDays();
			for (int i = 1; i <= days; i++) {
				dates.add(loggedDate.plusDays(i).toString());
			}
		}
		return dates;
	}

	@Override
	public List<JiraTO> getJiraSearchDetails(String issueKey) throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START,
				MethodConstants.GET_JIRASEARCHDETAILS);
		String jiraDetailsfromApi = jiraApi.getJiraSearchDetails(issueKey);
		List<JiraTO> jiraDetails = filterJiraDetails(jiraDetailsfromApi, null);
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END,
				MethodConstants.GET_JIRASEARCHDETAILS);
		return jiraDetails;
	}
}
