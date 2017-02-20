package com.zlzkj.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * LogReason entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "gt_logReason")
public class LogReason implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer logId;
	private Integer logHistoryId;
	private String reason;
	private String auditor;
	private Integer addTime;

	// Constructors

	/** default constructor */
	public LogReason() {
	}

	/** full constructor */
	public LogReason(Integer logId, Integer logHistoryId, String reason,
			String auditor, Integer addTime) {
		this.logId = logId;
		this.logHistoryId = logHistoryId;
		this.reason = reason;
		this.auditor = auditor;
		this.addTime = addTime;
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

	@Column(name = "logId", nullable = false)
	public Integer getLogId() {
		return this.logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	@Column(name = "logHistoryId", nullable = false)
	public Integer getLogHistoryId() {
		return this.logHistoryId;
	}

	public void setLogHistoryId(Integer logHistoryId) {
		this.logHistoryId = logHistoryId;
	}

	@Column(name = "reason", nullable = false)
	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Column(name = "auditor", nullable = false, length = 50)
	public String getAuditor() {
		return this.auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	@Column(name = "addTime", nullable = false)
	public Integer getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Integer addTime) {
		this.addTime = addTime;
	}

}