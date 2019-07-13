package com.cerner.shipit.taskmanagement.utility.constant;

public class ErrorMessages {

	public static final String FAILED_TO_FETCH_JIRA_DETAILS = "Failed to fetch Jira Details";
	public static final String FAILED_DURING_FILTERING_JIRA_DETAILS = "Failed during filtering the jira details";
	public static final String FAILED_TO_FETCH_WORKLOG_DETAILS = "Failed to fetch worklog Details";
	public static final String FAILED_DURING_FILTER_LAST_WORKLOG_DETAILS = "Failed during filtering last worklog date";
	public static final String FAILED_TO_AUTHENTICATE_USER = "Authentication is failed.";
	public static final String LOGIN_SUCCESSFUL = "Login is Successful";
	public static final String LOGIN_FAILED = "Login Failed";
	public static final String JIRA_DETAIALS_FETCHED_SUCCESFULLY = "Jira Details are fetched Successfully.";
	public static final String WORKLOG_ADDED_SUCCESFULLY = "Worklog added succesfully.";
	public static final String WORKLOG_FAILED_TO_ADD = "Worklog failed to add.";
	public static final String WORKLOG_FAILED_TO_ADD_DUE_TO_MALFORMEDURLEXCEPTION = "Worklog failed to add due to MalFormedURlException.";
	public static final String WORKLOG_FAILED_TO_ADD_DUE_TO_IOEXCEPTION = "Worklog failed to add due to IOException.";
	public static final String DATES_FETCHED_SUCCESFULLY = "Dates between last logged and current are fetched.";
	public static final String WORK_LOGGED = "Work is logged today.";
	public static final String JIRA_NOT_AVAILABLE = "Jira is not available or don't have permission to add.";
	public static final String WORKLOG_FAILED_TO_ADD_WITH_FORBIDDEN_ERROR = "Server responded with Forbidden Error. Plase logout and login to jira2.cerner.com to resolve the forbidden error and try after that.";
	public static final String FAILED_TO_FETCH_USER_DETAILS = "Failed to fetch user details";
	public static final String FAILED_DURING_FILTERING_USER_DETAILS = "Failed during filtering user details.";
	public static final String WORK_LOGGED_TODAY_OR_NEVER = "Work is either logged today are never logged.";
	public static final String WORKLOG_SUMMERY_FETCHED_SUCCESFULLY_LOGGING_JIRAS = "WorkLog Summery fetched Succesfully for logging jira";
	public static final String FAILED_DURING_JSON_PARING = "Failed during JSON paring for worklog data.";
	public static final String FAILED_DURING_WORKLOG_DATE = "Failed during worklog day parsing.";
	public static final String WORKLOG_VERIFICATION_DETAILS_FETCHED_SUCCESSFULLY = "Worklog verification details fetched succssfully.";
	public static final String FAILED_TO_FETCH_JIRA_DETAILS_FOR_SUMMARY = "Failed to fetch jira details for summary.";
	public static final String SUMMARY_DETAILS_FETCHED_SUCCESSFULLY = "Associate Summary details fetched succesfully.";
	public static final String FAILED_DURING_USER_SUMMARY_CREATION = "Failed during user summary creation.";
	public static final String NO_JIRA_UPDATED_IN_PROVIDED_TIME_FRAME = "No Jira updated in provided time frame.";
	public static final String FAILED_DURING_USER_WORKLOG_SUMMARY_CALCULATION = "Failed during jira worklog summary details.";
	public static final String USER_SUMMARY_GRAPH_DATA_FETCHED_SUCCESFULLY = "User Summary Graph data fetched succesfully.";
	public static final String USER_SUMMARY_DETAILS_NOT_AVAILABLE = "User Summary details not available to generate graph data.";
	public static final String FAILED_TO_CREATE_USER = "Failed to create user.";
	public static final String USER_CREATED = "User created Succesfully";
	public static final String USER_DETAILS_FETCHED = "User details fetched successfully.";
	public static final String TEAM_CREATED_SUCCESSFULLY = "Team Created Succesfully";
	public static final String TEAM_NOT_CREATED = "Failed to create Team. Please try after sometime.";
	public static final String TEAM_EXISTS = "Team already exist.";
	public static final String TEAMS_FETCHED_SUCCESSFULLY = "Teams Fetched Succesuffly.";
	public static final String NO_TEAMS_AVAILABLE = "No Teams Available";
	

	private ErrorMessages() {

	}
}
