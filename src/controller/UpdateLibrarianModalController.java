/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
public class UpdateLibrarianModalController implements Initializable {

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
    private Button updateBtn;
    @FXML
    private Button deleteBtn;

    private LibrarianService librarianService = new LibrarianService();
    private AlertUtil alertUtil = new AlertUtil();
    private ModalUtil modalUtil = new ModalUtil();

    private Librarian currentLibrarian;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set default values
        roleComboBox.setValue("librarian");
        statusComboBox.setValue("active");

        // Auto-fill librarian data if available
        Librarian librarianData = (Librarian) ModalUtil.getData("librarianToUpdate");
        if (librarianData != null) {
            currentLibrarian = librarianData; // Set the current librarian
            populateFields(librarianData);
            ModalUtil.clearData("librarianToUpdate"); // Clear the data after use
        } else {
            updateStatus("Select a librarian to edit", "#666666");
        }
    }

    public void setLibrarian(Librarian librarian) {
        this.currentLibrarian = librarian;
        populateFields(librarian);
    }

    private void populateFields(Librarian librarian) {
        firstNameField.setText(librarian.getFirstName());
        middleNameField.setText(librarian.getMiddleName());
        lastNameField.setText(librarian.getLastName());
        usernameField.setText(librarian.getUsername());
        emailField.setText(librarian.getEmail());
        // Don't populate password field for security
        roleComboBox.setValue(librarian.getRole());
        statusComboBox.setValue(librarian.getIsActive());

        updateStatus("Edit the fields and click Update to save changes", "#2196F3");
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        if (currentLibrarian == null) {
            alertUtil.error("No librarian selected for editing");
            return;
        }

        // Validate inputs
        if (!validateInputs()) {
            return;
        }

        try {
            // Update librarian object
            currentLibrarian.setFirstName(firstNameField.getText().trim());
            currentLibrarian.setMiddleName(middleNameField.getText().trim());
            currentLibrarian.setLastName(lastNameField.getText().trim());
            currentLibrarian.setUsername(usernameField.getText().trim());
            currentLibrarian.setEmail(emailField.getText().trim());
            currentLibrarian.setRole(roleComboBox.getValue());
            currentLibrarian.setIsActive(statusComboBox.getValue());

            // Update password only if provided
            String newPassword = passwordField.getText();
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                // Note: In a real application, you would hash the password
                // For now, we'll store it as plain text (not recommended for production)
                currentLibrarian.setPassword(newPassword);
            }

            // Attempt to update librarian
            updateStatus("Updating librarian account...", "#FF9800");
            updateBtn.setDisable(true);

            librarianService.update(currentLibrarian);

            updateStatus("✓ Librarian account updated successfully!", "#4CAF50");
            alertUtil.success("Librarian account updated successfully for " + currentLibrarian.getFullName());

            // Auto-close after 2 seconds
            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(2));
            pause.setOnFinished(e -> ModalUtil.closeCurrentModal());
            pause.play();

        } catch (IllegalArgumentException e) {
            updateStatus("Validation error: " + e.getMessage(), "#dc3545");
            alertUtil.error(e.getMessage());
            updateBtn.setDisable(false);
        } catch (Exception e) {
            updateStatus("Failed to update librarian account", "#dc3545");
            alertUtil.error("Failed to update librarian account: " + e.getMessage());
            updateBtn.setDisable(false);
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        if (currentLibrarian == null) {
            alertUtil.error("No librarian selected for deletion");
            return;
        }

        // Prevent deleting super admin accounts
        if ("super_admin".equals(currentLibrarian.getRole())) {
            alertUtil.error("Cannot delete super admin accounts");
            return;
        }

        // Confirmation dialog
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Librarian");
        alert.setHeaderText("Delete Librarian Account");
        alert.setContentText("Are you sure you want to delete the librarian account for " +
                           currentLibrarian.getFullName() + " (" + currentLibrarian.getUsername() + ")?\n\nThis action cannot be undone.");

        if (alert.showAndWait().get() == javafx.scene.control.ButtonType.OK) {
            try {
                // Soft delete - deactivate the account instead of removing it
                currentLibrarian.setIsActive("inactive");
                librarianService.update(currentLibrarian);

                alertUtil.success("Librarian account deleted successfully");
                ModalUtil.closeCurrentModal();

            } catch (Exception e) {
                alertUtil.error("Failed to delete librarian account: " + e.getMessage());
            }
        }
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
