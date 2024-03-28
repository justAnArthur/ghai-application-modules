package fiit.vava.client.controllers.coworker;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import fiit.vava.client.controllers.client.NavBarController;
import fiit.vava.client.Router;
import fiit.vava.client.Routes;

public class CoworkerNavBarController extends NavBarController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    void initialize() {

    }
    
    @FXML
    private void handleUserVerificationBtn() {
      Router.getInstance().navigateTo(Routes.Coworker.USER_VERIFICATION);
    }

}
