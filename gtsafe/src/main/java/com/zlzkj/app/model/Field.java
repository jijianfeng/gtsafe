package com.zlzkj.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "gt_field")
public class Field implements java.io.Serializable{

		// Fields

		private Integer id;
		private String number;
		private String fieldName;
		private Integer pid;
		private Short status;
		private Integer forward;
		private Integer addTime;
		private Integer orderId;

		// Constructors

		/** default constructor */
		public Field() {
		}

		/** full constructor */
		public Field(Integer id, String number,String fieldName,
				Integer pid, Short status,Integer forward) {
			this.id = id;
			this.number = number;
			this.fieldName = fieldName;
			this.pid = pid;
			this.status = status;
			this.forward = forward;
		}

		// Property accessors
		@Id
		@GeneratedValue(strategy = IDENTITY)
		@Column(name = "id", unique = true, nullable = false)
		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		@Column(name = "number", nullable = false)
		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}
		
		@Column(name = "fieldName", nullable = false)
		public String getFieldName() {
			return fieldName;
		}

		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}

		@Column(name = "pid", nullable = false)
		public Integer getPid() {
			return pid;
		}

		public void setPid(Integer pid) {
			this.pid = pid;
		}

		@Column(name = "status", nullable = false)
		public Short getStatus() {
			return this.status;
		}

		public void setStatus(Short status) {
			this.status = status;
		}

		@Column(name = "forward", nullable = false)
		public Integer getForward() {
			return this.forward;
		}

		public void setForward(Integer forward) {
			this.forward = forward;
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
	}