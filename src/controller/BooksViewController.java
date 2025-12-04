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
import java.sql.SQLException;
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.PropertyValueFactory;
import service.BookService;
import model.Book;

import util.ModalUtil;
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
      TableColumn<Book, String> titleCol,authorCol,genreCol, publisherCol, dateCol,copiesCol, availableCol; 
      @FXML
      ComboBox genreBox;
      BookService service = new BookService();
      
      
   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        genreBox.getItems().addAll(  "Contemporary Fiction",
            "Historical Fiction",
            "Science Fiction",
            "Fantasy",
            "Mystery",
            "Thriller",
            "Suspense",
            "Horror",
            "Romance",
            "Western",
            "Dystopian",
            "Adventure",
            "Crime",
            "Detective",
            "Espionage",
            "Gothic",
            "Magical Realism",
            "Paranormal",
            "Urban Fantasy",
            
            // Non-Fiction Categories
            "Biography",
            "Autobiography",
            "Memoir",
            "History",
            "Philosophy",
            "Psychology",
            "Science",
            "Technology",
            "Business",
            "Economics",
            "Self-Help",
            "True Crime",
            "Travel",
            "Cooking",
            "Health & Fitness",
            "Religion",
            "Spirituality",
            "Politics",
            "Social Sciences",
            "Nature",
            "Essays",
            "Journalism",
            
            // Academic & Reference
            "Textbook",
            "Reference",
            "Encyclopedia",
            "Dictionary",
            "Manual",
            "Guide",
            
            // Arts & Entertainment
            "Art",
            "Music",
            "Photography",
            "Film & TV",
            "Theater",
            "Comics",
            "Graphic Novels",
            
            // Children & Young Adult
            "Children's Picture Books",
            "Children's Fiction",
            "Middle Grade",
            "Young Adult (YA)",
            "Educational Children's",
            
            // Poetry & Drama
            "Poetry",
            "Drama",
            "Plays",
            
            // Other
            "Humor",
            "Satire",
            "Anthology",
            "Short Stories",
            "Other");
       
        bookTable.setItems(data); 
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("publicationDate"));
        copiesCol.setCellValueFactory(new PropertyValueFactory<>("copies"));
        availableCol.setCellValueFactory(new PropertyValueFactory<>("isAvailable"));
        
        try{ loadTable(); } catch(SQLException error){ error.printStackTrace(); }
    } 
    
    
    public void showAddModal(ActionEvent event) throws IOException,SQLException{
        modal_util.openModal("AddBookModal", "Add Book");
        loadTable();
        
       
    }
    
    public void loadTable() throws SQLException{
        ArrayList<Book> book_list = service.list();
        //Automatically clears old data and loads to the table
        data.setAll(book_list);
        
        
    }
    
    public void handleFilter(String course){
       
     
        
        
    }
    
    public void handleChange(){
        
    }
    
}
