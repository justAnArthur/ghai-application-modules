package fiit.vava.client.controllers.coworker.profile;

import io.github.palexdev.materialfx.controls.MFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;
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

}
