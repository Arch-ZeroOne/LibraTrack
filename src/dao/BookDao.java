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
import dao.GenreDao;
import javafx.collections.ObservableList;
import model.Genre;
/**
 *
 * @author Windyl
 */
public class BookDao implements BookInterface{
    
    public final DatabaseUtil util = new DatabaseUtil();
    public final Connection connection = util.connect();
    public GenreDao dao = new GenreDao();
    
    public boolean insert(Book book,ObservableList<Genre> genres) throws SQLException{
        String query = "INSERT INTO book (title,author,publisher,publication_date,isbn,isAvailable,copies)"
                     + "VALUES (?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
        
        for(int i = 0; i < book.getCopies(); i++){
            preparedStatement.setString(1,book.getTitle());
            preparedStatement.setString(2,book.getAuthor());
            preparedStatement.setString(3,book.getPublisher());
            preparedStatement.setString(4,book.getPublicationDate());
            preparedStatement.setString(5,book.getIsbn());
            preparedStatement.setString(6,book.getIsAvailable());
            preparedStatement.setInt(7,book.getCopies());
            preparedStatement.addBatch();
            
        }
          
        
        
        int[] updateCounts = preparedStatement.executeBatch();
        ResultSet keys = preparedStatement.getGeneratedKeys();
        
        while(keys.next()){
            for(Genre genre : genres){
                dao.insertGenre(keys.getInt(1), genre.getGenre_id(),book.getIsbn());
            }
           
        }
        
     
        
        
       return updateCounts.length != 0;
    }
    
    public boolean update(Book book,ObservableList<Genre> bookGenre) throws SQLException{
        String query = "UPDATE book SET title = ?,author = ? ,publisher = ? ,publication_date = ? ,"
                + "isbn = ? ,isAvailable = ? WHERE isbn = ?";
     String select = "SELECT * FROM book WHERE isbn = ?";
     
        PreparedStatement preparedStatement = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
        PreparedStatement selectStatement = connection.prepareStatement(select,Statement.RETURN_GENERATED_KEYS);
   
            preparedStatement.setString(1,book.getTitle());
            preparedStatement.setString(2,book.getAuthor());
            preparedStatement.setString(3,book.getPublisher());
            preparedStatement.setString(4,book.getPublicationDate());
            preparedStatement.setString(5,book.getIsbn());
            preparedStatement.setString(6,book.getIsAvailable());
            preparedStatement.setString(7,book.getIsbn());
            //For updating
            selectStatement.setString(1, book.getIsbn());
            
        
        
        int rows = preparedStatement.executeUpdate();
        ResultSet result = selectStatement.executeQuery();
        
        while(result.next()){
            
                 updateBookGenres(result.getInt("accession_number"),bookGenre);
             
        }
        
        
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
         
        while(result.next()){
            if(result.getString("isAvailable").equals("Borrowed")){
                continue;
                
            }
            int id = result.getInt("accession_number");
            String title = result.getString("title");
            String author = result.getString("author");
            String publisher = result.getString("publisher");
            String pub_date = result.getString("publication_date");
            String isbn = result.getString("isbn");
            int copies = result.getInt("copies");
            String isAvailable = result.getString("isAvailable");
            
            return new Book(id,title,author,publisher,pub_date,isbn,copies,isAvailable); 
        }
        
        return null;
    }
     
    public ArrayList<Book> list() throws SQLException{
        String query = "SELECT * FROM book";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet result = preparedStatement.executeQuery();
        ArrayList<Book> book_list = new ArrayList<>();
        
        while(result.next()){
            int id = result.getInt("accession_number");
            String title = result.getString("title");
            String author = result.getString("author");
            String publisher = result.getString("publisher");
            String publicationDate = result.getString("publication_date"); 
            String isbn = result.getString("isbn");
            String isAvailable = result.getString("isAvailable");
            
            Book book = new Book(id,title,author,publisher,publicationDate,isbn,isAvailable);    
            book_list.add(book);
            
        }
        
        
        
        return book_list;
        
       
        
    }
    public void updateBookGenres(int accession_number, ObservableList<Genre> genre) {

   String deleteSql  ="DELETE FROM book_genres WHERE accession_number = ?";
   String insertSql = "INSERT INTO book_genres (accession_number, genre_id) VALUES (?, ?)";

   try (Connection conn = util.connect()) {
        // 1. Delete old genres
    try (PreparedStatement ps = conn.prepareStatement(deleteSql)) {
        ps.setInt(1, accession_number);
            ps.executeUpdate();
        }

       // 2. Insert new genres in batch
        try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
               for(Genre genres: genre){
                ps.setInt(1, accession_number);
                ps.setInt(2, genres.getGenre_id());
                ps.addBatch();
               }
                  int[] updateCounts = ps.executeBatch();
            
         
            
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    
    
   
    
    
    
    
    
}
