package fiit.vava.client.controllers.admin;

import fiit.vava.client.Router;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SideBarController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label templatesButtonLink;

    @FXML
    private Label usersBtn;

    @FXML
    private Label settingsBtn;

    @FXML
    private Label profileBtn;

    @FXML
    void initialize() {

    }

    @FXML
    private void handleUsersBtn(MouseEvent event) throws IOException {
        noHighlight();
        usersBtn.setStyle("-fx-background-color: #555;");
    }

    public void handleProfileBtn(MouseEvent event) {
        noHighlight();
        profileBtn.setStyle("-fx-background-color: #555;");
    }

    public void handleSettingsBtn(MouseEvent event) {
        noHighlight();
        settingsBtn.setStyle("-fx-background-color: #555;");
    }

    public void handleTemplatesBtn(MouseEvent event) throws IOException {
        noHighlight();
        templatesButtonLink.setStyle("-fx-background-color: #555;");
        Router.getInstance().navigateTo("admin/templates");
    }

    private void noHighlight() {
        templatesButtonLink.setStyle("-fx-background-color: transparent;");
        usersBtn.setStyle("-fx-background-color: transparent;");
        settingsBtn.setStyle("-fx-background-color: transparent;");
        profileBtn.setStyle("-fx-background-color: transparent;");
    }
}
