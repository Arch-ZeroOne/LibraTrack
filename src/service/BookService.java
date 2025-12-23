/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.sql.SQLException;
import model.Book;
import model.Category;
import dao.BookDao;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import model.BookCategories;
import model.BookRowView;
/**
 *
 * @author Windyl
 */
public class BookService {
     BookDao book_dao = new BookDao();  
      
   
    public boolean insert(Book book,ObservableList<Category> category) throws SQLException{
        return book_dao.insert(book,category);
               
    }
    
    public boolean update(Book book,ObservableList<Category> category) throws SQLException{
        return book_dao.update(book,category);
    }
    
    public boolean remove(String command) throws SQLException{
        return book_dao.remove(command);
    }
    
     public Book search(String barcode) throws SQLException{
            return book_dao.search(barcode);
    }
     
     public ArrayList<BookRowView> list() throws SQLException{
         return book_dao.list();
         
     }
    
}
