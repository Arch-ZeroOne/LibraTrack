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
        String query = "INSERT INTO account (username, password, email , role) VALUES (?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getEmail());
        statement.setString(4, user.getRole());
        
        int  rows = statement.executeUpdate();
        
        if(rows > 0){
            System.out.println("Query Executed");
            return true;
        }
           
        return false;
        
    }
    
    @Override
    public boolean validate(User user) throws SQLException{
        String query = "SELECT * FROM account WHERE username = ? AND password = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());
        
  
       ResultSet result = statement.executeQuery();
       
       System.out.println(result);
       
       if(result.next()){
           System.out.println("User Logged In");
           return true;
           
       }
        
       return false;
    }
    
    
    
    
    
}
