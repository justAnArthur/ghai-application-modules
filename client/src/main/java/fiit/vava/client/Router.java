package fiit.vava.client;

import fiit.vava.client.controllers.AppController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Router {
    private static final Logger logger = LoggerFactory.getLogger("client." + Router.class.toString());

    private AppController appController;
    private final Stage modalStage = new Stage() {{
        initModality(Modality.APPLICATION_MODAL);
    }};

    private final Map<String, String> parameters = new HashMap<>();
    private final Map<String, URL> routes = new HashMap<>();
    private final List<String> routesHistory = new ArrayList<>();

    private static Router instance;
    private static Stage stage;

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
        String resourceDirectory = "/fiit/vava/client/fxml";
        URL dirURL = getClass().getResource(resourceDirectory);

        if (dirURL != null && dirURL.getProtocol().equals("jar")) {
            try {
                // If we're running from a JAR, the entries will be listed by the JarFile
                JarURLConnection jarConnection = (JarURLConnection) dirURL.openConnection();
                JarFile jarFile = jarConnection.getJarFile();
                Enumeration<JarEntry> entries = jarFile.entries();

                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    if (name.startsWith(resourceDirectory.substring(1)) && name.endsWith(".fxml")) {
                        addRouteFromResourceName(name, resourceDirectory.substring(1));
                    }
                }
            } catch (IOException e) {
                logger.warn("Unable to load routes from JAR", e);
            }
        } else if (dirURL != null) {
            // If we're running from the filesystem (e.g., from an IDE), use Files.walk
            try {
                Path start = Paths.get(dirURL.toURI());
                Files.walk(start)
                        .filter(Files::isRegularFile)
                        .forEach(path -> addRouteFromPath(path, start));
            } catch (URISyntaxException e) {
                logger.warn("Unable to load routes from filesystem", e);
            }
        } else {
            logger.warn("Resource directory not found: " + resourceDirectory);
        }

        logger.info("Routes loaded: " + routes);
    }

    private void addRouteFromResourceName(String resourceName, String resourceDirectory) {
        String route = resourceName.replaceAll("^" + resourceDirectory + "/", "").replaceAll(".fxml$", "");
        if (route.endsWith("/index")) {
            route = route.substring(0, route.length() - 6); // Removing "/index" from the end
        }
        if (!route.contains("_") && !route.endsWith("index")) {
            routes.put(route, getClass().getResource("/" + resourceName));
        }
    }

    private void addRouteFromPath(Path path, Path start) {
        try {
            String route = start.relativize(path).toString()
                    .replaceAll("\\\\", "/")
                    .replaceAll(".fxml$", "")
                    .replaceAll("/index$", "");
            if (!route.contains("_")) {
                routes.put(route, path.toUri().toURL());
            }
        } catch (MalformedURLException e) {
            logger.warn("Malformed URL for path: " + path, e);
        }
    }

    public void loadApp(Stage stage) throws IOException {
        this.stage = stage;
        URL path = routes.get("app");
        FXMLLoader loader = new FXMLLoader(path);
        Parent root = loader.load();
        stage.setTitle("GHAI");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    public void navigateTo(String route) throws IOException, NoSuchElementException {
        String pathString = routes.keySet().stream()
                .filter(_route -> {
                    String[] routeParts = route.split("/");
                    String[] _pathParts = _route.split("/");

                    if (routeParts.length != _pathParts.length)
                        return false;

                    for (int i = 0; i < routeParts.length; i++) {
                        if (_pathParts[i].startsWith("[") && _pathParts[i].endsWith("]")) {
                            parameters.put(_pathParts[i].substring(1, _pathParts[i].length() - 1), routeParts[i]);
                            continue;
                        }

                        if (!routeParts[i].equals(_pathParts[i]))
                            return false;
                    }

                    return true;
                })
                .findFirst()
                .orElse(null);

        URL path = routes.get(pathString);

        if (path == null)
            throw new NoSuchElementException("Route not found: " + route);

        routesHistory.add(route);

        FXMLLoader loader = new FXMLLoader(path);
        Node page = loader.load();

        logger.info("Navigating to: " + route);

        appController.setCenter(page);
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public void navigateBack() throws IOException, NoSuchElementException {
        if (routesHistory.size() <= 1)
            return;

        routesHistory.remove(routesHistory.size() - 1);
        String previousRoute = routesHistory.get(routesHistory.size() - 1);

        navigateTo(previousRoute);
    }

    public void changeNavBar(String route) throws IOException {
        stage.setMinWidth(1280);
        stage.setMinHeight(800);
        URL path = routes.get(route);

        FXMLLoader loader = new FXMLLoader(path);
        Node page = loader.load();

        appController.setSidebar(page);
    }

    public void showModal(String route) throws IOException {
        URL path = routes.get(route);

        FXMLLoader dialogLoader = new FXMLLoader(path);
        Parent dialogRoot = dialogLoader.load();

        modalStage.setScene(new Scene(dialogRoot));
        modalStage.showAndWait();
    }

    public void hideModal() {
        modalStage.hide();
    }
}
