/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 *
 * @author Windyl
 */
public class MainPane extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         launch(args);
    }
    
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/LoginView.fxml"));
        Scene scene = new Scene(parent);
        primaryStage.setTitle("LibraTrack");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
       
        
    }
    
}
