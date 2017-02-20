package com.zlzkj.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Backup entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "gt_backup")
public class Backup implements java.io.Serializable {

	// Fields

	private Integer id;
	private String fileName;
	private Integer number;
	private String format;
	private Integer addTime;
	private String size;

	// Constructors

	/** default constructor */
	public Backup() {
	}

	/** full constructor */
	public Backup(String fileName, Integer number, String format,
			Integer addTime, String size) {
		this.fileName = fileName;
		this.number = number;
		this.format = format;
		this.addTime = addTime;
		this.size = size;
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

	@Column(name = "fileName", nullable = false)
	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "number", nullable = false)
	public Integer getNumber() {
		return this.number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	@Column(name = "format", nullable = false, length = 50)
	public String getFormat() {
		return this.format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	@Column(name = "addTime", nullable = false)
	public Integer getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Integer addTime) {
		this.addTime = addTime;
	}

	@Column(name = "size", nullable = false)
	public String getSize() {
		return this.size;
	}

	public void setSize(String size) {
		this.size = size;
	}

}