package util;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.function.Consumer;
import java.util.HashMap;
import java.util.Map;

public class ModalUtil {
    private static Map<String, Object> dataStore = new HashMap<>();
    private static Stage currentModalStage = null;

    public static void setData(String key, Object value) {
        dataStore.put(key, value);
    }

    public static Object getData(String key) {
        return dataStore.get(key);
    }

    public static void clearData(String key) {
        dataStore.remove(key);
    }

    public static <T> T openModal(String fxml, String title,Consumer<T> controllerConsumer) throws IOException{

        FXMLLoader loader;
        if(fxml.startsWith("../view/")) {
            // Handle direct view paths
            loader = new FXMLLoader(ModalUtil.class.getResource(fxml+".fxml"));
        } else {
            // Handle modal paths
            loader = new FXMLLoader(ModalUtil.class.getResource("../view/modal/"+fxml+".fxml"));
        }
        Parent root = loader.load();
        T controller = loader.getController();

        Stage modal = new Stage();
        currentModalStage = modal; // Store reference to current modal
        modal.setTitle(title);
        modal.setScene(new Scene(root));
        modal.setResizable(false);
        modal.showAndWait();
        currentModalStage = null; // Clear reference when modal closes

        // Call the callback AFTER the modal closes
        if(controllerConsumer != null){
            controllerConsumer.accept(controller);
        }

        return controller;
        
        
        
   
    }
    
    public static void closeModal(ActionEvent event){
        //gets the currently open stage
        Stage stage =(Stage) ((Node) event.getSource()).getScene().getWindow();

        //closes the modal
        stage.close();
    }

    public static void closeCurrentModal(){
        //closes the current modal if one is open
        if(currentModalStage != null){
            currentModalStage.close();
            currentModalStage = null;
        }
    }
    
}
