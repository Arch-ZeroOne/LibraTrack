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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import managers.BookManager;
import model.Student;
import service.BorrowService;
import service.StudentService;
import util.AlertUtil;
import util.ModalUtil;

/**
 * FXML Controller class
 *
 * @author Windyl
 */
public class BorrowBookModalController implements Initializable {

    @FXML
    private Label bookTitleLabel;
    @FXML
    private Label bookAuthorLabel;
    @FXML
    private Label bookPublisherLabel;
    @FXML
    private Label bookIsbnLabel;
    @FXML
    private Label bookIdLabel;
    @FXML
    private TextField studentBarcodeField;
    @FXML
    private Label studentNameLabel;
    @FXML
    private Label studentIdLabel;
    @FXML
    private Label studentCourseLabel;
    @FXML
    private DatePicker borrowDatePicker;
    @FXML
    private DatePicker dueDatePicker;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button confirmBtn;

    private BookManager bookManager = BookManager.getInstance();
    private StudentService studentService = new StudentService();
    private BorrowService borrowService = new BorrowService();
    private AlertUtil alertUtil = new AlertUtil();
    private ModalUtil modalUtil = new ModalUtil();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Populate book information
        populateBookInfo();

        // Set default dates
        borrowDatePicker.setValue(java.time.LocalDate.now());
        dueDatePicker.setValue(java.time.LocalDate.now().plusDays(7));

        // Focus on student barcode field
        studentBarcodeField.requestFocus();

        // Handle barcode scanning
        studentBarcodeField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    scanStudent();
                } catch (SQLException e) {
                    alertUtil.error("Error scanning student: " + e.getMessage());
                }
            }
        });
    }

    private void populateBookInfo() {
        bookTitleLabel.setText(bookManager.getTitle());
        bookAuthorLabel.setText(bookManager.getAuthor());
        bookPublisherLabel.setText(bookManager.getPublisher());
        bookIsbnLabel.setText(bookManager.getIsbn());
        bookIdLabel.setText(String.valueOf(bookManager.getId()));
    }

    private void scanStudent() throws SQLException {
        String barcode = studentBarcodeField.getText().trim();
        if (barcode.isEmpty()) {
            alertUtil.error("Please enter a student barcode");
            return;
        }

        Student student = studentService.search(barcode);
        if (student != null) {
            populateStudentInfo(student);
        } else {
            alertUtil.error("Student not found");
            clearStudentInfo();
        }
    }

    private void populateStudentInfo(Student student) {
        studentNameLabel.setText(student.getFirstname() + " " + student.getMiddlename() + " " + student.getLastname());
        studentIdLabel.setText(String.valueOf(student.getStudent_id()));
        studentCourseLabel.setText(student.getCourse());
    }

    private void clearStudentInfo() {
        studentNameLabel.setText("Student Name");
        studentIdLabel.setText("Student ID");
        studentCourseLabel.setText("Course");
    }

    @FXML
    private void handleConfirm(ActionEvent event) {
        try {
            // Validate inputs
            if (studentIdLabel.getText().equals("Student ID")) {
                alertUtil.error("Please scan a valid student barcode");
                return;
            }

            if (borrowDatePicker.getValue() == null || dueDatePicker.getValue() == null) {
                alertUtil.error("Please select borrow and due dates");
                return;
            }

            // Get student ID
            int studentId = Integer.parseInt(studentIdLabel.getText());

            // Get dates
            String borrowDate = borrowDatePicker.getValue().toString();
            String dueDate = dueDatePicker.getValue().toString();

            // Attempt to borrow the book
            boolean success = borrowService.borrowBook(
                bookManager.getId(),
                studentId,
                1, // Default librarian ID
                borrowDate,
                dueDate
            );

            if (success) {
                alertUtil.success("Book borrowed successfully!");
                ModalUtil.closeModal(event);
            } else {
                alertUtil.error("Failed to borrow book. It may already be borrowed.");
            }

        } catch (NumberFormatException e) {
            alertUtil.error("Invalid student ID format");
        } catch (Exception e) {
            alertUtil.error("An error occurred while borrowing the book: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        modalUtil.closeModal(event);
    }
}
