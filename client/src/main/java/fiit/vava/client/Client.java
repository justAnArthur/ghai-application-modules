package fiit.vava.client;

import fiit.vava.server.Empty;
import fiit.vava.server.User;
import fiit.vava.server.UserServiceGrpc;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class Client extends Application {

    private static final Logger logger = LoggerFactory.getLogger("client");
    private static final String FXML_RESOURCES_PATH = "src/main/resources/fiit/vava/client";

    @Override
    public void start(Stage stage) throws IOException {
        String startingScenePath = "loginRegister";

        String[] credentials = CredentialsManager.retrieveCredentials();

        if (credentials != null) {
            UserServiceGrpc.UserServiceBlockingStub stub = StubsManager.getInstance().getUserServiceBlockingStub();

            try {
                User user = stub.me(Empty.newBuilder().build());
                startingScenePath = user.getRole().name().toLowerCase() + "/index";
            } catch (Exception ex) {
                System.out.println("Unable to call me" + ex);
            }
        }

        System.out.println(startingScenePath);

        stage.setTitle("GHAI");

        Router router = Router.getInstance();
        router.setPrimaryStage(stage);

        getRoutes(FXML_RESOURCES_PATH)
                .forEach(path -> router.addRoute(path.replaceAll(".fxml", ""), path));

        router.navigateTo(startingScenePath);
    }

    public List<String> getRoutes(String path) throws IOException {
        Path start = Paths.get(path);

        return Files.walk(start)
                .filter(Files::isRegularFile)
                .map(_path -> Path.of(path).relativize(_path).toString().replaceAll("\\\\", "/"))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        logger.info("Client started");
        launch();
    }
}