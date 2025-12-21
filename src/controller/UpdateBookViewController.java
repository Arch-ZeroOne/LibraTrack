/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.control.TextField;
import model.Book;
import service.BookService;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import model.Category;
import service.GenreService;
import util.AlertUtil;
import util.DateUtil;

/**
 * FXML Controller class
 *
 * @author Windyl
 */
public class UpdateBookViewController implements Initializable {

    @FXML
    TextField barcodeField,titleField,authorField,publisherField,isbnField,copiesField;
    @FXML
    ComboBox<String> isAvailableComboBox;
    @FXML
    DatePicker publicationDatePicker;
    @FXML
    ListView genreListView ;
    BookService book_service = new BookService();
    DateUtil date_util = new DateUtil();
    AlertUtil alert_util = new AlertUtil();
    GenreService genre_service = new GenreService();
    ObservableList<Category> genreList = FXCollections.observableArrayList();
     public ObservableList<Category> genreList(){
          return genreList;
     }
   
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb)  {
        barcodeField.requestFocus();
        genreListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        genreListView.setItems(genreList);
        isAvailableComboBox.setValue("Available");
        //Initializes list of genre
        try{
          genreList.setAll(genre_service.list());
        }catch(SQLException error){
            error.printStackTrace();
        }       
       
        
        isAvailableComboBox.getItems().setAll("Available","Unavailable");
        
      barcodeField.setOnKeyPressed(event -> {
          if(event.getCode() == KeyCode.ENTER){
            try{      
              String value = barcodeField.getText();
              Book book = book_service.search(value);
              if(book != null){
                  autoFill(book);
                  return;
              }
              
              alert_util.error("No Book Data Avaible");
              clearForm();
              
            }catch(SQLException error){
                  error.printStackTrace();
            }
              
              
          }
        });
    }


    
    public void autoFill(Book book){
       
//        titleField.setText(book.getTitle());
//        authorField.setText(book.getAuthor());
//        publisherField.setText(book.getPublisher());
//        isbnField.setText(book.getIsbn());
//        copiesField.setText(String.valueOf(book.getCopies()));
//       publicationDatePicker.setValue(LocalDate.now());
        
    }

    public void handleDelete() throws SQLException{
        if(book_service.remove("Unavailable")){
            alert_util.success("Book Deleted");
            return;
        }
         alert_util.error("Book Not Deleted");
        
        
        
        
        
        
    }
    
    public void handleUpdate(){
        String title = titleField.getText();
        String author = authorField.getText();
        String publisher = publisherField.getText();
        String publicationDate  = String.valueOf(publicationDatePicker.getValue());
        ObservableList<Category> selectedGenre = genreListView.getSelectionModel().getSelectedItems();
        String isbn = isbnField.getText();
        int copies  = Integer.parseInt(copiesField.getText());
        String isAvailable = isAvailableComboBox.getValue();
        
        
        
//        Book book = new Book(title,author,publisher,publicationDate,isbn,copies,isAvailable);
//        
//       try{
//           boolean updated  = book_service.update(book,selectedGenre);
//           if(updated){
//               alert_util.success("Book Updated Successfully");
//               clearForm();
//           }
//       }catch(SQLException error){
//           error.printStackTrace();
//       }
        
    }
    
    
     public void clearForm(){
       titleField.setText("");
       authorField.setText("");
       publisherField.setText("");
       isbnField.setText("");
       copiesField.setText("");
       barcodeField.setText("");
       
   }

    
    
}
