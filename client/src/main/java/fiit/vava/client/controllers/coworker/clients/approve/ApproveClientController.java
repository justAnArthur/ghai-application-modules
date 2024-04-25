package fiit.vava.client.controllers.coworker.clients.approve;

import fiit.vava.client.Router;
import fiit.vava.client.StubsManager;
import fiit.vava.client.controllers._components.page.card.DocumentCardController;
import fiit.vava.server.DocumentRequest;
import fiit.vava.server.DocumentServiceGrpc;
import fiit.vava.server.GetDocumentRequestByClientIdRequest;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.List;

public class ApproveClientController {
    public GridPane clientsDocuments;

    public void initialize() {
        loadData();
    }

    private void loadData() {
        DocumentServiceGrpc.DocumentServiceBlockingStub stub = StubsManager.getInstance().getDocumentServiceBlockingStub();

        String clientId = Router.getInstance().getParameter("clientId");

        List<DocumentRequest> documentRequests = stub.getDocumentRequestsByClientId(GetDocumentRequestByClientIdRequest.newBuilder()
                .setClientId(clientId).build()).getDocumentRequestsList();

        documentRequests.forEach(documentRequest -> {
            Pair<Node, DocumentCardController> pair = DocumentCardController.generateNodeFromDocument(documentRequest);

            int size = clientsDocuments.getChildren().size();

            clientsDocuments.add(pair.getKey(), (int) size % 4, (int) size / 4);
        });
    }
}
