/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import java.io.IOException;
import model.Librarian;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import service.LibrarianService;
import util.AlertUtil;
import util.WindowUtil;
/**
 * FXML Controller class
 *
 * @author Windyl
 */
public class LoginViewController implements Initializable {
    private LibrarianService librarianService = new LibrarianService();
    private AlertUtil alert_util = new AlertUtil();
    public WindowUtil window_util = new WindowUtil();
    
    @FXML
    TextField username;
    @FXML
    PasswordField password;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void validate(ActionEvent event) throws SQLException, IOException {
        String user_name = username.getText();
        String pass_word = password.getText();

        // Authenticate librarian
        Librarian librarian = librarianService.authenticate(user_name, pass_word);

        if (librarian != null) {
            alert_util.success("Welcome back, " + librarian.getFullName() + "!");

            // Redirect based on role
            if ("super_admin".equals(librarian.getRole())) {
                window_util.transfer("SuperAdminDashboard.fxml", event);
            } else {
                window_util.transfer("DashboardLayout.fxml", event);
            }
            return;
        }

        // Check if user exists but is deactivated
        Librarian existingUser = getUserByUsername(user_name);
        if (existingUser != null && "inactive".equals(existingUser.getIsActive())) {
            alert_util.error("Account deactivated. Please contact administrator.");
            return;
        }

        alert_util.error("Invalid username or password");

    }

    private Librarian getUserByUsername(String username) throws SQLException {
        // Use raw SQL to check user existence regardless of active status
        String query = "SELECT * FROM librarian WHERE username=?";
        try {
            java.sql.Connection conn = new util.DatabaseUtil().connect();
            java.sql.PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, username);
            java.sql.ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Librarian librarian = new Librarian(
                    rs.getInt("librarian_id"),
                    rs.getString("firstName"),
                    rs.getString("middleName"),
                    rs.getString("lastName"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("role"),
                    rs.getString("isActive"),
                    rs.getTimestamp("created_at")
                );
                // Set password separately since constructor doesn't include it
                librarian.setPassword(rs.getString("password"));

                rs.close();
                ps.close();
                conn.close();
                return librarian;
            }

            rs.close();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            System.err.println("Error checking user by username: " + e.getMessage());
        }
        return null;
    }

    
}
