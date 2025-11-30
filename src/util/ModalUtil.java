package util;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class ModalUtil {
    
    public <T> T openModal(String fxml, String title) throws IOException{
        
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../view/modal/"+fxml+".fxml"));
        Parent root = loader.load();
        
        Stage modal = new Stage();
        modal.setTitle(title);
        modal.setScene(new Scene(root));
        modal.setResizable(false);
        modal.showAndWait();
        
        return loader.getController();
        
        
        
   
    }
    
    public void closeModal(ActionEvent event){
        //gets the currently open stage
        Stage stage =(Stage) ((Node) event.getSource()).getScene().getWindow();
        
        //closes the modal
        stage.close();
    }
    
}
