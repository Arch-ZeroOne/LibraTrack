/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.BorrowDao;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Borrow;

public class BorrowService {
    
    BorrowDao dao = new BorrowDao();
    
    public boolean insert(Borrow borrow) throws SQLException {
        return dao.insert(borrow);
    }

    public boolean update(Borrow borrow) throws SQLException {
        return dao.update(borrow);
    }

    public boolean delete(int borrowId) throws SQLException {
        return dao.delete(borrowId);
    }

    public Borrow search(int borrowId) throws SQLException {
        return dao.search(borrowId);
    }

    public ArrayList<Borrow> list() throws SQLException {
        return dao.list();
    }
    // ==============================
    // Borrow a book by bookId
    // ==============================
    public boolean borrowBook(int id, int studentId, int librarianId, String borrowDate, String dueDate) throws SQLException {
        return dao.borrowBook(id, studentId, librarianId, borrowDate, dueDate);
    }

    // ==============================
    // Return a book by accession number
    // ==============================
    public boolean returnBook(int accessionNumber) throws SQLException {
        return dao.returnBook(accessionNumber);
    }

    // ==============================
    // Check if a book has at least one available copy
    // ==============================
    public boolean isBookAvailable(int bookId) throws SQLException {
        return dao.isBookAvailable(bookId);
    }

    // ==============================
    // Check if a specific copy is borrowed
    // ==============================
    public boolean isBorrowed(int accessionNumber) throws SQLException {
        return dao.isBorrowed(accessionNumber);
    }

    // ==============================
    // Get active borrow record by accession number
    // ==============================
    public Borrow getActiveBorrowByAccession(int accessionNumber) throws SQLException {
        return dao.searchByAccession(accessionNumber);
    }

    // ==============================
    // List all borrow records
    // ==============================
    public ArrayList<Borrow> listAllBorrows() throws SQLException {
        return dao.list();
    }

    // ==============================
    // Check if a book is currently borrowed
    // ==============================
    public boolean isBookCurrentlyBorrowed(int bookId) throws SQLException {
        return dao.getActiveBorrowByAccession(bookId) != null;
    }
}
