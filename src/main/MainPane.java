package main;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainPane extends Application {
    
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
