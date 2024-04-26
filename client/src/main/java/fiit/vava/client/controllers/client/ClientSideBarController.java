package fiit.vava.client.controllers.client;

import fiit.vava.client.bundles.XMLResourceBundle;
import fiit.vava.client.bundles.XMLResourceBundleProvider;
import fiit.vava.client.Router;
import javafx.scene.control.Button;

import java.io.IOException;

public class ClientSideBarController {

    public Button documents;
    public Button documentTemplates;

    XMLResourceBundleProvider instance;


    public ClientSideBarController() {
        this.instance = XMLResourceBundleProvider.getInstance();
    }
    public void handleDocuments() throws IOException {
        Router.getInstance().navigateTo("client/documents");
    }

    public void handleDocumentTemplates() throws IOException {
        Router.getInstance().navigateTo("client/templates");
    }

    public void initialize() {
        loadTexts();
        instance.subscribe(language -> loadTexts());
    }

    private void loadTexts() {
        XMLResourceBundle bundle = instance.getBundle("fiit.vava.client.bundles.client");

        if (bundle == null)
            return;
        documents.setText(bundle.getString("sidebar.button.documents")); 
        documentTemplates.setText(bundle.getString("sidebar.button.templates")); 
    }
}
