/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import util.DatabaseUtil;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import interfaces.UserInterface;
import model.User;
public class UserDao implements UserInterface {
    //Util for database
    private final DatabaseUtil DB_UTIL = new DatabaseUtil();
    private final Connection connection = DB_UTIL.connect();
   
    @Override
    public boolean insert(User user) throws SQLException{
        String query = "INSERT INTO librarian (firstName,middleName,lastName,username,email,password) VALUES (?,?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, user.getFirstName());
        statement.setString(2, user.getMiddleName());
        statement.setString(3, user.getLastName());
        statement.setString(4, user.getUsername());
        statement.setString(5, user.getEmail());
        statement.setString(6, user.getPassword());
        
        int  rows = statement.executeUpdate();
        
        return rows != 0;
        
    }
    
    @Override
    public boolean validate(User user) throws SQLException{
        String query = "SELECT * FROM librarian WHERE username = ? AND password = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());
        
       ResultSet result = statement.executeQuery();
       
     
       return result.next();
       
    }
    
    
    
    
    
}
