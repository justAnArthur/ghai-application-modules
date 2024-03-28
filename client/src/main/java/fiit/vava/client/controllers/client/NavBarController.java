package fiit.vava.client.controllers.client;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import fiit.vava.client.Router;
import fiit.vava.client.Routes;

public class NavBarController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button askQuestionBtn;

    @FXML
    protected Button documentsBtn;

    @FXML
    private Button libraryBtn;

    @FXML
    protected GridPane navGrid;

    @FXML
    protected Button profileBtn;

    @FXML
    protected Button settingsBtn;

    @FXML
    void initialize() {

    }
    
    @FXML
    protected void handleDocumentsBtn() {
      System.out.println("Action wasn't handled");
    }

    @FXML
    private void handleLibraryBtn() {
      System.out.println("What the fuck is library?");
    }

    @FXML
    private void handleAskQuestionBtn() {
      System.out.println("Action wasn't handled");
    }

    @FXML
    protected void handleSettingsBtn() {
      System.out.println("Action wasn't handled");
    }

    @FXML
    protected void handleProfileBtn() {
      Router.getInstance().navigateTo(Routes.Client.PROFILE);
    }
}
