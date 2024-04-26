package fiit.vava.client.controllers.admin.templates;

import fiit.vava.client.bundles.XMLResourceBundle;
import fiit.vava.client.bundles.XMLResourceBundleProvider;
import fiit.vava.client.Router;
import fiit.vava.client.StubsManager;
import fiit.vava.client.controllers._components.page.card.DocumentCardController;
import fiit.vava.server.DocumentServiceGrpc;
import fiit.vava.server.Empty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.io.IOException;

public class AdminTemplatesController {
    
    public Button createTemplate;
    public Label allTemplatesLabel;
    public Label allTemplatesText;
    public GridPane templates;

    public void handleCreateTemplate() throws IOException {
        Router.getInstance().navigateTo("admin/templates/create");
    }

    XMLResourceBundleProvider instance;

    public AdminTemplatesController() {
        this.instance = XMLResourceBundleProvider.getInstance();
    }

    public void initialize() {
      loadTexts();

      instance.subscribe(language -> loadTexts());
      loadTemplates();
    }

    public void loadTemplates() {
        DocumentServiceGrpc.DocumentServiceBlockingStub stub = StubsManager.getInstance().getDocumentServiceBlockingStub();

        stub.getAllDocumentTemplates(Empty.newBuilder().build()).getTemplatesList()
                .forEach(template -> {
                    Pair<Node, DocumentCardController> pair = DocumentCardController.generateNodeFromDocument(template);

                    int size = templates.getChildren().size();

                    templates.add(pair.getKey(), (int) size % 4, (int) size / 4);
                });
    }

    private void loadTexts() {
        XMLResourceBundle bundle = instance.getBundle("fiit.vava.client.bundles.admin");

        if (bundle == null)
            return;
        allTemplatesText.setText(bundle.getString("templates.label.text")); 
        allTemplatesLabel.setText(bundle.getString("templates.label.label")); 
        createTemplate.setText(bundle.getString("templates.button.create")); 
    }
}
