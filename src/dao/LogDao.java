package dao;


import model.Log;
import java.sql.SQLException;
import java.sql.Connection;
import util.DatabaseUtil;
import interfaces.LogInterface;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Date;


public class LogDao implements LogInterface {
       DatabaseUtil db_util = new DatabaseUtil();
    Connection connection = db_util.connect();

    @Override
    public ArrayList<Log> list() throws SQLException {
      String query = "SELECT * FROM log";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet result = preparedStatement.executeQuery();
        ArrayList<Log> log_list = new ArrayList<>();
        
        while(result.next()){
            String firstname = result.getString("firstname");
            String middlename = result.getString("middlename");
            String lastname = result.getString("lastname");
            Date log_date = result.getDate("log_date");
            String school_id = result.getString("school_id");
            
            Log log = new Log(firstname,middlename,lastname,log_date,school_id);    
            log_list.add(log);
            
        }
        
        return log_list;
          
    }

    @Override
    public boolean insert(String school_id) throws SQLException {
        String query = "INSERT INTO log (firstname,middlename,lastname,log_date,school_id,student_id)"
                + "SELECT firstname,middlename,lastname,NOW(),school_id,student_id"
                + " FROM student"
                + " WHERE school_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, school_id);
        System.out.println(preparedStatement);
         
        int rows_affected = preparedStatement.executeUpdate();
       
      
      return rows_affected != 0;
    }

    @Override
    public boolean insertAttendance(Log log, int studentId) throws SQLException {
        String query = "INSERT INTO log (firstname, middlename, lastname, log_date, school_id, student_id) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, log.getFirstname());
        preparedStatement.setString(2, log.getMiddlename());
        preparedStatement.setString(3, log.getLastname());
        preparedStatement.setDate(4, log.getLog_date());
        preparedStatement.setString(5, log.getSchool_id());
        preparedStatement.setInt(6, studentId);

        int rows_affected = preparedStatement.executeUpdate();
        return rows_affected != 0;
    }

    @Override
    public String search(String qrcode) throws SQLException {
        String query = "SELECT * FROM student WHERE school_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, qrcode);
        ResultSet result = preparedStatement.executeQuery();
         
        while(result.next()){
            return result.getString("school_id");
        }
        
        return null;
    }
    
    @Override
    public boolean isDuplicate(Date date,String school_id) throws SQLException{
        String query = "SELECT * FROM log WHERE log_date = ? && school_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setDate(1, date);
        preparedStatement.setString(2, school_id);

        System.out.println(preparedStatement);
        ResultSet result = preparedStatement.executeQuery();

         return result.next();

    }

    @Override
    public boolean delete(int logId) throws SQLException {
        String query = "DELETE FROM log WHERE log_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, logId);
        return preparedStatement.executeUpdate() > 0;
    }

  }
 
    
    


