package com.cerner.shipit.taskmanagement.service.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cerner.shipit.taskmanagement.service.service.UserDetailsService;
import com.cerner.shipit.taskmanagement.utility.constant.GeneralConstants;
import com.cerner.shipit.taskmanagement.utility.constant.MethodConstants;

@Service
public class UserDetailsFactory {
	Logger logger = LoggerFactory.getLogger(JiraDetailsFactory.class);

	@Autowired
	@Qualifier("UserDetailsServiceImpl")
	private UserDetailsService userDetailsService;

	public UserDetailsService getUserDetailsServiceInstance(String className) {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START,
				MethodConstants.GET_USERDETAILS_SERVICE_INSTANCE);
		UserDetailsService userDetailsServiceInstance = null;
		if ("userDetailsServiceImpl".equalsIgnoreCase(className)) {
			userDetailsServiceInstance = userDetailsService;
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END,
				MethodConstants.GET_USERDETAILS_SERVICE_INSTANCE);
		return userDetailsServiceInstance;

	}
}
