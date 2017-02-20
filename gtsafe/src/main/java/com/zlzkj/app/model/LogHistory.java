package com.zlzkj.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * LogHistory entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "gt_logHistory")
public class LogHistory implements java.io.Serializable {

	// Fields

	private Integer id;
	private String templateName;
	private Integer score;
	private Integer score2;
	private Integer score3;
	private Integer summary;
	private Integer addTime;
	private Integer day;
	private Integer checkTime;
	private Integer version;

	// Constructors

	/** default constructor */
	public LogHistory() {
	}

	/** full constructor */
	public LogHistory(String templateName, Integer score, Integer score2,
			Integer score3, Integer summary, Integer addTime, Integer day,
			Integer checkTime, Integer version) {
		this.templateName = templateName;
		this.score = score;
		this.score2 = score2;
		this.score3 = score3;
		this.summary = summary;
		this.addTime = addTime;
		this.day = day;
		this.checkTime = checkTime;
		this.version = version;
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

	@Column(name = "templateName", nullable = false, length = 50)
	public String getTemplateName() {
		return this.templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	@Column(name = "score", nullable = false)
	public Integer getScore() {
		return this.score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	@Column(name = "score2", nullable = false)
	public Integer getScore2() {
		return this.score2;
	}

	public void setScore2(Integer score2) {
		this.score2 = score2;
	}

	@Column(name = "score3", nullable = false)
	public Integer getScore3() {
		return this.score3;
	}

	public void setScore3(Integer score3) {
		this.score3 = score3;
	}

	@Column(name = "summary", nullable = false)
	public Integer getSummary() {
		return this.summary;
	}

	public void setSummary(Integer summary) {
		this.summary = summary;
	}

	@Column(name = "addTime", nullable = false)
	public Integer getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Integer addTime) {
		this.addTime = addTime;
	}

	@Column(name = "day", nullable = false)
	public Integer getDay() {
		return this.day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	@Column(name = "checkTime", nullable = false)
	public Integer getCheckTime() {
		return this.checkTime;
	}

	public void setCheckTime(Integer checkTime) {
		this.checkTime = checkTime;
	}

	@Column(name = "version", nullable = false)
	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}