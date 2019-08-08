package com.cerner.shipit.taskmanagement.utility.constant;

public class SQLQuery {
	public static final String FETCH_USER_BY_USERID = "from User user where USER_ID = :USER_ID";
	public static final String FETCH_USER_SUMMARY_BY_USER = "from UserSummary user_summary where USER_ID = :USER_ID";
	public static final String FETCH_BY_TEAM_NAME =  "from Teams teams where NAME LIKE :NAME";
	public static final String FIND_BY_ASSIGNED_USER = "from UserTeam user_team where USER = :USER";
	public static final String FIND_BY_TEAM_ID = "from UserTeam user_team where TEAM = :TEAM";
	public static final String FETCH_ACTIVE_NOTIFICATION_USER = "from User user where NOTIFICATION = 1";
	public static final String FETCH_USER_BY_EMAILID = "from User user where MAIL_ID = :MAIL_ID";;
}
