package fiit.vava.client.controllers;

import fiit.vava.client.CredentialsManager;
import fiit.vava.client.Router;
import fiit.vava.client.StubsManager;
import fiit.vava.server.Empty;
import fiit.vava.server.User;
import fiit.vava.server.UserServiceGrpc;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class AppController {

    @FXML
    private BorderPane borderPane;

    @FXML
    void initialize() throws IOException {
        Router router = Router.getInstance();
        router.setAppController(this);

        String startingScenePath = "auth/login";

        String[] credentials = CredentialsManager.retrieveCredentials();
        for (String str : credentials) {
          System.out.println(str);
        }
        if (credentials != null) {
            UserServiceGrpc.UserServiceBlockingStub stub = StubsManager.getInstance().getUserServiceBlockingStub();

            try {
                User user = stub.me(Empty.newBuilder().build());
                startingScenePath = user.getRole().name().toLowerCase();
            } catch (Exception ex) {
                System.out.println("Unable to call me" + ex);
            }
        }

        router.navigateTo(startingScenePath);
    }

    public void setCenter(Node page) {
        borderPane.setCenter(page);
    }

    public void setSidebar(Node node) {
        borderPane.setLeft(node);
    }
}
