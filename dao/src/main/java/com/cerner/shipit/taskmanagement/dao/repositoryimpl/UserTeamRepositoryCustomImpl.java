package com.cerner.shipit.taskmanagement.dao.repositoryimpl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.cerner.shipit.taskmanagement.dao.repository.UserTeamRepository;
import com.cerner.shipit.taskmanagement.dao.repository.UserTeamRepositoryCustom;
import com.cerner.shipit.taskmanagement.utility.constant.GeneralConstants;
import com.cerner.shipit.taskmanagement.utility.constant.MethodConstants;
import com.cerner.shipit.taskmanagement.utility.constant.SQLQuery;
import com.cerner.shipit.taskmanagement.utility.entity.User;
import com.cerner.shipit.taskmanagement.utility.entity.UserTeam;

@Repository
@Component("UserTeamRepositoryCustomImpl")
public class UserTeamRepositoryCustomImpl implements UserTeamRepositoryCustom {
	Logger logger = LoggerFactory.getLogger(UserTeamRepositoryCustomImpl.class);

	@Autowired
	private EntityManager entityManager;

	public List<UserTeam> findByAssignedUser(long id) {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START,
				MethodConstants.FIND_BY_ASSIGNED_USER);
		List<UserTeam> userTeams = new ArrayList<UserTeam>();
		Query query = entityManager.createQuery(SQLQuery.FIND_BY_ASSIGNED_USER);
		query.setParameter("USER", id);
		List response = query.getResultList();
		for (Object userTeam : response) {
			userTeams.add((UserTeam) userTeam);
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END,
				MethodConstants.FIND_BY_ASSIGNED_USER);
		return userTeams;
	}

	public List<UserTeam> findByTeamId(long id) {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START, MethodConstants.FIND_BY_TEAM_ID);
		List<UserTeam> userTeams = new ArrayList<UserTeam>();
		Query query = entityManager.createQuery(SQLQuery.FIND_BY_TEAM_ID);
		query.setParameter("TEAM", id);
		List response = query.getResultList();
		for (Object userTeam : response) {
			userTeams.add((UserTeam) userTeam);
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.FIND_BY_TEAM_ID);
		return userTeams;
	}
}
