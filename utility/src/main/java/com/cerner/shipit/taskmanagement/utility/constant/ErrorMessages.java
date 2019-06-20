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

	private ErrorMessages() {

	}
}
