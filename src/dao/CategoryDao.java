/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import interfaces.CategoryInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Category;
import util.DatabaseUtil;

/**
 *
 * @author Windyl
 */
public class CategoryDao  implements CategoryInterface{
    public final DatabaseUtil util = new DatabaseUtil();
    public final Connection connection = util.connect();
    @Override
    public ObservableList<String> getByIsbn(String isbn) throws SQLException {
         String query = "SELECT * FROM get_by_isbn WHERE isbn = ?";
          ObservableList<String> categories = FXCollections.observableArrayList();
         PreparedStatement preparedStatement = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
         preparedStatement.setString(1,isbn);
            
         ResultSet result = preparedStatement.executeQuery();
         
         while(result.next()){
              categories.add(result.getString("category_name"));
         }
         
         return categories;
        
       
    }

    @Override
    public ObservableList<Category> list() throws SQLException {
         String query = "SELECT * FROM categories";
         ObservableList<Category> categories = FXCollections.observableArrayList();
         PreparedStatement preparedStatement = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
        
            
         ResultSet result = preparedStatement.executeQuery();
         
         while(result.next()){
              
              int category_id = result.getInt("category_id");
              String category_name = result.getString("category_name");
              System.out.println(category_name);
              categories.add(new Category(category_id,category_name));
         }
         
         return categories;
       

        
    }
    
    
    
    
    
    
}
