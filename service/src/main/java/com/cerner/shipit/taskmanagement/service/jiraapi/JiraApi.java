package com.cerner.shipit.taskmanagement.service.jiraapi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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

	public CloseableHttpResponse fetchJiraDetailsById(String UserId) throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START,
				MethodConstants.FETCH_JIRADETAILS_BY_USERID);
		CloseableHttpResponse response = null;
		try {
			final CloseableHttpClient client = HttpClients.createDefault();
			final HttpGet httpGet = new HttpGet(
					"https://jira2.cerner.com/rest/api/2/search?jql=assignee=%22" + UserId + "%22");
			response = client.execute(httpGet);
			client.close();
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
				MethodConstants.FETCH_JIRADETAILS_BY_USERID);
		return response;
	}
}
