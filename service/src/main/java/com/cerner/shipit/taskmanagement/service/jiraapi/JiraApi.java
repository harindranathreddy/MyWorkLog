package com.cerner.shipit.taskmanagement.service.jiraapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cerner.shipit.taskmanagement.exception.TaskManagementServiceException;
import com.cerner.shipit.taskmanagement.utility.constant.ErrorCodes;
import com.cerner.shipit.taskmanagement.utility.constant.ErrorMessages;
import com.cerner.shipit.taskmanagement.utility.constant.GeneralConstants;
import com.cerner.shipit.taskmanagement.utility.constant.MethodConstants;

@Service
public class JiraApi {

	Logger logger = LoggerFactory.getLogger(JiraApi.class);

	public String getInProgressJiraDetailsByUserId(String UserId) throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START,
				MethodConstants.GET_JIRADETAILS_BY_USERID);
		String jiraDetails;
		try {
			URL jiraURL = new URL("https://jira2.cerner.com/rest/api/2/search?jql=assignee=%22" + UserId
					+ "%22+AND+status=%22IN%20PROGRESS%22");
			HttpURLConnection connection = (HttpURLConnection) jiraURL.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			Reader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
			jiraDetails = IOUtils.toString(in);
		} catch (final UnsupportedEncodingException e) {
			logger.error(ErrorCodes.E01, ErrorMessages.JIRA_DETAILS);
			throw new TaskManagementServiceException(ErrorCodes.E01, ErrorMessages.JIRA_DETAILS);
		} catch (final ClientProtocolException e) {
			logger.error(ErrorCodes.E02, ErrorMessages.JIRA_DETAILS);
			throw new TaskManagementServiceException(ErrorCodes.E02, ErrorMessages.JIRA_DETAILS);
		} catch (final IOException e) {
			logger.error(ErrorCodes.E03, ErrorMessages.JIRA_DETAILS);
			throw new TaskManagementServiceException(ErrorCodes.E03, ErrorMessages.JIRA_DETAILS);
		}

		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END,
				MethodConstants.GET_JIRADETAILS_BY_USERID);
		return jiraDetails;
	}
}
