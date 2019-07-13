package com.cerner.shipit.taskmanagement.dao.entitymanager;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cerner.shipit.taskmanagement.dao.repository.TeamRepositoryCustom;
import com.cerner.shipit.taskmanagement.dao.repository.TeamsRepository;
import com.cerner.shipit.taskmanagement.dao.repository.UserRepository;
import com.cerner.shipit.taskmanagement.dao.repository.UserRepositoryCustom;
import com.cerner.shipit.taskmanagement.dao.repository.UserSummaryRepository;
import com.cerner.shipit.taskmanagement.dao.repository.UserSummaryRepositoryCustom;
import com.cerner.shipit.taskmanagement.dao.repositoryimpl.TeamRepositoryCustomImpl;
import com.cerner.shipit.taskmanagement.dao.repositoryimpl.UserSummaryRepositoryCustomImpl;
import com.cerner.shipit.taskmanagement.exception.TaskManagementDBException;
import com.cerner.shipit.taskmanagement.utility.constant.ErrorCodes;
import com.cerner.shipit.taskmanagement.utility.constant.ErrorMessages;
import com.cerner.shipit.taskmanagement.utility.constant.GeneralConstants;
import com.cerner.shipit.taskmanagement.utility.constant.MethodConstants;
import com.cerner.shipit.taskmanagement.utility.converter.EntityToTO;
import com.cerner.shipit.taskmanagement.utility.entity.Teams;
import com.cerner.shipit.taskmanagement.utility.entity.User;
import com.cerner.shipit.taskmanagement.utility.entity.UserSummary;
import com.cerner.shipit.taskmanagement.utility.tos.TeamTO;
import com.cerner.shipit.taskmanagement.utility.tos.UserTO;

@Service
public class DAOImpl {
	Logger logger = LoggerFactory.getLogger(DAOImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	@Qualifier("UserRepositoryCustomImpl")
	private UserRepositoryCustom userRepositoryCustom;

	@Autowired
	private UserSummaryRepository userSummaryRepository;

	@Autowired
	@Qualifier("UserSummaryRepositoryCustomImpl")
	private UserSummaryRepositoryCustom userSummaryRepositoryCustomImpl;
	
	@Autowired
	private TeamsRepository teamsRepository;
	
	@Autowired
	@Qualifier("TeamRepositoryCustomImpl")
	private TeamRepositoryCustom teamRepositoryCustomImpl;

	/**
	 * This will save the user details.
	 * 
	 * @param user
	 * @return
	 * @throws TaskManagementDBException
	 */
	public boolean createUser(User user) throws TaskManagementDBException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START, MethodConstants.CREATE_USER);
		boolean userCreated = false;
		UserSummary userSummary = new UserSummary();
		User createdUser = userRepository.save(user);
		if (null != createdUser) {
			userSummary.setUser(createdUser);
			userSummary.setLoginAttempts(1);
			userSummary.setSuccess(1);
			userSummaryRepository.save(userSummary);
			userCreated = true;
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.CREATE_USER);
		return userCreated;
	}

	public UserTO findByUserId(String userId) throws TaskManagementDBException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START, MethodConstants.FIND_BY_USER_ID);
		EntityToTO entityToTO = new EntityToTO();
		UserTO userTO = null;
		User user = userRepositoryCustom.findByUserId(userId);
		if (user != null) {
			userTO = entityToTO.convertUserEntitytoUserTO(user);
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.FIND_BY_USER_ID);
		return userTO;
	}

	public void updateUserSummaryWithSuccess(User user) throws TaskManagementDBException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START, MethodConstants.UPDATE_USER_SUMMARY_WITH_SUCCESS);
		User userEntity = userRepositoryCustom.findByUserId(user.getUserId());
		UserSummary userSummary = userSummaryRepositoryCustomImpl.findByUser(userEntity);
		userSummary.setLoginAttempts(userSummary.getLoginAttempts() + 1);
		userSummary.setSuccess(userSummary.getSuccess() + 1);
		userSummaryRepository.save(userSummary);
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.UPDATE_USER_SUMMARY_WITH_SUCCESS);
	}

	public void updateUserSummaryWithFailure(User user) throws TaskManagementDBException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START, MethodConstants.UPDATE_USER_SUMMARY_WITH_FAILURE);
		User userEntity = userRepositoryCustom.findByUserId(user.getUserId());
		UserSummary userSummary = userSummaryRepositoryCustomImpl.findByUser(userEntity);
		userSummary.setLoginAttempts(userSummary.getLoginAttempts() + 1);
		userSummary.setFailed(userSummary.getFailed()+1);
		userSummaryRepository.save(userSummary);
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.UPDATE_USER_SUMMARY_WITH_FAILURE);
	}

	public UserTO getUserDetailsById(String userId) throws TaskManagementDBException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START, MethodConstants.GET_USERDETAILS_BY_ID);
		UserTO userTO = new UserTO();
		User userEntity = userRepositoryCustom.findByUserId(userId);
		if(userEntity != null) {
			EntityToTO entityToTO = new EntityToTO();
			userTO = entityToTO.convertUserEntitytoUserTO(userEntity);
		}else {
			userTO.setUserId(userId);
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.GET_USERDETAILS_BY_ID);
		return userTO;
	}

	public boolean createTeam(String teamName) throws TaskManagementDBException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START, MethodConstants.CREATE_TEAM);
		boolean isteamCreated = false;
		Teams team = teamRepositoryCustomImpl.findByTeamName(teamName);
		if(team == null) {
			Teams teams = new Teams();
			teams.setName(teamName);
			team = teamsRepository.save(teams);
			if(team!=null) {
				isteamCreated = true;
			}
		}else {
			throw new TaskManagementDBException(ErrorCodes.T03, ErrorMessages.TEAM_EXISTS);
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.CREATE_TEAM);
		return isteamCreated;
	}
	
	public List<TeamTO> getAllTeams() throws TaskManagementDBException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START, MethodConstants.GET_ALL_TEAMS);
		EntityToTO entityToTO = new EntityToTO();
		List<Teams> teams = teamsRepository.findAll();
		List<TeamTO> teamTOs = new ArrayList<TeamTO>();
		if(teams != null && !teams.isEmpty()) {
			for (Teams team : teams) {
				teamTOs.add(entityToTO.convertTeamEntitytoTeamTO(team));
			}
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.GET_ALL_TEAMS);
		return teamTOs;
	}

}
