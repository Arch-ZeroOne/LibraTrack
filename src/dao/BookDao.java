/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import util.DatabaseUtil;
import java.sql.Connection;
import interfaces.BookInterface;
import model.Book;
import java.sql.SQLException;
import java.sql.PreparedStatement;
/**
 *
 * @author Windyl
 */
public class BookDao implements BookInterface{
    
    public final DatabaseUtil util = new DatabaseUtil();
    public final Connection connection = util.connect();
    
    public boolean insert(Book book) throws SQLException{
        String query = "INSERT INTO book (title,author,genre,publisher,publication_date,isbn,copies,isAvailable,barcode)"
                     + "VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        
        preparedStatement.setString(1,book.getTitle());
        preparedStatement.setString(2,book.getAuthor());
        preparedStatement.setString(3,book.getGenre());
        preparedStatement.setString(4,book.getPublisher());
        preparedStatement.setString(5,book.getPublicationDate());
        preparedStatement.setString(6,book.getIsbn());
        preparedStatement.setInt(7,book.getCopies());
        preparedStatement.setBoolean(8,book.getIsAvailable());
        preparedStatement.setString(9,book.getBarcode());        
        
        
        int rows_affected = preparedStatement.executeUpdate();
        
        
        if(rows_affected != 0){
            System.out.println("Book Added");
            return true;
            
        }
        
        return false;
    }
    
    public boolean update(Book book) throws SQLException{
        return false;
    }
    
    public boolean remove(Book book) throws SQLException{
        return false;
    }
    
     public boolean search(Book book) throws SQLException{
        return false;
    }
    
    
    
    
    
}
