package com.zlzkj.app.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Log entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "gt_logImage")
public class LogImage implements java.io.Serializable{
	private Integer id;
	private Integer logId;
	private String path;
	
	public LogImage(){
		
	}

	public LogImage(Integer id, Integer logId, String path) {
		this.id = id;
		this.logId = logId;
		this.path = path;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "logId", nullable = false)
	public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	@Column(name = "path", nullable = false)
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	
}
