package com.zlzkj.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Template entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "gt_template")
public class Template implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private String remark;
	private Integer counts;
	private Integer addTime;

	// Constructors

	/** default constructor */
	public Template() {
	}

	/** full constructor */
	public Template(String name, String remark, Integer counts, Integer addTime) {
		this.name = name;
		this.remark = remark;
		this.counts = counts;
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

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "remark", nullable = false, length = 65535)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "counts", nullable = false)
	public Integer getCounts() {
		return this.counts;
	}

	public void setCounts(Integer counts) {
		this.counts = counts;
	}

	@Column(name = "addTime", nullable = false)
	public Integer getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Integer addTime) {
		this.addTime = addTime;
	}

}