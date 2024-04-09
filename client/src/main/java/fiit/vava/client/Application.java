package fiit.vava.client;

import io.github.palexdev.materialfx.theming.JavaFXThemes;
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets;
import io.github.palexdev.materialfx.theming.UserAgentBuilder;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Application extends javafx.application.Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class.toString());

    public void initStyles() {
        UserAgentBuilder.builder()
                .themes(JavaFXThemes.MODENA) // Optionally, if you don't need JavaFX's default theme, still recommended though
                .themes(MaterialFXStylesheets.forAssemble(true)) // Adds the MaterialFX's default theme. The boolean argument is to include legacy controls
                .setDeploy(true) // Whether to deploy each theme's assets on a temporary dir on the disk
                .setResolveAssets(true) // Whether to try resolving @import statements and resources urls
                .build() // Assembles all the added themes into a single CSSFragment (very powerful class check its documentation)
                .setGlobal(); // Finally, sets the produced stylesheet as the global User-Agent stylesheet
    }

    @Override
    public void start(Stage stage) throws IOException {
        initStyles();

        Router.getInstance().loadApp(stage);
    }

    public static void main(String[] args) {
        logger.info("Client started");
        launch();
    }
}
