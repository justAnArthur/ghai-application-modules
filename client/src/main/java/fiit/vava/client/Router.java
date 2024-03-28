package fiit.vava.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Router {
    private Stage primaryStage;
    private final Map<String, Path> routes;
    private final List<String> routesHistory;

    private static Router instance;

    private Router() {
        routes = new HashMap<>();
        routesHistory = new ArrayList<>();
    }

    public static Router getInstance() {
        if (instance == null) {
            instance = new Router();
        }
        return instance;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void addRoute(String route, Path path) {
        routes.put(route, path);
    }

    public void navigateTo(String route) throws IOException, NoSuchElementException {
        Path path = routes.get(route);
        if (path != null) {
            System.out.println("Navigating to: " + route + " : " + path);

            routesHistory.add(route);

            FXMLLoader loader = new FXMLLoader(path.toUri().toURL());
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle(route);
            primaryStage.show();
        } else {
            throw new NoSuchElementException("No route defined for: " + route);
        }
    }

    public void navigateBack() throws IOException, NoSuchElementException {
        if (routesHistory.size() <= 1)
            return;

        routesHistory.remove(routesHistory.size() - 1);
        String previousRoute = routesHistory.get(routesHistory.size() - 1);

        navigateTo(previousRoute);
    }
}