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
import java.sql.ResultSet;
import java.util.ArrayList;
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
        String query = "UPDATE book SET title = ?,author = ? ,genre = ?,publisher = ? ,publication_date = ? ,"
                + "isbn = ?,copies = ? ,isAvailable = ? WHERE barcode = ?";
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
            System.out.println("Book Updated");
            return true;
            
        }
        return false;
    }
    
    public boolean remove(Book book) throws SQLException{
        return false;
    }
    
     public Book search(String barcode) throws SQLException{
        String query = "SELECT * FROM book WHERE barcode = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, barcode);
        ResultSet result = preparedStatement.executeQuery();
         
        while(result.next()){
            int id = result.getInt("book_id");
            String title = result.getString("title");
            String author = result.getString("author");
            String genre = result.getString("genre");
            String publisher = result.getString("publisher");
            String pub_date = result.getString("publication_date");
            String isbn = result.getString("isbn");
            int copies = result.getInt("copies");
            boolean isAvailable = result.getBoolean("isAvailable");
            String book_barcode = result.getString("barcode");
            return new Book(id,title,author,genre,publisher,pub_date,isbn,copies,isAvailable,book_barcode); 
        }
        
        return null;
    }
     
    public ArrayList<Book> list() throws SQLException{
        String query = "SELECT * FROM book";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet result = preparedStatement.executeQuery();
        ArrayList<Book> book_list = new ArrayList<>();
        
        while(result.next()){
            String title = result.getString("title");
            String author = result.getString("author");
            String genre = result.getString("genre");
            String publisher = result.getString("publisher");
            String publicationDate = result.getString("publication_date");     
            int copies = result.getInt("copies");
            boolean isAvailable = result.getBoolean("isAvailable");
            
            Book book = new Book(title,author,genre,publisher,publicationDate,copies,isAvailable);    
            book_list.add(book);
            
        }
        
        
        
        return book_list;
        
       
        
    }
    
   
    
    
    
    
    
}
