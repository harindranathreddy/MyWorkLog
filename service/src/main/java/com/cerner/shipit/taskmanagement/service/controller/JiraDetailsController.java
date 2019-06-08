package com.cerner.shipit.taskmanagement.service.controller;

import java.util.ArrayList;
import java.util.List;

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
	public Object getJiraDetailsByUserId(@RequestParam(value = "userId") String userId) {
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
	public Object addWorkLog(@RequestBody WorkLogInfoTO workLogInfoTo) {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START, MethodConstants.ADD_WORK_LOG);
		ResponseEntity<Object> responseEntity = null;
		Response response = new Response();
		int responseStatus;
		try {
			final JiraDetailsService jiraDetailsService = jiraDetailsFactory
					.getJiraDetailsServiceInstance("jiraDetailsServiceImpl");
			responseStatus = jiraDetailsService.addWorkLog(workLogInfoTo);
			responseEntity = ResponseEntity.status(HttpStatus.OK).body(response.getSuccessResposne(ErrorCodes.W01,
					ErrorMessages.WORKLOG_ADDED_SUCCESFULLY, responseStatus));
		} catch (final TaskManagementServiceException e) {
			responseEntity = ResponseEntity.status(HttpStatus.OK).body(response.getErrorResponse(e));
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.ADD_WORK_LOG);
		return responseEntity;
	}
}
