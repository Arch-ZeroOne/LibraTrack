/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import dao.UserDao;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import model.User;
import java.sql.SQLException;
/**
 * FXML Controller class
 *
 * @author Windyl
 */
public class LoginViewController implements Initializable {
    private UserDao user_dao = new UserDao();
    
    @FXML
    TextField username;
    @FXML
    TextField password;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void validate() throws SQLException {
        String user_name = username.getText();
        String pass_word = password.getText();
        
        //Creates the user object
        User user = new User(user_name ,pass_word);
        
        //Validates the user
       if(user_dao.validate(user)){
           
       }
        
        
        
        
        
        
        
    }    
    
}
