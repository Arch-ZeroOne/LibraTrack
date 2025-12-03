/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import com.google.zxing.WriterException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Student;
import service.StudentService;
import service.QRService;
import util.ModalUtil;
import util.AlertUtil;
import util.QRConfig;
import java.nio.file.Path;
import javafx.scene.control.ComboBox;


/**
 * FXML Controller class
 *
 * @author Windyl
 */

//TODO : add isactive field to updates
public class AddStudentModalController implements Initializable {
    @FXML
    TextField firstnameField,middlenameField,lastnameField,idField;
    ModalUtil modal_util = new ModalUtil();
    AlertUtil alert = new AlertUtil();
    QRConfig qr_config = new QRConfig();
    StudentService service = new StudentService();
    QRService qr_service = new QRService();
    @FXML
    ImageView qrCodeImageView;
    @FXML
    ComboBox<String> courseComboBox;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        courseComboBox.getItems().setAll("BSIT","BSBA","BSA","BTLED");
    }

    public void handleSave(ActionEvent event) throws SQLException{
        String firstname = firstnameField.getText().trim();
        String middlename = middlenameField.getText().trim();
        String lastname = lastnameField.getText().trim();
        String school_id = idField.getText();
        String course = courseComboBox.getValue().trim();
        Student student = new Student(firstname,middlename,lastname,school_id,"Active",course);
        
        if(service.insert(student)){
           alert.success("Student Has Been Registered");
           clearForm();
           handleCancel(event);
           return;
            
        }
        
        alert.error("Student not registered");
      
       
   }
    
    public void handleGenerateQr()  throws WriterException,IOException{
         Path path = qr_service.generateQR(idField.getText(),qr_config.path+"/"+idField.getText()+".png");
         handleQrPreview(path);
    }
   
   //Handles the modal closing
   public void handleCancel(ActionEvent event){
       modal_util.closeModal(event);
       
       
   }
   
   public void handleQrPreview(Path path){
       //Nedds the file: in the image class
       Image image = new Image("file:"+String.valueOf(path));
       qrCodeImageView.setImage(image);
   }
   
   public void clearForm(){
       firstnameField.setText("");
       middlenameField.setText("");
       lastnameField.setText("");
       idField.setText("");
       qrCodeImageView.setImage(null);
       
       
   }
  
       
    
}
