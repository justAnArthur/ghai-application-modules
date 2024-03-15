package fiit.vava.client.controllers;

import fiit.vava.client.Router;
import fiit.vava.client.callers.BearerToken;
import fiit.vava.server.*;
import fiit.vava.server.config.Constants;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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

        BearerToken token = new BearerToken(
                Jwts.builder()
                        .setSubject(usernameField.getText())
                        .claim("password", passwordField.getText())
                        .signWith(SignatureAlgorithm.HS256, Constants.JWT_SIGNING_KEY)
                        .compact()
        );

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", Server.PORT)
                .usePlaintext()
                .build();

        UserServiceGrpc.UserServiceBlockingStub stub = UserServiceGrpc
                .newBlockingStub(channel)
                .withCallCredentials(token);

        try {
            User user = stub.me(Empty.newBuilder().build());

            if (user.getRole().equals(UserRole.CLIENT) && !user.getConfirmed())
                throw new Exception("User is not confirmed");

            Router.getInstance().navigateTo(user.getRole().name().toLowerCase());
        } catch (Exception ex) {
            messageLabel.setText(ex.getMessage());
        }
    }

    public void handleRegisterClient() {
        messageLabel.setText(null);

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", Server.PORT)
                .usePlaintext()
                .build();

        UserServiceGrpc.UserServiceBlockingStub stub = UserServiceGrpc
                .newBlockingStub(channel);

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