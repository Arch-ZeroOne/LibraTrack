/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import javafx.collections.ObservableList;
import model.Author;
import dao.AuthorDao;
import java.sql.SQLException;
/**
 *
 * @author Windyl
 */
public class AuthorService {
    AuthorDao dao = new AuthorDao();
    
    public ObservableList<Author> list() throws SQLException{
        return dao.list();
    }
    public boolean addAuthor(Author author) throws SQLException{
        return dao.addAuthor(author);
    }
    public boolean isExisting(Author author) throws SQLException{
       return dao.isExisting(author);
    }
     public int getId(Author author) throws SQLException{
       return dao.getId(author);
    }
    
}
