/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.beans.property.SimpleStringProperty;
import java.sql.Statement;
import java.sql.SQLException;
import java.time.LocalDate;
import util.DatabaseUtil;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import service.BookService;
import service.GenreService;
import model.Book;
import model.Student;
import model.Category;
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
      DatabaseUtil dbUtil = new DatabaseUtil();
      @FXML
      TableView<BookRowView> bookTable;
      ObservableList<BookRowView> data = FXCollections.observableArrayList();
      BookManager bManager = BookManager.getInstance();
    
      @FXML
      DatePicker datePicker;
      BookService service = new BookService();
      GenreService genreService = new GenreService();
      @FXML
      TextField searchField;



    @FXML
    private Button createBtn;
    @FXML
    private ComboBox<String> categoryComboBox;
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
        statusCol.setCellValueFactory(new PropertyValueFactory<>("displayStatus"));
       
        // Populate category combo box
        try {
            populateCategoryComboBox();
        } catch (SQLException e) {
            System.err.println("Error loading categories: " + e.getMessage());
        }

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
                        return;
                    }

                    BookRowView book = getTableView().getItems().get(getIndex());
                    HBox buttonBox;

                    if(book.isBorrowed()) {
                        // Show Return, Update, and Delete buttons for borrowed books
                        Button returnBtn = new Button("Return");
                        returnBtn.getStyleClass().addAll("btn", "btn-success");
                        returnBtn.setOnAction(event -> {
                           try{
                               int id = book.getBook_id();
                               bManager.setId(id);
                               bManager.setTitle(book.getTitle());
                               handleReturn();
                           }catch(IOException error){
                              System.out.println("Error On Return");
                           }
                        });

                        Button updateBtn = new Button("Update");
                        updateBtn.getStyleClass().addAll("btn", "btn-update");
                        updateBtn.setOnAction(event -> {
                           try{
                               int id = book.getBook_id();
                               bManager.setId(id);
                               handleUpdate();
                           }catch(IOException error){
                              System.out.println("Error On Update");
                           }
                        });

                        Button deleteBtn = new Button("Delete");
                        deleteBtn.getStyleClass().addAll("btn", "btn-delete");
                        deleteBtn.setOnAction(event -> {
                           try{
                               int id = book.getBook_id();
                               bManager.setId(id);
                               handleDelete();
                           }catch(SQLException error){
                              System.out.println("Error On Delete");
                           }
                        });

                        buttonBox = new HBox(8, returnBtn, updateBtn, deleteBtn);
                    } else {
                        // Show Borrow, Update, and Delete buttons for available books
                        Button borrowBtn = new Button("Borrow");
                        borrowBtn.getStyleClass().addAll("btn", "btn-primary");
                        borrowBtn.setOnAction(event -> {
                           try{
                               int id = book.getBook_id();
                               bManager.setId(id);
                               bManager.setTitle(book.getTitle());
                               bManager.setAuthor(book.getAuthor());
                               bManager.setPublisher(book.getPublisher());
                               bManager.setIsbn(book.getIsbn());
                               handleBorrow();
                           }catch(IOException error){
                              System.out.println("Error On Borrow");
                           }
                        });

                        Button updateBtn = new Button("Update");
                        updateBtn.getStyleClass().addAll("btn", "btn-update");
                        updateBtn.setOnAction(event -> {
                           try{
                               int id = book.getBook_id();
                               bManager.setId(id);
                               handleUpdate();
                           }catch(IOException error){
                              System.out.println("Error On Update");
                           }
                        });

                        Button deleteBtn = new Button("Delete");
                        deleteBtn.getStyleClass().addAll("btn", "btn-delete");
                        deleteBtn.setOnAction(event -> {
                           try{
                               int id = book.getBook_id();
                               bManager.setId(id);
                               handleDelete();
                           }catch(SQLException error){
                              System.out.println("Error On Delete");
                           }
                        });

                        buttonBox = new HBox(8, borrowBtn, updateBtn, deleteBtn);
                    }

                    buttonBox.setAlignment(Pos.CENTER);
                    setGraphic(buttonBox);
                }
        });
        
      
             
       
        
        
        try{ loadTable(); } catch(SQLException error){ error.printStackTrace(); }
    } 
    
    
    @FXML
    public void showAddModal(ActionEvent event) throws IOException,SQLException{
        ModalUtil.openModal("AddBookModal", "Add Book",null);
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

    private void populateCategoryComboBox() throws SQLException {
        categoryComboBox.getItems().clear();
        categoryComboBox.getItems().add("All Categories"); // Default option

        try {
            ObservableList<Category> categories = genreService.list();
            for (Category category : categories) {
                categoryComboBox.getItems().add(category.getCategory_name());
            }
        } catch (SQLException e) {
            System.err.println("Error loading categories: " + e.getMessage());
        }

        categoryComboBox.setValue("All Categories"); // Set default selection
    }
    @FXML
    public void handleSearch(KeyEvent event) throws SQLException{
        String symbol = searchField.getText();
        try{ handleSearchFilter(symbol); }catch(SQLException error){ error.printStackTrace();};
        
       
        
    }
    
    
    
     
     
    public void handleDateFilter(LocalDate localDate) throws SQLException{

        if(localDate == null){
            loadTable();
            return;
        }

        ArrayList<BookRowView> book_list = service.list();
        ArrayList<BookRowView> filtered = new ArrayList<>();

        for(BookRowView book : book_list){
            // Compare the publication date with the selected date
            if(book.getPublication_date() != null &&
               book.getPublication_date().equals(localDate)){
                filtered.add(book);
            }
        }

        data.setAll(filtered);
        System.out.println("Date filter applied: " + localDate + " - Found " + filtered.size() + " results");

    }
       public void handleSearchFilter(String symbol) throws SQLException{

         if(symbol.isEmpty()){
             loadTable();
             return;

         }

        ArrayList<BookRowView> book_list = service.list();
        ArrayList<BookRowView> filtered = new ArrayList<>();

        String searchTerm = symbol.toLowerCase().trim();

        for(BookRowView book : book_list){
            // Search in title, author, publisher, and ISBN
            if(book.getTitle() != null && book.getTitle().toLowerCase().contains(searchTerm) ||
               book.getAuthor() != null && book.getAuthor().toLowerCase().contains(searchTerm) ||
               book.getPublisher() != null && book.getPublisher().toLowerCase().contains(searchTerm) ||
               book.getIsbn() != null && book.getIsbn().toLowerCase().contains(searchTerm) ||
               String.valueOf(book.getBook_id()).contains(searchTerm)) {
                filtered.add(book);
            }
        }

        data.setAll(filtered);
        System.out.println("Search filter applied: '" + symbol + "' - Found " + filtered.size() + " results");

    }

    @FXML
    private void handleCategoryFilter(ActionEvent event) {
        String selectedCategory = categoryComboBox.getValue();

        if (selectedCategory == null || selectedCategory.equals("All Categories")) {
            // Show all books
            try {
                loadTable();
            } catch (SQLException e) {
                System.err.println("Error loading table: " + e.getMessage());
            }
            return;
        }

        // Filter by selected category
        try {
            handleCategoryFilterImpl(selectedCategory);
        } catch (SQLException e) {
            System.err.println("Error filtering by category: " + e.getMessage());
        }
    }

    private void handleCategoryFilterImpl(String categoryName) throws SQLException {
        ArrayList<BookRowView> book_list = service.list();
        ArrayList<BookRowView> filtered = new ArrayList<>();

        // Get category ID by name
        int categoryId = genreService.getByName(categoryName);

        if (categoryId > 0) {
            // Get all book IDs that belong to this category
            ArrayList<Integer> bookIdsInCategory = getBookIdsByCategory(categoryId);

            // Filter books that are in the selected category
            for (BookRowView book : book_list) {
                if (bookIdsInCategory.contains(book.getBook_id())) {
                    filtered.add(book);
                }
            }
        }

        data.setAll(filtered);
        System.out.println("Category filter applied: '" + categoryName + "' - Found " + filtered.size() + " results");
    }

    private ArrayList<Integer> getBookIdsByCategory(int categoryId) throws SQLException {
        ArrayList<Integer> bookIds = new ArrayList<>();
        String query = "SELECT book_id FROM book_categories WHERE category_id = ?";
        Connection connection = null;

        try {
            connection = dbUtil.connect();
            if (connection != null) {
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setInt(1, categoryId);
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                        bookIds.add(rs.getInt("book_id"));
                    }
                }
            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }

        return bookIds;
    }
    
    private void handleUpdate()throws IOException{
        ModalUtil.openModal("UpdateBookModal","Update Book",(UpdateBookModalController controller) -> {
            controller.setOnUpdateSuccess(() -> {
                try{
                   loadTable();
                }catch(SQLException error){
                    error.printStackTrace();
                }
             
                    
                    
                    });
                    
                    });
      
        
    }

    private void handleBorrow() throws IOException {
        ModalUtil.openModal("BorrowBookModal", "Borrow Book", (controller) -> {
            // Refresh table after modal closes
            try {
                loadTable();
            } catch (SQLException e) {
                System.out.println("Error refreshing table: " + e.getMessage());
            }
        });
    }

    private void handleReturn() throws IOException {
        ModalUtil.openModal("ReturnBookModal", "Return Book", (controller) -> {
            // Refresh table after modal closes
            try {
                loadTable();
            } catch (SQLException e) {
                System.out.println("Error refreshing table: " + e.getMessage());
            }
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
