/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

/**
 *
 * @author Windyl
 */
public class StatusManager {
    private int status_id;
    private String status_name;
    private static StatusManager instance;
    // Thread-safe method to get the instance
    public static synchronized StatusManager getInstance() {
        if (instance == null) {
            instance = new StatusManager();
        }
        return instance;
    }
  
    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public StatusManager(int status_id, String status_name) {
        this.status_id = status_id;
        this.status_name = status_name;
    }
    
       public StatusManager() {
        
    }
}
