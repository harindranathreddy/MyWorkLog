package com.cerner.shipit.taskmanagement.service.jiraapi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
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
import com.cerner.shipit.taskmanagement.utility.tos.WorkLogDetailsTO;
import com.cerner.shipit.taskmanagement.utility.tos.WorkLogInfoTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class JiraApi {

	Logger logger = LoggerFactory.getLogger(JiraApi.class);

	public String getInProgressJiraDetailsByUserId(String UserId) throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START,
				MethodConstants.GET_JIRADETAILS_BY_USERID);
		String jiraDetails;
		HttpURLConnection connection = null;
		try {
			URL jiraURL = new URL("https://jira2.cerner.com/rest/api/2/search?jql=assignee=%22" + UserId
					+ "%22+AND+status=%22IN%20PROGRESS%22");
			connection = (HttpURLConnection) jiraURL.openConnection();
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
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END,
				MethodConstants.GET_JIRADETAILS_BY_USERID);
		return jiraDetails;
	}

	public String getWorkLogDataByJiraId(String issueKey) throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START,
				MethodConstants.GET_WORKLOG_DATA_BY_JIRAID);
		String jiraDetails;
		HttpURLConnection connection = null;
		try {
			URL jiraURL = new URL("https://jira2.cerner.com/rest/api/2/issue/" + issueKey + "/worklog");
			connection = (HttpURLConnection) jiraURL.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			Reader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
			jiraDetails = IOUtils.toString(in);
		} catch (final UnsupportedEncodingException e) {
			logger.error(ErrorCodes.E01, ErrorMessages.WORKLOG_DETAILS);
			throw new TaskManagementServiceException(ErrorCodes.E01, ErrorMessages.WORKLOG_DETAILS);
		} catch (final ClientProtocolException e) {
			logger.error(ErrorCodes.E02, ErrorMessages.WORKLOG_DETAILS);
			throw new TaskManagementServiceException(ErrorCodes.E02, ErrorMessages.WORKLOG_DETAILS);
		} catch (final IOException e) {
			logger.error(ErrorCodes.E03, ErrorMessages.WORKLOG_DETAILS);
			throw new TaskManagementServiceException(ErrorCodes.E03, ErrorMessages.WORKLOG_DETAILS);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END,
				MethodConstants.GET_WORKLOG_DATA_BY_JIRAID);
		return jiraDetails;
	}

	public String putJiraDetailsByUserId(WorkLogInfoTO workLogInfo) throws TaskManagementServiceException {
		HttpURLConnection connection = null;
		try {
			String auth = new String(
					Base64.encodeBase64((workLogInfo.getUserName() + ":" + workLogInfo.getPassword()).getBytes()));
			List<WorkLogDetailsTO> workLog = workLogInfo.getWorklogs();
			Iterator<WorkLogDetailsTO> iterator = workLog.iterator();
			while (iterator.hasNext()) {
				WorkLogDetailsTO temp = iterator.next();
				URL jiraURL;
				jiraURL = new URL("https://jira2.cerner.com/rest/api/latest/issue/" + temp.getId()
						+ "/worklog?adjustEstimate=AUTO&newEstimate&reduceBy");
				temp.setId("");
				connection = (HttpURLConnection) jiraURL.openConnection();
				temp.setStarted((new Date()).toString());
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setRequestProperty("Accept", "application/json");
				connection.setRequestProperty("Authorization", "Basic " + auth);
				connection.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
				ObjectMapper Obj = new ObjectMapper();
				String jsonStr = Obj.writeValueAsString(temp);
				wr.writeBytes(jsonStr);
				wr.flush();
				wr.close();
				connection.connect();
				System.out.println(connection.getResponseCode());
				System.out.println(connection.getResponseMessage());
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

		return null;

	}
}
