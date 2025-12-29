/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import managers.BookManager;
import model.Book;
import model.Category;
import service.BookService;
import service.GenreService;
import util.AlertUtil;
import util.ModalUtil;
import dao.AuthorDao;

/**
 * FXML Controller class
 *
 * @author Windyl
 */
public class BorrowBookController implements Initializable {

     
    // Barcode Scanner Section
    @FXML
    private TextField barcodeField;
    
    @FXML
    private Button scanBtn;
    
    // Book Details Section
    @FXML
    private TextField titleField;
    
    @FXML
    private TextField authorField;
    
    private TextField genreField;
    
    @FXML
    private TextField publisherField;
    
    @FXML
    private TextField isbnField;
    
    @FXML
    private TextField availableField;
    
    // Action Buttons
    @FXML
    private Button cancelBtn;
    
    @FXML
    private Button confirmBtn;
    @FXML
    ListView<Category> genreListView;
    BookService book_service = new BookService();
    GenreService genre_service = new GenreService();
    AlertUtil alert_util = new AlertUtil();
    ModalUtil modal = new ModalUtil();
    AuthorDao author_dao = new AuthorDao();

    BookManager manager = BookManager.getInstance();
    ObservableList<Category> genres = FXCollections.observableArrayList();
    // Initialize method
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        barcodeField.requestFocus();
       
       
        genreListView.setItems(genres);
        
           
        barcodeField.setOnKeyPressed(event -> {
          if(event.getCode() == KeyCode.ENTER){
            try{
              String value = barcodeField.getText();
              Book book = book_service.search(value);
              if(book != null){
                  // Populate BookManager with book data
                  manager.setId(book.getBook_id());
                  manager.setTitle(book.getTitle());
                  manager.setPublisher(book.getPublisher());
                  manager.setIsbn(book.getIsbn());
                  // Get author name
                  String authorName = author_dao.getById(book.getAuthor_id());
                  manager.setAuthor(authorName != null ? authorName : "Unknown");

                  // Check availability - for now assume available if status_id != 3 (removed)
                  String availability = (book.getStatus_id() == 3) ? "Unavailable" : "Available";
                  manager.setIsAvailable(availability);

                  autoFill(book, authorName);
                  return;
              }

              alert_util.error("No Book Data Available");
              clearForm();

            }catch(SQLException error){
                  error.printStackTrace();
            }


          }
        });
    }


    public void autoFill(Book book, String authorName){
        titleField.setText(book.getTitle());
        authorField.setText(authorName != null ? authorName : "Unknown");
        publisherField.setText(book.getPublisher());
        isbnField.setText(book.getIsbn());
        // For availability, we need to check if the book has available copies
        // For now, show status based on status_id
        String availability = (book.getStatus_id() == 3) ? "Unavailable" : "Available";
        availableField.setText(availability);
    }

    public void clearForm(){
        titleField.setText("");
        authorField.setText("");
        genreField.setText("");
        publisherField.setText("");
        isbnField.setText("");
        availableField.setText("");
    }

    @FXML
    private void handleSave(ActionEvent event) throws IOException {
        if(availableField.getText().equals("Unavailable") || availableField.getText().equals("Borrowed")){
            alert_util.error("Book Unavailable or Borrowed");
            return;
        }

        modal.openModal("BorrowViewModal", "Confirm Student",null);
        clearForm();
    }
    
    
    
    
}
