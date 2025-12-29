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
import model.Log;
import service.LogService;
import service.StudentService;
import java.sql.Statement;
import model.Student;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * FXML Controller class
 *
 * @author Windyl
 */
public class LogsViewController implements Initializable {
    @FXML
    TextField searchField;
    @FXML
    TableView logTable;
    @FXML
    TableColumn<Log, String> firstnameCol, middlenameCol, lastnameCol, schoolIdCol, courseCol;
    @FXML
    TableColumn<Log, Date> logDateCol;
    @FXML
    TableColumn<Log, Void> actionsCol;
    @FXML
    ComboBox<String> courseComboBox;
    @FXML
    DatePicker datePicker;
    @FXML
    private Label totalLogsLabel;
    @FXML
    private Button createBtn;
    @FXML
    private Label statTotalLogsLabel;
    @FXML
    private Label statTodayLogsLabel;
    @FXML
    private Label statThisMonthLogsLabel;
    @FXML
    private Label statActiveStudentsLabel;

ObservableList<Log> data = FXCollections.observableArrayList();
ModalUtil modalUtil = new ModalUtil();
AlertUtil alertUtil = new AlertUtil();
LogService logService = new LogService();
    StudentService studentService = new StudentService();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM/d/yyyy");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        logTable.setItems(data);
        loadStatistics();

        // Set default date to today
        datePicker.setValue(LocalDate.now());

        courseComboBox.getItems().setAll("ALL", "BSIT", "BSBA", "BSA", "BTLED");
        courseComboBox.setValue("ALL");

        firstnameCol.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        firstnameCol.setStyle("-fx-alignment: CENTER_LEFT;");
        middlenameCol.setCellValueFactory(new PropertyValueFactory<>("middlename"));
        middlenameCol.setStyle("-fx-alignment: CENTER_LEFT;");
        lastnameCol.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        lastnameCol.setStyle("-fx-alignment: CENTER_LEFT;");
        schoolIdCol.setCellValueFactory(new PropertyValueFactory<>("school_id"));
        schoolIdCol.setStyle("-fx-alignment: CENTER;");
        logDateCol.setCellValueFactory(new PropertyValueFactory<>("log_date"));
        logDateCol.setStyle("-fx-alignment: CENTER;");

        // Add course column
        courseCol.setCellValueFactory(cellData -> {
            String schoolId = cellData.getValue().getSchool_id();
            try {
                Student student = studentService.search(schoolId);
                return new javafx.beans.property.SimpleStringProperty(student != null ? student.getCourse() : "N/A");
            } catch (SQLException e) {
                return new javafx.beans.property.SimpleStringProperty("N/A");
            }
        });
        courseCol.setStyle("-fx-alignment: CENTER;");

        setupActionsColumn();
        try {
            loadTable();
        } catch (SQLException e) {
            System.err.println("Error loading logs: " + e.getMessage());
        }

