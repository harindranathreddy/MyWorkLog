package com.cerner.shipit.taskmanagement.service.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.cerner.shipit.taskmanagement.service.factory.UserDetailsFactory;
import com.cerner.shipit.taskmanagement.service.service.UserDetailsService;
import com.cerner.shipit.taskmanagement.utility.constant.ErrorCodes;
import com.cerner.shipit.taskmanagement.utility.constant.ErrorMessages;
import com.cerner.shipit.taskmanagement.utility.constant.GeneralConstants;
import com.cerner.shipit.taskmanagement.utility.constant.MethodConstants;
import com.cerner.shipit.taskmanagement.utility.response.Response;
import com.cerner.shipit.taskmanagement.utility.tos.TeamTO;
import com.cerner.shipit.taskmanagement.utility.tos.UserTO;

@CrossOrigin
@RestController
@RequestMapping("/userDetails")
public class UserDetailsController {
	Logger logger = LoggerFactory.getLogger(UserDetailsController.class);

	@Autowired
	private UserDetailsFactory userDetailsFactory;

	@PostMapping(value = "/createTeam", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> createTeam(@RequestBody String teamName) {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START, MethodConstants.CREATE_TEAM);
		ResponseEntity<Object> responseEntity = null;
		Response response = new Response();
		final UserDetailsService userDetailsService = userDetailsFactory
				.getUserDetailsServiceInstance("userDetailsServiceImpl");
		boolean isTeamCreated;
		try {
			isTeamCreated = userDetailsService.createTeam(teamName);
			if (isTeamCreated) {
				responseEntity = ResponseEntity.status(HttpStatus.OK).body(
						response.getSuccessResposne(ErrorCodes.T01, ErrorMessages.TEAM_CREATED_SUCCESSFULLY, null));
			} else {
				responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(response.getSuccessResposne(ErrorCodes.T02, ErrorMessages.TEAM_NOT_CREATED, null));
			}
		} catch (TaskManagementServiceException e) {
			responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getErrorResponse(e));
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.CREATE_TEAM);
		return responseEntity;
	}

	@GetMapping(value = "/getAllteams", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getAllteams() {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START, MethodConstants.GET_ALL_TEAMS);
		ResponseEntity<Object> responseEntity = null;
		Response response = new Response();
		final UserDetailsService userDetailsService = userDetailsFactory
				.getUserDetailsServiceInstance("userDetailsServiceImpl");
		List<TeamTO> teams;
		try {
			teams = userDetailsService.getAllteams();
			if (!teams.isEmpty()) {
				responseEntity = ResponseEntity.status(HttpStatus.OK).body(
						response.getSuccessResposne(ErrorCodes.T04, ErrorMessages.TEAMS_FETCHED_SUCCESSFULLY, teams));
			} else {
				responseEntity = ResponseEntity.status(HttpStatus.OK)
						.body(response.getSuccessResposne(ErrorCodes.T05, ErrorMessages.NO_TEAMS_AVAILABLE, null));
			}
		} catch (TaskManagementServiceException e) {
			responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getErrorResponse(e));
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.GET_ALL_TEAMS);
		return responseEntity;
	}

	@PostMapping(value = "/updateUserRecords", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateUserRecords(@RequestBody UserTO userTO) {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START,
				MethodConstants.UPDATE_USER_RECORDS);
		ResponseEntity<Object> responseEntity = null;
		Response response = new Response();
		final UserDetailsService userDetailsService = userDetailsFactory
				.getUserDetailsServiceInstance("userDetailsServiceImpl");
		UserTO user;
		try {
			user = userDetailsService.updateUserRecords(userTO);
			if (user != null) {
				responseEntity = ResponseEntity.status(HttpStatus.OK)
						.body(response.getSuccessResposne(ErrorCodes.U07, ErrorMessages.USER_RECORD_UPDATED, user));
			} else {
				responseEntity = ResponseEntity.status(HttpStatus.OK).body(
						response.getSuccessResposne(ErrorCodes.U08, ErrorMessages.USER_RECORD_FAILED_TO_UPDATE, null));
			}
		} catch (TaskManagementServiceException e) {
			responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getErrorResponse(e));
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.UPDATE_USER_RECORDS);
		return responseEntity;
	}

	@GetMapping(value = "/getTeamMembers", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getTeamMembers(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "teamName") String teamName) {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START, MethodConstants.GET_TEAM_MEMBERS);
		ResponseEntity<Object> responseEntity = null;
		Response response = new Response();
		final UserDetailsService userDetailsService = userDetailsFactory
				.getUserDetailsServiceInstance("userDetailsServiceImpl");
		List<UserTO> teamMembers;
		try {
			teamMembers = userDetailsService.getTeamMembers(userId, teamName);
			if (teamMembers != null) {
				responseEntity = ResponseEntity.status(HttpStatus.OK).body(
						response.getSuccessResposne(ErrorCodes.TM01, ErrorMessages.TEAM_MEMBERS_FETCHED, teamMembers));
			} else {
				responseEntity = ResponseEntity.status(HttpStatus.OK).body(
						response.getSuccessResposne(ErrorCodes.TM02, ErrorMessages.TEAM_MEMBERS_FAILED_TO_FETCH, null));
			}
		} catch (TaskManagementServiceException e) {
			responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getErrorResponse(e));
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.GET_TEAM_MEMBERS);
		return responseEntity;
	}
}
