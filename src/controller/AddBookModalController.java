/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import com.google.zxing.WriterException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.Book;
import util.DateUtil;
import service.BookService;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import managers.AuthorManager;
import model.Author;
import model.Category;
import service.AuthorService;
import service.BarcodeService;
import util.ModalUtil;
import util.BarcodeConfig;
import service.GenreService;

import org.controlsfx.control.textfield.TextFields;

public class AddBookModalController implements Initializable {
     
     @FXML
     TextField titleField,authorField,publisherField,isbnField;
     @FXML
     ListView<Category> categoryListView;
     @FXML
     DatePicker publicationDatePicker;
     @FXML
     ImageView barcodeImageView;
     ModalUtil modal_util = new ModalUtil();
     BookService service = new BookService();
     BarcodeService barcode_service = new BarcodeService();
     BarcodeConfig barcode_config = new BarcodeConfig();
     GenreService genre_service = new GenreService();
     AuthorService author_service = new AuthorService();
     ObservableList<String> authorSuggestions = FXCollections.observableArrayList();
     ObservableList<Category> categoryList = FXCollections.observableArrayList();
     AuthorManager authorManager = AuthorManager.getInstance();
     
     public ObservableList<Category> genreList(){
          return categoryList;
     }
        
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Initializes list of genre
        categoryListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
         
       try{ 
        ObservableList<Author> authors =  author_service.list();
        
        for(Author author : authors){
            authorSuggestions.add(author.getAuthor_name());
        }
        
        
       }catch(SQLException error){
           error.printStackTrace();
       }
      
        try{
          
          categoryList.setAll(genre_service.list());
          categoryListView.setItems(categoryList);
          
         
        
        }catch(SQLException error){
            error.printStackTrace();
        }
        
         
        //Default value for publisher field
        publisherField.setText("DORSU-BEC");
        //Default value for date 
        publicationDatePicker.setValue(LocalDate.now());
        //Binds auto completion for author field
        TextFields.bindAutoCompletion(authorField,authorSuggestions);
        
        
       }
        
       
      

    //Handles the query for the new book
   public void handleSave(ActionEvent event) throws SQLException{
       String title = titleField.getText();
       ObservableList<Category> selectedCategory = categoryListView.getSelectionModel().getSelectedItems();
       String publisher = publisherField.getText();
       LocalDate publicationDate = publicationDatePicker.getValue();
       String isbn = isbnField.getText();
       handleAuthor();
       Book book = new Book(title,authorManager.getAuthor_id(),publisher,publicationDate,1,isbn);
       service.insert(book, selectedCategory);
       clearForm();  
       handleCancel(event);
   }
   
   //Handles the modal closing
   public void handleCancel(ActionEvent event){
       modal_util.closeModal(event);
           
   }
    
   public void clearForm(){
       titleField.setText("");
       authorField.setText("");
       publisherField.setText("");
       isbnField.setText("");
      
       
       
   }
   
   public void handleGenerateBarcode() throws IOException,WriterException{
         Path path = barcode_service.generateBarcode(isbnField.getText(),barcode_config.path+"/"+titleField.getText()+".png");
         handleBarcodePreview(path);
   }
   
   public void handleBarcodePreview(Path path) throws IOException{
       //Needs the file: in the image class
       Image image = new Image("file:"+String.valueOf(path));
       barcodeImageView.setImage(image);
   }
   
   public void handleAuthor() throws SQLException{
       if(!authorField.getText().isEmpty()){
           String author = authorField.getText();
           
           boolean isExisting = author_service.isExisting(new Author(author));
           
           if(!isExisting){
               author_service.addAuthor(new Author(author));
               int id = author_service.getId(new Author(author));
               authorManager.setAuthor_id(id);
               return;
           }
           
             int id = author_service.getId(new Author(author));
             authorManager.setAuthor_id(id);
       } 
      
   }
   
   public void handleStatus(){
       
   }
}
