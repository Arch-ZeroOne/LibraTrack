package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import com.gluonhq.charm.glisten.control.TextField;
import java.io.IOException;
import service.UserService;
import model.User;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import util.AlertUtil;
import util.WindowUtil;


public class RegisterViewController implements Initializable {
     public final UserService service = new UserService();
     public User user_model;
     public final AlertUtil alert_util = new AlertUtil();
     public final WindowUtil window_util = new WindowUtil();
     
    @FXML
    TextField username;
    @FXML
    TextField email;
    @FXML
    TextField password;
    @FXML
    ChoiceBox<String> role;
  
    
            
            

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> options = FXCollections.observableArrayList("Select A Role......","Librarian","Admin");
        role.setItems(options);
        role.getSelectionModel().selectFirst();
      
        
    }

    public void insert() throws SQLException{
        String user_name = username.getText();
        String user_email = email.getText();
        String user_pass = password.getText();
        String user_role = role.getValue();
        
        
       if(user_role.equals("Select A Role......")){
           alert_util.error("Please select a valid value for role");
           return;
       }
       
        
       user_model = new User(user_name,user_pass,user_email,user_role);
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
        username.setText("");
        email.setText("");
        password.setText("");
        role.getSelectionModel().selectFirst();
        
    
   }    
    
}
