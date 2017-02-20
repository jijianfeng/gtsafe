package com.zlzkj.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ContactLogHistory entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "gt_contactLogHistory")
public class ContactLogHistory implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer contactId;
	private Integer logHistoryId;
	private String value;
	private Integer addTime;

	// Constructors

	/** default constructor */
	public ContactLogHistory() {
	}

	/** full constructor */
	public ContactLogHistory(Integer contactId, Integer logHistoryId,
			String value, Integer addTime) {
		this.contactId = contactId;
		this.logHistoryId = logHistoryId;
		this.value = value;
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

	@Column(name = "contactId", nullable = false)
	public Integer getContactId() {
		return this.contactId;
	}

	public void setContactId(Integer contactId) {
		this.contactId = contactId;
	}

	@Column(name = "logHistoryId", nullable = false)
	public Integer getLogHistoryId() {
		return this.logHistoryId;
	}

	public void setLogHistoryId(Integer logHistoryId) {
		this.logHistoryId = logHistoryId;
	}

	@Column(name = "value", nullable = false)
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "addTime", nullable = false)
	public Integer getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Integer addTime) {
		this.addTime = addTime;
	}

}