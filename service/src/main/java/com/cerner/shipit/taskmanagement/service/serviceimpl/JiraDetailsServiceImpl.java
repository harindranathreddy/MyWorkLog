package com.cerner.shipit.taskmanagement.service.serviceimpl;

import java.util.ArrayList;
import java.util.List;

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

@Service
@Component("jiraDetailsServiceImpl")
public class JiraDetailsServiceImpl implements JiraDetailsService {

	Logger logger = LoggerFactory.getLogger(JiraDetailsServiceImpl.class);

	@Autowired
	JiraApi jiraApi;

	@Override
	public List<JiraTO> getJiraDetailsByUserId(String UserId) throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START,
				MethodConstants.GET_JIRADETAILS_BY_USERID);
		List<JiraTO> jiraDetails = new ArrayList<>();
		String jiraDetailsfromApi = jiraApi.getInProgressJiraDetailsByUserId(UserId);
		jiraDetails = filterJiraDetails(jiraDetailsfromApi);
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END,
				MethodConstants.GET_JIRADETAILS_BY_USERID);
		return jiraDetails;
	}

	private List<JiraTO> filterJiraDetails(String jiraDetailsfromApi) throws TaskManagementServiceException {
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
				JSONObject fields = jiraObject.getJSONObject("fields");
				jira.setDescription(fields.getString("description"));
				jira.setSummery(fields.getString("summary"));
				JSONObject status = fields.getJSONObject("status");
				jira.setStatus(status.getString("name"));
				JSONObject issueType = fields.getJSONObject("issuetype");
				jira.setType(issueType.getString("name"));
				jiraDetails.add(jira);
			}
		} catch (JSONException e) {
			logger.error(ErrorCodes.E04, ErrorMessages.FILTER_JIRA_DETAILS);
			throw new TaskManagementServiceException(ErrorCodes.E04, ErrorMessages.FILTER_JIRA_DETAILS);
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.FILTER_JIRADETAILS);
		return jiraDetails;
	}
}
