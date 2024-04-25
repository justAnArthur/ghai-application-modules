package fiit.vava.client.controllers.coworker;

import fiit.vava.client.Router;
import fiit.vava.client.bundles.XMLResourceBundleProvider;
import javafx.scene.control.Button;

import java.io.IOException;

public class CoworkerSideBarController {


    public Button documentRequests;
    public Button clientsApproving;

    XMLResourceBundleProvider instance;


    public CoworkerSideBarController() {
        this.instance = XMLResourceBundleProvider.getInstance();
    }

    public void handleDocumentRequests() throws IOException {
        Router.getInstance().navigateTo("coworker/documentRequests");
    }

    public void handleClientsApproving() throws IOException {
        Router.getInstance().navigateTo("coworker/clients");
    }

    public void initialize() {
        loadTexts();
    }

    private void loadTexts() {
        // TODO: Load texts from XML
    }
}
