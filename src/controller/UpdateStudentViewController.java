/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import service.StudentService;
import model.Student;

/**
 * FXML Controller class
 *
 * @author Windyl
 */
public class UpdateStudentViewController implements Initializable {
    
    @FXML
    TextField barcodeField,firstnameField,middlenameField,lastnameField;
    StudentService service = new StudentService();
  
  @Override
  public void initialize(URL url, ResourceBundle rb) {
      barcodeField.setOnKeyPressed(event -> {
          if(event.getCode() == KeyCode.ENTER){
            try{      
              String value = barcodeField.getText();
              Student student = service.search(value);
              if(student != null){
                  autoFill(student);
                  
              }
              
            }catch(SQLException error){
                error.printStackTrace();
            }
              
              
          }
      });
  }
    public void handleCancel(){
        
    }   
    
    public void handleDelete(){
        
    }
    
    public void handleUpdate(){
        
    }
    public void autoFill(Student student){
        firstnameField.setText(student.getFirstname());
        middlenameField.setText(student.getMiddlename());
        lastnameField.setText(student.getLastname());
        
    }
    
}
