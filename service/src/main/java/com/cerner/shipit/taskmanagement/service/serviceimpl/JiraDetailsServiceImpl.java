package com.cerner.shipit.taskmanagement.service.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.cerner.shipit.taskmanagement.exception.TaskManagementServiceException;
import com.cerner.shipit.taskmanagement.service.jiraapi.JiraApi;
import com.cerner.shipit.taskmanagement.service.service.JiraDetailsService;
import com.cerner.shitit.taskmanagement.utility.tos.JiraDetails;

@Service
@Component("jiraDetailsServiceImpl")
public class JiraDetailsServiceImpl implements JiraDetailsService {

	@Autowired
	JiraApi jiraApi;

	public List<JiraDetails> fetchJiraDetailsByUserId(String UserId) throws TaskManagementServiceException {
		List<JiraDetails> jiraDetails = new ArrayList<JiraDetails>();
		final CloseableHttpResponse jiraDetailsfromApi = jiraApi.fetchJiraDetailsById(UserId);
		jiraDetails = filterJiraDetails(jiraDetailsfromApi);
		return jiraDetails;
	}

	private List<JiraDetails> filterJiraDetails(CloseableHttpResponse jiraDetailsfromApi) {
		final List<JiraDetails> jiraDetails = new ArrayList<JiraDetails>();
		return jiraDetails;
	}
}
