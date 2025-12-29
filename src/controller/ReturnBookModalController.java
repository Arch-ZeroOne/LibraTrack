/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import managers.BookManager;
import model.Borrow;
import service.BorrowService;
import service.BookService;
import util.AlertUtil;
import util.ModalUtil;

/**
 * FXML Controller class
 *
 * @author Windyl
 */
public class ReturnBookModalController implements Initializable {

    @FXML
    private Label bookTitleLabel;
    @FXML
    private Label bookIdLabel;
    @FXML
    private Label bookAuthorLabel;
    @FXML
    private Label bookIsbnLabel;
    @FXML
    private TextField bookBarcodeField;
    @FXML
    private Label verificationStatusLabel;
    @FXML
    private Label studentNameLabel;
    @FXML
    private Label borrowDateLabel;
    @FXML
    private Label dueDateLabel;
    @FXML
    private Button confirmBtn;
    @FXML
    private Button cancelBtn;

    private BookManager bookManager = BookManager.getInstance();
    private BookService bookService = new BookService();
    private BorrowService borrowService = new BorrowService();
    private AlertUtil alertUtil = new AlertUtil();
    private ModalUtil modalUtil = new ModalUtil();

    private Borrow currentBorrow;
    private boolean bookVerified = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Populate book information
        populateBookInfo();

        // Focus on barcode field
        bookBarcodeField.requestFocus();

        // Handle barcode scanning
        bookBarcodeField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                verifyBook();
            }
        });
    }

    private void populateBookInfo() {
        bookTitleLabel.setText(bookManager.getTitle());
        bookIdLabel.setText(String.valueOf(bookManager.getId()));
        bookAuthorLabel.setText(bookManager.getAuthor());
        bookIsbnLabel.setText(bookManager.getIsbn());
    }

    private void verifyBook() {
        String scannedBarcode = bookBarcodeField.getText().trim();
        String expectedIsbn = bookManager.getIsbn();

        if (scannedBarcode.equals(expectedIsbn)) {
            // Book verified, load borrow information
            try {
                loadBorrowInformation();
                verificationStatusLabel.setStyle("-fx-text-fill: #28a745;");
                verificationStatusLabel.setText("✓ Book verified successfully");
                confirmBtn.setDisable(false);
                bookVerified = true;
            } catch (SQLException e) {
                verificationStatusLabel.setStyle("-fx-text-fill: #dc3545;");
                verificationStatusLabel.setText("✗ Error loading borrow information: " + e.getMessage());
                confirmBtn.setDisable(true);
                bookVerified = false;
            }
        } else {
            verificationStatusLabel.setStyle("-fx-text-fill: #dc3545;");
            verificationStatusLabel.setText("✗ Book barcode does not match. Please scan the correct book.");
            confirmBtn.setDisable(true);
            bookVerified = false;
            clearBorrowInfo();
        }
    }

    private void loadBorrowInformation() throws SQLException {
        // Get current borrow information for this book
        currentBorrow = borrowService.getActiveBorrowByAccession(bookManager.getId());

        if (currentBorrow != null) {
            // For now, we'll show placeholder information since we don't have student details
            // In a real implementation, you'd join with student table
            studentNameLabel.setText("Student ID: " + currentBorrow.getStudentId());
            borrowDateLabel.setText(currentBorrow.getBorrowDate());
            dueDateLabel.setText(currentBorrow.getDueDate());
        } else {
            throw new SQLException("No active borrow record found for this book");
        }
    }

    private void clearBorrowInfo() {
        studentNameLabel.setText("Student Name");
        borrowDateLabel.setText("Borrow Date");
        dueDateLabel.setText("Due Date");
        currentBorrow = null;
    }

    @FXML
    private void handleConfirm(ActionEvent event) {
        if (!bookVerified || currentBorrow == null) {
            alertUtil.error("Please verify the book barcode first");
            return;
        }

        try {
            boolean success = borrowService.returnBook(bookManager.getId());

            if (success) {
                alertUtil.success("Book returned successfully!");
                ModalUtil.closeModal(event);
            } else {
                alertUtil.error("Failed to return book. Please try again.");
            }

        } catch (SQLException e) {
            alertUtil.error("Error returning book: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        modalUtil.closeModal(event);
    }

    @FXML
    private void handleScanBook(ActionEvent event) {
        verifyBook();
    }
}
