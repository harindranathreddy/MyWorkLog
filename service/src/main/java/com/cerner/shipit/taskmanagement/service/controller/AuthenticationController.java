package com.cerner.shipit.taskmanagement.service.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cerner.shipit.taskmanagement.exception.TaskManagementServiceException;
import com.cerner.shipit.taskmanagement.service.factory.JiraAuthentactionFactory;
import com.cerner.shipit.taskmanagement.service.service.JiraAuthentactionService;
import com.cerner.shipit.taskmanagement.utility.constant.ErrorCodes;
import com.cerner.shipit.taskmanagement.utility.constant.ErrorMessages;
import com.cerner.shipit.taskmanagement.utility.constant.GeneralConstants;
import com.cerner.shipit.taskmanagement.utility.constant.MethodConstants;
import com.cerner.shipit.taskmanagement.utility.response.Response;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

	Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	@Autowired
	private JiraAuthentactionFactory jiraAuthentactionFactory;

	/**
	 * This will authenticate the user and send the response.
	 *
	 * @param userName
	 * @param password
	 */
	@GetMapping(value = "/authUser", produces = MediaType.APPLICATION_JSON_VALUE)
	public Object authenticateUser(@RequestParam(value = "userName") String userName,
			@RequestParam(value = "password") String password) {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START, MethodConstants.AUTHENTICATE_USER);
		ResponseEntity<Object> responseEntity = null;
		Response response = new Response();
		try {
			final JiraAuthentactionService jiraAuthenticateService = jiraAuthentactionFactory
					.getJiraAuthentactionServiceInstance("JiraAuthentactionServiceImpl");
			int responseStatus = jiraAuthenticateService.authenticateUser(userName, password);
			if (responseStatus == 200) {
				responseEntity = ResponseEntity.status(HttpStatus.OK)
						.body(response.getSuccessResposne(ErrorCodes.L01, ErrorMessages.LOGIN_SUCCESSFUL, null));
			} else {
				responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(response.getErrorResponse(ErrorCodes.L02, ErrorMessages.LOGIN_FAILED));
			}
		} catch (final TaskManagementServiceException e) {
			responseEntity = ResponseEntity.status(HttpStatus.OK).body(response.getErrorResponse(e));
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END,
				MethodConstants.GET_JIRADETAILS_BY_USERID);
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.AUTHENTICATE_USER);
		return responseEntity;
	}

	public static void main(String[] args) {
		URL jiraURL;
		try {
			jiraURL = new URL("https://jira2.cerner.com/");
			HttpURLConnection connection = (HttpURLConnection) jiraURL.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Accept", "*/*");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			String userCredentials = "HK063220:H@r!ndra@3oo9";
			String basicAuth = "Basic " + new String(new Base64().encode(userCredentials.getBytes()));
			connection.setRequestProperty("Authorization", basicAuth);
			connection.connect();
			Reader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
			System.out.println(connection.getResponseCode());
			for (int c; (c = in.read()) >= 0; System.out.print((char) c)) {
				;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
