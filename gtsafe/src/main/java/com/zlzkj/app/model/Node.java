package com.zlzkj.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Node entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "gt_node")
public class Node implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private String alias;
	private Integer pid;
	private String remark;
	private Short status;
	private Integer addTime;
	private Integer orderId;
	private Short isMenu;

	// Constructors

	/** default constructor */
	public Node() {
	}

	/** minimal constructor */
	public Node(String alias, Integer pid, String remark, Short status,
			Integer addTime, Integer orderId, Short isMenu) {
		this.alias = alias;
		this.pid = pid;
		this.remark = remark;
		this.status = status;
		this.addTime = addTime;
		this.orderId = orderId;
		this.isMenu = isMenu;
	}

	/** full constructor */
	public Node(String name, String alias, Integer pid, String remark,
			Short status, Integer addTime, Integer orderId, Short isMenu) {
		this.name = name;
		this.alias = alias;
		this.pid = pid;
		this.remark = remark;
		this.status = status;
		this.addTime = addTime;
		this.orderId = orderId;
		this.isMenu = isMenu;
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

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "alias", nullable = false, length = 100)
	public String getAlias() {
		return this.alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Column(name = "pid", nullable = false)
	public Integer getPid() {
		return this.pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	@Column(name = "remark", nullable = false)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "status", nullable = false)
	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
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

	@Column(name = "isMenu", nullable = false)
	public Short getIsMenu() {
		return this.isMenu;
	}

	public void setIsMenu(Short isMenu) {
		this.isMenu = isMenu;
	}

}