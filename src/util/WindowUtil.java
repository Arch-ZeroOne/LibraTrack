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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Windyl
 */
public class WindowUtil {
    private  static AnchorPane contentArea;
    
    //For switching stages
    public void transfer(String location , ActionEvent event) throws IOException{
        //Gets the current stage
        Stage current = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("../view/"+location));
        Scene scene = new Scene(root);
        
   
        current.setScene(scene);
        current.show();
        
       

    }
    
    
    public void setContentArea(AnchorPane pane){
        contentArea = pane;
        
    }
    
    //Routing in page withot switching scenes
    public void goTo(String viewname) throws IOException{
       
        FXMLLoader loader  = new FXMLLoader(this.getClass().getResource("../view/"+viewname+".fxml"));
        Node node = loader.load();
        
        contentArea.getChildren().setAll(node);
        
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        
        
    
}
    
}
