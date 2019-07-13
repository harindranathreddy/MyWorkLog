package com.cerner.shipit.taskmanagement.utility.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_summary")
public class UserSummary implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7801189041756432206L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", updatable = false, nullable = false)
	private long id;
	@Column(name = "LOGIN_ATTEMPTS")
	private long loginAttempts;
	@Column(name = "SUCCESS")
	private long success;
	@Column(name = "FAILED")
	private long failed;
	@OneToOne
	@JoinColumn(name = "USER_ID", referencedColumnName = "id")
	private User user;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getLoginAttempts() {
		return loginAttempts;
	}

	public void setLoginAttempts(long loginAttempts) {
		this.loginAttempts = loginAttempts;
	}

	public long getSuccess() {
		return success;
	}

	public void setSuccess(long success) {
		this.success = success;
	}

	public long getFailed() {
		return failed;
	}

	public void setFailed(long failed) {
		this.failed = failed;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
