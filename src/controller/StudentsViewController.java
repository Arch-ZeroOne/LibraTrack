/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import util.ModalUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Student;
import service.StudentService;

/**
 * FXML Controller class
 *
 * @author Windyl
 */
public class StudentsViewController implements Initializable {
    @FXML
    TableView studentTable;
    @FXML
    TableColumn<Student,String> idCol,firstnameCol,middlenameCol,lastnameCol;
    ObservableList<Student> data = FXCollections.observableArrayList();
    ModalUtil util = new ModalUtil();
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        studentTable.setItems(data);
        idCol.setCellValueFactory(new PropertyValueFactory<>("student_id"));
        firstnameCol.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        middlenameCol.setCellValueFactory(new PropertyValueFactory<>("middlename"));
        lastnameCol.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        try{ loadTable(); } catch(SQLException error) { error.printStackTrace(); };
        
    }

     public void showAddModal(ActionEvent event) throws IOException,SQLException{
        util.openModal("AddStudentModal", "Add Student");
        loadTable();
        
       
    }
    
    public void loadTable() throws SQLException{
        StudentService service = new StudentService();
        ArrayList<Student> book_list = service.list();
        //Automatically clears old data and loads to the table
        data.setAll(book_list);
        
        
    }    
    
}
