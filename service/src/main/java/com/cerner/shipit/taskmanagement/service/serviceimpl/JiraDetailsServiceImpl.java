package com.cerner.shipit.taskmanagement.service.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.cerner.shipit.taskmanagement.dao.entitymanager.DAOImpl;
import com.cerner.shipit.taskmanagement.exception.TaskManagementDBException;
import com.cerner.shipit.taskmanagement.exception.TaskManagementServiceException;
import com.cerner.shipit.taskmanagement.service.jiraapi.JiraApi;
import com.cerner.shipit.taskmanagement.service.service.JiraDetailsService;
import com.cerner.shipit.taskmanagement.utility.constant.ErrorCodes;
import com.cerner.shipit.taskmanagement.utility.constant.ErrorMessages;
import com.cerner.shipit.taskmanagement.utility.constant.GeneralConstants;
import com.cerner.shipit.taskmanagement.utility.constant.MethodConstants;
import com.cerner.shipit.taskmanagement.utility.tos.GraphDataTO;
import com.cerner.shipit.taskmanagement.utility.tos.GraphDatasetTO;
import com.cerner.shipit.taskmanagement.utility.tos.JiraSummaryTO;
import com.cerner.shipit.taskmanagement.utility.tos.JiraTO;
import com.cerner.shipit.taskmanagement.utility.tos.UserTO;
import com.cerner.shipit.taskmanagement.utility.tos.WorkLogDaySummaryTO;
import com.cerner.shipit.taskmanagement.utility.tos.WorkLogDetailsTO;
import com.cerner.shipit.taskmanagement.utility.tos.WorkLogInfoTO;

@Service
@Component("jiraDetailsServiceImpl")
public class JiraDetailsServiceImpl implements JiraDetailsService {

	Logger logger = LoggerFactory.getLogger(JiraDetailsServiceImpl.class);

	@Autowired
	JiraApi jiraApi;

	@Autowired
	private DAOImpl daoImpl;

