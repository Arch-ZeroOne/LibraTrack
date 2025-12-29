/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

import java.sql.SQLException;
import java.util.ArrayList;
import model.Borrow;

public interface BorrowInterface {
    boolean insert(Borrow borrow) throws SQLException;
    boolean update(Borrow borrow) throws SQLException;
    boolean delete(int borrowId) throws SQLException;
    Borrow search(int borrowId) throws SQLException;
    ArrayList<Borrow> list() throws SQLException;
    Borrow searchByAccession(int accessionNumber) throws SQLException;
    Borrow getActiveBorrowByAccession(int accessionNumber) throws SQLException;
}
