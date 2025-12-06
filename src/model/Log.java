/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;
public class Log {

   
     private int log_id;
     private String firstname;
     private String middlename;
     private String lastname;
     private Date  log_date;
     private String school_id;
  
    // For retrieving values
    public Log(int log_id, String firstname, String middlename, String lastname,String school_id) {
        this.log_id = log_id;
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.school_id = school_id;
    }
   
    // For new date entry
    public Log(String firstname, String middlename, String lastname, Date log_date, String school_id) {
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.log_date = log_date;
        this.school_id = school_id;
    }
   
     
     
    public int getLog_id() {
        return log_id;
    }

    public void setLog_id(int log_id) {
        this.log_id = log_id;
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

    public Date getLog_date() {
        return log_date;
    }

    public void setLog_date(Date log_date) {
        this.log_date = log_date;
    }
    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }
    
     
     
             
     
}
