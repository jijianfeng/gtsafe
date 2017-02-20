package com.zlzkj.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ContactAttribute entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "gt_contactAttribute")
public class ContactAttribute implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer contactId;
	private Integer attributeId;
	private Integer addTime;

	// Constructors

	/** default constructor */
	public ContactAttribute() {
	}

	/** full constructor */
	public ContactAttribute(Integer contactId, Integer attributeId,
			Integer addTime) {
		this.contactId = contactId;
		this.attributeId = attributeId;
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

	@Column(name = "attributeId", nullable = false)
	public Integer getAttributeId() {
		return this.attributeId;
	}

	public void setAttributeId(Integer attributeId) {
		this.attributeId = attributeId;
	}

	@Column(name = "addTime", nullable = false)
	public Integer getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Integer addTime) {
		this.addTime = addTime;
	}

}