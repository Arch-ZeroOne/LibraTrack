/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import model.BorrowedBook;
import model.Borrow;
import service.BorrowService;
import service.GenreService;
import service.StudentService;
import service.BookService;
import service.AuthorService;
import util.DatabaseUtil;
import util.ModalUtil;
import model.Category;

/**
 * FXML Controller class
 *
 * @author Windyl
 */
public class BorrowedBooksViewController implements Initializable {

    @FXML
    private TableView<BorrowedBook> borrowedBooksTable;
    @FXML
    private TableColumn<BorrowedBook, Integer> borrowIdCol;
    @FXML
    private TableColumn<BorrowedBook, String> bookTitleCol;
    @FXML
    private TableColumn<BorrowedBook, String> studentIdCol;
    @FXML
    private TableColumn<BorrowedBook, String> studentNameCol;
    @FXML
    private TableColumn<BorrowedBook, LocalDate> borrowDateCol;
    @FXML
    private TableColumn<BorrowedBook, LocalDate> dueDateCol;
    @FXML
    private TableColumn<BorrowedBook, String> statusCol;
    @FXML
    private TableColumn<BorrowedBook, Void> actionsCol;

    @FXML
    private TextField searchField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private Label totalBorrowedLabel;
    @FXML
    private Label overdueBooksLabel;
    @FXML
    private Label dueTodayLabel;
    @FXML
    private Label returnedTodayLabel;

    @FXML
    private Button returnBookBtn;

    @FXML
    private AnchorPane mainAnchorPane;

    private ObservableList<BorrowedBook> borrowedBooksData = FXCollections.observableArrayList();
    private BorrowService borrowService = new BorrowService();
    private StudentService studentService = new StudentService();
    private BookService bookService = new BookService();
    private AuthorService authorService = new AuthorService();
    private GenreService genreService = new GenreService();
    private DatabaseUtil dbUtil = new DatabaseUtil();

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns();
        setupActionsColumn();
        populateFilters();

