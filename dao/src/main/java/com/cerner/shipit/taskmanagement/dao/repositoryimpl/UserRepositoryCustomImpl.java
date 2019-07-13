package com.cerner.shipit.taskmanagement.dao.repositoryimpl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.cerner.shipit.taskmanagement.dao.repository.UserRepositoryCustom;
import com.cerner.shipit.taskmanagement.exception.TaskManagementDBException;
import com.cerner.shipit.taskmanagement.utility.constant.GeneralConstants;
import com.cerner.shipit.taskmanagement.utility.constant.MethodConstants;
import com.cerner.shipit.taskmanagement.utility.constant.SQLQuery;
import com.cerner.shipit.taskmanagement.utility.entity.User;

@Repository
@Component("UserRepositoryCustomImpl")
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

	Logger logger = LoggerFactory.getLogger(UserRepositoryCustomImpl.class);

	@Autowired
	private EntityManager entityManager;

	public User findByUserId(String userId) throws TaskManagementDBException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START, MethodConstants.FIND_BY_USER_ID);
		User user = null;
		Query query = entityManager.createQuery(SQLQuery.FETCH_USER_BY_USERID);
		query.setParameter("USER_ID", userId);
		List response = query.getResultList();
		if (!response.isEmpty() && response.get(0) instanceof User) {
			user = (User) response.get(0);
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.FIND_BY_USER_ID);
		return user;
	}

}