	@Override
	public List<JiraTO> getJiraDetailsByUserId(String userId) throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START,
				MethodConstants.GET_JIRADETAILS_BY_USERID);
		String jiraDetailsfromApi = jiraApi.getInProgressJiraDetailsByUserId(userId);
		List<JiraTO> jiraDetails = filterJiraDetails(jiraDetailsfromApi, userId);
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END,
				MethodConstants.GET_JIRADETAILS_BY_USERID);
		return jiraDetails;
	}

	@Override
	public List<JiraTO> getJiraDetailsByTeam(String teamName) throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START,
				MethodConstants.GET_JIRADETAILS_BY_TEAM);
		String jiraDetailsfromApi = jiraApi.getJiraDetailsByTeam(teamName);
		List<JiraTO> jiraDetails = filterJiraDetails(jiraDetailsfromApi, null);
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END,
				MethodConstants.GET_JIRADETAILS_BY_TEAM);
		return jiraDetails;
	}

	private List<JiraTO> filterJiraDetails(String jiraDetailsfromApi, String userId)
			throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START, MethodConstants.FILTER_JIRADETAILS);
		final List<JiraTO> jiraDetails = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(jiraDetailsfromApi);
			String issuesObject = jsonObject.getString("issues");
			JSONArray jiraList = new JSONArray(issuesObject);
			for (int i = 0; i < jiraList.length(); i++) {
				JSONObject jiraObject = (JSONObject) jiraList.get(i);
				JiraTO jira = new JiraTO();
				jira.setId(jiraObject.getString("key"));
				jira.setJiraLink("https://jira2.cerner.com/browse/" + jiraObject.getString("key"));
				JSONObject fields = jiraObject.getJSONObject("fields");
				jira.setDescription(fields.getString("description"));
				jira.setSummery(fields.getString("summary"));
				JSONObject status = fields.getJSONObject("status");
				jira.setStatus(status.getString("name"));
				jira.setStatusIcon(status.getString("iconUrl"));
				JSONObject issueType = fields.getJSONObject("issuetype");
				jira.setType(issueType.getString("name"));
				jira.setIssueIcon(issueType.getString("iconUrl"));
				if (userId != null) {
					jira.setLastLoggedDate(getJiraLastWorkLogDate(jiraObject.getString("key"), userId));
				} else {
					jira.setLastLoggedDate(null);
				}
				if (!fields.isNull("assignee")) {
					JSONObject assignedUser = fields.getJSONObject("assignee");
					jira.setAssignedTo(assignedUser.getString("displayName"));
				}
				jiraDetails.add(jira);
			}
		} catch (JSONException e) {
			logger.error(ErrorCodes.E04, ErrorMessages.FAILED_DURING_FILTERING_JIRA_DETAILS);
			throw new TaskManagementServiceException(ErrorCodes.E04,
					ErrorMessages.FAILED_DURING_FILTERING_JIRA_DETAILS);
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.FILTER_JIRADETAILS);
		return jiraDetails;
	}

	public String getJiraLastWorkLogDate(String issueKey, String userId) throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START,
				MethodConstants.GET_JIRA_WORKLOG_DATE);
		String jiraWorkLog = jiraApi.getWorkLogDataByJiraId(issueKey);
		String lastLoggedDate = filterWorkLogs(jiraWorkLog, userId);
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END,
				MethodConstants.GET_JIRA_WORKLOG_DATE);
		return lastLoggedDate;
	}

	private String filterWorkLogs(String jiraWorkLog, String userId) throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START, MethodConstants.FILTER_WORKLOGS);
		String lastLoggedDate = null;
		try {
			JSONObject jsonObject = new JSONObject(jiraWorkLog);
			String workLogArray = jsonObject.getString("worklogs");
			JSONArray jiraList = new JSONArray(workLogArray);
			for (int i = jiraList.length() - 1; i >= 0; i--) {
				JSONObject latestWorkLogObject = jiraList.getJSONObject(i);
				JSONObject author = latestWorkLogObject.getJSONObject("author");
				if ((null == userId) || userId.equalsIgnoreCase(author.getString("key"))) {
					lastLoggedDate = latestWorkLogObject.getString("started");
					break;
				}
			}
			if (null == lastLoggedDate) {
				lastLoggedDate = "0";
			}
		} catch (JSONException e) {
			logger.error(ErrorCodes.E04, ErrorMessages.FAILED_DURING_FILTER_LAST_WORKLOG_DETAILS);
			throw new TaskManagementServiceException(ErrorCodes.E04,
					ErrorMessages.FAILED_DURING_FILTER_LAST_WORKLOG_DETAILS);
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.FILTER_WORKLOGS);
		return lastLoggedDate;
	}

	@Override
	public int addWorkLog(WorkLogInfoTO workLogInfo) throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START,
				MethodConstants.GET_JIRADETAILS_BY_USERID);
		int responseStatus = jiraApi.addWorkLog(workLogInfo);
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END,
				MethodConstants.GET_JIRADETAILS_BY_USERID);
		return responseStatus;
	}

	@Override
	public List<String> getDates(String lastLoggedDate) {
		List<String> dates = new ArrayList<>();
		LocalDate currentDate = LocalDate.now();
		LocalDate loggedDate = LocalDate.parse(lastLoggedDate.substring(0, lastLoggedDate.indexOf('T')));
		if (loggedDate.isBefore(currentDate)) {
			int days = Days.daysBetween(loggedDate, currentDate).getDays();
			for (int i = 1; i <= days; i++) {
				dates.add(loggedDate.plusDays(i).toString());
			}
		}
		return dates;
	}

	@Override
	public List<JiraTO> getJiraSearchDetails(String issueKey, String userId) throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START,
				MethodConstants.GET_JIRASEARCHDETAILS);
		String jiraDetailsfromApi = jiraApi.getJiraSearchDetails(issueKey);
		List<JiraTO> jiraDetails = filterJiraDetails(jiraDetailsfromApi, userId);
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END,
				MethodConstants.GET_JIRASEARCHDETAILS);
		return jiraDetails;
	}

	@Override
	public List<JiraSummaryTO> getWorkLogVerificationSummary(WorkLogInfoTO workLogInfoTo)
			throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START,
				MethodConstants.GET_WORKLOG_VERIFICATION_SUMMARY);
		Map<String, List<JSONObject>> jiraWorkLogs = new HashMap<>();
		List<JiraSummaryTO> jiraVerificationSummarys = new ArrayList<>();
		List<JSONObject> workLogs = new ArrayList<>();
		try {
			List<WorkLogDetailsTO> workLogDetails = workLogInfoTo.getWorklogs();
			for (WorkLogDetailsTO workLogDetailsTO : workLogDetails) {
				if (jiraWorkLogs.get(workLogDetailsTO.getId()) == null) {
					String jiraWorkLog = jiraApi.getWorkLogDataByJiraId(workLogDetailsTO.getId());
					JSONObject jsonObject = new JSONObject(jiraWorkLog);
					JSONArray workLogsArray = jsonObject.getJSONArray("worklogs");
					for (int i = 0; i < workLogsArray.length(); i++) {
						JSONObject author = workLogsArray.getJSONObject(i).getJSONObject("author");
						if ((null == workLogInfoTo.getUserName())
								|| author.getString("key").equalsIgnoreCase(workLogInfoTo.getUserName())) {
							workLogs.add(workLogsArray.getJSONObject(i));
						}
					}
					jiraWorkLogs.put(workLogDetailsTO.getId(), workLogs);
				}
				JSONObject workLoggedForDay = findWorkLogForDate(workLogDetailsTO,
						jiraWorkLogs.get(workLogDetailsTO.getId()));
				WorkLogDaySummaryTO workLogSummeryTO = getWorkLogDaySummery(workLoggedForDay, workLogDetailsTO);
				boolean workLogAdded = false;
				if (!jiraVerificationSummarys.isEmpty()) {
					for (JiraSummaryTO jiraVerificationSummary : jiraVerificationSummarys) {
						if (jiraVerificationSummary.getKey().equalsIgnoreCase(workLogDetailsTO.getId())) {
							if (null == jiraVerificationSummary.getWorkLogDaySummeryTOs()) {
								jiraVerificationSummary.setWorkLogDaySummeryTOs(new ArrayList<>());
							}
							jiraVerificationSummary.getWorkLogDaySummeryTOs().add(workLogSummeryTO);
							workLogAdded = true;
							break;
						}
					}
				}
				if (!workLogAdded) {
					JiraSummaryTO jiraSummaryTO = new JiraSummaryTO();
					jiraSummaryTO.setKey(workLogDetailsTO.getId());
					if (null == jiraSummaryTO.getWorkLogDaySummeryTOs()) {
						jiraSummaryTO.setWorkLogDaySummeryTOs(new ArrayList<>());
					}
					jiraSummaryTO.getWorkLogDaySummeryTOs().add(workLogSummeryTO);
					jiraVerificationSummarys.add(jiraSummaryTO);
				}
			}
			calculatingTotalJiraLogTime(jiraVerificationSummarys);
		} catch (JSONException e) {
			logger.error(ErrorCodes.WLV01, ErrorMessages.FAILED_DURING_JSON_PARING);
			throw new TaskManagementServiceException(ErrorCodes.WLV01, ErrorMessages.FAILED_DURING_JSON_PARING);
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END,
				MethodConstants.GET_WORKLOG_VERIFICATION_SUMMARY);
		return jiraVerificationSummarys;
	}

	private void calculatingTotalJiraLogTime(List<JiraSummaryTO> jiraVerificationSummarys) {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START,
				MethodConstants.CALCULATING_TOTAL_JIRA_LOG_TIME);
		for (JiraSummaryTO jiraSummaryTO : jiraVerificationSummarys) {
			long totalLogTime = 0;
			for (WorkLogDaySummaryTO workLog : jiraSummaryTO.getWorkLogDaySummeryTOs()) {
				if (null != workLog) {
					totalLogTime += workLog.getTotalSeconds();
				}
			}
			jiraSummaryTO.setTotalHoursLogged((totalLogTime / 3600.00) + "h");
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END,
				MethodConstants.CALCULATING_TOTAL_JIRA_LOG_TIME);
	}

	private WorkLogDaySummaryTO getWorkLogDaySummery(JSONObject jsonObject, WorkLogDetailsTO workLogDetailsTO)
			throws TaskManagementServiceException {
		WorkLogDaySummaryTO workLogDaySummaryTO = new WorkLogDaySummaryTO();
		long totalWorkTime;
		try {
			long currentLogTimeInSeconds = 0;
			if (jsonObject.has("started")) {
				workLogDaySummaryTO.setStartDate(jsonObject.getString("started"));
			} else {
				workLogDaySummaryTO.setStartDate(workLogDetailsTO.getStarted());
			}
			if (jsonObject.has("timeSpent")) {
				workLogDaySummaryTO.setLoggedTime(jsonObject.getString("timeSpent"));
			} else {
				workLogDaySummaryTO.setLoggedTime("0h");
			}
			workLogDaySummaryTO.setCurrentTimeSpent(workLogDetailsTO.getTimeSpent());
			if (workLogDetailsTO.getTimeSpent().endsWith("h") || workLogDetailsTO.getTimeSpent().endsWith("H")) {
				double time = Double.parseDouble(workLogDetailsTO.getTimeSpent().replaceAll("(?i)(h)", ""));
				long hours = (long) time;
				long minutes = (long) (time * 60) % 60;
				long seconds = (long) (time * (60 * 60)) % 60;
				currentLogTimeInSeconds = TimeUnit.HOURS.toSeconds(hours) + TimeUnit.MINUTES.toSeconds(minutes)
						+ seconds;
			} else if (workLogDetailsTO.getTimeSpent().endsWith("m") || workLogDetailsTO.getTimeSpent().endsWith("M")) {
				currentLogTimeInSeconds = TimeUnit.MINUTES
						.toSeconds(Long.parseLong(workLogDetailsTO.getTimeSpent().replaceAll("(?i)(m)", "")));
			} else if (workLogDetailsTO.getTimeSpent().endsWith("s") || workLogDetailsTO.getTimeSpent().endsWith("S")) {
				currentLogTimeInSeconds = TimeUnit.SECONDS
						.toSeconds(Long.parseLong(workLogDetailsTO.getTimeSpent().replaceAll("(?i)(s)", "")));
			}
			if (jsonObject.has("timeSpentSeconds")) {
				totalWorkTime = currentLogTimeInSeconds + jsonObject.getLong("timeSpentSeconds");
			} else {
				totalWorkTime = currentLogTimeInSeconds;
			}
			workLogDaySummaryTO.setTotalSeconds(totalWorkTime);
			workLogDaySummaryTO.setTotalTimeSpent((totalWorkTime / 3600.00) + "h");

		} catch (JSONException e) {
			logger.error(ErrorCodes.WLD01, ErrorMessages.FAILED_DURING_WORKLOG_DATE);
			throw new TaskManagementServiceException(ErrorCodes.WLD01, ErrorMessages.FAILED_DURING_WORKLOG_DATE);
		}
		return workLogDaySummaryTO;
	}

	private JSONObject findWorkLogForDate(WorkLogDetailsTO currentWorkLogDetailsTO, List<JSONObject> workLogs)
			throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START,
				MethodConstants.FIND_WORK_LOG_FOR_DATE);
		JSONObject workLogDay = new JSONObject();
		try {
			LocalDate logDate = LocalDate.parse(currentWorkLogDetailsTO.getStarted().substring(0,
					currentWorkLogDetailsTO.getStarted().indexOf('T')));
			for (JSONObject workLog : workLogs) {
				LocalDate workLogDate = LocalDate
						.parse(workLog.getString("started").substring(0, workLog.getString("started").indexOf('T')));
				if (workLogDate.equals(logDate)) {
					workLogDay = workLog;
					break;
				}
			}
		} catch (JSONException e) {
			logger.error(ErrorCodes.WLD01, ErrorMessages.FAILED_DURING_WORKLOG_DATE);
			throw new TaskManagementServiceException(ErrorCodes.WLD01, ErrorMessages.FAILED_DURING_WORKLOG_DATE);
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END,
				MethodConstants.FIND_WORK_LOG_FOR_DATE);
		return workLogDay;
	}

	@Override
	public List<JiraSummaryTO> getUserSummary(String userId, int noOfDays) throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START, MethodConstants.GET_USER_SUMMARY);
		List<JiraSummaryTO> userSummaryDetails = new ArrayList<>();
		try {
			if (userId.endsWith("@cerner.com")) {
				UserTO userTO = daoImpl.findByUserEmailId(userId);
				if (userTO != null) {
					userId = userTO.getUserId();
				} else {
					logger.error(GeneralConstants.LOGGER_FORMAT_2, ErrorCodes.US05,
							ErrorMessages.USER_WITH_MAILID_DOES_NOT_EXIST, userId);
					throw new TaskManagementServiceException(ErrorCodes.US05,
							ErrorMessages.USER_WITH_MAILID_DOES_NOT_EXIST);
				}

			}
			String updatedJiraDetails = jiraApi.getAllJirasUpdatedForSummary(userId, noOfDays);
			JSONObject jiraDetails = new JSONObject(updatedJiraDetails);
			if (jiraDetails.getInt("total") > 0) {
				JSONArray updatedJiras = jiraDetails.getJSONArray("issues");
				for (int i = 0; i < jiraDetails.getInt("total"); i++) {
					JSONObject jira = updatedJiras.getJSONObject(i);
					JiraSummaryTO jiraSummaryTO = new JiraSummaryTO();
					jiraSummaryTO.setKey(jira.getString("key"));
					JSONObject jraDetails = jira.getJSONObject("fields");
					jiraSummaryTO.setSummary(jraDetails.getString("summary"));
					updateJiraWorklogSummary(jiraSummaryTO, userId, noOfDays);
					userSummaryDetails.add(jiraSummaryTO);
				}
				calculatingTotalJiraLogTime(userSummaryDetails);
			} else {
				throw new TaskManagementServiceException(ErrorCodes.US03,
						ErrorMessages.NO_JIRA_UPDATED_IN_PROVIDED_TIME_FRAME);
			}
		} catch (JSONException e) {
			logger.error(ErrorCodes.US02, ErrorMessages.FAILED_DURING_USER_SUMMARY_CREATION);
			throw new TaskManagementServiceException(ErrorCodes.US02,
					ErrorMessages.FAILED_DURING_USER_SUMMARY_CREATION);
		} catch (TaskManagementDBException e) {
			throw new TaskManagementServiceException(e);
		}

		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END, MethodConstants.GET_USER_SUMMARY);
		return userSummaryDetails;
	}

	private void updateJiraWorklogSummary(JiraSummaryTO jiraSummaryTO, String userId, int noOfDays)
			throws TaskManagementServiceException {
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_START,
				MethodConstants.UPDATE_JIRA_WORKLOG_SUMMARY);
		Map<String, WorkLogDaySummaryTO> daySummary = new LinkedHashMap();
		try {
			JSONObject jiraWorkLog = new JSONObject(jiraApi.getWorkLogDataByJiraId(jiraSummaryTO.getKey()));
			UpdateMapwithDates(daySummary, noOfDays);
			JSONArray dayWorkLogs = jiraWorkLog.getJSONArray("worklogs");
			for (int i = dayWorkLogs.length() - 1; i >= 0; i--) {
				JSONObject dayWorkLog = dayWorkLogs.getJSONObject(i);
				String worklogStartedDate = dayWorkLog.getString("started");
				JSONObject author = dayWorkLog.getJSONObject("author");
				if (daySummary.containsKey(worklogStartedDate.substring(0, worklogStartedDate.indexOf("T")))
						&& author.getString("key").equalsIgnoreCase(userId)) {
					WorkLogDaySummaryTO workLogDaySummaryTO = daySummary
							.get(worklogStartedDate.substring(0, worklogStartedDate.indexOf("T")));
					if (null == daySummary.get(worklogStartedDate.substring(0, worklogStartedDate.indexOf("T")))) {
						workLogDaySummaryTO = new WorkLogDaySummaryTO();
						daySummary.put(worklogStartedDate.substring(0, worklogStartedDate.indexOf("T")),
								workLogDaySummaryTO);
					}
					workLogDaySummaryTO.setStartDate(worklogStartedDate);
					workLogDaySummaryTO.setTotalSeconds(
							workLogDaySummaryTO.getTotalSeconds() + dayWorkLog.getLong("timeSpentSeconds"));
					workLogDaySummaryTO.setTotalTimeSpent((workLogDaySummaryTO.getTotalSeconds() / 3600.00) + "h");
					if (null == workLogDaySummaryTO.getComments()) {
						workLogDaySummaryTO.setComments("");
					}
					workLogDaySummaryTO.setComments(
							(workLogDaySummaryTO.getComments() + "\n" + dayWorkLog.getString("comment")).trim());
				}
			}
			while (daySummary.values().remove(null)) {
			}
			jiraSummaryTO.setWorkLogDaySummeryTOs(daySummary.values().stream().collect(Collectors.toList()));
		} catch (JSONException | TaskManagementServiceException e) {
			logger.error(ErrorCodes.US04, ErrorMessages.FAILED_DURING_USER_WORKLOG_SUMMARY_CALCULATION);
			throw new TaskManagementServiceException(ErrorCodes.US04,
					ErrorMessages.FAILED_DURING_USER_WORKLOG_SUMMARY_CALCULATION);
		}
		logger.debug(GeneralConstants.LOGGER_FORMAT, GeneralConstants.METHOD_END,
				MethodConstants.UPDATE_JIRA_WORKLOG_SUMMARY);

	}

	private void UpdateMapwithDates(Map<String, WorkLogDaySummaryTO> daySummary, int noOfDays) {
		LocalDate currentDate = LocalDate.now();
		for (int i = noOfDays; i >= 0; i--) {
			daySummary.put(currentDate.minusDays(i).toString(), null);
		}
	}

	@Override
	public GraphDataTO getUserSummaryGraphData(int noOfDays, List<JiraSummaryTO> jiraSummayDetails) {
		Random random = new Random();
		int low = 0;
		int high = 256;
		GraphDataTO graphData = new GraphDataTO();
		List<GraphDatasetTO> graphDatasets = new ArrayList<>();
		Map<String, Double> datesMap = new LinkedHashMap<>();
		UpdateListwithDates(datesMap, noOfDays);
		graphData.setLabels(datesMap.keySet().stream().collect(Collectors.toList()));
		for (JiraSummaryTO jiraSummaryTO : jiraSummayDetails) {
			GraphDatasetTO graphDataSet = new GraphDatasetTO();
			graphDataSet.setLabel(jiraSummaryTO.getKey());
			graphDataSet.setBackgroundColor("rgb(" + random.nextInt(high - low) + "," + random.nextInt(high - low) + ","
					+ random.nextInt(high - low) + ",0.5)");
			creatingWorkhoursData(datesMap, jiraSummaryTO.getWorkLogDaySummeryTOs());
			List<Double> hoursWorked = new ArrayList<>();
			for (Map.Entry<String, Double> entry : datesMap.entrySet()) {
				hoursWorked.add(entry.getValue());
				entry.setValue(null);
			}
			graphDataSet.setData(hoursWorked);
			graphDatasets.add(graphDataSet);
		}
		graphData.setDatasets(graphDatasets);
		return graphData;
	}

	private void creatingWorkhoursData(Map<String, Double> datesMap, List<WorkLogDaySummaryTO> workLogs) {
		for (WorkLogDaySummaryTO workLogDaySummaryTO : workLogs) {
			if (datesMap.containsKey(
					workLogDaySummaryTO.getStartDate().substring(0, workLogDaySummaryTO.getStartDate().indexOf("T")))) {
				datesMap.put(
						workLogDaySummaryTO.getStartDate().substring(0,
								workLogDaySummaryTO.getStartDate().indexOf("T")),
						Double.parseDouble(workLogDaySummaryTO.getTotalTimeSpent().replaceAll("(?i)(h)", "")));
			}
		}
		for (Map.Entry<String, Double> entry : datesMap.entrySet()) {
			if (entry.getValue() == null) {
				entry.setValue(0.0);
			}
		}
	}

	private void UpdateListwithDates(Map<String, Double> dates, int noOfDays) {
		LocalDate currentDate = LocalDate.now();
		for (int i = noOfDays; i >= 0; i--) {
			dates.put(currentDate.minusDays(i).toString(), null);
		}
	}
}
