package fiit.vava.client;

import fiit.vava.server.Empty;
import fiit.vava.server.User;
import fiit.vava.server.UserServiceGrpc;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Client extends Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class.toString());

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

        logger.info("Starting scene: " + startingScenePath);

        stage.setTitle("GHAI");

        Router router = Router.getInstance();
        router.setPrimaryStage(stage);

        getRoutes().forEach(router::addRoute);

        router.navigateTo(startingScenePath);
    }

    public Map<String, Path> getRoutes() throws IOException {
        Path start = Paths.get("src/main/java/fiit/vava/client/routes");

        Map<String, Path> routes = new HashMap<>();

        Files.walk(start)
                .filter(Files::isRegularFile)
                .filter(_path -> !_path.toString().contains("_")) // filter `_components`
                .filter(_path -> _path.toString().endsWith(".fxml")) // filter only `.fxml`s
                .forEach(_path -> {
                    String route = start.relativize(_path).toString()
                            .replaceAll("\\\\", "/")
                            .replaceAll(".fxml", "");
                    routes.put(route, _path);
                });

        return routes;
    }

    public static void main(String[] args) {
        logger.info("Client started");
        launch();
    }
}