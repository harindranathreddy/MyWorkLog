package com.cerner.shipit.taskmanagement.dao.repository;

import java.util.List;

import com.cerner.shipit.taskmanagement.exception.TaskManagementDBException;
import com.cerner.shipit.taskmanagement.utility.entity.Teams;

public interface TeamRepositoryCustom {

	Teams findByTeamName(String teamName) throws TaskManagementDBException;

}
