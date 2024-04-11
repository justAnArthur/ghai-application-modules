package fiit.vava.client.controllers.client;

import fiit.vava.client.Router;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientSideBarController {

    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private Label documentsBtn;
    @FXML
    private Label templatesBtn;
    @FXML
    private Label askQuestionBtn;
    @FXML
    private Label settingsBtn;
    @FXML
    private Label profileBtn;

    @FXML
    void initialize() {
    }

    @FXML
    private void handleTemplatesBtn(MouseEvent event) throws IOException {
        noHighlight();
        templatesBtn.setStyle("-fx-background-color: #555;");
        Router.getInstance().navigateTo("client/templates");
    }

    @FXML
    private void handleProfileBtn(MouseEvent event) {
        noHighlight();
        profileBtn.setStyle("-fx-background-color: #555;");
    }

    @FXML
    private void handleAskQuestionBtn(MouseEvent event) {
        noHighlight();
        askQuestionBtn.setStyle("-fx-background-color: #555;");
    }

    @FXML
    private void handleSettingsBtn(MouseEvent event) {
        noHighlight();
        settingsBtn.setStyle("-fx-background-color: #555;");
    }

    @FXML
    private void handleDocumentsBtn(MouseEvent event) throws IOException {
        noHighlight();
        documentsBtn.setStyle("-fx-background-color: #555;");
        Router.getInstance().navigateTo("client/documents");
    }

    private void noHighlight() {
        documentsBtn.setStyle("-fx-background-color: transparent;");
        askQuestionBtn.setStyle("-fx-background-color: transparent;");
        templatesBtn.setStyle("-fx-background-color: transparent;");
        settingsBtn.setStyle("-fx-background-color: transparent;");
        profileBtn.setStyle("-fx-background-color: transparent;");
    }

}
