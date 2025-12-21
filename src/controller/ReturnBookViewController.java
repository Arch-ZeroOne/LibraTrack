/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import model.Book;
import service.StudentService;
import service.BookService;
import java.sql.*;



/**
 * FXML Controller class
 *
 * @author Windyl
 */
import model.BorrowedBook;
import model.Category;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import model.Student;
import service.BorrowService;
import util.AlertUtil;
import util.DatabaseUtil;
public class ReturnBookViewController implements Initializable {

     @FXML 
     TextField studentQrField;
        @FXML
      private TableView<BorrowedBook> borrowedBooksTable;

      @FXML
      private TableColumn<BorrowedBook, String> titleColumn;
      @FXML
      private TableColumn<BorrowedBook, String> authorColumn;
      @FXML
      private TableColumn<BorrowedBook, LocalDate> borrowDateColumn;
      @FXML
      private TableColumn<BorrowedBook, LocalDate> dueDateColumn;
      @FXML
      private TableColumn<BorrowedBook, Integer> penaltyColumn;
      StudentService service = new StudentService();
      AlertUtil util = new AlertUtil();
      DatabaseUtil db_util = new DatabaseUtil();
      BorrowService borrow_service = new BorrowService();
      int id = 0;
      
     ObservableList<BorrowedBook> data = FXCollections.observableArrayList();
    @FXML
    private Button scanStudentBtn;
    @FXML
    private Button returnBookBtn;
    @FXML
    private TableColumn<?, ?> penaltyAmountColumn;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        borrowedBooksTable.setItems(data); 
        studentQrField.requestFocus();
    
    titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
    authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
    borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
    dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
    penaltyColumn.setCellValueFactory(new PropertyValueFactory<>("penaltyAmount"));
    
     studentQrField.setOnKeyPressed(event -> {
          if(event.getCode() == KeyCode.ENTER){
            try{      
              String value = studentQrField.getText();
              Student student = service.search(value);
              
              if(student != null){
                 
                 loadBorrowedBooks(student.getStudent_id());
                  id = student.getStudent_id();
                  return;
              }
              
              util.error("Student Data Not Found");
              
              
            }catch(SQLException error){
                  error.printStackTrace();
            }
              
              
          }
        });
        
    }
    


private void loadBorrowedBooks(int studentId) {
    ObservableList<BorrowedBook> list = FXCollections.observableArrayList();

    String sql = "{ CALL get_unavailable_borrowed_books(?) }";

    try (Connection conn = db_util.connect();
         CallableStatement cs = conn.prepareCall(sql)) {

        cs.setInt(1, studentId);
        ResultSet rs = cs.executeQuery();

        while (rs.next()) {
            list.add(new BorrowedBook(
                    rs.getInt("accession_number"),
                    rs.getInt("student_id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getDate("borrow_date").toLocalDate(),
                    rs.getDate("due_date").toLocalDate(),
                    rs.getInt("penalty_amount")
                    
            ));
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    if(list.isEmpty()){
        
        util.error("No Borrow Data Found");
    }

    borrowedBooksTable.setItems(list);
}

    @FXML
    private void handleReturn(ActionEvent event) throws SQLException{
        BorrowedBook selected = borrowedBooksTable.getSelectionModel().getSelectedItem();
        boolean returned = borrow_service.returnBook(selected.getAccessionNumber());
        if(returned){
            util.success("Book Returned");
            loadBorrowedBooks(id);
            return;
        }
        util.error("No Borrow Records found");
    }
    public void handleSave(){
        
    }



      
    
}
