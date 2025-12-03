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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import service.StudentService;
import model.Student;
import util.AlertUtil;

/**
 * FXML Controller class
 *
 * @author Windyl
 */
public class UpdateStudentViewController implements Initializable {
    
    @FXML
    TextField qrCodeField,firstnameField,middlenameField,lastnameField,schoolIdField;
    StudentService service = new StudentService();
    @FXML
    ComboBox<String> courseComboBox,isActiveComboBox;
    AlertUtil alert_util = new AlertUtil();
  
  @Override
  public void initialize(URL url, ResourceBundle rb) {
      
       courseComboBox.getItems().setAll("BSIT","BSBA","BSA","BTLED");
       isActiveComboBox.getItems().setAll("Active","Inactive");
       schoolIdField.setEditable(false);
      
     
  }
    public void handleScan(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER){
            try{      
              String value = qrCodeField.getText();
              Student student = service.search(value);
              if(student != null){
                  autoFill(student);
                  
              }
              
            }catch(SQLException error){
                error.printStackTrace();
            }
              
              
          }
        
    }
  
    public void handleDelete() throws SQLException{
         String schoolId = schoolIdField.getText().trim();
         
         
        
         
         if(service.remove(schoolId,"Inactive")){
            alert_util.success("Student Data Deactivated");
            clearStudentForm();
            return;
            
        }
          alert_util.error("Error Updating Student data");
        
    }
        
    
    
    public void handleUpdate() throws SQLException{
    String firstname = firstnameField.getText().trim();
    String middlename = middlenameField.getText().trim();
    String lastname = lastnameField.getText().trim();
    String schoolId = schoolIdField.getText().trim();
    String course = courseComboBox.getValue().trim();
    String isActive = isActiveComboBox.getValue().trim();
    
    
    Student student = new Student(firstname,middlename,lastname,schoolId,isActive,course);
        if(service.update(student)){
            alert_util.success("Student Data Updated");
            clearStudentForm();
            return;
            
        }
          alert_util.error("Error Updating Student data");
        
    }
    public void autoFill(Student student){
        firstnameField.setText(student.getFirstname());
        middlenameField.setText(student.getMiddlename());
        lastnameField.setText(student.getLastname());
        schoolIdField.setText(student.getSchoolId());
        courseComboBox.setValue(student.getCourse());
        isActiveComboBox.setValue(student.getIsActive());
       
        
    }
    
    public void clearStudentForm() {
    // Clear Text Fields
    firstnameField.setText("");
    middlenameField.setText("");
    lastnameField.setText("");
    schoolIdField.setText("");

    // Clear ComboBox Selections
    courseComboBox.setValue(null);
    isActiveComboBox.setValue(null);
}
    
}
