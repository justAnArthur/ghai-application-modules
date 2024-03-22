package fiit.vava.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import io.github.palexdev.materialfx.theming.UserAgentBuilder;
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets;
import io.github.palexdev.materialfx.theming.JavaFXThemes;
import java.io.IOException;

public class Client extends Application {
    @Override
    public void start(Stage stage) throws IOException {
    UserAgentBuilder.builder()
        .themes(JavaFXThemes.MODENA) // Optional if you don't need JavaFX's default theme, still recommended though
        .themes(MaterialFXStylesheets.forAssemble(true)) // Adds the MaterialFX's default theme. The boolean argument is to include legacy controls
        .setDeploy(true) // Whether to deploy each theme's assets on a temporary dir on the disk
        .setResolveAssets(true) // Whether to try resolving @import statements and resources urls
        .build() // Assembles all the added themes into a single CSSFragment (very powerful class check its documentation)
        .setGlobal(); // Finally, sets the produced stylesheet as the global User-Agent stylesheet
        

        FXMLLoader loader = new FXMLLoader(Client.class.getResource("App.fxml"));
        Parent root = loader.load();

        stage.setTitle("GHAI");
        stage.setScene(new Scene(root, 1280, 800));
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
