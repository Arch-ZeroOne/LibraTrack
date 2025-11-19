/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Windyl
 */
public class WindowUtil {
    
    public void transfer(String location , ActionEvent event) throws IOException{
        //Gets the current stage
        Stage current = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("../view/"+location));
        
        Scene scene = new Scene(root);
        
        
        current.setScene(scene);
        
        current.show();
        
       

    }
    
}
