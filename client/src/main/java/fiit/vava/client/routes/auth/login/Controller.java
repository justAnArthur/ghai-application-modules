package fiit.vava.client.routes.auth.login;

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
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class Controller {

    @FXML
    private MFXPasswordField passwordField;

    @FXML
    private MFXButton signInBtn;

    @FXML
    private MFXButton signUpBtn;

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
        usernameField.setPromptText(bundle.getString("usr_name"));
        passwordField.setPromptText(bundle.getString("usr_pass"));
    }

    @FXML
    private void handleLogin() throws IOException {
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
}