/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;
import dao.LogDao;
import model.Log;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
public class LogService {
    LogDao dao = new LogDao();
    public boolean insert(String school_id) throws SQLException{
        return dao.insert(school_id);
        
    }
    public String search(String symbol) throws SQLException{
        return dao.search(symbol);
    }
    public ArrayList<Log> list() throws SQLException{
        return dao.list();
        
    }
    
    public boolean isDuplicate(Date date,String school_id) throws SQLException{
       return dao.isDuplicate(date,school_id);
    }
    
}
