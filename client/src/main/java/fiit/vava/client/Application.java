package fiit.vava.client;

import io.github.palexdev.materialfx.theming.JavaFXThemes;
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets;
import io.github.palexdev.materialfx.theming.UserAgentBuilder;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Application extends javafx.application.Application {

    private static final Logger logger = LoggerFactory.getLogger("client." + Application.class);

    public void initStyles() {
        UserAgentBuilder.builder()
                .themes(JavaFXThemes.MODENA)
                .themes(MaterialFXStylesheets.forAssemble(true)) // Adds the MaterialFX's default theme. The boolean argument is to include legacy controls
                .setDeploy(true)
                .setResolveAssets(true)
                .build()
                .setGlobal();
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
