package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class StatisticsViewController implements Initializable {

    @FXML
    private Label totalStudentsLabel;
    @FXML
    private Label totalBooksLabel;
    @FXML
    private Label totalBorrowedLabel;
    @FXML
    private Label totalGenresLabel;

    // Database connection constants
    private final String DB_URL = "jdbc:mysql://localhost:3306/libratrack_qr_barcode";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadDashboardTotals();
    }

    private void loadDashboardTotals() {
        String query = "SELECT * FROM library_dashboard_totals"; // Using the view we created

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                totalBooksLabel.setText(String.valueOf(rs.getInt("total_books")));
                totalStudentsLabel.setText(String.valueOf(rs.getInt("total_students")));
                totalBorrowedLabel.setText(String.valueOf(rs.getInt("total_borrowed_books")));
                totalGenresLabel.setText(String.valueOf(rs.getInt("total_genres")));
            }

        } catch (Exception e) {
            e.printStackTrace();
            totalBooksLabel.setText("0");
            totalStudentsLabel.setText("0");
            totalBorrowedLabel.setText("0");
            totalGenresLabel.setText("0");
        }
    }
}
