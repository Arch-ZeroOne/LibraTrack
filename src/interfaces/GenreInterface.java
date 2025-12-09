/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

/**
 *
 * @author Windyl
 */
import java.util.ArrayList;
import model.Genre;
import java.sql.SQLException;
public interface GenreInterface {
      public ArrayList<Genre> list() throws SQLException;
      
   
}
