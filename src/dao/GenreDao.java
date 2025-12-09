/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Windyl
 */
import model.Genre;
import interfaces.GenreInterface;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import util.DatabaseUtil;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class GenreDao implements GenreInterface{
    DatabaseUtil util = new DatabaseUtil();
    Connection connection = util.connect();
    

    public ArrayList<Genre> list() throws SQLException{
        String query = "SELECT * FROM genre";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet result = preparedStatement.executeQuery();
        ArrayList<Genre> genre_list = new ArrayList<>();
        
        while(result.next()){
            int id  = result.getInt("genre_id");
            String genre_name = result.getString("genre_name");
          
            
          Genre genre = new Genre(id,genre_name);
          genre_list.add(genre);
            
        }
        
        return genre_list;
    }
      public ObservableList<Genre> getByIsbn(String isbn) throws SQLException {
        String query = "SELECT * FROM get_by_genre"
                + " WHERE isbn = ?";
        
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1,isbn);
        ResultSet result = preparedStatement.executeQuery();
        ObservableList<Genre> genreList = FXCollections.observableArrayList();
        
       
        
        while(result.next()){
             genreList.add(new Genre(
               result.getInt("genre_id"),
               result.getString("genre_name")
             ));
    }
        
        return genreList;
      
}
       public boolean insertGenre(int accessionNumber,int genre_id,String isbn) throws SQLException{
            String query = "INSERT INTO book_genres (accession_number,genre_id)"
                    + "VALUES (?,?)";
            
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, accessionNumber);
        preparedStatement.setInt(2, genre_id);
        
         
        int rows_affected = preparedStatement.executeUpdate();
       
      
      return rows_affected != 0;
           
       }
    
    
    
}
