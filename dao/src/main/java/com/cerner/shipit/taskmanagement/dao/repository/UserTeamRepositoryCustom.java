package com.cerner.shipit.taskmanagement.dao.repository;

import java.util.List;

import com.cerner.shipit.taskmanagement.utility.entity.UserTeam;

public interface UserTeamRepositoryCustom {

	List<UserTeam> findByAssignedUser(long id);

}
