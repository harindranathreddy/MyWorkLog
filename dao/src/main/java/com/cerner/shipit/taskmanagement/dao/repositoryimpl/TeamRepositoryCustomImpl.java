package com.cerner.shipit.taskmanagement.dao.repositoryimpl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.cerner.shipit.taskmanagement.dao.repository.TeamRepositoryCustom;
import com.cerner.shipit.taskmanagement.exception.TaskManagementDBException;
import com.cerner.shipit.taskmanagement.utility.constant.GeneralConstants;
import com.cerner.shipit.taskmanagement.utility.constant.MethodConstants;
import com.cerner.shipit.taskmanagement.utility.constant.SQLQuery;
import com.cerner.shipit.taskmanagement.utility.entity.Teams;

@Repository
@Component("TeamRepositoryCustomImpl")
public class TeamRepositoryCustomImpl implements TeamRepositoryCustom{
	
	Logger logger = LoggerFactory.getLogger(TeamRepositoryCustomImpl.class);

	@Autowired
	private EntityManager entityManager;

	public Teams findByTeamName(String teamName) throws TaskManagementDBException{
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START, MethodConstants.FIND_BY_TEAM_NAME);
		Teams team= null;
		Query query = entityManager.createQuery(SQLQuery.FETCH_BY_TEAM_NAME);
		query.setParameter("NAME", teamName);
		List response = query.getResultList();
		if (!response.isEmpty() && response.get(0) instanceof Teams) {
			team = (Teams) response.get(0);
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.FIND_BY_TEAM_NAME);
		return team;
	}

}
