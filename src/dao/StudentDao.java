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
        String query = "INSERT INTO student (firstname,middlename,lastname,barcode) VALUES (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        
        preparedStatement.setString(1,student.getFirstname());
        preparedStatement.setString(2,student.getMiddlename());
        preparedStatement.setString(3,student.getLastname());
        preparedStatement.setString(4,student.getBarcode());
        

        int rows_affected = preparedStatement.executeUpdate();
        
        
        if(rows_affected != 0){
            return true;
            
        }
        
        return false;
    }
    
    public boolean update(Student student) throws SQLException{
        return false;
    }
    
    public boolean remove(Student student) throws SQLException{
        return false;
    }
    
     public Student search(String barcode) throws SQLException{
        String query = "SELECT * FROM student WHERE barcode = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, barcode);
        ResultSet result = preparedStatement.executeQuery();
         
        while(result.next()){
            int id = result.getInt("student_id");
            String firstname = result.getString("firstname");
            String middlename = result.getString("middlename");
            String lastname = result.getString("lastname");
            boolean isActive = result.getBoolean("isActive");
            return new Student(id,firstname,middlename,lastname,isActive); 
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
            String barcode = result.getString("barcode");
            boolean isActive = result.getBoolean("isActive");
         
            Student student = new Student(id,firstname,middlename,lastname,barcode,isActive);
            student_list.add(student);
            
        }
        
        
        return student_list;

    }
    
    
}
