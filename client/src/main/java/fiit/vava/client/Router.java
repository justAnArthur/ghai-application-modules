package fiit.vava.client;

import fiit.vava.client.routes.AppController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Router {
    private static final Logger logger = LoggerFactory.getLogger(Router.class.toString());

    private AppController appController;
    private final Stage modalStage = new Stage() {{
        initModality(Modality.APPLICATION_MODAL);
    }};

    private final Map<String, Path> routes = new HashMap<>();
    private final List<String> routesHistory = new ArrayList<>();

    private static Router instance;

    private Router() {
        try {
            loadRoutes();
        } catch (IOException e) {
            logger.warn("Unable to load routes" + e);
        }
    }

    public static Router getInstance() {
        if (instance == null) {
            instance = new Router();
        }
        return instance;
    }

    private void loadRoutes() throws IOException {
        Path start = Paths.get("src/main/java/fiit/vava/client/routes");


        try (Stream<Path> paths = Files.walk(start)) {
            paths.filter(Files::isRegularFile)
                    .filter(_path -> !_path.toString().contains("_")) // filter `_components`
                    .filter(_path -> _path.toString().endsWith(".fxml")) // filter only `.fxml`s
                    .forEach(_path -> {
                        String route = start.relativize(_path).toString()
                                .replaceAll("\\\\", "/")
                                .replaceAll(".fxml", "")
                                .replaceAll("/index", "");
                        routes.put(route, _path);
                    });
        } catch (IOException e) {
            logger.warn("Unable to load routes" + e);
        }

        logger.info("Routes loaded: " + routes);
    }

    public void loadApp(Stage stage) throws IOException {
        Path path = routes.get("app");

        FXMLLoader loader = new FXMLLoader(path.toUri().toURL());
        Parent root = loader.load();

        stage.setTitle("GHAI");
        stage.setScene(new Scene(root, 1280, 800));
        stage.show();
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    public void navigateTo(String route) throws IOException, NoSuchElementException {
        Path path = routes.get(route);

        routesHistory.add(route);

        if (path == null)
            throw new NoSuchElementException("Route not found: " + route);

        FXMLLoader loader = new FXMLLoader(path.toUri().toURL());
        Node page = loader.load();

        logger.info("Navigating to: " + route);

        appController.setCenter(page);
    }

    public void navigateBack() throws IOException, NoSuchElementException {
        if (routesHistory.size() <= 1)
            return;

        routesHistory.remove(routesHistory.size() - 1);
        String previousRoute = routesHistory.get(routesHistory.size() - 1);

        navigateTo(previousRoute);
    }

    public void changeNavBar(String route) throws IOException {
        Path path = routes.get(route);

        FXMLLoader loader = new FXMLLoader(path.toUri().toURL());
        Node page = loader.load();

        appController.setSidebar(page);
    }

    public void showModal(String route) throws IOException {
        Path path = routes.get(route);

        FXMLLoader dialogLoader = new FXMLLoader(path.toUri().toURL());
        Parent dialogRoot = dialogLoader.load();

        modalStage.setScene(new Scene(dialogRoot));
        modalStage.showAndWait();
    }

    public void hideModal() {
        modalStage.hide();
    }
}