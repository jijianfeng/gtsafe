package com.zlzkj.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Contact entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "gt_contact")
public class Contact implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer contactCateId;
	private String number;
	private Short type;
	private String name;
	private Integer addTime;
	private Integer orderId;
	private Short isSelect;
	private Integer forward;
	private Short status;

	// Constructors

	/** default constructor */
	public Contact() {
	}

	/** full constructor */
	public Contact(Integer contactCateId, String number, Short type,
			String name, Integer addTime, Integer orderId, Short isSelect,
			Integer forward, Short status) {
		this.contactCateId = contactCateId;
		this.number = number;
		this.type = type;
		this.name = name;
		this.addTime = addTime;
		this.orderId = orderId;
		this.isSelect = isSelect;
		this.forward = forward;
		this.status = status;
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

	@Column(name = "number", nullable = false, length = 30)
	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Column(name = "type", nullable = false)
	public Short getType() {
		return this.type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	@Column(name = "name", nullable = false, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "addTime", nullable = false)
	public Integer getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Integer addTime) {
		this.addTime = addTime;
	}

	@Column(name = "orderId", nullable = false)
	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	@Column(name = "isSelect", nullable = false)
	public Short getIsSelect() {
		return this.isSelect;
	}

	public void setIsSelect(Short isSelect) {
		this.isSelect = isSelect;
	}

	@Column(name = "forward", nullable = false)
	public Integer getForward() {
		return this.forward;
	}

	public void setForward(Integer forward) {
		this.forward = forward;
	}

	@Column(name = "status", nullable = false)
	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

}