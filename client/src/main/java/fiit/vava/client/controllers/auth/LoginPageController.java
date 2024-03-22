package fiit.vava.client.controllers.auth;

import fiit.vava.client.Router;
import fiit.vava.client.callers.BearerToken;
import fiit.vava.server.*;
import fiit.vava.server.config.Constants;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.scene.control.Label;
import javafx.fxml.FXML;

public class LoginPageController {

    @FXML
    private MFXPasswordField passwordField;

    @FXML
    private MFXButton signInBtn;

    @FXML
    private MFXButton signUpBtn;

    @FXML
    private MFXTextField usernameField;

    @FXML
    private Label messageLabel;
    
    @FXML
    private void handleLogin(){
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
}
