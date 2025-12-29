/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import util.ModalUtil;
import util.DatabaseUtil;
import util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;
import model.Librarian;
import service.LibrarianService;
import java.sql.Statement;

/**
 * FXML Controller class
 *
 * @author Windyl
 */
public class LibrariansManagementController implements Initializable {
    @FXML
    TextField searchField;
    @FXML
    TableView librariansTable;
    @FXML
    TableColumn<Librarian, String> firstnameCol, middlenameCol, lastnameCol, usernameCol, emailCol, roleCol, statusCol;
    @FXML
    TableColumn<Librarian, Date> createdAtCol;
    @FXML
    TableColumn<Librarian, Void> actionsCol;
    @FXML
    ComboBox<String> roleComboBox;
    @FXML
    ComboBox<String> statusComboBox;
    @FXML
    DatePicker datePicker;
    @FXML
    private Label statTotalLibrariansLabel;
    @FXML
    private Label statActiveLibrariansLabel;
    @FXML
    private Label statSuperAdminsLabel;
    @FXML
    private Label statInactiveLibrariansLabel;
    @FXML
    private Button createBtn;

    ObservableList<Librarian> data = FXCollections.observableArrayList();
    ModalUtil modalUtil = new ModalUtil();
    AlertUtil alertUtil = new AlertUtil();
    LibrarianService librarianService = new LibrarianService();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM/dd/yyyy");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        librariansTable.setItems(data);

        roleComboBox.getItems().setAll("ALL", "super_admin", "librarian");
        roleComboBox.setValue("ALL");
        statusComboBox.getItems().setAll("ALL", "active", "inactive");
        statusComboBox.setValue("ALL");

        firstnameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        firstnameCol.setStyle("-fx-alignment: CENTER_LEFT;");
        middlenameCol.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        middlenameCol.setStyle("-fx-alignment: CENTER_LEFT;");
        lastnameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        lastnameCol.setStyle("-fx-alignment: CENTER_LEFT;");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameCol.setStyle("-fx-alignment: CENTER_LEFT;");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setStyle("-fx-alignment: CENTER_LEFT;");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleCol.setStyle("-fx-alignment: CENTER;");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("isActive"));
        statusCol.setStyle("-fx-alignment: CENTER;");
        createdAtCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        createdAtCol.setStyle("-fx-alignment: CENTER;");

        setupActionsColumn();
        try {
            loadTable();
            loadStatistics();
        } catch (SQLException e) {
            System.err.println("Error loading librarians: " + e.getMessage());
        }

        // Responsive column sizing
        firstnameCol.prefWidthProperty().bind(librariansTable.widthProperty().multiply(0.13));
        middlenameCol.prefWidthProperty().bind(librariansTable.widthProperty().multiply(0.10));
        lastnameCol.prefWidthProperty().bind(librariansTable.widthProperty().multiply(0.13));
        usernameCol.prefWidthProperty().bind(librariansTable.widthProperty().multiply(0.12));
        emailCol.prefWidthProperty().bind(librariansTable.widthProperty().multiply(0.18));
        roleCol.prefWidthProperty().bind(librariansTable.widthProperty().multiply(0.10));
        statusCol.prefWidthProperty().bind(librariansTable.widthProperty().multiply(0.08));
        createdAtCol.prefWidthProperty().bind(librariansTable.widthProperty().multiply(0.10));
        actionsCol.prefWidthProperty().bind(librariansTable.widthProperty().multiply(0.06));
    }

    private void setupActionsColumn() {
        actionsCol.setCellFactory(column -> new TableCell<Librarian, Void>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox box = new HBox(5, editBtn, deleteBtn);

            {
                box.setAlignment(Pos.CENTER);
                editBtn.getStyleClass().addAll("btn", "btn-update");
                deleteBtn.getStyleClass().addAll("btn", "btn-danger");

                editBtn.setOnAction(event -> {
                    Librarian librarian = getTableView().getItems().get(getIndex());
                    try {
                        handleEdit(librarian);
                    } catch (IOException e) {
                        System.err.println("Error editing librarian: " + e.getMessage());
                    }
                });

                deleteBtn.setOnAction(event -> {
                    Librarian librarian = getTableView().getItems().get(getIndex());
                    try {
                        handleDelete(librarian);
                    } catch (SQLException e) {
                        System.err.println("Error deleting librarian: " + e.getMessage());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(box);
                }
            }
        });
    }

    @FXML
    public void showAddModal(ActionEvent event) throws IOException {
        ModalUtil.openModal("AddLibrarianModal", "Add Librarian", (controller) -> {
            // Refresh table after modal closes
            try {
                loadTable();
                loadStatistics();
            } catch (SQLException e) {
                System.err.println("Error refreshing table: " + e.getMessage());
            }
        });
    }

    @FXML
    public void handleRoleChange(ActionEvent event) {
        String selectedRole = roleComboBox.getValue();
        try {
            handleRoleFilter(selectedRole);
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    @FXML
    public void handleStatusChange(ActionEvent event) {
        String selectedStatus = statusComboBox.getValue();
        try {
            handleStatusFilter(selectedStatus);
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    @FXML
    public void handleDateChange(ActionEvent event) {
        LocalDate date = datePicker.getValue();
        try {
            handleDateFilter(date);
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    @FXML
    public void handleSearch(KeyEvent event) throws SQLException {
        String symbol = searchField.getText();
        try {
            handleSearchFilter(symbol);
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    public void loadTable() throws SQLException {
        ArrayList<Librarian> librarianList = librarianService.list();
        data.setAll(librarianList);
    }

    public void handleRoleFilter(String role) throws SQLException {
        if (role.equals("ALL")) {
            loadTable();
            return;
        }

        ArrayList<Librarian> librarianList = librarianService.list();
        List<Librarian> filtered = librarianList.stream()
                .filter(librarian -> role.equals(librarian.getRole()))
                .collect(Collectors.toList());
        data.setAll(filtered);
    }

    public void handleStatusFilter(String status) throws SQLException {
        if (status.equals("ALL") || status == null || status.isEmpty()) {
            loadTable();
            return;
        }

        ArrayList<Librarian> librarianList = librarianService.list();
        List<Librarian> filtered = librarianList.stream()
                .filter(librarian -> status.equals(librarian.getIsActive()))
                .collect(Collectors.toList());
        data.setAll(filtered);
    }

    public void handleDateFilter(LocalDate localDate) throws SQLException {
        if (localDate == null) {
            loadTable();
            return;
        }

        ArrayList<Librarian> librarianList = librarianService.list();
        Date filterDate = Date.valueOf(localDate);
        List<Librarian> filtered = librarianList.stream()
                .filter(librarian -> {
                    if (librarian.getCreatedAt() == null) return false;
                    Date createdDate = new Date(librarian.getCreatedAt().getTime());
                    return filterDate.equals(createdDate);
                })
                .collect(Collectors.toList());
        data.setAll(filtered);
    }

    public void handleSearchFilter(String searchTerm) throws SQLException {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            loadTable();
            return;
        }

        ArrayList<Librarian> librarianList = librarianService.list();
        String lowerSearchTerm = searchTerm.toLowerCase();
        List<Librarian> filtered = librarianList.stream()
                .filter(librarian ->
                    (librarian.getFirstName() != null && librarian.getFirstName().toLowerCase().contains(lowerSearchTerm)) ||
                    (librarian.getMiddleName() != null && librarian.getMiddleName().toLowerCase().contains(lowerSearchTerm)) ||
                    (librarian.getLastName() != null && librarian.getLastName().toLowerCase().contains(lowerSearchTerm)) ||
                    (librarian.getUsername() != null && librarian.getUsername().toLowerCase().contains(lowerSearchTerm)) ||
                    (librarian.getEmail() != null && librarian.getEmail().toLowerCase().contains(lowerSearchTerm))
                )
                .collect(Collectors.toList());
        data.setAll(filtered);
    }

    private void handleEdit(Librarian librarian) throws IOException {
        ModalUtil.setData("librarianToUpdate", librarian);
        ModalUtil.openModal("UpdateLibrarianModal", "Update Librarian", (controller) -> {
            // Refresh table after modal closes
            try {
                loadTable();
                loadStatistics();
            } catch (SQLException e) {
                System.err.println("Error refreshing table: " + e.getMessage());
            }
        });
    }

    private void handleDelete(Librarian librarian) throws SQLException {
        // Prevent deactivating super admin accounts
        if ("super_admin".equals(librarian.getRole())) {
            alertUtil.error("Cannot deactivate super admin accounts");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Librarian");
        alert.setHeaderText("Delete Librarian Account");
        alert.setContentText("Are you sure you want to delete the librarian account for " +
                           librarian.getFullName() + " (" + librarian.getUsername() + ")?\n\nThis action cannot be undone.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                // Soft delete - deactivate the account instead of removing it
                librarian.setIsActive("inactive");
                if (librarianService.update(librarian)) {
                    alertUtil.success("Librarian account deactivated successfully");
                    loadTable();
                    loadStatistics();
                } else {
                    alertUtil.error("Failed to deactivate librarian account");
                }
            } catch (SQLException e) {
                alertUtil.error("Error deactivating librarian account: " + e.getMessage());
            }
        }
    }

    private void loadStatistics() {
        try {
            DatabaseUtil dbUtil = new DatabaseUtil();
            Connection conn = dbUtil.connect();
            if (conn == null) return;

            Statement stmt = conn.createStatement();

            // Total librarians
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM librarian");
            if (rs.next()) {
                statTotalLibrariansLabel.setText(String.valueOf(rs.getInt("count")));
            }

            // Active librarians
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM librarian WHERE isActive = 'active'");
            if (rs.next()) {
                statActiveLibrariansLabel.setText(String.valueOf(rs.getInt("count")));
            }

            // Super admins
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM librarian WHERE role = 'super_admin'");
            if (rs.next()) {
                statSuperAdminsLabel.setText(String.valueOf(rs.getInt("count")));
            }

            // Inactive librarians
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM librarian WHERE isActive = 'inactive'");
            if (rs.next()) {
                statInactiveLibrariansLabel.setText(String.valueOf(rs.getInt("count")));
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            System.err.println("Error loading statistics: " + e.getMessage());
        }
    }
}
