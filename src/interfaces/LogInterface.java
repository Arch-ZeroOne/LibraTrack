/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

import java.util.ArrayList;
import model.Log;
import java.sql.SQLException;
import java.sql.Date;
public interface LogInterface {
      public ArrayList<Log> list() throws SQLException;
      public boolean insert(String school_id) throws SQLException;
      public String search(String qrcode) throws SQLException;
      public boolean isDuplicate(Date logDate,String school_id) throws SQLException;
      public boolean insertAttendance(Log log, int studentId) throws SQLException;
      public boolean delete(int logId) throws SQLException;

}