        // Responsive column sizing
        firstnameCol.prefWidthProperty().bind(logTable.widthProperty().multiply(0.18));
        middlenameCol.prefWidthProperty().bind(logTable.widthProperty().multiply(0.15));
        lastnameCol.prefWidthProperty().bind(logTable.widthProperty().multiply(0.18));
        schoolIdCol.prefWidthProperty().bind(logTable.widthProperty().multiply(0.15));
        courseCol.prefWidthProperty().bind(logTable.widthProperty().multiply(0.12));
        logDateCol.prefWidthProperty().bind(logTable.widthProperty().multiply(0.12));
        actionsCol.prefWidthProperty().bind(logTable.widthProperty().multiply(0.10));
    }

    private void setupActionsColumn() {
        actionsCol.setCellFactory(column -> new TableCell<Log, Void>() {
            private final Button deleteBtn = new Button("Delete");
            private final HBox box = new HBox(5, deleteBtn);

            {
                box.setAlignment(Pos.CENTER);
                deleteBtn.getStyleClass().addAll("btn", "btn-danger");
                deleteBtn.setOnAction(event -> {
                    Log log = getTableView().getItems().get(getIndex());
                    try {
                        handleDelete(log);
                    } catch (SQLException e) {
                        System.err.println("Error deleting log: " + e.getMessage());
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
    public void showAddModal(ActionEvent event) throws IOException, SQLException {
        ModalUtil.openModal("StudentAttendanceModal", "Log Student Attendance", (controller) -> {
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
    public void handleCourseChange(ActionEvent event) {
        String selectedCourse = courseComboBox.getValue();
        try {
            handleCourseFilter(selectedCourse);
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
        ArrayList<Log> logList = logService.list();
        // Automatically clears old data and loads to the table
        data.setAll(logList);
    }

    public void handleCourseFilter(String course) throws SQLException {
        if (course.equals("ALL")) {
            loadTable();
            return;
        }

        ArrayList<Log> logList = logService.list();
        List<Log> filtered = logList.stream()
                .filter(log -> {
                    try {
                        Student student = studentService.search(log.getSchool_id());
                        return student != null && course.equals(student.getCourse());
                    } catch (SQLException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
        data.setAll(filtered);
    }

    public void handleDateFilter(LocalDate localDate) throws SQLException {
        if (localDate == null) {
            loadTable();
            return;
        }

        ArrayList<Log> logList = logService.list();
        Date filterDate = Date.valueOf(localDate);
        List<Log> filtered = logList.stream()
                .filter(log -> filterDate.equals(log.getLog_date()))
                .collect(Collectors.toList());
        data.setAll(filtered);
    }

    public void handleSearchFilter(String searchTerm) throws SQLException {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            loadTable();
            return;
        }

        ArrayList<Log> logList = logService.list();
        String lowerSearchTerm = searchTerm.toLowerCase();
        List<Log> filtered = logList.stream()
                .filter(log ->
                    (log.getFirstname() != null && log.getFirstname().toLowerCase().contains(lowerSearchTerm)) ||
                    (log.getMiddlename() != null && log.getMiddlename().toLowerCase().contains(lowerSearchTerm)) ||
                    (log.getLastname() != null && log.getLastname().toLowerCase().contains(lowerSearchTerm)) ||
                    (log.getSchool_id() != null && log.getSchool_id().toLowerCase().contains(lowerSearchTerm))
                )
                .collect(Collectors.toList());
        data.setAll(filtered);
    }

    private void handleDelete(Log log) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Log");
        alert.setHeaderText("Delete Log Entry");
        alert.setContentText("Are you sure you want to delete this log entry for " +
                           log.getFirstname() + " " + log.getLastname() + "?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            if (logService.delete(log.getLog_id())) {
                alertUtil.success("Log entry deleted successfully");
                loadTable();
                loadStatistics();
            } else {
                alertUtil.error("Failed to delete log entry");
            }
        }
    }

    private void loadStatistics() {
        try {
            DatabaseUtil dbUtil = new DatabaseUtil();
            Connection conn = dbUtil.connect();
            if (conn == null) return;

            Statement stmt = conn.createStatement();

            // Total logs
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM log");
            if (rs.next()) {
                statTotalLogsLabel.setText(String.valueOf(rs.getInt("count")));
            }

            // Today's logs
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM log WHERE DATE(log_date) = CURDATE()");
            if (rs.next()) {
                statTodayLogsLabel.setText(String.valueOf(rs.getInt("count")));
            }

            // This month's logs
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM log WHERE MONTH(log_date) = MONTH(CURDATE()) AND YEAR(log_date) = YEAR(CURDATE())");
            if (rs.next()) {
                statThisMonthLogsLabel.setText(String.valueOf(rs.getInt("count")));
            }

            // Active students count
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM student WHERE isActive = 'active'");
            if (rs.next()) {
                statActiveStudentsLabel.setText(String.valueOf(rs.getInt("count")));
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            System.err.println("Error loading statistics: " + e.getMessage());
        }
    }
}
