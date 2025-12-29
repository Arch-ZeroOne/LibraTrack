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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Librarian;
import service.LibrarianService;
import util.AlertUtil;
import util.ModalUtil;

/**
 * FXML Controller class
 *
 * @author Windyl
 */
public class AddLibrarianModalController implements Initializable {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField middleNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private Label statusLabel;
    @FXML
    private Button createBtn;
    @FXML
    private Button cancelBtn;

    private LibrarianService librarianService = new LibrarianService();
    private AlertUtil alertUtil = new AlertUtil();
    private ModalUtil modalUtil = new ModalUtil();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set default values
        roleComboBox.setValue("librarian");
        statusComboBox.setValue("active");

        updateStatus("Ready to create new librarian account", "#666666");
    }

    @FXML
    private void handleCreate(ActionEvent event) {
        // Validate inputs
        if (!validateInputs()) {
            return;
        }

        try {
            // Create librarian object
            Librarian librarian = new Librarian(
                firstNameField.getText().trim(),
                middleNameField.getText().trim(),
                lastNameField.getText().trim(),
                usernameField.getText().trim(),
                emailField.getText().trim(),
                passwordField.getText(),
                roleComboBox.getValue()
            );
            librarian.setIsActive(statusComboBox.getValue());

            // Attempt to create librarian
            updateStatus("Creating librarian account...", "#2196F3");
            createBtn.setDisable(true);

            librarianService.insert(librarian);

            updateStatus("✓ Librarian account created successfully!", "#4CAF50");
            alertUtil.success("Librarian account created successfully for " + librarian.getFullName());

            // Auto-close after 2 seconds
            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(2));
            pause.setOnFinished(e -> ModalUtil.closeCurrentModal());
            pause.play();

        } catch (IllegalArgumentException e) {
            updateStatus("Validation error: " + e.getMessage(), "#dc3545");
            alertUtil.error(e.getMessage());
            createBtn.setDisable(false);
        } catch (Exception e) {
            updateStatus("Failed to create librarian account", "#dc3545");
            alertUtil.error("Failed to create librarian account: " + e.getMessage());
            createBtn.setDisable(false);
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        modalUtil.closeModal(event);
    }

    private boolean validateInputs() {
        // Check required fields
        if (firstNameField.getText() == null || firstNameField.getText().trim().isEmpty()) {
            updateStatus("First name is required", "#dc3545");
            alertUtil.error("First name is required");
            firstNameField.requestFocus();
            return false;
        }

        if (lastNameField.getText() == null || lastNameField.getText().trim().isEmpty()) {
            updateStatus("Last name is required", "#dc3545");
            alertUtil.error("Last name is required");
            lastNameField.requestFocus();
            return false;
        }

        if (usernameField.getText() == null || usernameField.getText().trim().isEmpty()) {
            updateStatus("Username is required", "#dc3545");
            alertUtil.error("Username is required");
            usernameField.requestFocus();
            return false;
        }

        if (emailField.getText() == null || emailField.getText().trim().isEmpty()) {
            updateStatus("Email is required", "#dc3545");
            alertUtil.error("Email is required");
            emailField.requestFocus();
            return false;
        }

        if (passwordField.getText() == null || passwordField.getText().trim().isEmpty()) {
            updateStatus("Password is required", "#dc3545");
            alertUtil.error("Password is required");
            passwordField.requestFocus();
            return false;
        }

        if (roleComboBox.getValue() == null || roleComboBox.getValue().trim().isEmpty()) {
            updateStatus("Role selection is required", "#dc3545");
            alertUtil.error("Role selection is required");
            roleComboBox.requestFocus();
            return false;
        }

        if (statusComboBox.getValue() == null || statusComboBox.getValue().trim().isEmpty()) {
            updateStatus("Status selection is required", "#dc3545");
            alertUtil.error("Status selection is required");
            statusComboBox.requestFocus();
            return false;
        }

        // Basic email validation
        String email = emailField.getText().trim();
        if (!email.contains("@") || !email.contains(".")) {
            updateStatus("Please enter a valid email address", "#dc3545");
            alertUtil.error("Please enter a valid email address");
            emailField.requestFocus();
            return false;
        }

        return true;
    }

    private void updateStatus(String message, String color) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-weight: 500; -fx-font-size: 14px; -fx-alignment: CENTER;");
    }
}
