package com.zlzkj.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * NoticeBoard entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "gt_noticeBoard")
public class NoticeBoard implements java.io.Serializable {

	// Fields

	private Integer id;
	private String content;
	private Short isShow;
	private Integer orderId;

	// Constructors

	/** default constructor */
	public NoticeBoard() {
	}

	/** full constructor */
	public NoticeBoard(String content, Short isShow, Integer orderId) {
		this.content = content;
		this.isShow = isShow;
		this.orderId = orderId;
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

	@Column(name = "content", nullable = false, length = 65535)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "isShow", nullable = false)
	public Short getIsShow() {
		return this.isShow;
	}

	public void setIsShow(Short isShow) {
		this.isShow = isShow;
	}

	@Column(name = "orderId", nullable = false)
	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

}