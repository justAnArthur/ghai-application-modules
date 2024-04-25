package fiit.vava.client.controllers.admin.templates;

import fiit.vava.client.Router;
import fiit.vava.client.StubsManager;
import fiit.vava.client.controllers._components.page.card.DocumentCardController;
import fiit.vava.server.DocumentServiceGrpc;
import fiit.vava.server.Empty;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.io.IOException;

public class AdminTemplatesController {

    public GridPane templates;

    public void handleCreateTemplate() throws IOException {
        Router.getInstance().navigateTo("admin/templates/create");
    }

    public void initialize() {
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
}
