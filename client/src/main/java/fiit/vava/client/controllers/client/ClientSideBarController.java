package fiit.vava.client.controllers.client;

import fiit.vava.client.Router;
import javafx.scene.control.Button;

import java.io.IOException;

public class ClientSideBarController {

    public Button documents;
    public Button documentTemplates;

    public void handleDocuments() throws IOException {
        Router.getInstance().navigateTo("client/documents");
    }

    public void handleDocumentTemplates() throws IOException {
        Router.getInstance().navigateTo("client/templates");
    }

    public void initialize() {
        loadTexts();
    }

    private void loadTexts() {
        // TODO: Load texts
    }
}
