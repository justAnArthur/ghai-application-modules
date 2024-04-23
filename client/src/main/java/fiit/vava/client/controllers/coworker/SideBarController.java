package fiit.vava.client.controllers.coworker;

import fiit.vava.client.Router;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class SideBarController {

    @FXML
    private Label documentsBtn;

    @FXML
    private Label profileBtn;

    @FXML
    private Label settingsBtn;

    @FXML
    private Label usersBtn;

    @FXML
    public void handleDocumentsApproveBtn() throws IOException {
        Router.getInstance().navigateTo("coworker/documentRequests");
    }

    @FXML
    public void handleUserVerificationBtn() throws IOException {
        Router.getInstance().navigateTo("coworker/clients");
    }

    @FXML
    public void handleSettingsBtn() {
    }

    @FXML
    public void handleProfileBtn() {
    }
}
