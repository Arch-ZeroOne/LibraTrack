/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import java.io.IOException;
import model.User;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import service.UserService;
import util.AlertUtil;
import util.WindowUtil;
/**
 * FXML Controller class
 *
 * @author Windyl
 */
public class LoginViewController implements Initializable {
    private UserService service = new UserService();
    private AlertUtil alert_util = new AlertUtil();
    public WindowUtil window_util = new WindowUtil();
    
    @FXML
    TextField username;
    @FXML
    PasswordField password;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void validate(ActionEvent event) throws SQLException, IOException {
        String user_name = username.getText();
        String pass_word = password.getText();
        
        //Creates the user object
        User user = new User(user_name ,pass_word);
       
         
       //Validates the user
       if(service.validate(user)){
          alert_util.success("User has been successfully logged in");
          window_util.transfer("DashboardLayout.fxml", event);
          return;
       }
       
       alert_util.error("User not found");
        
    }

   public void transfer(ActionEvent event) throws IOException{
       window_util.transfer("RegisterView.fxml", event);
       
   }    
    
}
