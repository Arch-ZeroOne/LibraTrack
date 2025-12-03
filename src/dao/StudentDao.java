/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Student;
import util.DatabaseUtil;
import interfaces.StudentInterface;


/**
 *
 * @author Windyl
 */
public class StudentDao implements StudentInterface{
    
    public final DatabaseUtil util = new DatabaseUtil();
    public final Connection connection = util.connect();
    
    public boolean insert(Student student) throws SQLException{
        String query = "INSERT INTO student (firstname,middlename,lastname,school_id,isActive,course) VALUES (?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        
        preparedStatement.setString(1,student.getFirstname());
        preparedStatement.setString(2,student.getMiddlename());
        preparedStatement.setString(3,student.getLastname());
        preparedStatement.setString(4,student.getSchoolId());
        preparedStatement.setString(5, student.getIsActive());
        preparedStatement.setString(6, student.getCourse());
        

        int rows_affected = preparedStatement.executeUpdate();
        
        
        if(rows_affected != 0){
            return true;
            
        }
        
        return false;
    }
    
    public boolean update(Student student) throws SQLException{
        
        String query = "UPDATE student SET firstname = ?,middlename = ? ,lastname = ?,isActive = ? ,"
                + "course = ? WHERE school_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1,student.getFirstname());
        preparedStatement.setString(2,student.getMiddlename());
        preparedStatement.setString(3,student.getLastname());
        preparedStatement.setString(4,student.getIsActive());
        preparedStatement.setString(5,student.getCourse());
        preparedStatement.setString(6,student.getSchoolId());
       
        
        int rows_affected = preparedStatement.executeUpdate();
        
        if(rows_affected != 0){
            System.out.println("Book Updated");
            return true;
            
        }
        return false;
        
    }
    
    public boolean remove(String qrcode,String isActive ) throws SQLException{
        
    // Note: Assumes 'connection' object is available and valid.
    // Also assumes 'student_id' is the correct column name for the primary key.
    String query = "UPDATE student SET isActive = ? WHERE school_id = ?";
    
    PreparedStatement preparedStatement = null;
    
    try {
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, isActive);
        preparedStatement.setString(2, qrcode);
        
        int rows_affected = preparedStatement.executeUpdate();
        
        if (rows_affected != 0) {
            System.out.println("Student Status Updated");
            return true;
        }
        return false;
        
    } catch (SQLException e) {
        System.err.println("Database error: " + e.getMessage());
        return false;
    
}
        
    }
    
     public Student search(String qrcode) throws SQLException{
        String query = "SELECT * FROM student WHERE school_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, qrcode);
        ResultSet result = preparedStatement.executeQuery();
         
        while(result.next()){
            int id = result.getInt("student_id");
            String firstname = result.getString("firstname");
            String middlename = result.getString("middlename");
            String lastname = result.getString("lastname");
            String school_id = result.getString("school_id");
            String isActive = result.getString("isActive");
            String course = result.getString("course");
            return new Student(id,firstname,middlename,lastname,school_id,isActive,course); 
        }
        return null;
    }
     
    public ArrayList<Student> list() throws SQLException{
        String query = "SELECT * FROM student";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet result = preparedStatement.executeQuery();
        ArrayList<Student> student_list = new ArrayList<>();
        
        while(result.next()){
            int id = result.getInt("student_id");
            String firstname = result.getString("firstname");
            String middlename = result.getString("middlename");
            String lastname = result.getString("lastname");
            String school_id = result.getString("school_id");
            String isActive = result.getString("isActive");
            String course = result.getString("course");
         
            Student student = new Student(id,firstname,middlename,lastname,school_id,isActive,course);
            student_list.add(student);
            
        }
        
        
        return student_list;

    }
    
    
}
