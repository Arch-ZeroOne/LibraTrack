/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import java.sql.Statement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import service.BookService;
import model.Book;
import model.Student;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.SelectionMode;
import model.Category;
import util.ModalUtil;
import service.GenreService;
/**
 * FXML Controller class
 *
 * @author Windyl
 */
public class BooksViewController implements Initializable {
      ModalUtil modal_util = new ModalUtil();
      @FXML
      TableView<Book> bookTable;
      ObservableList<Book> data = FXCollections.observableArrayList();
      @FXML
      TableColumn<Book, String> titleCol,authorCol, publisherCol, dateCol, availableCol,accessionCol; 
    
      @FXML
      DatePicker datePicker;
      BookService service = new BookService();
      @FXML
      TextField searchField;
    @FXML
    private Button createBtn;
    @FXML
    private ComboBox<?> categoryComboBox;
    @FXML
    private TableColumn<?, ?> actionCol;
    @FXML
    private Pagination pagination;
   
      
      
   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        
      
    
       
        bookTable.setItems(data); 
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("publicationDate"));
        availableCol.setCellValueFactory(new PropertyValueFactory<>("isAvailable"));
        accessionCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        
        try{ loadTable(); } catch(SQLException error){ error.printStackTrace(); }
    } 
    
    
    @FXML
    public void showAddModal(ActionEvent event) throws IOException,SQLException{
        modal_util.openModal("AddBookModal", "Add Book");
        loadTable();
 
       
    }
    
    public void loadTable() throws SQLException{
        ArrayList<Book> book_list = service.list();
        //Automatically clears old data and loads to the table
        data.setAll(book_list);
        
        
    }
    
    
    
    @FXML
    public void handleDateChange(ActionEvent event){
        LocalDate date = datePicker.getValue();
        try{ handleDateFilter(date); }catch(SQLException error){ error.printStackTrace();};
        
    }
    @FXML
    public void handleSearch(KeyEvent event) throws SQLException{
        String symbol = searchField.getText();
        try{ handleSearchFilter(symbol); }catch(SQLException error){ error.printStackTrace();};
        
       
        
    }
    
    
    
     
     
    public void handleDateFilter(LocalDate localDate) throws SQLException{
        
//       if(localDate.equals(LocalDate.now())){
//            loadTable();
//             return;
//        }
//       String dateString = String.valueOf(localDate);
//       
//        
//        ArrayList<Book> book_list = service.list();
//        List<Book> filtered = book_list.stream().
//                                     filter(book -> book.getPublicationDate()
//                                     .equals(dateString)).collect(Collectors.toList());
//        data.setAll(filtered);
        
        
    }
       public void handleSearchFilter(String symbol) throws SQLException{
        
         if(symbol.isEmpty()){
             loadTable();
             return;
             
         }
       
        
//        ArrayList<Book> book_list = service.list();
//        List<Book> filtered = book_list.stream().
//                                     filter(book -> book.getTitle().contains(symbol)
//                                         || book.getAuthor().contains(symbol)
//                                         || book.getPublisher().contains(symbol) 
//                                         || book.getPublicationDate().contains(symbol)
//                                         
//                                                 )
//                                    .collect(Collectors.toList());
//        
//        data.setAll(filtered);
        
        
    }

    @FXML
    private void handleCategoryFilter(ActionEvent event) {
    }
   
    
}
