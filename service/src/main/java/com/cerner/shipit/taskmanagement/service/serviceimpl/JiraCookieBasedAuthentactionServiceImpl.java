package com.cerner.shipit.taskmanagement.service.serviceimpl;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.cerner.shipit.taskmanagement.dao.entitymanager.DAOImpl;
import com.cerner.shipit.taskmanagement.exception.TaskManagementDBException;
import com.cerner.shipit.taskmanagement.exception.TaskManagementServiceException;
import com.cerner.shipit.taskmanagement.service.jiraapi.JiraApi;
import com.cerner.shipit.taskmanagement.service.service.JiraAuthentactionService;
import com.cerner.shipit.taskmanagement.utility.constant.ErrorCodes;
import com.cerner.shipit.taskmanagement.utility.constant.ErrorMessages;
import com.cerner.shipit.taskmanagement.utility.constant.GeneralConstants;
import com.cerner.shipit.taskmanagement.utility.constant.MethodConstants;
import com.cerner.shipit.taskmanagement.utility.converter.TOToEntity;
import com.cerner.shipit.taskmanagement.utility.entity.User;
import com.cerner.shipit.taskmanagement.utility.enums.Role;
import com.cerner.shipit.taskmanagement.utility.tos.LoginResponseTO;
import com.cerner.shipit.taskmanagement.utility.tos.UserTO;

@Service
@Component("JiraCookieBasedAuthentactionServiceImpl")
public class JiraCookieBasedAuthentactionServiceImpl implements JiraAuthentactionService {

	Logger logger = LoggerFactory.getLogger(JiraCookieBasedAuthentactionServiceImpl.class);

	@Autowired
	JiraApi jiraApi;

	@Autowired
	private DAOImpl daoImpl;

	@Override
	public LoginResponseTO authenticateUser(String userName, String password) throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START, MethodConstants.AUTHENTICATE_USER);
		LoginResponseTO response = null;
		TOToEntity toConverter = new TOToEntity();
		try {
			response = jiraApi.authenticateUserforCookies(userName, password);
			UserTO userTO = daoImpl.findByUserId(userName);
			if (response.getStatus() == 200) {
				if (userTO == null) {
					String userDetails = jiraApi.getUserDetailsFromJira(userName, response.getHeaders());
					if (userDetails != null) {
						userTO = updateUserDetails(userDetails, userName);
					} else {
						userDetails = jiraApi.getInProgressJiraDetailsByUserId(userName);
						if (userDetails != null) {
							userTO = updateUserDetails(userDetails, userName);
						}
						if (userTO.getUserId() == null) {
							userTO = new UserTO();
							userTO.setUserId(userName);
							userTO.setNotification(false);
							userTO.setRole(Role.ASSOCIATE.toString());
						}
					}
					if (userTO != null) {
						User user = toConverter.convertUserTOtoUserEntity(userTO);
						if (daoImpl.createUser(user)) {
							logger.info(GeneralConstants.LOGGER_FORMAT_2, ErrorCodes.U05, ErrorMessages.USER_CREATED,
									userName);
						}
					}
				} else {
					User user = toConverter.convertUserTOtoUserEntity(userTO);
					daoImpl.updateUserSummaryWithSuccess(user);
				}
				response.setUserTO(userTO);
			} else {
				if (userTO != null) {
					User user = toConverter.convertUserTOtoUserEntity(userTO);
					daoImpl.updateUserSummaryWithFailure(user);
				}
			}
		} catch (TaskManagementDBException e) {
			throw new TaskManagementServiceException(e);
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.AUTHENTICATE_USER);
		return response;
	}

	private UserTO updateUserDetails(String userDetails, String userId) throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START,
				MethodConstants.UPDATE_USER_DETAILS);
		UserTO userTO = new UserTO();
		try {
			JSONObject responseObject = new JSONObject(userDetails);
			if (responseObject.getJSONArray("issues") != null) {
				JSONObject jiraObject = responseObject.getJSONArray("issues").getJSONObject(0);
				JSONObject fields = jiraObject.getJSONObject("fields");
				JSONObject assignedUser = null;
				if (!fields.isNull("assignee")) {
					assignedUser = fields.getJSONObject("assignee");
				}
				JSONObject solutionDesigner = null;
				if (!fields.isNull("customfield_16200")) {
					solutionDesigner = fields.getJSONObject("customfield_16200");
				}
				JSONObject testAnalyst = null;
				if (!fields.isNull("customfield_14500")) {
					testAnalyst = fields.getJSONObject("customfield_14500");
				}
				if (assignedUser.getString("key").equalsIgnoreCase(userId)) {
					userTO.setRole(Role.ENGINEER.toString());
					responseObject = assignedUser;
				} else if (solutionDesigner.getString("key").equalsIgnoreCase(userId)) {
					userTO.setRole(Role.SOLUTION_DESIGNER.toString());
					responseObject = solutionDesigner;
				} else if (testAnalyst.getString("key").equalsIgnoreCase(userId)) {
					userTO.setRole(Role.TEST_ANALYST.toString());
					responseObject = testAnalyst;
				}
			}
			if ((responseObject != null) && responseObject.has("displayName")) {
				userTO.setName(responseObject.getString("displayName"));
				userTO.setMailId(responseObject.getString("emailAddress"));
				userTO.setUserId(responseObject.getString("key"));
				JSONObject avatarObject = responseObject.getJSONObject("avatarUrls");
				userTO.setAvatar(avatarObject.getString("32x32"));
			}
		} catch (JSONException e) {
			logger.error(ErrorCodes.U04, ErrorMessages.FAILED_DURING_FILTERING_USER_DETAILS);
			throw new TaskManagementServiceException(ErrorCodes.U04,
					ErrorMessages.FAILED_DURING_FILTERING_USER_DETAILS);
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.UPDATE_USER_DETAILS);
		return userTO;
	}

}
