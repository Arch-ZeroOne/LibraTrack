package util;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
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
    
}
