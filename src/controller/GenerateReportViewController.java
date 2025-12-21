package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import util.DatabaseUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class GenerateReportViewController implements Initializable {

    @FXML
    private Button generateBtn;

    private final DatabaseUtil dbUtil = new DatabaseUtil();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }

    @FXML
    private void handleGenerateReport() {
        try (Connection conn = dbUtil.connect()) {

            exportBooks(conn);
            exportStudents(conn);
            exportLogs(conn);
            exportBorrow(conn);

            // Success Alert with file path
            File reportsFolder = new File("reports");
            String absolutePath = reportsFolder.getAbsolutePath();
            
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Reports Generated Successfully");
            successAlert.setHeaderText("All Excel Reports Created!");
            successAlert.setContentText(
                "The following reports have been generated:\n\n" +
                "• Books.xlsx\n" +
                "• Students.xlsx\n" +
                "• Logs.xlsx\n" +
                "• Borrow.xlsx\n\n" +
                "Location: " + absolutePath
            );
            successAlert.showAndWait();

            System.out.println("ALL EXCEL REPORTS CREATED IN /reports FOLDER!");

        } catch (Exception e) {
            e.printStackTrace();
            
            // Error Alert
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Report Generation Failed");
            errorAlert.setHeaderText("An Error Occurred");
            errorAlert.setContentText(
                "Failed to generate reports.\n\n" +
                "Error: " + e.getMessage() + "\n\n" +
                "Please check the console for more details."
            );
            errorAlert.showAndWait();
        }
    }

    // =====================================================================
    //                     EXPORT BOOKS TO Books.xlsx
    // =====================================================================
    private void exportBooks(Connection conn) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Books");

        String[] cols = {
                "Accession Number", "Title", "Author", "Genre",
                "Publisher", "Publication Date", "ISBN",
                "Is Available", "Copies"
        };

        String[] dbCols = {
                "accession_number", "title", "author", "genre",
                "publisher", "publication_date", "isbn",
                "isAvailable", "copies"
        };

        createHeader(sheet, cols);
        populateSheet(sheet, conn, "SELECT * FROM book", cols.length, dbCols);
        autoSize(sheet, cols.length);
        saveWorkbook(workbook, "Books.xlsx");
    }

    // =====================================================================
    //                    EXPORT STUDENTS TO Students.xlsx
    // =====================================================================
    private void exportStudents(Connection conn) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Students");

        String[] cols = {
                "Student ID", "Firstname", "Middlename", "Lastname",
                "Is Active", "School ID", "Course", "Created At"
        };

        String[] dbCols = {
                "student_id", "firstname", "middlename", "lastname",
                "isActive", "school_id", "course", "createdAt"
        };

        createHeader(sheet, cols);
        populateSheet(sheet, conn, "SELECT * FROM student", cols.length, dbCols);
        autoSize(sheet, cols.length);
        saveWorkbook(workbook, "Students.xlsx");
    }

    // =====================================================================
    //                       EXPORT LOGS TO Logs.xlsx
    // =====================================================================
    private void exportLogs(Connection conn) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Logs");

        String[] cols = {
                "Log ID", "Firstname", "Middlename", "Lastname",
                "Log Date", "School ID", "Student ID"
        };

        String[] dbCols = {
                "log_id", "firstname", "middlename", "lastname",
                "log_date", "school_id", "student_id"
        };

        createHeader(sheet, cols);
        populateSheet(sheet, conn, "SELECT * FROM log", cols.length, dbCols);
        autoSize(sheet, cols.length);
        saveWorkbook(workbook, "Logs.xlsx");
    }

    // =====================================================================
    //                     EXPORT BORROW TO Borrow.xlsx
    // =====================================================================
    private void exportBorrow(Connection conn) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Borrow");

        String[] cols = {
                "Borrow ID", "Borrow Date", "Due Date",
                "Return Date", "Student ID"
        };

        String[] dbCols = {
                "borrow_id", "borrow_date", "due_date",
                "return_date", "student_id"
        };

        createHeader(sheet, cols);
        populateSheet(sheet, conn, "SELECT * FROM borrow", cols.length, dbCols);
        autoSize(sheet, cols.length);
        saveWorkbook(workbook, "Borrow.xlsx");
    }

    // =====================================================================
    //                          HELPER FUNCTIONS
    // =====================================================================

    private void createHeader(Sheet sheet, String[] columns) {
        Row header = sheet.createRow(0);

        CellStyle style = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        style.setFont(font);

        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(style);
        }
    }

    private void populateSheet(Sheet sheet, Connection conn, String query, int colCount, String[] dbCols)
            throws SQLException {

        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        int rowIndex = 1;
        while (rs.next()) {
            Row row = sheet.createRow(rowIndex++);
            for (int i = 0; i < colCount; i++) {
                row.createCell(i).setCellValue(rs.getString(dbCols[i]));
            }
        }
    }

    private void autoSize(Sheet sheet, int colCount) {
        for (int i = 0; i < colCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void saveWorkbook(XSSFWorkbook workbook, String fileName) throws Exception {
        File folder = new File("reports");
        if (!folder.exists()) folder.mkdirs();

        File file = new File(folder, fileName);
        try (FileOutputStream out = new FileOutputStream(file)) {
            workbook.write(out);
        }

        workbook.close();

        System.out.println("FILE GENERATED: " + file.getAbsolutePath());
    }
}