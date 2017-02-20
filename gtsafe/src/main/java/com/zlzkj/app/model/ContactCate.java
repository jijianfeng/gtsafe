package com.zlzkj.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ContactCate entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "gt_contactCate")
public class ContactCate implements java.io.Serializable {

	// Fields

	private Integer id;
	private String number;
	private String name;
	private Integer pid;
	private Integer counts;
	private Short isShow;
	private Integer addTime;
	private Integer orderId;
	private Integer forward;
	private Short status;

	// Constructors

	/** default constructor */
	public ContactCate() {
	}

	/** full constructor */
	public ContactCate(String number, String name, Integer pid, Integer counts,
			Short isShow, Integer addTime, Integer orderId, Integer forward,
			Short status) {
		this.number = number;
		this.name = name;
		this.pid = pid;
		this.counts = counts;
		this.isShow = isShow;
		this.addTime = addTime;
		this.orderId = orderId;
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

	@Column(name = "number", nullable = false, length = 50)
	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Column(name = "name", nullable = false, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "pid", nullable = false)
	public Integer getPid() {
		return this.pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	@Column(name = "counts", nullable = false)
	public Integer getCounts() {
		return this.counts;
	}

	public void setCounts(Integer counts) {
		this.counts = counts;
	}

	@Column(name = "isShow", nullable = false)
	public Short getIsShow() {
		return this.isShow;
	}

	public void setIsShow(Short isShow) {
		this.isShow = isShow;
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