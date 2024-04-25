package fiit.vava.client.controllers.auth.login;

import fiit.vava.client.CredentialsManager;
import fiit.vava.client.Router;
import fiit.vava.client.StubsManager;
import fiit.vava.client.bundles.SupportedLanguages;
import fiit.vava.client.bundles.XMLResourceBundle;
import fiit.vava.client.bundles.XMLResourceBundleProvider;
import fiit.vava.client.controllers.auth.Validation;
import fiit.vava.server.Empty;
import fiit.vava.server.User;
import fiit.vava.server.UserRole;
import fiit.vava.server.UserServiceGrpc;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    public Label labelHeaderLogin;
    public Label labelEmail;
    public TextField email;
    public Label labelPassword;
    public PasswordField password;
    public Label labelGoToRegistration;
    public Button goToRegistration;
    public Button signIn;
    public Label labelError;

    XMLResourceBundleProvider instance;

    public LoginController() {
        this.instance = XMLResourceBundleProvider.getInstance();
    }

    public void handleGoToRegistration() throws IOException {
        Router.getInstance().navigateTo("auth/registration");
    }

    public void handleSignIn() {
        labelError.setText(null);

        try {
            checkFields();

            CredentialsManager.storeCredentials(email.getText(), password.getText());

            UserServiceGrpc.UserServiceBlockingStub stub = StubsManager.getInstance().getUserServiceBlockingStub();

            User user = stub.me(Empty.newBuilder().build());

            if (user.getRole().equals(UserRole.CLIENT) && !user.getConfirmed()) {
                Router.getInstance().navigateTo("auth/awaitingOnConfirmation");
                return;
            }

            Router.setRole(user.getRole().name().toLowerCase());
            Router.getInstance().navigateTo(Router.getRole());
        } catch (Exception e) {
            e.printStackTrace();
            labelError.setText(e.getMessage());
        }
    }

    public void initialize() {
        loadTexts(instance);

        instance.subscribe(new XMLResourceBundleProvider.OnChangeValueListener() {
            @Override
            public void onChangeValue(SupportedLanguages language) {
                loadTexts(instance);
            }
        });
    }

    private void loadTexts(XMLResourceBundleProvider instance) {
        XMLResourceBundle bundle = instance.getBundle("fiit.vava.client.bundles.auth");

        if (bundle == null)
            return;

        labelEmail.setText(bundle.getString("label.email"));
        labelPassword.setText(bundle.getString("label.password"));
        email.setPromptText(bundle.getString("label.email"));
        password.setPromptText(bundle.getString("label.password"));

        labelGoToRegistration.setText(bundle.getString("label.goToRegistration"));
        goToRegistration.setText(bundle.getString("button.goToRegistration"));
        labelHeaderLogin.setText(bundle.getString("header.login"));
        signIn.setText(bundle.getString("button.signIn"));
        labelHeaderLogin.setText(bundle.getString("header.login"));
    }

    private void checkFields() throws Exception {
        String error = Validation.validateFields(
                Validation.pair("Email is not valid", Validation.notNull, Validation.email)
                        .set(email.getText()),
                Validation.pair("Password is not valid", Validation.notNull/*, Validation.password*/)
                        .set(password.getText())
        );

        if (error != null)
            throw new Exception(error);
    }
}
