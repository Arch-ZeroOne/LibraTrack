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
import model.Student;
import service.StudentService;
import java.sql.Statement;


/**
 * FXML Controller class
 *
 * @author Windyl
 */
public class StudentsViewController implements Initializable {
    @FXML
    TextField searchField;
    @FXML
    TableView studentTable;
    @FXML
    TableColumn<Student,String> idCol,firstnameCol,middlenameCol,lastnameCol,courseCol,statusCol;
    @FXML
    TableColumn<Student,Void> actionsCol;
    @FXML
    ComboBox<String> courseComboBox;
    @FXML
    ComboBox<String> statusComboBox;
    @FXML
    DatePicker datePicker;
    ObservableList<Student> data = FXCollections.observableArrayList();
    ModalUtil util = new ModalUtil();
    StudentService service = new StudentService();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM/d/yyyy");
    @FXML
    private Label totalStudentsLabel;
    @FXML
    private Button createBtn;
    @FXML
    private Label statTotalStudentsLabel;
    @FXML
    private Label statActiveLabel;
    @FXML
    private Label statInactiveLabel;
    @FXML
    private Label statProgramsLabel;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        studentTable.setItems(data);
        loadStatistics();
        courseComboBox.getItems().setAll("ALL","BSIT","BSBA","BSA","BTLED");
        courseComboBox.setValue("ALL");
        statusComboBox.getItems().setAll("ALL","active","inactive");
        statusComboBox.setValue("ALL");
        idCol.setCellValueFactory(new PropertyValueFactory<>("school_id"));
        idCol.setStyle("-fx-alignment: CENTER;");
        firstnameCol.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        firstnameCol.setStyle("-fx-alignment: CENTER_LEFT;");
        middlenameCol.setCellValueFactory(new PropertyValueFactory<>("middlename"));
        middlenameCol.setStyle("-fx-alignment: CENTER_LEFT;");
        lastnameCol.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        lastnameCol.setStyle("-fx-alignment: CENTER_LEFT;");
        courseCol.setCellValueFactory(new PropertyValueFactory<>("course"));
        courseCol.setStyle("-fx-alignment: CENTER;");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("isActive"));
        statusCol.setStyle("-fx-alignment: CENTER;");

        // Make columns responsive
        studentTable.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            if (newWidth != null && newWidth.doubleValue() > 0) {
                double totalWidth = newWidth.doubleValue() - 20; // Account for padding
                idCol.setPrefWidth(totalWidth * 0.15);           // 15%
                firstnameCol.setPrefWidth(totalWidth * 0.18);    // 18%
                middlenameCol.setPrefWidth(totalWidth * 0.18);   // 18%
                lastnameCol.setPrefWidth(totalWidth * 0.18);     // 18%
                courseCol.setPrefWidth(totalWidth * 0.13);       // 13%
                statusCol.setPrefWidth(totalWidth * 0.10);       // 10%
                actionsCol.setPrefWidth(totalWidth * 0.25);      // 25%
            }
        });

        // Setup actions column
        setupActionsColumn();
        
        datePicker.setConverter(new StringConverter<LocalDate>(){
          @Override
          public String toString(LocalDate date){
              if(date != null){
                  return formatter.format(date);
                  
              }
              
              return null;
          }
          
          @Override
          public LocalDate fromString(String dateString){
              if(!dateString.isEmpty()){
                  return LocalDate.parse(dateString,formatter);
                  
              }
              
              return null;
          }
        });
        
        
        datePicker.setValue(LocalDate.now());
        try{ loadTable(); } catch(SQLException error) { error.printStackTrace(); };

    }

    private void setupActionsColumn() {
        actionsCol.setCellFactory(column -> new TableCell<>() {
            private final Button updateBtn = new Button("Update");
            private final Button deleteBtn = new Button("Delete");
            private final Button viewBtn = new Button("View");
            private final HBox box = new HBox(6, viewBtn, updateBtn, deleteBtn);

            {
                // Enhanced button styling with icons and better colors
                viewBtn.getStyleClass().addAll("btn", "btn-info");
                viewBtn.setPrefWidth(65);
                updateBtn.getStyleClass().addAll("btn", "btn-update");
                updateBtn.setPrefWidth(70);
                deleteBtn.getStyleClass().addAll("btn", "btn-delete");
                deleteBtn.setPrefWidth(70);

                // Add icons to buttons (using FontAwesome)
                viewBtn.setStyle(viewBtn.getStyle() + ";-fx-graphic: url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTYiIGhlaWdodD0iMTYiIHZpZXdCb3g9IjAgMCAyNCAyNCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHBhdGggZD0iTTEyIDJDMTMuMSAyIDE0IDIuOSAxNCA0VjE2QzE0IDE3LjEgMTMuMSAxOCA5IDE4QzQuOSAxOCA0IDE3LjEgNCAxNlY0QzQgMi45IDQuOSAyIDYgMkg5VjBIMTBaTTkgMTJDOSAxMi43NSA5LjI1IDEzLjUgOC41IDEzLjVDNy43NSAxMy41IDcgMTIuNzUgNyAxMkM3IDExLjI1IDcuNzUgMTAuNSA4LjUgMTAuNUM5LjI1IDEwLjUgOSAxMS4yNSA5IDEyWk0xMiAxNkMxMiAxNC45MSAxMS4wOSA0IDEwIDRWMTZIMTJWMTRIMTBWMTJIMTJWMTZaIiBmaWxsPSJ3aGl0ZSIvPgo8L3N2Zz4K'); -fx-content-display: left;");
                updateBtn.setStyle(updateBtn.getStyle() + ";-fx-graphic: url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTYiIGhlaWdodD0iMTYiIHZpZXdCb3g9IjAgMCAyNCAyNCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHBhdGggZD0iTTE5IDhIMTVWMTRIMTlWOFoiIGZpbGw9IndoaXRlIi8+CjxwYXRoIGQ9Ik0xNCAxMEgxMFYxNkgxNFYxMFoiIGZpbGw9IndoaXRlIi8+CjxwYXRoIGQ9Ik0xNiA0VjJIMThWMkgMjBWNFY2SDE4VjE0SDE2VjRaIiBmaWxsPSJ3aGl0ZSIvPgo8L3N2Zz4K'); -fx-content-display: left;");
                deleteBtn.setStyle(deleteBtn.getStyle() + ";-fx-graphic: url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTYiIGhlaWdodD0iMTYiIHZpZXdCb3g9IjAgMCAyNCAyNCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHBhdGggZD0iTTkgMkgxN1YyI5MTcgNFYxMEgxNVY0SDlWMloiIGZpbGw9IndoaXRlIi8+CjxwYXRoIGQ9Ik0xMyAxMkgxMVYxNkgxM1YxMFoiIGZpbGw9IndoaXRlIi8+CjxwYXRoIGQ9Ik0xOCAxNkg2VjE4SDE4VjE2Wk0xNiAxNFYyMEgxOFYyMFoiIGZpbGw9IndoaXRlIi8+Cjwvc3ZnPgo='); -fx-content-display: left;");

                box.setAlignment(Pos.CENTER);
                box.setStyle("-fx-background-color: rgba(248, 249, 250, 0.8); -fx-background-radius: 8; -fx-padding: 4;");

                viewBtn.setOnAction(event -> {
                    Student student = getTableView().getItems().get(getIndex());
                    handleViewStudent(student);
                });

                updateBtn.setOnAction(event -> {
                    try {
                        Student student = getTableView().getItems().get(getIndex());
                        handleUpdate(student);
                    } catch (IOException e) {
                        System.err.println("Error opening update modal: " + e.getMessage());
                    }
                });

                deleteBtn.setOnAction(event -> {
                    Student student = getTableView().getItems().get(getIndex());
                    handleDelete(student);
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
     public void showAddModal(ActionEvent event) throws IOException,SQLException{
        ModalUtil.openModal("AddStudentModal", "Add Student", (controller) -> {
            // Refresh table after modal closes
            try {
                loadTable();
                loadStatistics();
            } catch (SQLException e) {
                System.err.println("Error refreshing table: " + e.getMessage());
            }
        });
        loadTable();

    }
    
     
    @FXML
    public void handleCourseChange(ActionEvent event){
        String selectedCourse = courseComboBox.getValue();
        try{ handleCourseFilter(selectedCourse); }catch(SQLException error){ error.printStackTrace();};

    }

    @FXML
    public void handleStatusChange(ActionEvent event){
        String selectedStatus = statusComboBox.getValue();
        try{ handleStatusFilter(selectedStatus); }catch(SQLException error){ error.printStackTrace();};

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
    
    public void loadTable() throws SQLException{
       
        ArrayList<Student> book_list = service.list();
        //Automatically clears old data and loads to the table
        data.setAll(book_list);
        
        
    }
    
     public void handleCourseFilter(String course) throws SQLException{

       if(course.equals("ALL")){
            loadTable();
             return;
        }

        ArrayList<Student> student_list= service.list();
        List<Student> filtered = student_list.stream().
                                     filter(student -> student.getCourse()
                                     .equals(course)).collect(Collectors.toList());
        data.setAll(filtered);


    }

    public void handleStatusFilter(String status) throws SQLException{

       if(status.equals("ALL") || status == null || status.isEmpty()){
            loadTable();
             return;
        }

        ArrayList<Student> student_list= service.list();
        List<Student> filtered = student_list.stream().
                                     filter(student -> student.getIsActive()
                                     .equals(status)).collect(Collectors.toList());
        data.setAll(filtered);


    }
     
    public void handleDateFilter(LocalDate localDate) throws SQLException{
        
       if(localDate.equals(LocalDate.now())){
            loadTable();
            loadStatistics();
             return;
        }
       
        
        ArrayList<Student> student_list = service.list();
        List<Student> filtered = student_list.stream().
                                     filter(student -> student.getCreatedAt().toLocalDate()
                                     .equals(localDate)).collect(Collectors.toList());
        data.setAll(filtered);
        
        
    }
       public void handleSearchFilter(String symbol) throws SQLException{
        
         if(symbol.isEmpty()){
             loadTable();
             return;
             
         }
       
        
        ArrayList<Student> student_list = service.list();
        List<Student> filtered = student_list.stream().
                                     filter(student -> student.getFirstname().contains(symbol)
                                         || student.getMiddlename().contains(symbol)
                                         || student.getLastname().contains(symbol) 
                                         || student.getCourse().contains(symbol)
                                         || student.getSchoolId().contains(symbol)
                                                 )
                                    .collect(Collectors.toList());
        
        data.setAll(filtered);
        
        
    }
    private void loadStatistics() {
        util.DatabaseUtil dbUtil = new util.DatabaseUtil();
        Connection conn = dbUtil.connect();
        if (conn == null) {
            return;
        }

        try (Statement stmt = conn.createStatement()) {

        // Total students
        ResultSet rsTotal = stmt.executeQuery("SELECT COUNT(*) AS total FROM student");
        if (rsTotal.next()) statTotalStudentsLabel.setText(rsTotal.getString("total"));

        // Active students
        ResultSet rsActive = stmt.executeQuery("SELECT COUNT(*) AS total FROM student WHERE isActive = 'active'");
        if (rsActive.next()) statActiveLabel.setText(rsActive.getString("total"));

        // Inactive students
        ResultSet rsInactive = stmt.executeQuery("SELECT COUNT(*) AS total FROM student WHERE isActive = 'inactive'");
        if (rsInactive.next()) statInactiveLabel.setText(rsInactive.getString("total"));

        // Total programs / distinct courses
        ResultSet rsPrograms = stmt.executeQuery("SELECT COUNT(DISTINCT course) AS total FROM student");
        if (rsPrograms.next()) statProgramsLabel.setText(rsPrograms.getString("total"));

    } catch (SQLException e) {
        e.printStackTrace();
        statTotalStudentsLabel.setText("0");
        statActiveLabel.setText("0");
        statInactiveLabel.setText("0");
        statProgramsLabel.setText("0");
    } finally {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            // Ignore
        }
    }
    }

    private void handleViewStudent(Student student) {
        // Show student details in an information dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Student Details");
        alert.setHeaderText("Detailed Information for " + student.getFirstname() + " " + student.getLastname());

        String content = String.format(
            "Student ID: %s\n\n" +
            "Name: %s %s %s\n\n" +
            "Course/Program: %s\n\n" +
            "Status: %s\n\n" +
            "Registration Date: %s",
            student.getSchool_id(),
            student.getFirstname(),
            student.getMiddlename(),
            student.getLastname(),
            student.getCourse(),
            student.getIsActive(),
            student.getCreatedAt() != null ? student.getCreatedAt().toString() : "N/A"
        );

        alert.setContentText(content);
        alert.setResizable(true);
        alert.getDialogPane().setPrefWidth(400);
        alert.showAndWait();
    }

    private void handleUpdate(Student student) throws IOException {
        // Pass student data to update modal
        ModalUtil.setData("studentToUpdate", student);
        ModalUtil.openModal("../view/UpdateStudentView", "Update Student", (controller) -> {
            // Refresh table after modal closes
            try {
                loadTable();
                loadStatistics();
            } catch (SQLException e) {
                System.err.println("Error refreshing table: " + e.getMessage());
            }
        });
    }

    private void handleDelete(Student student) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Soft Delete Student");
        alert.setContentText("Are you sure you want to deactivate this student? This will set their status to 'inactive'.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Perform soft delete by setting status to inactive
                    service.remove(student.getSchool_id(), "inactive");
                    loadTable();
                    loadStatistics();
                    System.out.println("Student " + student.getSchool_id() + " has been deactivated");
                } catch (SQLException e) {
                    System.err.println("Error deactivating student: " + e.getMessage());
                }
            }
        });
    }


}
