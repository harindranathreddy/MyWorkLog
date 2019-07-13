package com.cerner.shipit.taskmanagement.exception;

public class TaskManagementDBException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3003626744893108930L;
	private final String errorCode;
    private final String errorMessage;
    
    public TaskManagementDBException(String errorCode, String errorMessage) {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public TaskManagementDBException(Throwable cause, String errorCode, String errorMessage) {
        super(cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

}
