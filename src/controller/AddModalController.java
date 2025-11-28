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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.Book;
import util.DateUtil;
import service.BookService;
import java.sql.SQLException;
/**
 * FXML Controller class
 *
 * @author Windyl
 */
public class AddModalController implements Initializable {
     
     @FXML
     TextField titleField,authorField,publisherField,isbnField,copiesField,barcodeField;
     @FXML
     ComboBox<String> genreComboBox;
     @FXML
     DatePicker publicationDatePicker;
     DateUtil date_util = new DateUtil();
     BookService service = new BookService();
     
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        genreComboBox.getItems().addAll("Literary Fiction",
            "Contemporary Fiction",
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
    }

    //Handles the query for the new book
   public void handleSave(ActionEvent event) throws SQLException{
       String title = titleField.getText();
       String author = authorField.getText();
       String genre = genreComboBox.getValue();
       String publisher = publisherField.getText();
       String publicationDate = date_util.getFormattedDate(publicationDatePicker.getValue());
       String isbn = isbnField.getText();
       int copies = Integer.parseInt(copiesField.getText());
       String barcode = barcodeField.getText();
     
       Book book = new Book(title,author,genre,publisher,publicationDate,isbn,copies,true,barcode);
       service.insert(book);
   }
   
   //Handles the modal closing
   public void handleCancel(ActionEvent event){
       
       
   }
  
   
   
    
}
