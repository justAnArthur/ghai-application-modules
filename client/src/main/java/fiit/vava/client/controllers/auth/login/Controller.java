package fiit.vava.client.controllers.auth.login;

import fiit.vava.client.CredentialsManager;
import fiit.vava.client.Router;
import fiit.vava.client.StubsManager;
import fiit.vava.client.bundles.SupportedLanguages;
import fiit.vava.client.bundles.XMLResourceBundle;
import fiit.vava.client.bundles.XMLResourceBundleProvider;
import fiit.vava.server.Empty;
import fiit.vava.server.User;
import fiit.vava.server.UserRole;
import fiit.vava.server.UserServiceGrpc;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class Controller {
    private boolean offline = true;
    @FXML
    private MFXPasswordField passwordField;

    @FXML
    private Button signInBtn;

    @FXML
    private Button signUpBtn;

    @FXML
    private MFXTextField usernameField;

    @FXML
    private Label errorMessageLabel;

    @FXML
    private MFXComboBox<String> langSelectCombo;

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

        signInBtn.setText(bundle.getString("sign_in"));
        // usernameField.setPromptText(bundle.getString("usr_name"));
        // passwordField.setPromptText(bundle.getString("usr_pass"));
    }

    @FXML
    private void handleLogin() throws IOException {
        if (offline) {
            Router.getInstance().navigateTo(usernameField.getText());
            return;
        }

        errorMessageLabel.setText(null);

        CredentialsManager.storeCredentials(usernameField.getText(), passwordField.getText());

        UserServiceGrpc.UserServiceBlockingStub stub = StubsManager.getInstance().getUserServiceBlockingStub();

        User user = stub.me(Empty.newBuilder().build());

        if (user.getRole().equals(UserRole.CLIENT) && !user.getConfirmed()) {
            errorMessageLabel.setText("user is not confirmed");
            return;
        }

        Router.getInstance().navigateTo(user.getRole().name().toLowerCase());
    }

    @FXML
    private void goToRegistration() throws IOException {
        Router.getInstance().navigateTo("auth/registration");
    }
}
