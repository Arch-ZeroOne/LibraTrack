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
import model.Category;
import java.sql.SQLException;
import javafx.collections.ObservableList;
public interface CategoryInterface {
      //Gets all the list of category
      public ObservableList<Category>  list() throws SQLException;
      //Gets associated category on a specific book
      public ObservableList<String> getByIsbn(String isbn) throws SQLException;
      public int getByName(String name ) throws SQLException;
      
   
}
