package fiit.vava.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class Router {
    private Stage primaryStage;
    private final Map<String, String> routes;

    private static Router instance;

    private Router() {
        routes = new HashMap<>();
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

    public void addRoute(String route, String fxmlPath) {
        routes.put(route, fxmlPath);
    }

    public void navigateTo(String route) {
        String fxmlPath = routes.get(route);
        if (fxmlPath != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                primaryStage.setScene(scene);
                primaryStage.setTitle(route);
                primaryStage.show();
            } catch (IOException ignored) {
            }
        } else {
            throw new NoSuchElementException("No route defined for: " + route);
        }
    }
}