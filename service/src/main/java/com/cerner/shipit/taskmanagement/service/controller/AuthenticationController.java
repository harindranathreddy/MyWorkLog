package com.cerner.shipit.taskmanagement.service.controller;

import java.util.Locale;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
import com.cerner.shipit.taskmanagement.service.factory.JiraAuthentactionFactory;
import com.cerner.shipit.taskmanagement.service.factory.UserDetailsFactory;
import com.cerner.shipit.taskmanagement.service.service.JiraAuthentactionService;
import com.cerner.shipit.taskmanagement.service.service.UserDetailsService;
import com.cerner.shipit.taskmanagement.utility.constant.ErrorCodes;
import com.cerner.shipit.taskmanagement.utility.constant.ErrorMessages;
import com.cerner.shipit.taskmanagement.utility.constant.GeneralConstants;
import com.cerner.shipit.taskmanagement.utility.constant.MethodConstants;
import com.cerner.shipit.taskmanagement.utility.response.Response;
import com.cerner.shipit.taskmanagement.utility.tos.AuthTO;
import com.cerner.shipit.taskmanagement.utility.tos.LoginResponseTO;
import com.cerner.shipit.taskmanagement.utility.tos.UserTO;

@CrossOrigin
@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

	Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	@Autowired
	private JiraAuthentactionFactory jiraAuthentactionFactory;

	@Autowired
	private UserDetailsFactory userDetailsFactory;

	/**
	 * This will authenticate the user and send the response.
	 *
	 * @param userName
	 * @param password
	 */
	@PostMapping(value = "/authUser", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> authenticateUser(@RequestBody AuthTO authTO) {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START, MethodConstants.AUTHENTICATE_USER);
		ResponseEntity<Object> responseEntity = null;
		Response response = new Response();
		try {
			final JiraAuthentactionService jiraAuthenticateService = jiraAuthentactionFactory
					.getJiraAuthentactionServiceInstance("JiraCookieBasedAuthentactionServiceImpl");
			LoginResponseTO responseObject = jiraAuthenticateService.authenticateUser(
					authTO.getUsername().toUpperCase(Locale.ROOT),
					new String(Base64.decodeBase64(authTO.getPassword().getBytes())));
			if ((responseObject != null) && (responseObject.getStatus() == 200)) {
				HttpHeaders hpptHeaders = new HttpHeaders();
				hpptHeaders.addAll("Set-Cookie", responseObject.getHeaders().get("Set-Cookie"));
				responseEntity = ResponseEntity.status(HttpStatus.OK).headers(hpptHeaders)
						.body(response.getSuccessResposne(ErrorCodes.L01, ErrorMessages.LOGIN_SUCCESSFUL,
								responseObject.getUserTO()));
				logger.info(GeneralConstants.LOGGER_FORMAT_2, ErrorCodes.L01, ErrorMessages.LOGIN_SUCCESSFUL,
						authTO.getUsername());
			} else if ((responseObject != null) && (responseObject.getStatus() == 401)) {
				HttpHeaders hpptHeaders = new HttpHeaders();
				responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(hpptHeaders)
						.body(response.getSuccessResposne(ErrorCodes.L02, ErrorMessages.FAILED_TO_AUTHENTICATE_USER,
								responseObject.getUserTO()));
				logger.info(GeneralConstants.LOGGER_FORMAT_2, ErrorCodes.L02, ErrorMessages.FAILED_TO_AUTHENTICATE_USER,
						authTO.getUsername());
			} else {
				responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(response.getErrorResponse(ErrorCodes.L02, ErrorMessages.LOGIN_FAILED));
				logger.info(GeneralConstants.LOGGER_FORMAT_2, ErrorCodes.L02, ErrorMessages.LOGIN_FAILED,
						authTO.getUsername());
			}
		} catch (final TaskManagementServiceException e) {
			logger.error(GeneralConstants.LOGGER_FORMAT, e.getErrorCode(), e.getErrorMessage());
			responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getErrorResponse(e));
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.AUTHENTICATE_USER);
		return responseEntity;
	}

	@GetMapping(value = "/getUser", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getUserDetails(@RequestParam(value = "userId") String userId) {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START, MethodConstants.AUTHENTICATE_USER);
		ResponseEntity<Object> responseEntity = null;
		Response response = new Response();
		try {
			final UserDetailsService userDetailsService = userDetailsFactory
					.getUserDetailsServiceInstance("userDetailsServiceImpl");
			UserTO userTO = userDetailsService.getUserDetailsById(userId.toUpperCase(Locale.ROOT));
			if (userTO.getUserId() != null) {
				responseEntity = ResponseEntity.status(HttpStatus.OK)
						.body(response.getSuccessResposne(ErrorCodes.U06, ErrorMessages.USER_DETAILS_FETCHED, userTO));
			} else {
				responseEntity = ResponseEntity.status(HttpStatus.OK).body(
						response.getSuccessResposne(ErrorCodes.U09, ErrorMessages.USER_DETAILS_NOT_AVAILABLE, null));
			}

		} catch (final TaskManagementServiceException e) {
			logger.error(GeneralConstants.LOGGER_FORMAT, e.getErrorCode(), e.getErrorMessage());
			responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getErrorResponse(e));
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.AUTHENTICATE_USER);
		return responseEntity;
	}
}
