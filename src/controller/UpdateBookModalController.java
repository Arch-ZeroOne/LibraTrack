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
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import service.BookService;
import service.GenreService;
import service.AuthorService;
import java.sql.SQLException;
import model.Book;
import model.Category;
import managers.BookManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * FXML Controller class
 *
 * @author Windyl
 */
public class UpdateBookModalController implements Initializable {

    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private ListView<Category> categoryListView;
    @FXML
    private TextField publisherField;
    @FXML
    private DatePicker publicationDatePicker;
    BookService service = new BookService();
    GenreService genService = new GenreService();
    AuthorService aService = new AuthorService();
    BookManager instance = BookManager.getInstance();
    ObservableList<Category> categoryData = FXCollections.observableArrayList();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         try{
             autoFill();
         }catch(SQLException error){
             System.out.println("Error auto fill on update form");
         }
    }    

    @FXML
    private void handleCancel(ActionEvent event) {
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
    }
    
    public void autoFill() throws SQLException{
        Book book = service.getById(instance.getId());
        categoryData = genService.list();
        categoryListView.setItems(categoryData);
        titleField.setText(book.getTitle());
        authorField.setText(aService.getById(book.getAuthor_id()));
        publisherField.setText(book.getPublisher());
        publicationDatePicker.setValue(book.getPublication_date());
    }
    
}
