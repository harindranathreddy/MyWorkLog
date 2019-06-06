package com.cerner.shipit.taskmanagement.utility.response;

import com.cerner.shipit.taskmanagement.exception.TaskManagementServiceException;
import com.cerner.shipit.taskmanagement.utility.tos.ErrorResponseTO;
import com.cerner.shipit.taskmanagement.utility.tos.SuccessResponseTO;

public class Response {
	public SuccessResponseTO getSuccessResposne(String responseCode, String responseMessage, Object responseObject) {
		SuccessResponseTO successResponseTO = new SuccessResponseTO();
		successResponseTO.setResponseCode(responseCode);
		successResponseTO.setResponseMessage(responseMessage);
		successResponseTO.setResponseData(responseObject);
		return successResponseTO;

	}

	public Object getErrorResponse(TaskManagementServiceException e) {
		ErrorResponseTO errorResponseTO = new ErrorResponseTO();
		errorResponseTO.setResponseCode(e.getErrorCode());
		errorResponseTO.setResponseMessage(e.getErrorMessage());
		return errorResponseTO;
	}

	public Object getErrorResponse(String responseCode, String responseMessage) {
		ErrorResponseTO errorResponseTO = new ErrorResponseTO();
		errorResponseTO.setResponseCode(responseCode);
		errorResponseTO.setResponseMessage(responseMessage);
		return errorResponseTO;
	}
}
