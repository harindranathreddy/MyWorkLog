package com.cerner.shipit.taskmanagement.exception;

public class TaskManagementServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String errorCode;
	private final String errorMessage;
	
	 public TaskManagementServiceException(String errorCode, String errorMessage) {
	        super();
	        this.errorCode = errorCode;
	        this.errorMessage = errorMessage;
	    }

	    public TaskManagementServiceException(Throwable cause, String errorCode, String errorMessage) {
	        super(cause);
	        this.errorCode = errorCode;
	        this.errorMessage = errorMessage;
	    }

	    public TaskManagementServiceException(TaskManagementDBException error) {
	        super(error.getCause());
	        this.errorCode = error.getErrorCode();
	        this.errorMessage = error.getErrorMessage();
	    }
	    
	    public String getErrorCode() {
	        return this.errorCode;
	    }

	    public String getErrorMessage() {
	        return this.errorMessage;
	    }

}
