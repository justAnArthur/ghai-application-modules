package fiit.vava.client.controllers.auth;

import fiit.vava.client.Router;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class AwaitingOnConfirmation {
    public Button goToLogin;
    public Label textLabel;

    public void handleGoToLogin() throws IOException {
        Router.getInstance().navigateTo("auth/login");
    }
}
