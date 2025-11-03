/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author Windyl
 */
import java.sql.Connection;
import java.sql.DriverManager;
public class Connector {
    
    public final String URL = "jdbc:mysql://localhost:3306/libratrack_library_barcode";
    public final String USERNAME = "root";
    public final String PASSWORD = "";
    
    public Connection connect(){
        
         try{
             Class.forName("com.mysql.cj.jdbc.Driver");
             System.out.println("Connection Established");
             return DriverManager.getConnection(URL,USERNAME,PASSWORD);

         }catch(Exception e){
             System.out.println("Connection Failure");
             e.printStackTrace();
         }
         
         
        
        
        
        
        return null;
        
        
    }
    
   
    
}
