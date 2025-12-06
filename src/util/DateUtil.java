/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class DateUtil {
    
    public String getFormattedDate(LocalDate date){
        
        if(date != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            return formatter.format(date);
            
        }
        
        return null;
        
    }
    
 
        
    }
    
    
    
    
    
    

