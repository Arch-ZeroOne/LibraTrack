/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Windyl
 */
public class Student {

  
    private int student_id;
    private String firstname;
    private String middlename;
    private String lastname;
    private String barcode;
    private boolean isActive;
    
    //For Insertion
    public Student(String firstname, String middlename, String lastname, String barcode,boolean isActive) {
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.barcode = barcode;
        this.isActive = isActive;
    }
    
    //Retrieving the value
    public Student(int student_id, String firstname, String middlename, String lastname, boolean isActive) {
        this.student_id = student_id;
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.isActive = isActive;
    }
    
    public Student(int student_id, String firstname, String middlename, String lastname, String barcode, boolean isActive) {
        this.student_id = student_id;
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.barcode = barcode;
        this.isActive = isActive;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    
}
