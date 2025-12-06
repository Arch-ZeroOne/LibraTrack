/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.Log;
import service.LogService;
import util.AlertUtil;
import java.sql.Date;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import javafx.util.StringConverter;
import util.DateUtil;


/**
 * FXML Controller class
 *
 * @author Windyl
 */
public class TrackStudentController implements Initializable {
     
    @FXML
    TextField qrCodeField,searchField;
    @FXML
    DatePicker datePicker;
    @FXML
    TableView logTable;
    @FXML
    TableColumn<Log,String> idCol,firstnameCol,middlenameCol,lastnameCol,loggedCol;
    ObservableList<Log> data  = FXCollections.observableArrayList();
    LogService service = new LogService();
    AlertUtil util = new AlertUtil();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM/d/yyyy");
    

 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("school_id"));
        firstnameCol.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        middlenameCol.setCellValueFactory(new PropertyValueFactory<>("middlename"));
        lastnameCol.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        loggedCol.setCellValueFactory(new PropertyValueFactory<>("log_date"));
        logTable.setItems(data); 
        
        datePicker.setConverter(new StringConverter<LocalDate>(){
          @Override
          public String toString(LocalDate date){
              if(date != null){
                  return formatter.format(date);
                  
              }
              
              return null;
          }
          
          @Override
          public LocalDate fromString(String dateString){
              if(!dateString.isEmpty()){
                  return LocalDate.parse(dateString,formatter);
                  
              }
              
              return null;
          }
        });
        datePicker.setValue(LocalDate.now());
        try{handleDateFilter(LocalDate.now());}catch(SQLException error){error.printStackTrace();};
        try{loadTable();}catch(SQLException error){error.printStackTrace();};
    }
 
    public void handleScan(KeyEvent event){
        
      if(event.getCode() == KeyCode.ENTER){
         try{      
             String value = qrCodeField.getText();
             String school_id = service.search(value);
             
             if(service.isDuplicate(Date.valueOf(LocalDate.now()),value)){
                 util.error("Student Log Already Recorded");
                 qrCodeField.setText("");
                 return;
             }    

             if(school_id != null ){
                 service.insert(school_id);
                 qrCodeField.setText("");
                 loadTable();
                 return;
              }
              util.error("Student Not Found");
              
            }catch(SQLException error){
                error.printStackTrace();
            }    
         }
    }

   public void loadTable() throws SQLException{
       ArrayList<Log> log_list = service.list();
       data.setAll(log_list);
       
   }
   
   public void handleSearch(){
       String symbol = searchField.getText();
       try{ handleSearchFilter(symbol); }catch(SQLException error){error.printStackTrace();};
   }
   
   public void handleDateChange(){
        LocalDate date = datePicker.getValue();
        try{ handleDateFilter(date); }catch(SQLException error){ error.printStackTrace();};
       
   }
   
   public void handleSearchFilter(String symbol)throws SQLException{
          if(symbol.isEmpty()){
             loadTable();
             return;
             
         }
       
        
        ArrayList<Log> log_list = service.list();
        List<Log> filtered = log_list.stream().
                                     filter(log -> log.getFirstname().contains(symbol)
                                         || log.getMiddlename().contains(symbol)
                                         || log.getLastname().contains(symbol) 
                                         || log.getSchool_id().contains(symbol)
                                                 )
                                    .collect(Collectors.toList());
        
        data.setAll(filtered);
       
   }
   
   public void handleDateFilter(LocalDate localDate) throws SQLException{
       if(localDate.equals(LocalDate.now())){
            loadTable();
             return;
        }
       
        
        ArrayList<Log> student_list = service.list();
        List<Log> filtered = student_list.stream().
                                     filter(log -> log.getLog_date().toLocalDate()
                                     .equals(localDate)).collect(Collectors.toList());
        data.setAll(filtered);
       
   }
   
    
}
