/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import util.WindowUtil;
/**
 * FXML Controller class
 *
 * @author Windyl
 */
public class DashboardLayoutController implements Initializable {
    WindowUtil util = new WindowUtil();
    @FXML
    AnchorPane contentpane;
    @FXML
    Button homebtn,booksbtn,studentsBtn,borrowBookBtn,attendanceBtn,returnBtn,logoutBtn,reportBtn;
    private List<HBox> navButtons;
    @FXML private HBox homebtnHBox;
    @FXML private HBox booksbtnHBox;
    @FXML private HBox studentsBtnHBox;
    @FXML private HBox updateStudentBtnHBox;
    @FXML private HBox updateBookBtnHBox;
    @FXML private HBox borrowBookBtnHBox;
    @FXML private HBox returnBtnHBox;
    @FXML private HBox attendanceBtnHBox;

    @Override
    public void initialize(URL url, ResourceBundle rb)  {
    navButtons = Arrays.asList(homebtnHBox, booksbtnHBox, studentsBtnHBox, updateStudentBtnHBox,
                                   updateBookBtnHBox, borrowBookBtnHBox, returnBtnHBox, attendanceBtnHBox);

        // Add click listeners for each Button
        homebtn.setOnAction(e -> setActiveButton(homebtnHBox));
        booksbtn.setOnAction(e -> setActiveButton(booksbtnHBox));
        studentsBtn.setOnAction(e -> setActiveButton(studentsBtnHBox));
       
        borrowBookBtn.setOnAction(e -> setActiveButton(borrowBookBtnHBox));
        returnBtn.setOnAction(e -> setActiveButton(returnBtnHBox));
        attendanceBtn.setOnAction(e -> setActiveButton(attendanceBtnHBox));
       try{
          //Loads the default home page
          util.setContentArea(contentpane); 
          util.goTo("BooksView");
         
          homebtn.setOnAction(event -> {
            try{
                 util.goTo("StatisticsView");
                 
             }catch(IOException e){
                 e.printStackTrace();   
            }
          }); 
          
          booksbtn.setOnAction(event ->{
          try{
             util.goTo("BooksView") ;
             
             }catch(IOException e){
                 e.printStackTrace();   
             }
          });
          
          studentsBtn.setOnAction(event ->{
          try{
             util.goTo("StudentsView") ;
             
             }catch(IOException e){
                 e.printStackTrace();   
             }
          });
        
          
          returnBtn.setOnAction(event ->{
          try{
             util.goTo("ReturnBookView") ;
             
             }catch(IOException e){
                 e.printStackTrace();   
             }
          });
          
       }catch(IOException e){
           e.printStackTrace();
       }
       
       borrowBookBtn.setOnAction(event ->{
          try{
             util.goTo("BorrowBookView");
             
             }catch(IOException e){
                 e.printStackTrace();   
             }
          });
       
       attendanceBtn.setOnAction(event ->{
          try{
             util.goTo("TrackStudentView") ;
             
             }catch(IOException e){
                 e.printStackTrace();   
             }
          });
        logoutBtn.setOnAction(event ->{
          try{
             util.transfer("LoginView.fxml",event);
             
             }catch(IOException e){
                 e.printStackTrace();   
             }
          });
        reportBtn.setOnAction(event ->{
          try{
             util.goTo("GenerateReportView");
             
             }catch(IOException e){
                 e.printStackTrace();   
             }
          });
       
          
      
          
      
    } 
    
    private void setActiveButton(HBox activeHBox) {
      // Remove 'active' from all
        navButtons.forEach(h -> h.getStyleClass().remove("active"));

        // Add 'active' to the clicked HBox
        if (!activeHBox.getStyleClass().contains("active")) {
            activeHBox.getStyleClass().add("active");
        }
    }
    
    
    
   
    
       
}
