package fiit.vava.client.controllers.coworker;

import fiit.vava.client.Router;
import fiit.vava.client.bundles.XMLResourceBundle;
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
        instance.subscribe(language -> loadTexts());
    }

    private void loadTexts() {
        XMLResourceBundle bundle = instance.getBundle("fiit.vava.client.bundles.coworker");

        if (bundle == null)
            return;
        documentRequests.setText(bundle.getString("sidebar.button.documents")); 
        clientsApproving.setText(bundle.getString("sidebar.button.clients")); 
    }
}
