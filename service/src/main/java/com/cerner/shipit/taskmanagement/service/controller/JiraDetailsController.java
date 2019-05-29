package com.cerner.shipit.taskmanagement.service.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cerner.shipit.taskmanagement.exception.TaskManagementServiceException;
import com.cerner.shipit.taskmanagement.service.factory.JiraDetailsFactory;
import com.cerner.shipit.taskmanagement.service.service.JiraDetailsService;
import com.cerner.shipit.taskmanagement.utility.constant.GeneralConstants;
import com.cerner.shipit.taskmanagement.utility.constant.MethodConstants;
import com.cerner.shitit.taskmanagement.utility.tos.JiraDetails;

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
	public Object fetchJiraDetailsByUserId(@RequestParam(value = "userId") String userId)
			throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START,
				MethodConstants.FETCH_JIRADETAILS_BY_USERID);
		List<JiraDetails> jiraDetilas = new ArrayList<JiraDetails>();
		try {
			final JiraDetailsService jiraDetailsService = jiraDetailsFactory
					.getJiraDetailsServiceInstance("jiraDetailsServiceImpl");
			jiraDetilas = jiraDetailsService.fetchJiraDetailsByUserId(userId);
		} catch (final TaskManagementServiceException e) {
			throw e;
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END,
				MethodConstants.FETCH_JIRADETAILS_BY_USERID);
		return jiraDetilas;
	}
}
