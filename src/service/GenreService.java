/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.util.ArrayList;
import model.Category;
import java.sql.SQLException;
import dao.CategoryDao;
import javafx.collections.ObservableList;
import model.BookCategories;
/**
 *
 * @author Windyl
 */
public class GenreService {
    CategoryDao dao = new CategoryDao();
     public ObservableList<Category>  list() throws SQLException{
          return dao.list();
     }
     public ObservableList<String> getByIsbn(String isbn) throws SQLException{
          return dao.getByIsbn(isbn);
     }
     
     
    
}
