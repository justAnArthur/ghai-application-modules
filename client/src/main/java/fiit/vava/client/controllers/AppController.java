package fiit.vava.client.controllers;
import fiit.vava.client.*;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
public class AppController {

    @FXML
    private BorderPane borderPane;

    @FXML
    void initialize(){
      Router router = Router.getInstance();
      router.setAppController(this);
      router.navigateTo(Routes.LOGIN_PAGE);
    }
    public void setCenter(Node page){
      borderPane.setCenter(page);
    }
    public void setNavBar(Node navBar){
      borderPane.setLeft(navBar);
  }
}
