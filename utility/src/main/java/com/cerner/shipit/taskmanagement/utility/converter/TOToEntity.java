package com.cerner.shipit.taskmanagement.utility.converter;

import org.springframework.beans.BeanUtils;

import com.cerner.shipit.taskmanagement.utility.entity.User;
import com.cerner.shipit.taskmanagement.utility.enums.Role;
import com.cerner.shipit.taskmanagement.utility.tos.UserTO;

public class TOToEntity {
	public User convertUserTOtoUserEntity(UserTO userTO) {
		User userEntity = new User();
		BeanUtils.copyProperties(userTO, userEntity);
		userEntity.setRole(Role.valueOf(userTO.getRole()));
		return userEntity;
	}
}
