package com.cerner.shipit.taskmanagement.service.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cerner.shipit.taskmanagement.service.service.JiraDetailsService;
import com.cerner.shipit.taskmanagement.utility.constant.GeneralConstants;
import com.cerner.shipit.taskmanagement.utility.constant.MethodConstants;

@Service
public class JiraDetailsFactory {

	Logger logger = LoggerFactory.getLogger(JiraDetailsFactory.class);

	@Autowired
	@Qualifier("jiraDetailsServiceImpl")
	private JiraDetailsService jiraDetails;

	public JiraDetailsService getJiraDetailsServiceInstance(String className) {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START,
				MethodConstants.GET_JIRADETAILS_SERVICE_INSTANCE);
		JiraDetailsService jiraDetailsServiceInstance = null;
		if ("jiraDetailsServiceImpl".equalsIgnoreCase(className)) {
			jiraDetailsServiceInstance = jiraDetails;
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END,
				MethodConstants.GET_JIRADETAILS_SERVICE_INSTANCE);
		return jiraDetailsServiceInstance;

	}
}
