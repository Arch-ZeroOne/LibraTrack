/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import model.Borrow;
import model.Student;
import model.Category;
import managers.BookManager;
import service.BorrowService;
import service.StudentService;
import util.AlertUtil;
import util.ModalUtil;


/**
 * FXML Controller class
 *
 * @author Windyl
 */
public class BorrowViewModalController implements Initializable {
 // Borrow Information Section
    @FXML
    private TextField bookIdField;
    
    @FXML
    private DatePicker borrowDatePicker;
    
    @FXML
    private DatePicker dueDatePicker;
    
    // Student Barcode Scanner Section
    @FXML
    private TextField studentBarcodeField;
    
    @FXML
    private Button scanStudentBtn;
    
    // Student Details Section
    @FXML
    private TextField studentIdField;
    
    @FXML
    private TextField studentNameField;
    
    @FXML
    private TextField courseField;
    
    @FXML
    private TextField librarianIdField;
    
    // Action Buttons
    @FXML
    private Button cancelBtn;
    
    @FXML
    private Button confirmBtn;
    BookManager manager = BookManager.getInstance();
    
    // Private variables to store data
    private String bookAccessionNumber;
    private int currentLibrarianId;
    StudentService service = new StudentService();
    AlertUtil alert_util = new AlertUtil();
    Borrow borrow = new Borrow();
    ModalUtil modal_util = new ModalUtil();
    BorrowService borrow_service = new BorrowService();
    // Initialize method
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        studentBarcodeField.requestFocus();
        studentBarcodeField.setOnKeyPressed(event -> {
          if(event.getCode() == KeyCode.ENTER){
            try{      
              String value = studentBarcodeField.getText();
              Student student = service.search(value);
              if(student != null){
                  autoFill(student);

                  return;
              }
              
              alert_util.error("No Student Data Avaible");
             
              
            }catch(SQLException error){
                  error.printStackTrace();
            }
              
              
          }
        });
    }
 public void autoFill(Student student){
        studentNameField.setText(student.getFirstname()+", "+student.getMiddlename()+", "+student.getLastname());
        studentIdField.setText(String.valueOf(student.getStudent_id()));
        courseField.setText(student.getCourse());
        librarianIdField.setText("1");
       
       
        
    }
 
 public void handleConfirm(ActionEvent event) throws SQLException{
     Borrow borrow = new Borrow();
     // Create a new Borrow object


try {
    

    // Get studentId from TextField
    int studentId = Integer.parseInt(studentIdField.getText().trim());

    // Get librarianId from TextField
    int librarianId = Integer.parseInt(librarianIdField.getText().trim());

    // Get borrow date from DatePicker
    String borrowDate = borrowDatePicker.getValue().toString(); // YYYY-MM-DD

    // Get due date from DatePicker
    String dueDate = dueDatePicker.getValue().toString(); // YYYY-MM-DD

    // Set values in Borrow object
    System.out.println(manager.getAccessionNumber());
    
   
    boolean borrowed = borrow_service.borrowBook(manager.getAccessionNumber(), studentId, librarianId, borrowDate, dueDate);

    if(borrowed){
        alert_util.success("Book Borrowed");
        handleCancel(event);
        
    }
} catch (NumberFormatException e) {
    e.printStackTrace();
    System.out.println("Error: Invalid number input.");
} catch (NullPointerException e) {
    System.out.println("Error: Date not selected.");
}

     
 }
  //Handles the modal closing
   public void handleCancel(ActionEvent event){
       modal_util.closeModal(event);
           
   }
   public void handleSave(){
       
   }
   
}
