package fiit.vava.client.controllers.client;

import fiit.vava.client.Router;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SideBarController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    
    @FXML
    private Label documentsBtn;
    
    @FXML
    private Label libraryBtn;
    
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
    private void handleLibraryBtn(MouseEvent event) throws IOException {
        noHighlight();
        libraryBtn.setStyle("-fx-background-color: #555;");
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
    private void handleDocumentsBtn(MouseEvent event) {
        noHighlight();
        documentsBtn.setStyle("-fx-background-color: #555;");
    }

    private void noHighlight(){
      documentsBtn.setStyle("-fx-background-color: transparent;");
      askQuestionBtn.setStyle("-fx-background-color: transparent;");
      libraryBtn.setStyle("-fx-background-color: transparent;");
      settingsBtn.setStyle("-fx-background-color: transparent;");
      profileBtn.setStyle("-fx-background-color: transparent;");
    }
    
}
