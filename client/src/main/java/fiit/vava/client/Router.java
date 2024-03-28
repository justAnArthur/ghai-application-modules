package fiit.vava.client;
import fiit.vava.client.controllers.AppController;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.Parent;
import java.io.IOException;

public class Router {
    private AppController appController;

    private Stage modalStage;

    private static Router instance;

    private Router() {
      modalStage = new Stage();
      modalStage.initModality(Modality.APPLICATION_MODAL);
   }

    public static Router getInstance() {
        if (instance == null) {
            instance = new Router();
        }
        return instance;
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    public void navigateTo(String route) {
      try {
          FXMLLoader loader = new FXMLLoader(getClass().getResource(route));
          Node page = loader.load();
          appController.setCenter(page);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    
    public void changeNavBar(String route) {
      try {
          FXMLLoader loader = new FXMLLoader(getClass().getResource(route));
          Node page = loader.load();
          appController.setNavBar(page);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    
    public void showModal(String dialogFxml){
      try {
        FXMLLoader dialogLoader = new FXMLLoader(getClass().getResource(dialogFxml));
        Parent dialogRoot = dialogLoader.load();
        modalStage.setScene(new Scene(dialogRoot));
        modalStage.showAndWait();
      } catch(IOException e){
        e.printStackTrace();
      } 
    }

    public void hideModal(){
      modalStage.hide();
    }
}
