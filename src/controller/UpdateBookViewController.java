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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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
    ComboBox<String> genreComboBox , isAvailableComboBox;
    @FXML
    DatePicker publicationDatePicker;
    BookService book_service = new BookService();
    DateUtil date_util = new DateUtil();
    AlertUtil alert_util = new AlertUtil();
   
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb)  {
        
        isAvailableComboBox.getItems().setAll("true","false");
        
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
        String[] getSeparatedDate = book.getPublicationDate().split("-");
        int year = Integer.parseInt(getSeparatedDate[2]);
        int day = Integer.parseInt(getSeparatedDate[1]);
        int month = Integer.parseInt(getSeparatedDate[0]);
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        genreComboBox.setValue(book.getGenre());
        publisherField.setText(book.getPublisher());
        publicationDatePicker.setValue(LocalDate.of(year,month,day));
        isbnField.setText(book.getIsbn());
        copiesField.setText(String.valueOf(book.getCopies()));
        
    }

    public void handleDelete(){
        
    }
    
    public void handleUpdate(){
        String barcode = barcodeField.getText();
        String title = titleField.getText();
        String author = authorField.getText();
        String genre = genreComboBox.getValue();
        String publisher = publisherField.getText();
        String publicationDate  = date_util.getFormattedDate(publicationDatePicker.getValue());
        String isbn = isbnField.getText();
        int copies = Integer.parseInt(copiesField.getText());
        boolean isAvailable = Boolean.parseBoolean(isAvailableComboBox.getValue());
        
        Book book = new Book(title,author,genre,publisher,publicationDate,isbn,copies,isAvailable,barcode);
        
       try{
           boolean updated  = book_service.update(book);
           if(updated){
               alert_util.success("Book Updated Successfully");
               clearForm();
           }
       }catch(SQLException error){
           error.printStackTrace();
       }
        
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
