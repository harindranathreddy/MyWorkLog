package com.cerner.shipit.taskmanagement.service.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cerner.shipit.taskmanagement.service.service.JiraAuthentactionService;
import com.cerner.shipit.taskmanagement.utility.constant.GeneralConstants;
import com.cerner.shipit.taskmanagement.utility.constant.MethodConstants;

@Service
public class JiraAuthentactionFactory {

	Logger logger = LoggerFactory.getLogger(JiraDetailsFactory.class);

	@Autowired
	@Qualifier("JiraAuthentactionServiceImpl")
	private JiraAuthentactionService jiraAuthenticationService;

	public JiraAuthentactionService getJiraAuthentactionServiceInstance(String className) {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START,
				MethodConstants.GET_JIRAAUTHENTACTION_SERVICE_INSTANCE);
		JiraAuthentactionService jiraAuthentactionServiceInstance = null;
		if ("JiraAuthentactionServiceImpl".equalsIgnoreCase(className)) {
			jiraAuthentactionServiceInstance = jiraAuthenticationService;
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END,
				MethodConstants.GET_JIRADETAILS_SERVICE_INSTANCE);
		return jiraAuthentactionServiceInstance;

	}

}
