/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

/**
 *
 * @author Windyl
 */

public class AuthorManager {
    private int author_id;
    private String author_name;
    
    private static AuthorManager instance;
     // Private constructor
    private AuthorManager() {}

    // Thread-safe method to get the instance
    public static synchronized AuthorManager getInstance() {
        if (instance == null) {
            instance = new AuthorManager();
        }
        return instance;
    }
    
    public AuthorManager(int author_id, String author_name) {
        this.author_id = author_id;
        this.author_name = author_name;
    }
     public AuthorManager(String author_name) {
        this.author_name = author_name;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }
    @Override
    public String toString() {
        return super.toString(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

   
    
    
}
