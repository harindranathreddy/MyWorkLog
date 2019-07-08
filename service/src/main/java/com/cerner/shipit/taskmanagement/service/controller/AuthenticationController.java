package com.cerner.shipit.taskmanagement.service.controller;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cerner.shipit.taskmanagement.exception.TaskManagementServiceException;
import com.cerner.shipit.taskmanagement.service.factory.JiraAuthentactionFactory;
import com.cerner.shipit.taskmanagement.service.service.JiraAuthentactionService;
import com.cerner.shipit.taskmanagement.utility.constant.ErrorCodes;
import com.cerner.shipit.taskmanagement.utility.constant.ErrorMessages;
import com.cerner.shipit.taskmanagement.utility.constant.GeneralConstants;
import com.cerner.shipit.taskmanagement.utility.constant.MethodConstants;
import com.cerner.shipit.taskmanagement.utility.response.Response;
import com.cerner.shipit.taskmanagement.utility.tos.AuthTO;
import com.cerner.shipit.taskmanagement.utility.tos.LoginResponseTO;

@CrossOrigin
@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

	Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	@Autowired
	private JiraAuthentactionFactory jiraAuthentactionFactory;

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
			LoginResponseTO responseObject = jiraAuthenticateService.authenticateUser(authTO.getUsername(),
					new String(Base64.decodeBase64(authTO.getPassword().getBytes())));
			if ((responseObject != null) && (responseObject.getStatus() == 200)) {
				HttpHeaders hpptHeaders = new HttpHeaders();
				hpptHeaders.addAll("Set-Cookie", responseObject.getHeaders().get("Set-Cookie"));
				responseEntity = ResponseEntity.status(HttpStatus.OK).headers(hpptHeaders)
						.body(response.getSuccessResposne(ErrorCodes.L01, ErrorMessages.LOGIN_SUCCESSFUL,
								responseObject.getUserTO()));
				logger.info(GeneralConstants.LOGGER_FORMAT_2, ErrorCodes.L01, ErrorMessages.LOGIN_SUCCESSFUL,
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
}
