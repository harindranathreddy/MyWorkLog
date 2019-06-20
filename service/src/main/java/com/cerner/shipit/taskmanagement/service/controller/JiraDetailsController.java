package com.cerner.shipit.taskmanagement.service.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cerner.shipit.taskmanagement.exception.TaskManagementServiceException;
import com.cerner.shipit.taskmanagement.service.factory.JiraDetailsFactory;
import com.cerner.shipit.taskmanagement.service.service.JiraDetailsService;
import com.cerner.shipit.taskmanagement.utility.constant.ErrorCodes;
import com.cerner.shipit.taskmanagement.utility.constant.ErrorMessages;
import com.cerner.shipit.taskmanagement.utility.constant.GeneralConstants;
import com.cerner.shipit.taskmanagement.utility.constant.MethodConstants;
import com.cerner.shipit.taskmanagement.utility.response.Response;
import com.cerner.shipit.taskmanagement.utility.tos.JiraTO;
import com.cerner.shipit.taskmanagement.utility.tos.WorkLogInfoTO;

@CrossOrigin
@RestController
@RequestMapping("/details")
public class JiraDetailsController {

	Logger logger = LoggerFactory.getLogger(JiraDetailsController.class);

	@Autowired
	private JiraDetailsFactory jiraDetailsFactory;

	/**
	 * This will fetch the user jiras based on the user Id.
	 *
	 * @param userId
	 * @return
	 * @throws TaskManagementServiceException
	 */
	@GetMapping(value = "/jiraDetailsByUserId", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getJiraDetailsByUserId(@RequestParam(value = "userId") String userId) {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START,
				MethodConstants.GET_JIRADETAILS_BY_USERID);
		List<JiraTO> jiraDetilas = new ArrayList<>();
		ResponseEntity<Object> responseEntity = null;
		Response response = new Response();
		try {
			final JiraDetailsService jiraDetailsService = jiraDetailsFactory
					.getJiraDetailsServiceInstance("jiraDetailsServiceImpl");
			jiraDetilas = jiraDetailsService.getJiraDetailsByUserId(userId);
			responseEntity = ResponseEntity.status(HttpStatus.OK).body(response.getSuccessResposne(ErrorCodes.J01,
					ErrorMessages.JIRA_DETAIALS_FETCHED_SUCCESFULLY, jiraDetilas));
		} catch (final TaskManagementServiceException e) {
			responseEntity = ResponseEntity.status(HttpStatus.OK).body(response.getErrorResponse(e));
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END,
				MethodConstants.GET_JIRADETAILS_BY_USERID);
		return responseEntity;
	}

	// Logging the work
	@PostMapping(value = "/addWorkLog", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addWorkLog(@RequestBody WorkLogInfoTO workLogInfoTo) {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START, MethodConstants.ADD_WORK_LOG);
		ResponseEntity<Object> responseEntity = null;
		Response response = new Response();
		int responseStatus;
		try {
			final JiraDetailsService jiraDetailsService = jiraDetailsFactory
					.getJiraDetailsServiceInstance("jiraDetailsServiceImpl");
			workLogInfoTo.setPassword(new String(Base64.decodeBase64(workLogInfoTo.getPassword().getBytes())));
			responseStatus = jiraDetailsService.addWorkLog(workLogInfoTo);
			responseEntity = ResponseEntity.status(HttpStatus.OK).body(response.getSuccessResposne(ErrorCodes.W01,
					ErrorMessages.WORKLOG_ADDED_SUCCESFULLY, responseStatus));
		} catch (final TaskManagementServiceException e) {
			responseEntity = ResponseEntity.status(HttpStatus.OK).body(response.getErrorResponse(e));
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.ADD_WORK_LOG);
		return responseEntity;
	}

	@GetMapping(value = "/getDates", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getDates(@RequestParam(value = "lastLoggedDate") String lastLoggedDate) {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START, MethodConstants.GET_DATES);
		List<String> dates = new ArrayList<>();
		ResponseEntity<Object> responseEntity = null;
		Response response = new Response();
		final JiraDetailsService jiraDetailsService = jiraDetailsFactory
				.getJiraDetailsServiceInstance("jiraDetailsServiceImpl");
		if (!lastLoggedDate.equalsIgnoreCase("0")) {
			dates = jiraDetailsService.getDates(lastLoggedDate);
		} else {
			dates = null;
		}
		if ((dates != null) && !dates.isEmpty()) {
			responseEntity = ResponseEntity.status(HttpStatus.OK)
					.body(response.getSuccessResposne(ErrorCodes.D01, ErrorMessages.DATES_FETCHED_SUCCESFULLY, dates));
		} else {
			responseEntity = ResponseEntity.status(HttpStatus.OK)
					.body(response.getSuccessResposne(ErrorCodes.D02, ErrorMessages.WORK_LOGGED, dates));
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.GET_DATES);
		return responseEntity;
	}

	/**
	 * This will fetch the jiras based on the Jira Id.
	 *
	 * @param IssueKey
	 * @return
	 * @throws TaskManagementServiceException
	 */
	@GetMapping(value = "/jiraDetailsByJiraId", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getJiraDetailsByJiraId(@RequestParam(value = "issueKey") String issueKey) {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START,
				MethodConstants.GET_JIRADETAILS_BY_JIRAID);
		List<JiraTO> jiraDetials = new ArrayList<>();
		ResponseEntity<Object> responseEntity = null;
		Response response = new Response();
		try {
			final JiraDetailsService jiraDetailsService = jiraDetailsFactory
					.getJiraDetailsServiceInstance("jiraDetailsServiceImpl");
			jiraDetials = jiraDetailsService.getJiraSearchDetails(issueKey);
			if (!jiraDetials.isEmpty()) {
				responseEntity = ResponseEntity.status(HttpStatus.OK).body(response.getSuccessResposne(ErrorCodes.JS04,
						ErrorMessages.JIRA_DETAIALS_FETCHED_SUCCESFULLY, jiraDetials));
			} else {
				responseEntity = ResponseEntity.status(HttpStatus.OK)
						.body(response.getSuccessResposne(ErrorCodes.JS05, ErrorMessages.JIRA_NOT_AVAILABLE, null));
			}
		} catch (final TaskManagementServiceException e) {
			responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getErrorResponse(e));
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END,
				MethodConstants.GET_JIRADETAILS_BY_JIRAID);
		return responseEntity;
	}
}
