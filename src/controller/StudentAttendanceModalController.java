/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import model.Student;
import service.LogService;
import service.StudentService;
import util.AlertUtil;
import util.ModalUtil;

/**
 * FXML Controller class
 *
 * @author Windyl
 */
public class StudentAttendanceModalController implements Initializable {

    @FXML
    private TextField studentBarcodeField;
    @FXML
    private Label statusLabel;
    @FXML
    private Button cancelBtn;

    private StudentService studentService = new StudentService();
    private LogService logService = new LogService();
    private AlertUtil alertUtil = new AlertUtil();
    private ModalUtil modalUtil = new ModalUtil();

    private Student currentStudent;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Focus on barcode field
        studentBarcodeField.requestFocus();

        // Handle barcode scanning
        studentBarcodeField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                scanAndLogAttendance();
            }
        });

        // Set initial status
        updateStatus("Ready to scan student barcode", "#666666");
    }

    private void scanAndLogAttendance() {
        String barcode = studentBarcodeField.getText().trim();
        if (barcode.isEmpty()) {
            updateStatus("Please enter a student barcode", "#dc3545");
            alertUtil.error("Please enter a student barcode");
            return;
        }

        updateStatus("Searching for student...", "#ff9800");

        try {
            Student student = studentService.search(barcode);
            if (student != null) {
                updateStatus("Student found! Logging attendance...", "#2196F3");
                currentStudent = student;

                // Automatically log attendance
                boolean success = logService.logAttendance(
                    currentStudent.getFirstname(),
                    currentStudent.getMiddlename(),
                    currentStudent.getLastname(),
                    LocalDate.now(),
                    currentStudent.getSchool_id(),
                    currentStudent.getStudent_id()
                );

                if (success) {
                    updateStatus("✓ Attendance logged successfully for " + currentStudent.getFirstname() + " " + currentStudent.getLastname(), "#4CAF50");
                    alertUtil.success("Attendance logged successfully for " + currentStudent.getFirstname() + " " + currentStudent.getLastname());

                    // Clear the field for next scan
                    studentBarcodeField.clear();

                    // Auto-close modal after 2 seconds
                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished(event -> modalUtil.closeModal(null));
                    pause.play();

                } else {
                    updateStatus("Failed to log attendance. Please try again.", "#dc3545");
                    alertUtil.error("Failed to log attendance. Please try again.");
                }
            } else {
                updateStatus("Student not found. Please check the barcode.", "#dc3545");
                alertUtil.error("Student not found. Please check the barcode.");
                currentStudent = null;
            }
        } catch (SQLException e) {
            updateStatus("Error logging attendance", "#dc3545");
            alertUtil.error("Error logging attendance: " + e.getMessage());
            currentStudent = null;
        }
    }


    private void updateStatus(String message, String color) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-weight: 600; -fx-font-size: 13px; -fx-alignment: CENTER;");
    }


    @FXML
    private void handleCancel(ActionEvent event) {
        modalUtil.closeModal(event);
    }
}
