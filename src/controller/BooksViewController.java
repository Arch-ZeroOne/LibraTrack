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
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.geometry.Pos;
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
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import model.BookRowView;

import util.ModalUtil;

import managers.BookManager;
import controller.UpdateBookModalController;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 * FXML Controller class
 *
 * @author Windyl
 */
public class BooksViewController implements Initializable {
      ModalUtil modal_util = new ModalUtil();
      @FXML
      TableView<BookRowView> bookTable;
      ObservableList<BookRowView> data = FXCollections.observableArrayList();
      BookManager bManager = BookManager.getInstance();
    
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
    private Pagination pagination;
    @FXML
    private TableColumn<?, ?> idCol;
    @FXML
    private TableColumn<?, ?> titleCol;
    @FXML
    private TableColumn<?, ?> authorCol;
    @FXML
    private TableColumn<?, ?> publisherCol;
    @FXML
    private TableColumn<?, ?> publicationCol;
    @FXML
    private TableColumn<?, ?> statusCol;
    @FXML
    private TableColumn<BookRowView, Void> actionsCol;
   
      
      
   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        
      
    
       
        bookTable.setItems(data); 
        idCol.setCellValueFactory(new PropertyValueFactory<>("book_id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        publicationCol.setCellValueFactory(new PropertyValueFactory<>("publication_date"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
       
        actionsCol.setCellFactory(column -> new TableCell<>(){
                private final Button deleteBtn = new Button("Delete");
                private final Button updateBtn = new Button("Update");
                private  HBox box = new HBox(8,deleteBtn,updateBtn);
                
                
                
  
               
                {
            
                    updateBtn.getStyleClass().addAll("btn", "btn-update");
                    deleteBtn.getStyleClass().addAll("btn", "btn-delete");
                    
                     //Events for the button
                    box.setAlignment(Pos.CENTER);
                    deleteBtn.setOnAction(event -> {
                      
                       try{
                           
                           //Gets where the current row is clicked
                           BookRowView col = getTableView().getItems().get(getIndex());
                           //Retrieve the book id
                           int id = col.getBook_id();
                           bManager.setId(id);
                           handleDelete();
                           
                       }catch(SQLException error){
                          System.out.println("Error On Update");
                       }
                    });
                    
                     updateBtn.setOnAction(event -> {
                       try{
                           //Gets where the current row is clicked
                           BookRowView col = getTableView().getItems().get(getIndex());
                           //Retrieve the book id
                           int id = col.getBook_id();
                           bManager.setId(id);
                           handleUpdate();
                       }catch(IOException error){
                          System.out.println("Error On Update");
                       }
                       
                    });
                  
                }
                //Needed to fully render the button
                @Override
                protected void updateItem(Void item,boolean empty){
                    super.updateItem(item, empty);
                    
                    if(empty){
                        setGraphic(null);
                    }else{
                        setGraphic(box);
                    }
                }
        });
        
      
             
       
        
        
        try{ loadTable(); } catch(SQLException error){ error.printStackTrace(); }
    } 
    
    
    @FXML
    public void showAddModal(ActionEvent event) throws IOException,SQLException{
        modal_util.openModal("AddBookModal", "Add Book",null);
        loadTable();
 
       
    }
    
    public void loadTable() throws SQLException{
        ArrayList<BookRowView> book_list = service.list();
       
        for(BookRowView book : book_list){
         System.out.println(book.getTitle());
        }
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
    
    private void handleUpdate()throws IOException{
        modal_util.openModal("UpdateBookModal","Update Book",(UpdateBookModalController controller) -> {
            controller.setOnUpdateSuccess(() -> {
                try{
                   loadTable();
                }catch(SQLException error){
                    error.printStackTrace();
                }
             
                    
                    
                    });
                    
                    });
      
        
    }
    
    private void handleDelete() throws SQLException {
        
        
        showConfirmationAlert();
       
        
    }
    
    public void showConfirmationAlert() throws SQLException{
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Confirmation Dialog");
    alert.setHeaderText("A change is about to be made.");
    alert.setContentText("Do you really want to proceed?");

    // Show the alert and wait for a response
    Optional<ButtonType> result = alert.showAndWait();

    // Process the user's choice
    if (result.isPresent() && result.get() == ButtonType.OK) {
         service.remove(bManager.getId());
        loadTable();
    } else {
        System.out.println("User cancelled or closed the dialog. Action aborted.");
    }
}
   
    
}
