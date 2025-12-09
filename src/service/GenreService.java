/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.util.ArrayList;
import model.Genre;
import java.sql.SQLException;
import dao.GenreDao;
import javafx.collections.ObservableList;

/**
 *
 * @author Windyl
 */
public class GenreService {
    GenreDao dao = new GenreDao();
     public ArrayList<Genre> list() throws SQLException{
          return dao.list();
     }
     public ObservableList<Genre> getByIsbn(String isbn) throws SQLException {
          return dao.getByIsbn(isbn);
     }
    
}
