/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import interfaces.AuthorInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import model.Author;
import util.DatabaseUtil;
import java.sql.SQLException;
import javafx.collections.FXCollections;

/**
 *
 * @author Windyl
 */
public class AuthorDao implements AuthorInterface {


   
    public final DatabaseUtil util = new DatabaseUtil();
    public final Connection connection = util.connect();
   
    @Override
    public ObservableList<Author> list() throws SQLException{
        String query = "SELECT * FROM authors";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet result = preparedStatement.executeQuery();
        ObservableList<Author> authors = FXCollections.observableArrayList();
        
        while(result.next()){
            int id = result.getInt("author_id");
            String name = result.getString("author_name");
            authors.add(new Author(id,name));
        }
        
        return authors;
    }

    @Override
    public boolean addAuthor(Author author) throws SQLException{
        String query = "INSERT INTO authors (author_name) VALUES(?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, author.getAuthor_name());
      
        int rows = preparedStatement.executeUpdate();
        
        return rows != 0;
        

    }

    @Override
    public boolean isExisting(Author author) throws SQLException {
        String query = "SELECT * FROM authors WHERE author_name = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, author.getAuthor_name());
      
        
        ResultSet result = preparedStatement.executeQuery();
         
        return result.next();
       
        
    }

    @Override
    public ObservableList<Author> findAuthor(String symbol) throws SQLException {
       ObservableList<Author>queriedAuthors = FXCollections.observableArrayList();
               
       String query = "SELECT * FROM authors WHERE author_name LIKE ?%";
       PreparedStatement preparedStatement = connection.prepareStatement(query); 
       preparedStatement.setString(1, symbol);
      

        ResultSet result = preparedStatement.executeQuery();
         
        while(result.next()){
            int id = result.getInt("author_id");
            String name = result.getString("author_name");
            
            queriedAuthors.add(new Author(id,name));
        }
        
        return queriedAuthors;
        
    }
    @Override
    public int getId(Author author) throws SQLException {
       String query = "SELECT author_id FROM authors WHERE author_name = ? LIMIT 1";
       PreparedStatement preparedStatement = connection.prepareStatement(query); 
       preparedStatement.setString(1, author.getAuthor_name());
      

        ResultSet result = preparedStatement.executeQuery();
        while(result.next()){
            return result.getInt("author_id");
        }
        
       return 0;
    }
    
    @Override
    public String getById(int id) throws SQLException {
      String query = "SELECT author_name FROM authors WHERE author_id = ?";
       PreparedStatement preparedStatement = connection.prepareStatement(query); 
       preparedStatement.setInt(1, id);
     
        ResultSet result = preparedStatement.executeQuery();
        while(result.next()){
            return result.getString("author_name");
        }
        
       return null;
    }
}
