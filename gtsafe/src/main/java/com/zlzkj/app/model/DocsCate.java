package com.zlzkj.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * DocsCate entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "gt_docsCate")
public class DocsCate implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private Integer pid;
	private Integer count;
	private Integer addTime;

	// Constructors

	/** default constructor */
	public DocsCate() {
	}

	/** full constructor */
	public DocsCate(String name, Integer pid, Integer count, Integer addTime) {
		this.name = name;
		this.pid = pid;
		this.count = count;
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

	@Column(name = "count", nullable = false)
	public Integer getCount() {
		return this.count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Column(name = "addTime", nullable = false)
	public Integer getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Integer addTime) {
		this.addTime = addTime;
	}

}