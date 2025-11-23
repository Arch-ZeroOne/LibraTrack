/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import util.WindowUtil;
/**
 * FXML Controller class
 *
 * @author Windyl
 */
public class DashboardViewController implements Initializable {
    WindowUtil util = new WindowUtil();
    @FXML
    AnchorPane contentpane;
    @FXML
    Button homebtn,booksbtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)  {
    util.setContentArea(contentpane);
       try{
           util.goTo("StatisticsView");
           
           
           
            //Loads the default home page
           homebtn.setOnAction(event -> {
             try{
                 util.goTo("StatisticsView");
                 
             }catch(IOException e){
                 e.printStackTrace();   
             }
           } 
           );         
           booksbtn.setOnAction(event ->{
           try{
                util.goTo("BooksView") ;
              }catch(IOException e){
                 e.printStackTrace();   
             }
           } );
       }catch(IOException e){
           e.printStackTrace();
       }
      
       
       
       
    }  
    
    
    
}
