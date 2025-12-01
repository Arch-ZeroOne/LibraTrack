/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.sql.SQLException;
import java.util.ArrayList;
import dao.StudentDao;
import model.Student;

public class StudentService {
    StudentDao student_dao = new StudentDao();

    public boolean insert(Student student) throws SQLException{
        return student_dao.insert(student);
               
    }
    
    public boolean update(Student student) throws SQLException{
        return student_dao.update(student);
    }
    
    public boolean remove(Student student) throws SQLException{
        return student_dao.remove(student);
    }
    
     public Student search(String barcode) throws SQLException{
        return student_dao.search(barcode);
    }
     
     public ArrayList<Student> list() throws SQLException{
         return student_dao.list();
         
     }
    
}


