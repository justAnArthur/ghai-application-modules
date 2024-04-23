package fiit.vava.client.controllers.profile;

import fiit.vava.client.CredentialsManager;
import fiit.vava.client.Router;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private MFXTextField countryEdit;

    @FXML
    private Label countryView;

    @FXML
    private GridPane dateOfBirthEdit;

    @FXML
    private Label dateOfBirthView;

    @FXML
    private MFXTextField dateOfRegEdit;

    @FXML
    private Label dateOfRegView;

    @FXML
    private MFXTextField fNameEdit;

    @FXML
    private Label fNameVIew;

    @FXML
    private MFXTextField lNameEdit;

    @FXML
    private Label lNameView;

    @FXML
    private MFXTextField regionEdit;

    @FXML
    private Label regionView;

    @FXML
    private Button saveBtn;

    @FXML
    void initialize() {

    }
    
    @FXML 
    private void handleSaveBtn(ActionEvent event) {
        System.out.println("Save button handler is empty");
    }
    
    @FXML 
    private void handleLogOut(){
        try {
            CredentialsManager.removeCredentials();
            Router.getInstance().removeSidebar();
            Router.getInstance().navigateTo("auth/login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
