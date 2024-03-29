package fiit.vava.client.controllers.auth.registerClient_Legacy;

import fiit.vava.client.CredentialsManager;
import fiit.vava.client.Router;
import fiit.vava.client.StubsManager;
import fiit.vava.server.*;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginRegisterController {
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Label messageLabel;


    @FXML
    public TextField registerUsernameField;
    @FXML
    public PasswordField registerPasswordField;
    @FXML
    public Label registerMessageLabel;
    @FXML
    public TextField registerFirstNameField;
    @FXML
    public TextField registerLastNameField;
    @FXML
    public DatePicker registerDateOfBirth;
    @FXML
    public TextField registerRegionField;
    @FXML
    public TextField registerCountryField;

    @FXML
    protected void handleLogin() {
        messageLabel.setText(null);

        CredentialsManager.storeCredentials(usernameField.getText(), passwordField.getText());

        UserServiceGrpc.UserServiceBlockingStub stub = StubsManager.getInstance().getUserServiceBlockingStub();

        try {
            User user = stub.me(Empty.newBuilder().build());

            if (user.getRole().equals(UserRole.CLIENT) && !user.getConfirmed())
                throw new Exception("User is not confirmed");

            Router.getInstance().navigateTo(user.getRole().name().toLowerCase() + "/index");
        } catch (Exception ex) {
            ex.printStackTrace();
            messageLabel.setText(ex.getMessage());
        }
    }

    @FXML
    public void handleRegisterClient() {
        messageLabel.setText(null);

        UserServiceGrpc.UserServiceBlockingStub stub = StubsManager.getInstance().getUserServiceBlockingStub();

        Client request = Client.newBuilder()
                .setUser(User.newBuilder()
                        .setEmail(registerUsernameField.getText())
                        .setPassword(registerPasswordField.getText())
                        .build()
                )
                .setFirstName(registerFirstNameField.getText())
                .setLastName(registerLastNameField.getText())
                .setDateOfBirth(registerDateOfBirth.getValue().toString())
                .setRegion(registerRegionField.getText())
                .setCountry(registerCountryField.getText())
                .build();

        Response response = stub.registerClient(request);

        if (response.getUser() != null) {
            registerMessageLabel.setText("Status: " + response.getUser().getStatus().name());
        } else {
            registerMessageLabel.setText(response.getError());
        }
    }
}
