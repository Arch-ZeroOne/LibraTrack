/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

/**
 *
 * @author Windyl
 */

import java.sql.SQLException;
import model.Book;
public interface BookInterface {
    public boolean insert(Book book) throws SQLException;
    public boolean update(Book book) throws SQLException;
    public boolean remove(Book book) throws SQLException;
    public boolean search(Book book) throws SQLException;
    
}
