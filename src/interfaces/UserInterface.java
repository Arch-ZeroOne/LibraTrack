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
import model.User;
public interface UserInterface {
    public boolean validate(User user) throws SQLException;
    public boolean insert(User user)  throws SQLException;
}
