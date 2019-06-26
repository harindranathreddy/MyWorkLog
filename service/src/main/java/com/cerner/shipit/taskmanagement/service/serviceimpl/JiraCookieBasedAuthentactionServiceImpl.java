package com.cerner.shipit.taskmanagement.service.serviceimpl;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.cerner.shipit.taskmanagement.exception.TaskManagementServiceException;
import com.cerner.shipit.taskmanagement.service.jiraapi.JiraApi;
import com.cerner.shipit.taskmanagement.service.service.JiraAuthentactionService;
import com.cerner.shipit.taskmanagement.utility.constant.ErrorCodes;
import com.cerner.shipit.taskmanagement.utility.constant.ErrorMessages;
import com.cerner.shipit.taskmanagement.utility.constant.GeneralConstants;
import com.cerner.shipit.taskmanagement.utility.constant.MethodConstants;
import com.cerner.shipit.taskmanagement.utility.tos.LoginResponseTO;
import com.cerner.shipit.taskmanagement.utility.tos.UserTO;

@Service
@Component("JiraCookieBasedAuthentactionServiceImpl")
public class JiraCookieBasedAuthentactionServiceImpl implements JiraAuthentactionService {

	Logger logger = LoggerFactory.getLogger(JiraCookieBasedAuthentactionServiceImpl.class);

	@Autowired
	JiraApi jiraApi;

	@Override
	public LoginResponseTO authenticateUser(String userName, String password) throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START, MethodConstants.AUTHENTICATE_USER);
		LoginResponseTO response = jiraApi.authenticateUserforCookies(userName, password);
		if (response.getStatus() == 200) {
			String userDetails = jiraApi.getUserDetailsFromJira(userName, response.getHeaders());
			if (userDetails != null) {
				updateUserDetails(userDetails, response);
			}
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.AUTHENTICATE_USER);
		return response;
	}

	private void updateUserDetails(String userDetails, LoginResponseTO response) throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START,
				MethodConstants.UPDATE_USER_DETAILS);
		UserTO userTO = new UserTO();
		try {
			JSONObject responseObject = new JSONObject(userDetails);
			userTO.setUserName(responseObject.getString("displayName"));
			userTO.setEmailAddress(responseObject.getString("emailAddress"));
			userTO.setUserId(responseObject.getString("key"));
			JSONObject avatarObject = responseObject.getJSONObject("avatarUrls");
			userTO.setAvatar(avatarObject.getString("32x32"));
			response.setUserTO(userTO);
		} catch (JSONException e) {
			logger.error(ErrorCodes.U04, ErrorMessages.FAILED_DURING_FILTERING_USER_DETAILS);
			throw new TaskManagementServiceException(ErrorCodes.U04,
					ErrorMessages.FAILED_DURING_FILTERING_USER_DETAILS);
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.UPDATE_USER_DETAILS);
	}

}
