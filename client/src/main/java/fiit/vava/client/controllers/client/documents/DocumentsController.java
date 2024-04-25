package fiit.vava.client.controllers.client.documents;

import fiit.vava.client.StubsManager;
import fiit.vava.client.controllers._components.page.card.DocumentCardController;
import fiit.vava.server.DocumentRequest;
import fiit.vava.server.DocumentServiceGrpc;
import fiit.vava.server.Empty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.List;

public class DocumentsController {

    @FXML
    public GridPane documents;

    @FXML
    public void initialize() {
        loadDocuments();
    }

    private void loadDocuments() {
        DocumentServiceGrpc.DocumentServiceBlockingStub stub = StubsManager.getInstance().getDocumentServiceBlockingStub();

        List<DocumentRequest> documentRequests = stub.getAllMineDocumentRequests(Empty.newBuilder().build()).getDocumentRequestsList();

        documentRequests.stream()
                .filter(documentRequest -> !documentRequest.getTemplate().getPrivate())
                .forEach(documentRequest -> {
                    Pair<Node, DocumentCardController> pair = DocumentCardController.generateNodeFromDocument(documentRequest);

                    int size = documents.getChildren().size();

                    documents.add(pair.getKey(), (int) size % 4, (int) size / 4);
                });
    }
}
