package com.zlzkj.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Query entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "gt_query")
public class Query implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private String fields;
	private String content;
	private String sqlEnd;
	private String sqlTop;
	private Integer addTime;

	// Constructors

	/** default constructor */
	public Query() {
	}

	/** full constructor */
	public Query(String name, String fields, String content, String sqlEnd,
			String sqlTop, Integer addTime) {
		this.name = name;
		this.fields = fields;
		this.content = content;
		this.sqlEnd = sqlEnd;
		this.sqlTop = sqlTop;
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

	@Column(name = "fields", nullable = false)
	public String getFields() {
		return this.fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	@Column(name = "content", nullable = false, length = 20)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "sqlEnd", nullable = false, length = 65535)
	public String getSqlEnd() {
		return this.sqlEnd;
	}

	public void setSqlEnd(String sqlEnd) {
		this.sqlEnd = sqlEnd;
	}

	@Column(name = "sqlTop", nullable = false, length = 65535)
	public String getSqlTop() {
		return this.sqlTop;
	}

	public void setSqlTop(String sqlTop) {
		this.sqlTop = sqlTop;
	}

	@Column(name = "addTime", nullable = false)
	public Integer getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Integer addTime) {
		this.addTime = addTime;
	}

}