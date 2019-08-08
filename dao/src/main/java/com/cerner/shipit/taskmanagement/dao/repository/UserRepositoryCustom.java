package com.cerner.shipit.taskmanagement.dao.repository;

import java.util.List;

import com.cerner.shipit.taskmanagement.exception.TaskManagementDBException;
import com.cerner.shipit.taskmanagement.utility.entity.User;

public interface UserRepositoryCustom {
	User findByUserId(String userId) throws TaskManagementDBException;
	
	List<User> getActiveNotificationUsers() throws TaskManagementDBException;
	
	User findByUserEmailId(String emailId) throws TaskManagementDBException;
}
