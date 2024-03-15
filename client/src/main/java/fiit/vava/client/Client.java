package fiit.vava.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Client extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Client.class.getResource("loginRegister.fxml"));
        Parent root = loader.load();

        stage.setTitle("GHAI");
        stage.setScene(new Scene(root, 320, 240));
        stage.show();

        Router router = Router.getInstance();
        router.setPrimaryStage(stage);

        router.addRoute("loginRegister", "loginRegister.fxml");
        router.addRoute("admin", "admin.fxml");
        router.addRoute("coworker", "coworker.fxml");
        router.addRoute("client", "client.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}