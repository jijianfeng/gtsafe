package com.zlzkj.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * UserLogin entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "gt_userLogin")
public class UserLogin implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer userId;
	private Integer loginTime;

	// Constructors

	/** default constructor */
	public UserLogin() {
	}

	/** full constructor */
	public UserLogin(Integer userId, Integer loginTime) {
		this.userId = userId;
		this.loginTime = loginTime;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "userId", nullable = false)
	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "loginTIme", nullable = false)
	public Integer getLoginTime() {
		return this.loginTime;
	}

	public void setLoginTime(Integer loginTime) {
		this.loginTime = loginTime;
	}

}