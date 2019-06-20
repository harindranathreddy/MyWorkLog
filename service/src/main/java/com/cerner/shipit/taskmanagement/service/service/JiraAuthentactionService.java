package com.cerner.shipit.taskmanagement.service.service;

import org.springframework.stereotype.Service;

import com.cerner.shipit.taskmanagement.exception.TaskManagementServiceException;
import com.cerner.shipit.taskmanagement.utility.tos.LoginResponseTO;

@Service
public interface JiraAuthentactionService {

	LoginResponseTO authenticateUser(String userName, String password) throws TaskManagementServiceException;
}
