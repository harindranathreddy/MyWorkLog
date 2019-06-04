package com.cerner.shipit.taskmanagement.service.serviceimpl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.cerner.shipit.taskmanagement.exception.TaskManagementServiceException;
import com.cerner.shipit.taskmanagement.service.service.JiraAuthentactionService;
import com.cerner.shipit.taskmanagement.utility.constant.ErrorCodes;
import com.cerner.shipit.taskmanagement.utility.constant.ErrorMessages;

@Service
@Component("JiraAuthentactionServiceImpl")
public class JiraAuthentactionServiceImpl implements JiraAuthentactionService {

	@Override
	public int authenticateUser(String userName, String password) throws TaskManagementServiceException {
		URL jiraURL;
		int responseCode = 0;
		try {
			jiraURL = new URL("https://jira2.cerner.com");
			HttpURLConnection connection = (HttpURLConnection) jiraURL.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "*/*");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			String userCredentials = userName + ":" + password;
			String basicAuth = "Basic " + new String(new Base64().encode(userCredentials.getBytes()));
			connection.setRequestProperty("Authorization", basicAuth);
			connection.connect();
			responseCode = connection.getResponseCode();
			if (responseCode != 200) {
				throw new TaskManagementServiceException(ErrorCodes.E05, ErrorMessages.FAILED_TO_AUTHENTICATE_USER);
			}
		} catch (final UnsupportedEncodingException e) {
			throw new TaskManagementServiceException(ErrorCodes.E05, ErrorMessages.FAILED_TO_AUTHENTICATE_USER);
		} catch (final ClientProtocolException e) {
			throw new TaskManagementServiceException(ErrorCodes.E05, ErrorMessages.FAILED_TO_AUTHENTICATE_USER);
		} catch (final IOException e) {
			throw new TaskManagementServiceException(ErrorCodes.E05, ErrorMessages.FAILED_TO_AUTHENTICATE_USER);
		}
		return responseCode;
	}

}
