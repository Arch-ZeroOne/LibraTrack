package util;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.function.Consumer;
public class ModalUtil {
    
    public <T> T openModal(String fxml, String title,Consumer<T> controllerConsumer) throws IOException{
        
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../view/modal/"+fxml+".fxml"));
        Parent root = loader.load();
        T controller = loader.getController();
        
        if(controllerConsumer != null){
            controllerConsumer.accept(controller);
        }
        
        Stage modal = new Stage();
        modal.setTitle(title);
        modal.setScene(new Scene(root));
        modal.setResizable(false);
        modal.showAndWait();
        
        return controller;
        
        
        
   
    }
    
    public void closeModal(ActionEvent event){
        //gets the currently open stage
        Stage stage =(Stage) ((Node) event.getSource()).getScene().getWindow();
        
        //closes the modal
        stage.close();
    }
    
}
