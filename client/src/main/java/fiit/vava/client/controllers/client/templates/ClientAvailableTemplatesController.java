package fiit.vava.client.controllers.client.templates;


import fiit.vava.client.Router;
import fiit.vava.client.StubsManager;
import fiit.vava.client.controllers._components.page.card.DocumentCardController;
import fiit.vava.server.DocumentServiceGrpc;
import fiit.vava.server.DocumentTemplate;
import fiit.vava.server.Empty;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.io.IOException;
import java.util.List;


public class ClientAvailableTemplatesController {

    public GridPane availableTemplates;

    public void initialize() {
        loadData();
    }

    private void loadData() {
        DocumentServiceGrpc.DocumentServiceBlockingStub stub = StubsManager.getInstance().getDocumentServiceBlockingStub();
        List<DocumentTemplate> templates = stub.getAllDocumentTemplates(Empty.newBuilder().build()).getTemplatesList();

        templates.stream()
                .filter(template -> !template.getPrivate())
                .forEach(template -> {
                    Pair<Node, DocumentCardController> pair = DocumentCardController.generateNodeFromDocument(template);

                    int size = availableTemplates.getChildren().size();

                    Node node = pair.getKey();

                    node.setOnMouseClicked(event -> {
                        try {
                            Router.getInstance().navigateTo("client/templates/createBy/" + template.getId());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                    availableTemplates.add(node, (int) size % 4, (int) size / 4);
                });
    }
}
