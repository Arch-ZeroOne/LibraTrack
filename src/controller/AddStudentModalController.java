/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import model.Student;
import service.StudentService;
import util.ModalUtil;


/**
 * FXML Controller class
 *
 * @author Windyl
 */

//TODO : add isactive field to updates
public class AddStudentModalController implements Initializable {
    @FXML
    TextField firstnameField,middlenameField,lastnameField,barcodeField;
    ModalUtil modal_util = new ModalUtil();
    StudentService service = new StudentService();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

public void handleSave(ActionEvent event) throws SQLException{
        String firstname = firstnameField.getText();
        String middlename = middlenameField.getText();
        String lastname = lastnameField.getText();
        String barcode = barcodeField.getText();
        
        Student student = new Student(firstname,middlename,lastname,barcode,true);
        service.insert(student);
        clearForm();
        handleCancel(event);
   }
   
   //Handles the modal closing
   public void handleCancel(ActionEvent event){
       modal_util.closeModal(event);
       
       
   }
   
   
   public void clearForm(){
       firstnameField.setText("");
       middlenameField.setText("");
       lastnameField.setText("");
       barcodeField.setText("");
       
       
   }
  
       
    
}
