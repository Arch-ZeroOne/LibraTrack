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
import java.time.LocalDate;
import model.Book;
import model.Category;
import managers.BookManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SelectionMode;
import model.Author;
import org.controlsfx.control.textfield.TextFields;
import util.ModalUtil;
import managers.AuthorManager;
import managers.CategoryManager;
import managers.StatusManager;

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
    private Runnable onUpdateSuccess;
    ModalUtil modal_util = new ModalUtil();
    BookService service = new BookService();
    GenreService genService = new GenreService();
    AuthorService aService = new AuthorService();
    AuthorManager author_manager = AuthorManager.getInstance();
    BookManager instance = BookManager.getInstance();
    StatusManager status_manager = StatusManager.getInstance();
    ObservableList<String> authorSuggestions = FXCollections.observableArrayList();
    ObservableList<Category> categoryList = FXCollections.observableArrayList();
    ObservableList<Category> categoryData = FXCollections.observableArrayList();
    @FXML
    private ChoiceBox<String> availabilityChoiceBox;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //Initializes list of genre
        categoryListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
         // Populate the availability options
         availabilityChoiceBox.getItems().addAll("Available", "Borrowed", "Deactivated");
         availabilityChoiceBox.setValue("Available"); // Set default value
         //Sets the defaut value of id for status
         status_manager.setStatus_id(1);
         availabilityChoiceBox.setOnAction(event -> {
           try{
                handleStatusChange();
                
                }catch(SQLException error){
                  error.printStackTrace();
            };   
            });
         
         
       try{ 
        ObservableList<Author> authors =  aService.list();
        
        for(Author author : authors){
            authorSuggestions.add(author.getAuthor_name());
        }
        
        
       }catch(SQLException error){
           error.printStackTrace();
       }
      
        try{
          
          categoryList.setAll(genService.list());
          categoryListView.setItems(categoryList);
          
         

        }catch(SQLException error){
            error.printStackTrace();
        }
        
        
        
         try{
             autoFill();
         }catch(SQLException error){
             System.out.println("Error auto fill on update form");
         }
         
          //Binds auto completion for author field
        TextFields.bindAutoCompletion(authorField,authorSuggestions);
    }    

    @FXML
    private void handleCancel(ActionEvent event) {
         modal_util.closeModal(event);
    }

    @FXML
    private void handleUpdate(ActionEvent event) throws SQLException {
       String title = titleField.getText();
       ObservableList<Category> selectedCategory = categoryListView.getSelectionModel().getSelectedItems();
       String publisher = publisherField.getText();
       LocalDate publicationDate = publicationDatePicker.getValue();
       handleAuthor();
       
       Book book = new Book(instance.getId(),title,author_manager.getAuthor_id(),publisher,publicationDate,status_manager.getStatus_id());
       service.update(book, selectedCategory);
       clearForm();
       //Runs the thread if update is not null
       if (onUpdateSuccess != null) {
            onUpdateSuccess.run();
         }
       handleCancel(event);
       
    }
    
    //autofills modal fields
    public void autoFill() throws SQLException{
        Book book = service.getById(instance.getId());
        categoryData = genService.list();
        categoryListView.setItems(categoryData);
        titleField.setText(book.getTitle());
        authorField.setText(aService.getById(book.getAuthor_id()));
        publisherField.setText(book.getPublisher());
        publicationDatePicker.setValue(book.getPublication_date());
    }
    
     public void clearForm(){
       titleField.setText("");
       authorField.setText("");
       publisherField.setText("");
      
       
       
   }
   public void handleAuthor() throws SQLException{
       if(!authorField.getText().isEmpty()){
           String author = authorField.getText();
           
           boolean isExisting = aService.isExisting(new Author(author));
           
           if(!isExisting){
               aService.addAuthor(new Author(author));
               int id = aService.getId(new Author(author));
               author_manager.setAuthor_id(id);
               return;
           }
         
             int id = aService.getId(new Author(author));
             author_manager.setAuthor_id(id);
       } 
      
   }
   
   public void handleStatusChange() throws SQLException{
       String status = availabilityChoiceBox.getValue();
       System.out.println(status);
       int statusId = genService.getByName(status);
       status_manager.setStatus_id(statusId);
       
   }
   
   public void setOnUpdateSuccess(Runnable onUpdateSuccess){
       this.onUpdateSuccess = onUpdateSuccess;
   }
    
}
