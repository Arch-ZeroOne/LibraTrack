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
import model.BookCategories;

public interface BookInterface {
    public boolean insert(Book book,ObservableList<BookCategories> genres) throws SQLException;
    public boolean insertCategories(ObservableList<BookCategories> genres) throws SQLException;
    public boolean update(Book book,ObservableList<BookCategories> genres) throws SQLException;
    public boolean remove(String command) throws SQLException;
    public Book search(String barcode) throws SQLException;
    public ArrayList<Book> list() throws SQLException;
    
}
