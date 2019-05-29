package com.cerner.shipit.taskmanagement.service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cerner.shipit.taskmanagement.exception.TaskManagementServiceException;
import com.cerner.shitit.taskmanagement.utility.tos.JiraDetails;

@Service
public interface JiraDetailsService {

	List<JiraDetails> fetchJiraDetailsByUserId(String UserId) throws TaskManagementServiceException;
}
