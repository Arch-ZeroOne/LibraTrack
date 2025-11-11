package service;
import dao.UserDao;
import java.sql.SQLException;
import model.User;
public class UserService {
       UserDao user_dao = new UserDao();
       
    
    public boolean insert(User user) throws SQLException{
        return user_dao.insert(user);
               
    }
    
    public boolean validate(User user) throws SQLException{
        return user_dao.validate(user);
        
    }
    
}
