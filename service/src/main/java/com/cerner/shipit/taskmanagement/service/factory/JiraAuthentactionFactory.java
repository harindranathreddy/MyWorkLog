package com.cerner.shipit.taskmanagement.service.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cerner.shipit.taskmanagement.service.service.JiraAuthentactionService;
import com.cerner.shipit.taskmanagement.service.serviceimpl.JiraCookieBasedAuthentactionServiceImpl;
import com.cerner.shipit.taskmanagement.utility.constant.GeneralConstants;
import com.cerner.shipit.taskmanagement.utility.constant.MethodConstants;

@Service
public class JiraAuthentactionFactory {

	Logger logger = LoggerFactory.getLogger(JiraDetailsFactory.class);

	@Autowired
	@Qualifier("JiraAuthentactionServiceImpl")
	private JiraAuthentactionService jiraAuthenticationService;

	@Autowired
	@Qualifier("JiraCookieBasedAuthentactionServiceImpl")
	private JiraCookieBasedAuthentactionServiceImpl jiraCookieBasedAuthentactionServiceImpl;

	public JiraAuthentactionService getJiraAuthentactionServiceInstance(String className) {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START,
				MethodConstants.GET_JIRAAUTHENTACTION_SERVICE_INSTANCE);
		JiraAuthentactionService jiraAuthentactionServiceInstance = null;
		if ("JiraAuthentactionServiceImpl".equalsIgnoreCase(className)) {
			jiraAuthentactionServiceInstance = jiraAuthenticationService;
		} else if ("JiraCookieBasedAuthentactionServiceImpl".equalsIgnoreCase(className)) {
			jiraAuthentactionServiceInstance = jiraCookieBasedAuthentactionServiceImpl;
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END,
				MethodConstants.GET_JIRAAUTHENTACTION_SERVICE_INSTANCE);
		return jiraAuthentactionServiceInstance;

	}

}
