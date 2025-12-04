/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;

/**
 *
 * @author Windyl
 */
public class Student {

   
    private int student_id;
    private String firstname;
    private String middlename;
    private String lastname;
    private String school_id;
    private String isActive;
    private String course;
    private Date createdAt;
    
    //For Insertion
    public Student(String firstname, String middlename, String lastname, String school_id,String isActive, String course) {
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.school_id = school_id;
        this.isActive = isActive;
        this.course = course;
    }
    
    //Retrieving the value
    public Student(int student_id, String firstname, String middlename, String lastname, String school_id,String isActive,String course,Date createdAt) {
        this.student_id = student_id;
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.school_id = school_id;
        this.isActive = isActive;
        this.course = course;
        this.createdAt = createdAt;
    }

  
  

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
    
     public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getSchoolId() {
        return school_id;
    }

    public void setQRCode(String school_id) {
        this.school_id = school_id;
    }
    
      public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
     public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

  
}
