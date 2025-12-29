
package util;

import java.sql.Connection;
import java.sql.DriverManager;
public class DatabaseUtil {
    
    public final String URL = "jdbc:mysql://localhost:3306/revise_db";
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
