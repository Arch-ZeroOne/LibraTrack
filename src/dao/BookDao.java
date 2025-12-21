/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Statement;
import util.DatabaseUtil;
import java.sql.Connection;
import interfaces.BookInterface;
import model.Book;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javafx.collections.ObservableList;
import model.BookCategories;

/**
 *
 * @author Windyl
 */
public class BookDao implements BookInterface{
    
    public final DatabaseUtil util = new DatabaseUtil();
    public final Connection connection = util.connect();
   
    public boolean insert(Book book,ObservableList<BookCategories> categories) throws SQLException{
        String query = "INSERT INTO books (title,author_id,publisher,isbn,publication_date,status_id)"
                     + "VALUES (?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        
       preparedStatement.setString(1, book.getTitle());
       preparedStatement.setInt(2, book.getAuthor_id());
       preparedStatement.setInt(3, book.getPublisher_id());
       preparedStatement.setString(4,book.getIsbn());
       preparedStatement.setDate(5, book.getPublication_date());
       preparedStatement.setInt(6, book.getCategory_id());
       
          
        ResultSet result  = preparedStatement.executeQuery();
        
        
       insertCategories(categories);
        

       return result.next();
    }
     
    public boolean insertCategories(ObservableList<BookCategories> categories) throws SQLException{
         String query = "INSERT INTO book_categories (book_id,category_id)"
                     + "VALUES (?,?)";
           PreparedStatement preparedStatement = connection.prepareStatement(query);
        for(BookCategories category : categories){
           
       
          
          preparedStatement.setInt(1, category.getBook_id());
          preparedStatement.setInt(2, category.getCategory_id());
          preparedStatement.addBatch();
        }
        
        
       int[] updates = preparedStatement.executeBatch();
       
          
       return updates.length != 0;
        
     

      
    }
    
    
    public boolean update(Book book,ObservableList<BookCategories> bookGenre) throws SQLException{
        String query = "UPDATE book SET title = ?,author = ? ,publisher = ? ,publication_date = ? ,"
                + "isbn = ? ,isAvailable = ? WHERE isbn = ?";
     String select = "SELECT * FROM book WHERE isbn = ?";
     
     
     
        PreparedStatement preparedStatement = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
        PreparedStatement selectStatement = connection.prepareStatement(select,Statement.RETURN_GENERATED_KEYS);
//
//            preparedStatement.setString(1,book.getTitle());
//            preparedStatement.setString(2,book.getAuthor());
//            preparedStatement.setString(3,book.getPublisher());
//            preparedStatement.setString(4,book.getPublicationDate());
//            preparedStatement.setString(5,book.getIsbn());
//            preparedStatement.setString(6,book.getIsAvailable());
//            preparedStatement.setString(7,book.getIsbn());
//            //For updating
//            selectStatement.setString(1, book.getIsbn());
               System.out.println(preparedStatement);
        
        
        int rows = preparedStatement.executeUpdate();
        ResultSet result = selectStatement.executeQuery();
        
      
        
        System.out.println(rows);
        return rows !=0;
    }
    
    public boolean remove(String command) throws SQLException{
        String query = "UPDATE book SET isAvailable = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
        
   
            preparedStatement.setString(1,command);
            
        
        
        int rows = preparedStatement.executeUpdate();
       
        return rows != 0;
    }
    
     public Book search(String barcode) throws SQLException{
        String query = "SELECT * FROM book WHERE isbn = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, barcode);
        ResultSet result = preparedStatement.executeQuery();
//         
//        while(result.next()){
//            if(result.getString("isAvailable").equals("Borrowed")){
//                continue;
//                
//            }
//            int id = result.getInt("accession_number");
//            String title = result.getString("title");
//            String author = result.getString("author");
//            String publisher = result.getString("publisher");
//            String pub_date = result.getString("publication_date");
//            String isbn = result.getString("isbn");
//            int copies = result.getInt("copies");
//            String isAvailable = result.getString("isAvailable");
//            
//            return new Book(id,title,author,publisher,pub_date,isbn,copies,isAvailable); 
//        }
//        
        return null;
    }
     
    public ArrayList<Book> list() throws SQLException{
        String query = "SELECT * FROM books";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet result = preparedStatement.executeQuery();
        ArrayList<Book> book_list = new ArrayList<>();
//        
//        while(result.next()){
//            int id = result.getInt("accession_number");
//            String title = result.getString("title");
//            String author = result.getString("author");
//            String publisher = result.getString("publisher");
//            String publicationDate = result.getString("publication_date"); 
//            String isbn = result.getString("isbn");
//            String isAvailable = result.getString("isAvailable");
//            
//            Book book = new Book(id,title,author,publisher,publicationDate,isbn,isAvailable);    
//            book_list.add(book);
//            
//        }
//        
//        
        
        return book_list;
        
       
        
    }
    
}

    
    
   
    
    
    
    
    

