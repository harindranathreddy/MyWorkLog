package com.cerner.shipit.taskmanagement.utility.constant;

public class SQLQuery {
	public static final String FETCH_USER_BY_USERID = "from User user where USER_ID = :USER_ID";
	public static final String FETCH_USER_SUMMARY_BY_USER = "from UserSummary user_summary where USER_ID = :USER_ID";
	public static final String FETCH_BY_TEAM_NAME =  "from Teams teams where NAME LIKE :NAME";;
}
