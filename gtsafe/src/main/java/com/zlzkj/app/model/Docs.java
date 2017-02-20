package com.zlzkj.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Docs entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="gt_docs")

public class Docs  implements java.io.Serializable {


    // Fields    

     private Integer id;
     private Integer userId;
     private String number;
     private String docsName;
     private String releaseUnit;
     private String name;
     private Integer docsCateId;
     private Integer addTime;
     private Integer buildTime;
     private String remarks;


    // Constructors

    /** default constructor */
    public Docs() {
    }

    
    /** full constructor */
    public Docs(Integer userId, String number, String docsName, String releaseUnit, String name, Integer docsCateId, Integer addTime, Integer buildTime, String remarks) {
        this.userId = userId;
        this.number = number;
        this.docsName = docsName;
        this.releaseUnit = releaseUnit;
        this.name = name;
        this.docsCateId = docsCateId;
        this.addTime = addTime;
        this.buildTime = buildTime;
        this.remarks = remarks;
    }

   
    // Property accessors
    @Id @GeneratedValue(strategy=IDENTITY)
    
    @Column(name="id", unique=true, nullable=false)

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    @Column(name="userId", nullable=false)

    public Integer getUserId() {
        return this.userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    @Column(name="number", nullable=false, length=20)

    public String getNumber() {
        return this.number;
    }
    
    public void setNumber(String number) {
        this.number = number;
    }
    
    @Column(name="docsName", nullable=false)

    public String getDocsName() {
        return this.docsName;
    }
    
    public void setDocsName(String docsName) {
        this.docsName = docsName;
    }
    
    @Column(name="releaseUnit", nullable=false, length=100)

    public String getReleaseUnit() {
        return this.releaseUnit;
    }
    
    public void setReleaseUnit(String releaseUnit) {
        this.releaseUnit = releaseUnit;
    }
    
    @Column(name="name", nullable=false)

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name="docsCateId", nullable=false)

    public Integer getDocsCateId() {
        return this.docsCateId;
    }
    
    public void setDocsCateId(Integer docsCateId) {
        this.docsCateId = docsCateId;
    }
    
    @Column(name="addTime", nullable=false)

    public Integer getAddTime() {
        return this.addTime;
    }
    
    public void setAddTime(Integer addTime) {
        this.addTime = addTime;
    }
    
    @Column(name="buildTime", nullable=false)

    public Integer getBuildTime() {
        return this.buildTime;
    }
    
    public void setBuildTime(Integer buildTime) {
        this.buildTime = buildTime;
    }
    
    @Column(name="remarks", nullable=false, length=500)

    public String getRemarks() {
        return this.remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
   








}