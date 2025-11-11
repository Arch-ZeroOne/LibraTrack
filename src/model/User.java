package model;

public class User {
  
    private String username;
    private String password;
    private String email;
    private String role;
    
    //Default Constructor
    public User(){}
    
    //Registration constructor
    public User(String username,String password, String email,String role){
       this.username = username;
       this.password = password;
       this.email = email;
       this.role = role;
        
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    //Validation constructor
    public User(String username, String password){
        this.username = username;
        this.password = password;
        
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

  
    

    
   
    
}
