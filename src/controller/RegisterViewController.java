package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import java.io.IOException;
import service.UserService;
import model.User;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import util.AlertUtil;
import util.WindowUtil;


public class RegisterViewController implements Initializable {
     public final UserService service = new UserService();
     public User user_model;
     public final AlertUtil alert_util = new AlertUtil();
     public final WindowUtil window_util = new WindowUtil();
     
    @FXML
    TextField firstnameField,middlenameField,lastnameField,usernameField,emailField,passwordField;
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
      
        
    }

    public void insert() throws SQLException{
        String firstName = firstnameField.getText();
        String middleName = middlenameField.getText();
        String lastName = lastnameField.getText();
        String userName = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        
       user_model = new User(firstName,middleName,lastName,userName,email,password);
       boolean inserted = service.insert(user_model);
       
       if(inserted){
           alert_util.success("User registered successfully");
           clear();
           
       }    
    }
    
    
    public void transfer(ActionEvent event) throws IOException{
        window_util.transfer("LoginView.fxml", event);
       
    }

   public void clear(){
       firstnameField.setText("");
       middlenameField.setText("");
       lastnameField.setText("");
       usernameField.setText("");
       emailField.setText("");
       passwordField.setText("");
    
   }    
    
}
