package com.cerner.shipit.taskmanagement.utility.converter;

import org.springframework.beans.BeanUtils;

import com.cerner.shipit.taskmanagement.utility.entity.Teams;
import com.cerner.shipit.taskmanagement.utility.entity.User;
import com.cerner.shipit.taskmanagement.utility.tos.TeamTO;
import com.cerner.shipit.taskmanagement.utility.tos.UserTO;

public class EntityToTO {
	public UserTO convertUserEntitytoUserTO(User userEntity) {
		UserTO userTO = new UserTO();
		BeanUtils.copyProperties(userEntity, userTO);
		userTO.setRole(userEntity.getRole().toString());
		return userTO;
	}
	
	public TeamTO convertTeamEntitytoTeamTO(Teams teams) {
		TeamTO teamTO = new TeamTO();
		BeanUtils.copyProperties(teams, teamTO);
		return teamTO;
	}
}
