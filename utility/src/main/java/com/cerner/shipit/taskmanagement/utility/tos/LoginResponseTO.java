package com.cerner.shipit.taskmanagement.utility.tos;

import java.util.List;
import java.util.Map;

public class LoginResponseTO {
	int status;
	String message;
	Map<String, List<String>> headers;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, List<String>> headers) {
		this.headers = headers;
	}

}
