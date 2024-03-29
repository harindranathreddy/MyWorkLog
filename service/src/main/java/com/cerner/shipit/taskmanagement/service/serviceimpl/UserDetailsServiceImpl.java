package com.cerner.shipit.taskmanagement.service.serviceimpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.cerner.shipit.taskmanagement.dao.entitymanager.DAOImpl;
import com.cerner.shipit.taskmanagement.exception.TaskManagementDBException;
import com.cerner.shipit.taskmanagement.exception.TaskManagementServiceException;
import com.cerner.shipit.taskmanagement.service.service.UserDetailsService;
import com.cerner.shipit.taskmanagement.utility.constant.ErrorCodes;
import com.cerner.shipit.taskmanagement.utility.constant.ErrorMessages;
import com.cerner.shipit.taskmanagement.utility.constant.GeneralConstants;
import com.cerner.shipit.taskmanagement.utility.constant.MethodConstants;
import com.cerner.shipit.taskmanagement.utility.enums.Role;
import com.cerner.shipit.taskmanagement.utility.tos.TeamTO;
import com.cerner.shipit.taskmanagement.utility.tos.UserTO;

@Service
@Component("UserDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

	Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	@Autowired
	private DAOImpl daoImpl;

	@Override
	public UserTO getUserDetailsById(String userId) throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START,
				MethodConstants.GET_USERDETAILS_BY_ID);
		UserTO userTO;
		try {
			userTO = daoImpl.getUserDetailsById(userId);
		} catch (TaskManagementDBException e) {
			throw new TaskManagementServiceException(e);
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END,
				MethodConstants.GET_USERDETAILS_BY_ID);
		return userTO;
	}

	@Override
	public boolean createTeam(String teamName) throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START, MethodConstants.CREATE_TEAM);
		boolean isTeamCreated;
		try {
			isTeamCreated = daoImpl.createTeam(teamName);
		} catch (TaskManagementDBException e) {
			throw new TaskManagementServiceException(e);
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.CREATE_TEAM);
		return isTeamCreated;
	}

	@Override
	public List<TeamTO> getAllteams() throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START, MethodConstants.CREATE_TEAM);
		List<TeamTO> teams;
		try {
			teams = daoImpl.getAllTeams();
		} catch (TaskManagementDBException e) {
			throw new TaskManagementServiceException(e);
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.CREATE_TEAM);
		return teams;
	}

	@Override
	public UserTO updateUserRecords(UserTO userTO) throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START,
				MethodConstants.UPDATE_USER_RECORDS);
		UserTO user;
		try {
			user = daoImpl.updateUserRecords(userTO);
			if ((userTO.getTeam() != null) && !userTO.getTeam().isEmpty()) {
				userTO = daoImpl.updateUserTeam(userTO);
			}
		} catch (TaskManagementDBException e) {
			throw new TaskManagementServiceException(e);
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.UPDATE_USER_RECORDS);
		return userTO;
	}

	@Override
	public List<UserTO> getTeamMembers(String userId, String teamName) throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START, MethodConstants.GET_TEAM_MEMBERS);
		List<UserTO> teamMembers = null;
		try {
			UserTO userTO = daoImpl.getUserDetailsById(userId);
			if ((Role.TEAM_LEAD.toString().equalsIgnoreCase(userTO.getRole())
					&& userTO.getTeam().equalsIgnoreCase(teamName))
					|| (Role.PROJECT_MANAGER.toString().equalsIgnoreCase(userTO.getRole())
							|| Role.ADMIN.toString().equalsIgnoreCase(userTO.getRole())
							|| Role.ENGINEER.toString().equalsIgnoreCase(userTO.getRole()))) {
				TeamTO team = daoImpl.getTeamBasedOnTeamName(teamName);
				if (team.getId() > 0) {
					teamMembers = daoImpl.getUserDetailsByTeamId(team.getId());
				}
			} else {
				throw new TaskManagementServiceException(ErrorCodes.T07,
						ErrorMessages.NO_PERMISIONS_TO_ACCESS_THIS_DATA);
			}
		} catch (TaskManagementDBException e) {
			throw new TaskManagementServiceException(e);
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.GET_TEAM_MEMBERS);
		return teamMembers;
	}

}
