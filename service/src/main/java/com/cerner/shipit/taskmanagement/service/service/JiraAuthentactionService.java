package com.cerner.shipit.taskmanagement.service.service;

import org.springframework.stereotype.Service;

import com.cerner.shipit.taskmanagement.exception.TaskManagementServiceException;

@Service
public interface JiraAuthentactionService {

	int authenticateUser(String userName, String password) throws TaskManagementServiceException;
}
