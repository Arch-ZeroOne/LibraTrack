/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import util.ModalUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;
import model.Student;
import service.StudentService;
import java.sql.Statement;


/**
 * FXML Controller class
 *
 * @author Windyl
 */
public class StudentsViewController implements Initializable {
    @FXML
    TextField searchField;
    @FXML
    TableView studentTable;
    @FXML
    TableColumn<Student,String> idCol,firstnameCol,middlenameCol,lastnameCol,courseCol;
    @FXML 
    ComboBox<String> courseComboBox;
    @FXML
    DatePicker datePicker;
    ObservableList<Student> data = FXCollections.observableArrayList();
    ModalUtil util = new ModalUtil();
    StudentService service = new StudentService();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM/d/yyyy");
    @FXML
    private Label totalStudentsLabel;
    @FXML
    private Button createBtn;
    @FXML
    private Label statTotalStudentsLabel;
    @FXML
    private Label statActiveLabel;
    @FXML
    private Label statInactiveLabel;
    @FXML
    private Label statProgramsLabel;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        studentTable.setItems(data);
        loadStatistics();
        courseComboBox.getItems().setAll("ALL","BSIT","BSBA","BSA","BTLED");
        idCol.setCellValueFactory(new PropertyValueFactory<>("school_id"));
        firstnameCol.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        middlenameCol.setCellValueFactory(new PropertyValueFactory<>("middlename"));
        lastnameCol.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        courseCol.setCellValueFactory(new PropertyValueFactory<>("course"));
        
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
        try{ loadTable(); } catch(SQLException error) { error.printStackTrace(); };
        
    }

    @FXML
     public void showAddModal(ActionEvent event) throws IOException,SQLException{
        util.openModal("AddStudentModal", "Add Student");
        loadTable();
          
    }
    
     
    @FXML
    public void handleCourseChange(ActionEvent event){
        String selectedCourse = courseComboBox.getValue();
        try{ handleCourseFilter(selectedCourse); }catch(SQLException error){ error.printStackTrace();};
        
    }
    
    @FXML
    public void handleDateChange(ActionEvent event){
        LocalDate date = datePicker.getValue();
        try{ handleDateFilter(date); }catch(SQLException error){ error.printStackTrace();};
        
    }
    @FXML
    public void handleSearch(KeyEvent event) throws SQLException{
        String symbol = searchField.getText();
        try{ handleSearchFilter(symbol); }catch(SQLException error){ error.printStackTrace();};
        
       
        
    }
    
    public void loadTable() throws SQLException{
       
        ArrayList<Student> book_list = service.list();
        //Automatically clears old data and loads to the table
        data.setAll(book_list);
        
        
    }
    
     public void handleCourseFilter(String course) throws SQLException{
        
       if(course.equals("ALL")){
            loadTable();
             return;
        }
       
        ArrayList<Student> student_list= service.list();
        List<Student> filtered = student_list.stream().
                                     filter(student -> student.getCourse()
                                     .equals(course)).collect(Collectors.toList());
        data.setAll(filtered);
        
        
    }
     
    public void handleDateFilter(LocalDate localDate) throws SQLException{
        
       if(localDate.equals(LocalDate.now())){
            loadTable();
            loadStatistics();
             return;
        }
       
        
        ArrayList<Student> student_list = service.list();
        List<Student> filtered = student_list.stream().
                                     filter(student -> student.getCreatedAt().toLocalDate()
                                     .equals(localDate)).collect(Collectors.toList());
        data.setAll(filtered);
        
        
    }
       public void handleSearchFilter(String symbol) throws SQLException{
        
         if(symbol.isEmpty()){
             loadTable();
             return;
             
         }
       
        
        ArrayList<Student> student_list = service.list();
        List<Student> filtered = student_list.stream().
                                     filter(student -> student.getFirstname().contains(symbol)
                                         || student.getMiddlename().contains(symbol)
                                         || student.getLastname().contains(symbol) 
                                         || student.getCourse().contains(symbol)
                                         || student.getSchoolId().contains(symbol)
                                                 )
                                    .collect(Collectors.toList());
        
        data.setAll(filtered);
        
        
    }
    private void loadStatistics() {
    String DB_URL = "jdbc:mysql://localhost:3306/libratrack_qr_barcode";
    String DB_USER = "root";
    String DB_PASS = "";

    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
         Statement stmt = conn.createStatement()) {

        // Total students
        ResultSet rsTotal = stmt.executeQuery("SELECT COUNT(*) AS total FROM student");
        if (rsTotal.next()) statTotalStudentsLabel.setText(rsTotal.getString("total"));

        // Active students
        ResultSet rsActive = stmt.executeQuery("SELECT COUNT(*) AS total FROM student WHERE isActive = 'active'");
        if (rsActive.next()) statActiveLabel.setText(rsActive.getString("total"));

        // Inactive students
        ResultSet rsInactive = stmt.executeQuery("SELECT COUNT(*) AS total FROM student WHERE isActive = 'inactive'");
        if (rsInactive.next()) statInactiveLabel.setText(rsInactive.getString("total"));

        // Total programs / distinct courses
        ResultSet rsPrograms = stmt.executeQuery("SELECT COUNT(DISTINCT course) AS total FROM student");
        if (rsPrograms.next()) statProgramsLabel.setText(rsPrograms.getString("total"));

    } catch (SQLException e) {
        e.printStackTrace();
        statTotalStudentsLabel.setText("0");
        statActiveLabel.setText("0");
        statInactiveLabel.setText("0");
        statProgramsLabel.setText("0");
    }
}

       
}
