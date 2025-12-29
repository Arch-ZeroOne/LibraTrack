package dao;

import model.Librarian;
import util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibrarianDao {
    private final DatabaseUtil dbUtil = new DatabaseUtil();
    private final Connection connection = dbUtil.connect();

    public ArrayList<Librarian> list() throws SQLException {
        String query = "SELECT * FROM librarian ORDER BY created_at DESC";
        PreparedStatement ps = connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        ArrayList<Librarian> librarians = new ArrayList<>();
        while (rs.next()) {
            Librarian librarian = new Librarian(
                rs.getInt("librarian_id"),
                rs.getString("firstName"),
                rs.getString("middleName"),
                rs.getString("lastName"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("role") != null ? rs.getString("role") : "librarian", // Default to librarian if no role
                rs.getString("isActive"),
                rs.getTimestamp("created_at")
            );
            librarians.add(librarian);
        }
        rs.close();
        ps.close();
        return librarians;
    }

    public boolean insert(Librarian librarian) throws SQLException {
        String query = "INSERT INTO librarian (firstName, middleName, lastName, username, email, password, role, isActive) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, librarian.getFirstName());
        ps.setString(2, librarian.getMiddleName());
        ps.setString(3, librarian.getLastName());
        ps.setString(4, librarian.getUsername());
        ps.setString(5, librarian.getEmail());
        ps.setString(6, librarian.getPassword());
        ps.setString(7, librarian.getRole());
        ps.setString(8, librarian.getIsActive());

        int rowsAffected = ps.executeUpdate();
        ps.close();
        return rowsAffected > 0;
    }

    public boolean update(Librarian librarian) throws SQLException {
        StringBuilder query = new StringBuilder("UPDATE librarian SET firstName=?, middleName=?, lastName=?, username=?, email=?, role=?, isActive=?");
        boolean updatePassword = librarian.getPassword() != null && !librarian.getPassword().trim().isEmpty();

        if (updatePassword) {
            query.append(", password=?");
        }
        query.append(" WHERE librarian_id=?");

        PreparedStatement ps = connection.prepareStatement(query.toString());
        ps.setString(1, librarian.getFirstName());
        ps.setString(2, librarian.getMiddleName());
        ps.setString(3, librarian.getLastName());
        ps.setString(4, librarian.getUsername());
        ps.setString(5, librarian.getEmail());
        ps.setString(6, librarian.getRole());
        ps.setString(7, librarian.getIsActive());

        int paramIndex = 8;
        if (updatePassword) {
            ps.setString(paramIndex++, librarian.getPassword());
        }
        ps.setInt(paramIndex, librarian.getLibrarianId());

        int rowsAffected = ps.executeUpdate();
        ps.close();
        return rowsAffected > 0;
    }

    public boolean delete(int librarianId) throws SQLException {
        String query = "DELETE FROM librarian WHERE librarian_id=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, librarianId);

        int rowsAffected = ps.executeUpdate();
        ps.close();
        return rowsAffected > 0;
    }

    public Librarian getById(int librarianId) throws SQLException {
        String query = "SELECT * FROM librarian WHERE librarian_id=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, librarianId);
        ResultSet rs = ps.executeQuery();

        Librarian librarian = null;
        if (rs.next()) {
            librarian = new Librarian(
                rs.getInt("librarian_id"),
                rs.getString("firstName"),
                rs.getString("middleName"),
                rs.getString("lastName"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("role") != null ? rs.getString("role") : "librarian",
                rs.getString("isActive"),
                rs.getTimestamp("created_at")
            );
        }
        rs.close();
        ps.close();
        return librarian;
    }

    public Librarian authenticate(String username, String password) throws SQLException {
        String query = "SELECT * FROM librarian WHERE username=? AND password=? AND isActive='active'";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();

        Librarian librarian = null;
        if (rs.next()) {
            librarian = new Librarian(
                rs.getInt("librarian_id"),
                rs.getString("firstName"),
                rs.getString("middleName"),
                rs.getString("lastName"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("role") != null ? rs.getString("role") : "librarian",
                rs.getString("isActive"),
                rs.getTimestamp("created_at")
            );
        }
        rs.close();
        ps.close();
        return librarian;
    }

    public boolean isUsernameTaken(String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM librarian WHERE username=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        rs.next();
        boolean taken = rs.getInt(1) > 0;
        rs.close();
        ps.close();
        return taken;
    }

    public boolean isEmailTaken(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM librarian WHERE email=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        rs.next();
        boolean taken = rs.getInt(1) > 0;
        rs.close();
        ps.close();
        return taken;
    }
}
