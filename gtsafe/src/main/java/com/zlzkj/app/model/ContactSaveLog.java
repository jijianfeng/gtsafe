package com.zlzkj.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ContactSaveLog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "gt_contactSaveLog")
public class ContactSaveLog implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer templateId;
	private Integer contactId;
	private String value;
	private Integer addTime;

	// Constructors

	/** default constructor */
	public ContactSaveLog() {
	}

	/** full constructor */
	public ContactSaveLog(Integer templateId, Integer contactId, String value,
			Integer addTime) {
		this.templateId = templateId;
		this.contactId = contactId;
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

	@Column(name = "templateId", nullable = false)
	public Integer getTemplateId() {
		return this.templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	@Column(name = "contactId", nullable = false)
	public Integer getContactId() {
		return this.contactId;
	}

	public void setContactId(Integer contactId) {
		this.contactId = contactId;
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