package service;

import dao.LibrarianDao;
import model.Librarian;
import java.sql.SQLException;
import java.util.ArrayList;

public class LibrarianService {
    private LibrarianDao dao = new LibrarianDao();

    public ArrayList<Librarian> list() throws SQLException {
        return dao.list();
    }

    public boolean insert(Librarian librarian) throws SQLException {
        // Validate required fields
        if (librarian.getFirstName() == null || librarian.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (librarian.getLastName() == null || librarian.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (librarian.getUsername() == null || librarian.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (librarian.getEmail() == null || librarian.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (librarian.getPassword() == null || librarian.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        // Check for duplicates
        if (dao.isUsernameTaken(librarian.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (dao.isEmailTaken(librarian.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Set default role if not specified
        if (librarian.getRole() == null || librarian.getRole().trim().isEmpty()) {
            librarian.setRole("librarian");
        }

        return dao.insert(librarian);
    }

    public boolean update(Librarian librarian) throws SQLException {
        // Validate required fields
        if (librarian.getFirstName() == null || librarian.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (librarian.getLastName() == null || librarian.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (librarian.getUsername() == null || librarian.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (librarian.getEmail() == null || librarian.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        // Check for duplicates (excluding current user)
        Librarian existing = dao.getById(librarian.getLibrarianId());
        if (existing != null) {
            if (!existing.getUsername().equals(librarian.getUsername()) && dao.isUsernameTaken(librarian.getUsername())) {
                throw new IllegalArgumentException("Username already exists");
            }
            if (!existing.getEmail().equals(librarian.getEmail()) && dao.isEmailTaken(librarian.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
        }

        return dao.update(librarian);
    }

    public boolean delete(int librarianId) throws SQLException {
        return dao.delete(librarianId);
    }

    public Librarian getById(int librarianId) throws SQLException {
        return dao.getById(librarianId);
    }

    public Librarian authenticate(String username, String password) throws SQLException {
        return dao.authenticate(username, password);
    }

    public boolean isUsernameTaken(String username) throws SQLException {
        return dao.isUsernameTaken(username);
    }

    public boolean isEmailTaken(String email) throws SQLException {
        return dao.isEmailTaken(email);
    }
}
