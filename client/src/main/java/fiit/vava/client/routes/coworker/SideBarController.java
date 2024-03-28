package fiit.vava.client.routes.coworker;

import fiit.vava.client.Router;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SideBarController /*extends NavBarController*/ {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    void initialize() {
    }

    @FXML
    private void handleUserVerificationBtn() throws IOException {
        Router.getInstance().navigateTo("coworker/clients/approve");
    }

    public void handleProfileBtn(ActionEvent actionEvent) {
    }

    public void handleSettingsBtn(ActionEvent actionEvent) {
    }

    public void handleDocumentsBtn(ActionEvent actionEvent) {
    }
}
