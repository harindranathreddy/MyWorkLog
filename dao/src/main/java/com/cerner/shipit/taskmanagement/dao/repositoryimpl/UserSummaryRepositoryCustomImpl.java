package com.cerner.shipit.taskmanagement.dao.repositoryimpl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.cerner.shipit.taskmanagement.dao.repository.UserSummaryRepositoryCustom;
import com.cerner.shipit.taskmanagement.utility.constant.GeneralConstants;
import com.cerner.shipit.taskmanagement.utility.constant.MethodConstants;
import com.cerner.shipit.taskmanagement.utility.constant.SQLQuery;
import com.cerner.shipit.taskmanagement.utility.entity.User;
import com.cerner.shipit.taskmanagement.utility.entity.UserSummary;

@Repository
@Component("UserSummaryRepositoryCustomImpl")
public class UserSummaryRepositoryCustomImpl implements UserSummaryRepositoryCustom{

	Logger logger = LoggerFactory.getLogger(UserSummaryRepositoryCustomImpl.class);

	@Autowired
	private EntityManager entityManager;
	
	public UserSummary findByUser(User user) {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START, MethodConstants.FIND_BY_USER_ID);
		UserSummary userSummary = null;
		Query query = entityManager.createQuery(SQLQuery.FETCH_USER_SUMMARY_BY_USER);
		query.setParameter("USER_ID", user.getId());
		List response = query.getResultList();
		if (!response.isEmpty() && response.get(0) instanceof UserSummary) {
			userSummary = (UserSummary) response.get(0);
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.FIND_BY_USER_ID);
		return userSummary;
	}

}
