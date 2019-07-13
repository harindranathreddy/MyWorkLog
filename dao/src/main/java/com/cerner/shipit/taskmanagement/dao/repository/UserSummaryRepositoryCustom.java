package com.cerner.shipit.taskmanagement.dao.repository;

import com.cerner.shipit.taskmanagement.utility.entity.User;
import com.cerner.shipit.taskmanagement.utility.entity.UserSummary;

public interface UserSummaryRepositoryCustom {

	UserSummary findByUser(User id);
}
