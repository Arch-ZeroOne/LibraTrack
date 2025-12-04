/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Date;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import model.Student;
import service.StudentService;


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
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        studentTable.setItems(data);
        courseComboBox.getItems().setAll("ALL","BSIT","BSBA","BSA","BTLED");
        idCol.setCellValueFactory(new PropertyValueFactory<>("school_id"));
        firstnameCol.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        middlenameCol.setCellValueFactory(new PropertyValueFactory<>("middlename"));
        lastnameCol.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        courseCol.setCellValueFactory(new PropertyValueFactory<>("course"));
        try{ loadTable(); } catch(SQLException error) { error.printStackTrace(); };
        
    }

     public void showAddModal(ActionEvent event) throws IOException,SQLException{
        util.openModal("AddStudentModal", "Add Student");
        loadTable();
        
       
    }
    
     
    public void handleCourseChange(ActionEvent event){
        String selectedCourse = courseComboBox.getValue();
        try{ handleCourseFilter(selectedCourse); }catch(SQLException error){ error.printStackTrace();};
        
    }
    
    public void handleDateChange(ActionEvent event){
        LocalDate date = datePicker.getValue();
        try{ handleDateFilter(date); }catch(SQLException error){ error.printStackTrace();};
        
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
        
        for(Student student : filtered){
            System.out.println("Retrieved data:");
            System.out.println(student.getCourse());
        }
        data.setAll(filtered);
        
        
    }
      public void handleDateFilter(LocalDate localDate) throws SQLException{
        
       if(localDate.equals(LocalDate.now())){
            loadTable();
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
        
        for(Student student : filtered){
            System.out.println(student.getCreatedAt());
        }
        data.setAll(filtered);
        
        
    }
      
      
     
    public void handleSearch(KeyEvent event) throws SQLException{
        String symbol = searchField.getText();
        System.out.println(symbol);
        try{ handleSearchFilter(symbol); }catch(SQLException error){ error.printStackTrace();};
        
       
        
    }
    
    
}
