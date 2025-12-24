/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

import javafx.collections.ObservableList;
import model.Author;
import java.sql.SQLException;

/**
 *
 * @author Windyl
 */
public interface AuthorInterface {
    public ObservableList<Author> list() throws SQLException;
    public boolean addAuthor(Author author) throws SQLException;
    public String getById(int id) throws SQLException;
    public boolean isExisting(Author author) throws SQLException;
    public ObservableList<Author> findAuthor(String symbol) throws SQLException;
    public int getId(Author author) throws SQLException;
}
