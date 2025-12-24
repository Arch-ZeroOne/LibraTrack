/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

/**
 *
 * @author Windyl
 */

import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import model.Book;
import model.Category;
import model.BookRowView;

public interface BookInterface {
    public boolean insert(Book book,ObservableList<Category> genres) throws SQLException;
    public boolean insertCategories(ObservableList<Category> genres,int primaryKey) throws SQLException;
    public boolean update(Book book,ObservableList<Category> genres) throws SQLException;
    public boolean insertCopies(Book book,int bookId) throws SQLException;
    public boolean remove(String command) throws SQLException;
    public Book search(String barcode) throws SQLException;
    public Book getById(int id)  throws SQLException;
    public ArrayList<BookRowView> list() throws SQLException;
    
    
}
