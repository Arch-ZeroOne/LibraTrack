/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author Windyl
 */
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
public class AlertUtil {
    private final Alert success_alert = new Alert(AlertType.INFORMATION);
    private final Alert error_alert = new Alert(AlertType.ERROR);
    
    
    public void success(String message){
       success_alert.setContentText(message);
       success_alert.show();
    }
    
    public void error(String message){
        error_alert.setContentText(message);
        error_alert.show();
    }
    
    
    
    
}
