/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.sql.SQLException;
import model.Book;
import dao.BookDao;
import java.util.ArrayList;
/**
 *
 * @author Windyl
 */
public class BookService {
     BookDao book_dao = new BookDao();  
      
   
    public boolean insert(Book book) throws SQLException{
        return book_dao.insert(book);
               
    }
    
    public boolean update(Book book) throws SQLException{
        return book_dao.update(book);
    }
    
    public boolean remove(Book book) throws SQLException{
        return book_dao.remove(book);
    }
    
     public boolean search(Book book) throws SQLException{
        return book_dao.search(book);
    }
     
     public ArrayList<Book> list() throws SQLException{
         return book_dao.list();
         
     }
    
}
