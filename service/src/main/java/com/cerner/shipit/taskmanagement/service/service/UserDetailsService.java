package com.cerner.shipit.taskmanagement.service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cerner.shipit.taskmanagement.exception.TaskManagementServiceException;
import com.cerner.shipit.taskmanagement.utility.tos.TeamTO;
import com.cerner.shipit.taskmanagement.utility.tos.UserTO;

@Service
public interface UserDetailsService {
	UserTO getUserDetailsById(String userId) throws TaskManagementServiceException;

	boolean createTeam(String teamName) throws TaskManagementServiceException;

	List<TeamTO> getAllteams() throws TaskManagementServiceException;

	UserTO updateUserRecords(UserTO userTO) throws TaskManagementServiceException;
}
