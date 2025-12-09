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
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Genre;
import service.BarcodeService;
import util.ModalUtil;
import util.BarcodeConfig;
import service.GenreService;


public class AddBookModalController implements Initializable {
     
     @FXML
     TextField titleField,authorField,publisherField,isbnField,copiesField,barcodeField;
     @FXML
     ListView<Genre> genreListView;
     @FXML
     DatePicker publicationDatePicker;
     @FXML
     ImageView barcodeImageView;
     DateUtil date_util = new DateUtil();
     ModalUtil modal_util = new ModalUtil();
     BookService service = new BookService();
     BarcodeService barcode_service = new BarcodeService();
     BarcodeConfig barcode_config = new BarcodeConfig();
     GenreService genre_service = new GenreService();
     
     ObservableList<Genre> genreList = FXCollections.observableArrayList();
     public ObservableList<Genre> genreList(){
          return genreList;
     }
        
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        genreListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        genreListView.setItems(genreList);
        //Initializes list of genre
        try{
          genreList.setAll(genre_service.list());
        }catch(SQLException error){
            error.printStackTrace();
        }       
       }

    //Handles the query for the new book
   public void handleSave(ActionEvent event) throws SQLException{
       String title = titleField.getText();
       String author = authorField.getText();
       ObservableList<Genre> selectedGenre = genreListView.getSelectionModel().getSelectedItems();
       String publisher = publisherField.getText();
       String publicationDate = date_util.getFormattedDate(publicationDatePicker.getValue());
       String isbn = isbnField.getText();
       int copies = Integer.parseInt(copiesField.getText());
       
      
     
     
       Book book = new Book(title,author,publisher,publicationDate,isbn,copies,"Available");
       service.insert(book,selectedGenre);
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
       copiesField.setText("");
       
       
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
   
   
  
  
}
