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

/**
 * FXML Controller class
 *
 * @author Windyl
 */
public class UpdateBookViewController implements Initializable {

    @FXML
    TextField barcodeField,titleField,authorField,publisherField,isbnField,copiesField;
    @FXML
    ComboBox genreComboBox;
    @FXML
    DatePicker publicationDatePicker;
    BookService service = new BookService();
   
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb)  {
        
        barcodeField.setOnKeyPressed(event -> {
          if(event.getCode() == KeyCode.ENTER){
            try{      
              String value = barcodeField.getText();
              Book book = service.search(value);
              if(book != null){
                  autoFill(book);
                  
              }
              
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

    public void handleCancel(){
        
    }   
    
    public void handleDelete(){
        
    }
    
    public void handleUpdate(){
        
    }

    
    
}
