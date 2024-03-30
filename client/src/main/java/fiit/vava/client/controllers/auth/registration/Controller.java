package fiit.vava.client.controllers.auth.registration;

import fiit.vava.client.bundles.SupportedLanguages;
import fiit.vava.client.bundles.XMLResourceBundle;
import fiit.vava.client.bundles.XMLResourceBundleProvider;
import fiit.vava.client.CredentialsManager;
import fiit.vava.client.Router;
import fiit.vava.client.StubsManager;
import fiit.vava.server.*;
import fiit.vava.client.Router;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class Controller {
 @FXML
    private TextField countryField;

    @FXML
    private DatePicker dateOfBirthPicker;

    @FXML
    private TextField emailField;

    @FXML
    private Label errorMessageLabel;

    @FXML
    private TextField fNameField;

    @FXML
    private TextField lNameField;

    @FXML
    private MFXComboBox<String> langSelectCombo;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField regionField;

    @FXML
    private Button signInBtn;

    @FXML
    private Button signUpBtn;

    @FXML
    public void initialize() {
        langSelectCombo.getItems().addAll(SupportedLanguages.asList().stream().map(SupportedLanguages::name).toList());
        langSelectCombo.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            // TODO exchange on @FXML controller method
            XMLResourceBundleProvider.getInstance().changeLanguage(SupportedLanguages.valueOf(newValue));
            loadTexts();
        });
        loadTexts();
    }

    private void loadTexts() {
        XMLResourceBundle bundle = XMLResourceBundleProvider.getInstance().getBundle("fiit.vava.client.bundles.auth.messages");

    }
    @FXML
    private void goToLogin(ActionEvent event) throws IOException {
      Router.getInstance().navigateTo("auth/login");
    }

    // Doesn't work
    @FXML
    private void handleRegistration(ActionEvent event) {
        errorMessageLabel.setText(null);

        UserServiceGrpc.UserServiceBlockingStub stub = StubsManager.getInstance().getUserServiceBlockingStub();

        Client request = Client.newBuilder()
                .setUser(User.newBuilder()
                        .setEmail(emailField.getText())
                        .setPassword(passwordField.getText())
                        .build()
                )
                .setFirstName(fNameField.getText())
                .setLastName(lNameField.getText())
                .setDateOfBirth(dateOfBirthPicker.getValue().toString())
                .setRegion(regionField.getText())
                .setCountry(countryField.getText())
                .build();

        Response response = stub.registerClient(request);

        if (response.getUser() != null) {
            errorMessageLabel.setText("Status: " + response.getUser().getStatus().name());
        } else {
            errorMessageLabel.setText(response.getError());
        }
    }
}
