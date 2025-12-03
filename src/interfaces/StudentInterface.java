/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

/**
 *
 * @author Windyl
 */
import model.Student;
import java.sql.SQLException;
import java.util.ArrayList;
public interface StudentInterface {
     public ArrayList<Student> list() throws SQLException;
     public boolean insert(Student student) throws SQLException;
     public boolean update(Student student) throws SQLException;
     public boolean remove(String barcode,String isActive) throws SQLException;
     public Student search(String qrcode) throws SQLException;
     
     
    
}
