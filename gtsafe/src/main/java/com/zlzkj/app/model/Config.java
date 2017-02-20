package com.zlzkj.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Config entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "gt_config")
public class Config implements java.io.Serializable {

	// Fields

	private Integer id;
	private String title;
	private String name;
	private String value;
	private String grouping;

	// Constructors

	/** default constructor */
	public Config() {
	}

	/** full constructor */
	public Config(String title, String name, String value, String grouping) {
		this.title = title;
		this.name = name;
		this.value = value;
		this.grouping = grouping;
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

	@Column(name = "title", nullable = false, length = 50)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "name", nullable = false, length = 64)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "value", nullable = false, length = 65535)
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "grouping", nullable = false, length = 64)
	public String getGrouping() {
		return this.grouping;
	}

	public void setGrouping(String grouping) {
		this.grouping = grouping;
	}

}