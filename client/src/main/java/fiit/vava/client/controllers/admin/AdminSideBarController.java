package fiit.vava.client.controllers.admin;

import fiit.vava.client.Router;
import javafx.scene.control.Button;

import java.io.IOException;

public class AdminSideBarController {

    public Button templates;

    public void handleTemplates() throws IOException {
        Router.getInstance().navigateTo("admin/templates");
    }

    public void initialize() {
        loadTexts();
    }

    public void loadTexts() {
        // TODO
    }
}
