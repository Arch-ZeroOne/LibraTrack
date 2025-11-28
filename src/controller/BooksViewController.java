/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.Modality;
import util.ModalUtil;
/**
 * FXML Controller class
 *
 * @author Windyl
 */
public class BooksViewController implements Initializable {
     ModalUtil modal_util = new ModalUtil();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
        
    } 
    
    
    public void showAddModal(ActionEvent event) throws IOException{
        System.out.println("Event Fired");
        modal_util.openModal("AddBookModal", "Add Book");
        
       
    }
    
}
