package com.zlzkj.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Attribute entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "gt_attribute")
public class Attribute implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private Integer important;
	private Integer score;
	private String measures;
	private Integer addTime;
	private Integer forward;
	private Short status;

	// Constructors

	/** default constructor */
	public Attribute() {
	}

	/** full constructor */
	public Attribute(String name, Integer important, Integer score,
			String measures, Integer addTime, Integer forward, Short status) {
		this.name = name;
		this.important = important;
		this.score = score;
		this.measures = measures;
		this.addTime = addTime;
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

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "important", nullable = false)
	public Integer getImportant() {
		return this.important;
	}

	public void setImportant(Integer important) {
		this.important = important;
	}

	@Column(name = "score", nullable = false)
	public Integer getScore() {
		return this.score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	@Column(name = "measures", nullable = false)
	public String getMeasures() {
		return this.measures;
	}

	public void setMeasures(String measures) {
		this.measures = measures;
	}

	@Column(name = "addTime", nullable = false)
	public Integer getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Integer addTime) {
		this.addTime = addTime;
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