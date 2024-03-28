package fiit.vava.client.routes.client.profile;

import fiit.vava.client.Router;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class Controller {

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
    void handleMyDocumentsBtn(ActionEvent event) throws IOException {
        Router.getInstance().navigateTo("client/documents");
    }
}
