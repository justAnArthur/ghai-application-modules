package fiit.vava.client.controllers.client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import fiit.vava.client.Router;
import fiit.vava.client.Routes;
public class ProfileController{

    @FXML
    private Label dateOfBirth;

    @FXML
    private Button myDocumentsBtn;

    @FXML
    private Label name;

    @FXML
    private Label sex;

    @FXML
    private Label surname;

    @FXML
    private Label verified;

    @FXML
    void handleMyDocumentsBtn(ActionEvent event) {
      Router.getInstance().navigateTo(Routes.Client.MY_DOCUMENTS);
    }

}
