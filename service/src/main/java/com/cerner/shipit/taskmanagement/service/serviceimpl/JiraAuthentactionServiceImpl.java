package com.cerner.shipit.taskmanagement.service.serviceimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.cerner.shipit.taskmanagement.exception.TaskManagementServiceException;
import com.cerner.shipit.taskmanagement.service.jiraapi.JiraApi;
import com.cerner.shipit.taskmanagement.service.service.JiraAuthentactionService;
import com.cerner.shipit.taskmanagement.utility.constant.GeneralConstants;
import com.cerner.shipit.taskmanagement.utility.constant.MethodConstants;

@Service
@Component("JiraAuthentactionServiceImpl")
public class JiraAuthentactionServiceImpl implements JiraAuthentactionService {

	Logger logger = LoggerFactory.getLogger(JiraAuthentactionServiceImpl.class);

	@Autowired
	JiraApi jiraApi;

	@Override
	public int authenticateUser(String userName, String password) throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START, MethodConstants.AUTHENTICATE_USER);
		int responseStatus = jiraApi.authenticateUser(userName, password);
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.AUTHENTICATE_USER);
		return responseStatus;
	}

}
