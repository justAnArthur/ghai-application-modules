package fiit.vava.client;
import fiit.vava.client.controllers.AppController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import java.io.IOException;

public class Router {
    private AppController appController;

    private static Router instance;

    private Router() {
    
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
}
