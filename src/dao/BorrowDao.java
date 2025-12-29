package dao;

import interfaces.BorrowInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import model.Borrow;
import util.DatabaseUtil;

public class BorrowDao implements BorrowInterface {

    private final DatabaseUtil util = new DatabaseUtil();
    private final Connection connection = util.connect();

    @Override
    public boolean insert(Borrow borrow) throws SQLException {
        String sql = "INSERT INTO borrow (borrow_date, due_date, student_id, accession_number, librarian_id) "
                   + "VALUES (?, ?, ?, ?, ?)";
        
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, borrow.getBorrowDate());
        ps.setString(2, borrow.getDueDate());
        ps.setInt(3, borrow.getStudentId());
        ps.setInt(4, borrow.getAccessionNumber());
        ps.setInt(5, borrow.getLibrarianId());

        int rows = ps.executeUpdate();
        return rows != 0;
    }

    @Override
    public boolean update(Borrow borrow) throws SQLException {
        String sql = "UPDATE borrow SET borrow_date = ?, due_date = ?, student_id = ?, accession_number = ?, "
                   + "librarian_id = ? WHERE borrow_id = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, borrow.getBorrowDate());
        ps.setString(2, borrow.getDueDate());
        ps.setInt(3, borrow.getStudentId());
        ps.setInt(4, borrow.getAccessionNumber());
        ps.setInt(5, borrow.getLibrarianId());
        ps.setInt(6, borrow.getBorrowId());

        int rows = ps.executeUpdate();
        return rows != 0;
    }

    @Override
    public boolean delete(int borrowId) throws SQLException {
        String sql = "DELETE FROM borrow WHERE borrow_id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, borrowId);

        int rows = ps.executeUpdate();
        return rows != 0;
    }

    @Override
    public Borrow search(int borrowId) throws SQLException {
        String sql = "SELECT * FROM borrow WHERE borrow_id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, borrowId);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new Borrow(
                rs.getInt("borrow_id"),
                rs.getString("borrow_date"),
                rs.getString("due_date"),
                rs.getInt("student_id"),
                rs.getInt("accession_number"),
                rs.getInt("librarian_id")
            );
        }

        return null;
    }

    @Override
    public ArrayList<Borrow> list() throws SQLException {
        String sql = "SELECT * FROM borrow";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        ArrayList<Borrow> list = new ArrayList<>();

        while (rs.next()) {
            Borrow borrow = new Borrow(
                rs.getInt("borrow_id"),
                rs.getString("borrow_date"),
                rs.getString("due_date"),
                rs.getInt("student_id"),
                rs.getInt("accession_number"),
                rs.getInt("librarian_id")
            );
            list.add(borrow);
        }

        return list;
    }
    
    // ==============================
    // 1. Check if at least one copy is available for a book (by book_id)
    // ==============================
    public boolean isBookAvailable(int bookId) throws SQLException {
        String sql = "SELECT COUNT(*) AS available_count FROM book WHERE book_id = ? AND isAvailable = 'Available'";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, bookId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("available_count") > 0;
        }
        return false;
    }

    // ==============================
    // 2. Get the next available accession number for a specific book
    // ==============================
    public int getAvailableAccessionForBook(int bookId) throws SQLException {
        String sql = "SELECT accession_number FROM book WHERE book_id = ? AND isAvailable = 'Available' LIMIT 1";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, bookId);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("accession_number");
        }
        return -1; // No available copies
    }

    // ==============================
    // 3. Borrow a book by book_id (finds any available copy)
    // ==============================
    public boolean borrowBook(int bookId, int studentId, int librarianId, String borrowDate, String dueDate) throws SQLException {
        int accessionNumber = getAvailableAccessionForBook(bookId);
        if (accessionNumber == -1) {
            return false; // No available copies
        }

        // Mark the copy as borrowed
        markAsBorrowed(accessionNumber);

        // Insert borrow record
        String sql = "INSERT INTO borrow (borrow_date, due_date, student_id, accession_number, librarian_id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, borrowDate);
        ps.setString(2, dueDate);
        ps.setInt(3, studentId);
        ps.setInt(4, accessionNumber);
        ps.setInt(5, librarianId);

        return ps.executeUpdate() > 0;
    }

    // ==============================
    // 4. Return a book
    // ==============================
    public boolean returnBook(int accessionNumber) throws SQLException {
        // Mark copy as available
        boolean success = markAsReturned(accessionNumber);
        if (!success) return false;

        // Update borrow table (set return_date)
        String sql = "UPDATE borrow SET return_date = NOW() WHERE accession_number = ? AND return_date IS NULL";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, accessionNumber);
        ps.executeUpdate();

        return true;
    }

    // ==============================
    // 5. Mark a copy as borrowed
    // ==============================
    private boolean markAsBorrowed(int accessionNumber) throws SQLException {
        String sql = "UPDATE book SET isAvailable = 'Borrowed' WHERE accession_number = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, accessionNumber);
        return ps.executeUpdate() > 0;
    }

    // ==============================
    // 6. Mark a copy as returned
    // ==============================
    private boolean markAsReturned(int accessionNumber) throws SQLException {
        String sql = "UPDATE book SET isAvailable = 'Available' WHERE accession_number = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, accessionNumber);
        return ps.executeUpdate() > 0;
    }

    // ==============================
    // 7. Check if a specific copy is currently borrowed
    // ==============================
    public boolean isBorrowed(int accessionNumber) throws SQLException {
        String sql = "SELECT isAvailable FROM book WHERE accession_number = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, accessionNumber);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("isAvailable").equalsIgnoreCase("Borrowed");
        }
        return false;
    }

    

    // ==============================
    // 9. Search borrow record by accession number
    // ==============================
    @Override
    public Borrow searchByAccession(int accessionNumber) throws SQLException {
        String sql = "SELECT * FROM borrow WHERE accession_number = ? AND return_date IS NULL";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, accessionNumber);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new Borrow(
                rs.getInt("borrow_id"),
                rs.getString("borrow_date"),
                rs.getString("due_date"),
                rs.getInt("student_id"),
                rs.getInt("accession_number"),
                rs.getInt("librarian_id")
            );
        }
        return null;
    }

    // ==============================
    // 10. Get active borrow record by accession number
    // ==============================
    @Override
    public Borrow getActiveBorrowByAccession(int accessionNumber) throws SQLException {
        return searchByAccession(accessionNumber);
    }

}
