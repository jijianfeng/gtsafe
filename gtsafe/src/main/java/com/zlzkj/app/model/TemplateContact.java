package com.zlzkj.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TemplateContact entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "gt_templateContact")
public class TemplateContact implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer contactCateId;
	private Integer templateId;
	private Integer contactId;
	private Integer addTime;

	// Constructors

	/** default constructor */
	public TemplateContact() {
	}

	/** full constructor */
	public TemplateContact(Integer contactCateId, Integer templateId,
			Integer contactId, Integer addTime) {
		this.contactCateId = contactCateId;
		this.templateId = templateId;
		this.contactId = contactId;
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

	@Column(name = "contactCateId", nullable = false)
	public Integer getContactCateId() {
		return this.contactCateId;
	}

	public void setContactCateId(Integer contactCateId) {
		this.contactCateId = contactCateId;
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

	@Column(name = "addTime", nullable = false)
	public Integer getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Integer addTime) {
		this.addTime = addTime;
	}

}