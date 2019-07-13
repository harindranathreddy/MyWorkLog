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

import com.cerner.shipit.taskmanagement.utility.enums.Role;

@Entity
@Table(name = "user")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6744716634929692607L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", updatable = false, nullable = false)
	private long id;
	@Column(name = "USER_ID", nullable = false, unique=true)
	private String userId;
	@Column(name = "NAME")
	private String name;
	@Column(name = "MAIL_ID",unique=true)
	private String mailId;
	@Column(name = "ROLE", updatable = true)
	private Enum<Role> role = Role.ASSOCIATE;
	@Column(name = "NOTIFICATION", nullable = false, updatable = true)
	private boolean notification = true;
	@Column(name = "AVATAR")
	private String avatar;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMailId() {
		return mailId;
	}

	public void setMailId(String mailId) {
		this.mailId = mailId;
	}

	public Enum<Role> getRole() {
		return role;
	}

	public void setRole(Enum<Role> role) {
		this.role = role;
	}

	public boolean isNotification() {
		return notification;
	}

	public void setNotification(boolean notification) {
		this.notification = notification;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

}
