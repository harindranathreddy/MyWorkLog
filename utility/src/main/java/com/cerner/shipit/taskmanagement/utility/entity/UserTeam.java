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
@Table(name = "user_team")
public class UserTeam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1456671834528623024L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", updatable = false, nullable = false)
	private long id;
	@OneToOne
	@JoinColumn(name = "USER", referencedColumnName = "id")
	private User user;
	@OneToOne
	@JoinColumn(name = "TEAM", referencedColumnName = "id")
	private Teams team;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Teams getTeam() {
		return team;
	}

	public void setTeam(Teams team) {
		this.team = team;
	}

}