        try {
            loadBorrowedBooks();
            loadStatistics();

            // Force column resize after data is loaded
            Platform.runLater(() -> {
                if (borrowedBooksTable.isVisible()) {
                    resizeColumns();
                }
            });
        } catch (SQLException e) {
            System.err.println("Error initializing BorrowedBooksView: " + e.getMessage());
        }
    }

    private void setupTableColumns() {
        // Configure column properties for better responsiveness
        borrowIdCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getAccessionNumber()).asObject());
        borrowIdCol.setStyle("-fx-alignment: CENTER;");

        bookTitleCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitle()));
        bookTitleCol.setStyle("-fx-alignment: CENTER_LEFT;");

        studentIdCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getStudentId())));
        studentIdCol.setStyle("-fx-alignment: CENTER;");

        studentNameCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStudentName()));
        studentNameCol.setStyle("-fx-alignment: CENTER_LEFT;");

        borrowDateCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getBorrowDate()));
        borrowDateCol.setStyle("-fx-alignment: CENTER;");
        borrowDateCol.setCellFactory(column -> new TableCell<BorrowedBook, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(dateFormatter.format(date));
                }
            }
        });

        dueDateCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getDueDate()));
        dueDateCol.setStyle("-fx-alignment: CENTER;");
        dueDateCol.setCellFactory(column -> new TableCell<BorrowedBook, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(dateFormatter.format(date));
                }
            }
        });

        statusCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(getBorrowStatus(cellData.getValue())));
        statusCol.setStyle("-fx-alignment: CENTER;");

        // Make columns responsive with improved calculation
        setupResponsiveColumns();

        // Force initial column sizing
        Platform.runLater(() -> {
            resizeColumns();
        });

        borrowedBooksTable.setItems(borrowedBooksData);
    }

    private void setupResponsiveColumns() {
        // Set minimum widths to prevent columns from becoming too small
        borrowIdCol.setMinWidth(60);
        bookTitleCol.setMinWidth(150);
        studentIdCol.setMinWidth(90);
        studentNameCol.setMinWidth(120);
        borrowDateCol.setMinWidth(90);
        dueDateCol.setMinWidth(90);
        statusCol.setMinWidth(70);
        actionsCol.setMinWidth(120);

        // Set initial preferred widths (these will be overridden by responsive sizing)
        borrowIdCol.setPrefWidth(80);
        bookTitleCol.setPrefWidth(200);
        studentIdCol.setPrefWidth(100);
        studentNameCol.setPrefWidth(150);
        borrowDateCol.setPrefWidth(110);
        dueDateCol.setPrefWidth(110);
        statusCol.setPrefWidth(80);
        actionsCol.setPrefWidth(150);

        // Add responsive listener to the table's width property
        borrowedBooksTable.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            resizeColumns();
        });

        // Also listen to the scene width for better responsiveness
        borrowedBooksTable.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.widthProperty().addListener((sceneObs, oldSceneWidth, newSceneWidth) -> {
                    // Small delay to ensure table has finished resizing
                    Platform.runLater(() -> resizeColumns());
                });
            }
        });

        // Listen for visibility changes to ensure columns are sized when table becomes visible
        borrowedBooksTable.visibleProperty().addListener((obs, wasVisible, isVisible) -> {
            if (isVisible && !wasVisible) {
                Platform.runLater(() -> resizeColumns());
            }
        });

        // Listen to the main container width changes for ultimate responsiveness
        if (mainAnchorPane != null) {
            mainAnchorPane.widthProperty().addListener((obs, oldWidth, newWidth) -> {
                Platform.runLater(() -> resizeColumns());
            });
        }
    }

    private void resizeColumns() {
        if (borrowedBooksTable.getWidth() <= 0 || !borrowedBooksTable.isVisible()) {
            return; // Table not yet sized or visible
        }

        double tableWidth = borrowedBooksTable.getWidth();

        // Account for potential scrollbar and borders
        double availableWidth = Math.max(tableWidth - 25, 600); // Minimum 600px width

        // Calculate proportional widths with minimum constraints
        double idWidth = Math.max(60, availableWidth * 0.08);      // 8% min 60px
        double titleWidth = Math.max(150, availableWidth * 0.25);  // 25% min 150px
        double studIdWidth = Math.max(90, availableWidth * 0.12);  // 12% min 90px
        double nameWidth = Math.max(120, availableWidth * 0.18);   // 18% min 120px
        double borrowWidth = Math.max(100, availableWidth * 0.12); // 12% min 100px
        double dueWidth = Math.max(100, availableWidth * 0.12);    // 12% min 100px
        double statusWidth = Math.max(80, availableWidth * 0.08);  // 8% min 80px
        double actionsWidth = Math.max(120, availableWidth * 0.15); // 15% min 120px

        // Apply calculated widths
        borrowIdCol.setPrefWidth(idWidth);
        bookTitleCol.setPrefWidth(titleWidth);
        studentIdCol.setPrefWidth(studIdWidth);
        studentNameCol.setPrefWidth(nameWidth);
        borrowDateCol.setPrefWidth(borrowWidth);
        dueDateCol.setPrefWidth(dueWidth);
        statusCol.setPrefWidth(statusWidth);
        actionsCol.setPrefWidth(actionsWidth);

        // Debug logging (can be removed in production)
        System.out.println("Table resized: " + availableWidth + "px available, columns updated");

        // Force table layout refresh
        borrowedBooksTable.refresh();
    }

    private void setupActionsColumn() {
        actionsCol.setCellFactory(column -> new TableCell<>() {
            private final Button returnBtn = new Button("Return");
            private final Button extendBtn = new Button("Extend");
            private final HBox box = new HBox(8, returnBtn, extendBtn);

            {
                returnBtn.getStyleClass().addAll("btn", "btn-success");
                extendBtn.getStyleClass().addAll("btn", "btn-warning");

                box.setAlignment(Pos.CENTER);

                returnBtn.setOnAction(event -> {
                    BorrowedBook book = getTableView().getItems().get(getIndex());
                    handleReturnBook(book);
                });

                extendBtn.setOnAction(event -> {
                    BorrowedBook book = getTableView().getItems().get(getIndex());
                    handleExtendBook(book);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    BorrowedBook book = getTableView().getItems().get(getIndex());
                    String status = getBorrowStatus(book);

                    // Only show buttons for active borrows
                    if ("Active".equals(status)) {
                        setGraphic(box);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });
    }

    private void populateFilters() {
        // Status filter
        statusComboBox.getItems().addAll("ALL", "Active", "Overdue", "Returned");
        statusComboBox.setValue("ALL");

        // Category filter
        categoryComboBox.getItems().add("ALL");
        try {
            ObservableList<Category> categories = genreService.list();
            for (Category category : categories) {
                categoryComboBox.getItems().add(category.getCategory_name());
            }
        } catch (SQLException e) {
            System.err.println("Error loading categories: " + e.getMessage());
        }
        categoryComboBox.setValue("ALL");
    }

    private void loadBorrowedBooks() throws SQLException {
        borrowedBooksData.clear();

        // Query to get all ACTIVE borrow records (not returned) with book and student details
        String sql = "SELECT b.*, bk.book_id, books.title, books.author_id, authors.author_name, " +
                    "s.firstname, s.lastname " +
                    "FROM borrow b " +
                    "JOIN book bk ON b.accession_number = bk.accession_number " +
                    "JOIN books ON bk.book_id = books.book_id " +
                    "JOIN authors ON books.author_id = authors.author_id " +
                    "JOIN student s ON b.student_id = s.student_id " +
                    "WHERE b.return_date IS NULL " +
                    "ORDER BY b.borrow_date DESC";

        Connection conn = dbUtil.connect();
        if (conn == null) return;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                try {
                    LocalDate borrowDate = LocalDate.parse(rs.getString("borrow_date"));
                    LocalDate dueDate = LocalDate.parse(rs.getString("due_date"));

                    String authorName = rs.getString("author_name");
                    String studentName = rs.getString("firstname") + " " + rs.getString("lastname");

                    BorrowedBook borrowedBook = new BorrowedBook(
                        rs.getInt("accession_number"),
                        rs.getInt("student_id"),
                        studentName,
                        rs.getString("title"),
                        authorName,
                        borrowDate,
                        dueDate,
                        0 // penalty - could be calculated
                    );

                    borrowedBooksData.add(borrowedBook);

                } catch (Exception e) {
                    System.err.println("Error processing borrow record: " + e.getMessage());
                }
            }

        } catch (SQLException e) {
            System.err.println("Error loading borrowed books: " + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                // Ignore
            }
        }
    }

    private String getStudentName(int studentId) {
        try {
            model.Student student = studentService.search(String.valueOf(studentId));
            return student != null ? student.getFirstname() + " " + student.getLastname() : "Unknown";
        } catch (SQLException e) {
            return "Unknown";
        }
    }

    private String getBorrowStatus(BorrowedBook book) {
        LocalDate today = LocalDate.now();
        LocalDate dueDate = book.getDueDate();

        if (dueDate.isBefore(today)) {
            return "Overdue";
        } else if (dueDate.equals(today)) {
            return "Due Today";
        } else {
            return "Active";
        }
    }

    private void loadStatistics() {
        Connection conn = dbUtil.connect();
        if (conn == null) return;

        try (Statement stmt = conn.createStatement()) {
            // Total borrowed books (active borrows)
            ResultSet rsTotal = stmt.executeQuery("SELECT COUNT(*) AS total FROM borrow WHERE return_date IS NULL");
            if (rsTotal.next()) {
                totalBorrowedLabel.setText(rsTotal.getString("total"));
            }

            // Overdue books
            ResultSet rsOverdue = stmt.executeQuery("SELECT COUNT(*) AS total FROM borrow WHERE return_date IS NULL AND due_date < CURDATE()");
            if (rsOverdue.next()) {
                overdueBooksLabel.setText(rsOverdue.getString("total"));
            }

            // Books due today
            ResultSet rsDueToday = stmt.executeQuery("SELECT COUNT(*) AS total FROM borrow WHERE return_date IS NULL AND DATE(due_date) = CURDATE()");
            if (rsDueToday.next()) {
                dueTodayLabel.setText(rsDueToday.getString("total"));
            }

            // Books returned today
            ResultSet rsReturnedToday = stmt.executeQuery("SELECT COUNT(*) AS total FROM borrow WHERE DATE(return_date) = CURDATE()");
            if (rsReturnedToday.next()) {
                returnedTodayLabel.setText(rsReturnedToday.getString("total"));
            }

        } catch (SQLException e) {
            System.err.println("Error loading statistics: " + e.getMessage());
            // Set default values
            totalBorrowedLabel.setText("0");
            overdueBooksLabel.setText("0");
            dueTodayLabel.setText("0");
            returnedTodayLabel.setText("0");
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                // Ignore
            }
        }
    }

    @FXML
    private void handleSearch(KeyEvent event) {
        String searchTerm = searchField.getText().toLowerCase().trim();
        if (searchTerm.isEmpty()) {
            try {
                loadBorrowedBooks();
            } catch (SQLException e) {
                System.err.println("Error reloading books: " + e.getMessage());
            }
            return;
        }

        ObservableList<BorrowedBook> filtered = FXCollections.observableArrayList();
        for (BorrowedBook book : borrowedBooksData) {
            if (book.getTitle().toLowerCase().contains(searchTerm) ||
                book.getAuthor().toLowerCase().contains(searchTerm) ||
                String.valueOf(book.getStudentId()).contains(searchTerm) ||
                book.getStudentName().toLowerCase().contains(searchTerm)) {
                filtered.add(book);
            }
        }
        borrowedBooksTable.setItems(filtered);
        // Trigger column resize after filtering
        Platform.runLater(() -> resizeColumns());
    }

    @FXML
    private void handleDateChange(ActionEvent event) {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null) {
            borrowedBooksTable.setItems(borrowedBooksData);
            return;
        }

        ObservableList<BorrowedBook> filtered = FXCollections.observableArrayList();
        for (BorrowedBook book : borrowedBooksData) {
            if (book.getBorrowDate().equals(selectedDate) ||
                book.getDueDate().equals(selectedDate)) {
                filtered.add(book);
            }
        }
        borrowedBooksTable.setItems(filtered);
        // Trigger column resize after filtering
        Platform.runLater(() -> resizeColumns());
    }

    @FXML
    private void handleStatusChange(ActionEvent event) {
        String selectedStatus = statusComboBox.getValue();
        if (selectedStatus == null || "ALL".equals(selectedStatus)) {
            borrowedBooksTable.setItems(borrowedBooksData);
            return;
        }

        ObservableList<BorrowedBook> filtered = FXCollections.observableArrayList();
        for (BorrowedBook book : borrowedBooksData) {
            String bookStatus = getBorrowStatus(book);
            if (selectedStatus.equals(bookStatus)) {
                filtered.add(book);
            }
        }
        borrowedBooksTable.setItems(filtered);
        // Trigger column resize after filtering
        Platform.runLater(() -> resizeColumns());
    }

    @FXML
    private void handleCategoryFilter(ActionEvent event) {
        String selectedCategory = categoryComboBox.getValue();
        if (selectedCategory == null || "ALL".equals(selectedCategory)) {
            borrowedBooksTable.setItems(borrowedBooksData);
            return;
        }

        // This would require joining with book_categories table
        // For now, we'll implement a basic filter
        ObservableList<BorrowedBook> filtered = FXCollections.observableArrayList();
        // Implementation would filter by book category
        borrowedBooksTable.setItems(filtered);
        // Trigger column resize after filtering
        Platform.runLater(() -> resizeColumns());
    }

    @FXML
    private void showReturnBookModal(ActionEvent event) {
        try {
            ModalUtil.openModal("../view/ReturnBookView", "Return Book", (controller) -> {
                try {
                    loadBorrowedBooks();
                    loadStatistics();
                } catch (SQLException e) {
                    System.err.println("Error refreshing data: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            System.err.println("Error opening return book modal: " + e.getMessage());
        }
    }

    private String getAuthorName(int authorId) {
        try {
            // This would require an AuthorDao method to get author name by ID
            // For now, return a placeholder
            return "Author #" + authorId;
        } catch (Exception e) {
            return "Unknown Author";
        }
    }

    private void handleReturnBook(BorrowedBook book) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Return Book");
        alert.setHeaderText("Confirm Book Return");
        alert.setContentText("Are you sure you want to return '" + book.getTitle() + "'?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    if (borrowService.returnBook(book.getAccessionNumber())) {
                        // Success - refresh data
                        loadBorrowedBooks();
                        loadStatistics();
                        showSuccessAlert("Book returned successfully!");
                    } else {
                        showErrorAlert("Failed to return book. Please try again.");
                    }
                } catch (SQLException e) {
                    System.err.println("Error returning book: " + e.getMessage());
                    showErrorAlert("Error returning book: " + e.getMessage());
                }
            }
        });
    }

    private void handleExtendBook(BorrowedBook book) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Extend Due Date");
        alert.setHeaderText("Extend Borrowing Period");
        alert.setContentText("Are you sure you want to extend the due date for '" + book.getTitle() + "' by 7 days?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Implementation for extending due date would go here
                    // This would require updating the borrow record in the database
                    showInfoAlert("Extend functionality not yet implemented.");
                } catch (Exception e) {
                    System.err.println("Error extending book: " + e.getMessage());
                    showErrorAlert("Error extending due date: " + e.getMessage());
                }
            }
        });
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
