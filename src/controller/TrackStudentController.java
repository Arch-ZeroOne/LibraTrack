/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.lang.classfile.Label;
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

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import javafx.util.StringConverter;
import util.DateUtil;
import java.sql.Statement;


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
    @FXML
    private javafx.scene.control.Label todayAttendanceLabel;
    @FXML
    private javafx.scene.control.Label totalStudentsLabel;
    @FXML
    private javafx.scene.control.Label attendanceRateLabel;
    

 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadStatistics();
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
        //Handles the on start of date to filter the log for today
        handleDateChange();
        
    }
 
    @FXML
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
                 handleDateChange();
                 
                 return;
              }
              util.error("Student Not Found");
              
            }catch(SQLException error){
                error.printStackTrace();
            }    
         }
    }

 
   
    @FXML
   public void handleSearch(){
       String symbol = searchField.getText();
       try{ handleSearchFilter(symbol); }catch(SQLException error){error.printStackTrace();};
   }
   
    @FXML
   public void handleDateChange(){
        LocalDate date = datePicker.getValue();
        try{ handleDateFilter(date); }catch(SQLException error){ error.printStackTrace();};
       
   }
   
   public void handleSearchFilter(String symbol)throws SQLException{
        ArrayList<Log> log_list = service.list();
          if(symbol.isEmpty()){
               List<Log> filtered = log_list.stream().
                                     filter(log -> log.getLog_date().toLocalDate()
                                     .equals(LocalDate.now())).collect(Collectors.toList());
                         data.setAll(filtered);
            
         }else{
            List<Log> filtered = log_list.stream().
                                     filter(log -> log.getFirstname().contains(symbol)
                                         || log.getMiddlename().contains(symbol)
                                         || log.getLastname().contains(symbol) 
                                         || log.getSchool_id().contains(symbol)
                                                 )
                                    .collect(Collectors.toList());
                 data.setAll(filtered);
          }
   }
   
   public void handleDateFilter(LocalDate localDate) throws SQLException{
       ArrayList<Log> student_list = service.list();
       
       //If the date is current load the logs for todays date
       if(localDate.equals(LocalDate.now())){
            List<Log> filtered = student_list.stream().
                                     filter(log -> log.getLog_date().toLocalDate()
                                     .equals(LocalDate.now())).collect(Collectors.toList());
            data.setAll(filtered);
            
        }else{
           //If not loads the date based on the value of date picker
            List<Log> filtered = student_list.stream().
                                     filter(log -> log.getLog_date().toLocalDate()
                                     .equals(localDate)).collect(Collectors.toList());
            data.setAll(filtered);
           
       }
       
   }


private void loadStatistics() {
    String DB_URL = "jdbc:mysql://localhost:3306/libratrack_qr_barcode";
    String DB_USER = "root";
    String DB_PASS = "";

    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
         Statement stmt = conn.createStatement()) {

        // Total logs
        ResultSet rsTotal = stmt.executeQuery("SELECT COUNT(*) AS total FROM log");
        if (rsTotal.next()) totalStudentsLabel.setText(rsTotal.getString("total"));

        // Today's logs
        ResultSet rsToday = stmt.executeQuery("SELECT COUNT(*) AS total FROM log WHERE log_date = CURDATE()");
        if (rsToday.next()) todayAttendanceLabel.setText(rsToday.getString("total"));

        // This month's logs
        ResultSet rsMonth = stmt.executeQuery(
                "SELECT COUNT(*) AS total FROM log WHERE MONTH(log_date) = MONTH(CURDATE()) AND YEAR(log_date) = YEAR(CURDATE())"
        );
        if (rsMonth.next()) attendanceRateLabel.setText(rsMonth.getString("total")+"%");

    } catch (SQLException e) {
        e.printStackTrace();
       
    }
}

    
}
