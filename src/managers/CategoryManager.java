/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

/**
 *
 * @author Windyl
 */
public class CategoryManager {
    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Windyl
 */

     private int category_id;
     private String category_name;

 // Private constructor
    private CategoryManager() {}
  // ======= Singleton Instance =======
    private static CategoryManager instance;
    // Thread-safe method to get the instance
    public static synchronized CategoryManager getInstance() {
        if (instance == null) {
            instance = new CategoryManager();
        }
        return instance;
    }
  
    
    public int getCategory_id() {
        return category_id;
    }
    
    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }


    @Override
    public String toString(){
        return category_name;
    }
     
     
     
}


