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
@Table(name="gt_upimg")

public class Upimg  implements java.io.Serializable {


    // Fields    

     private Integer id;
     private Integer userId;
     private String imgName;
     private Integer contactId;
     private Integer addTime;


    // Constructors

    /** default constructor */
    public Upimg() {
    }

    
    /** full constructor */
    public Upimg(Integer userId , String imgName, String name, Integer contactId, Integer addTime) {
        this.userId = userId;
        this.imgName = imgName;
        this.contactId = contactId;
        this.addTime = addTime;
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
    
    @Column(name="imgName", nullable=false)

    public String getimgName() {
        return this.imgName;
    }
    
    public void setimgName(String imgName) {
        this.imgName = imgName;
    }

    
    @Column(name="contactId", nullable=false)

    public Integer getcontactId() {
        return this.contactId;
    }
    
    public void setcontactId(Integer contactId) {
        this.contactId = contactId;
    }
    
    @Column(name="addTime", nullable=false)

    public Integer getAddTime() {
        return this.addTime;
    }
    
    public void setAddTime(Integer addTime) {
        this.addTime = addTime;
    }


}