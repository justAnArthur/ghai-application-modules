package fiit.vava.client.controllers.client.documents;

import fiit.vava.client.Router;
import fiit.vava.client.StubsManager;
import fiit.vava.server.DocumentRequest;
import fiit.vava.server.DocumentServiceGrpc;
import fiit.vava.server.Empty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.List;

public class DocumentsController {

    @FXML
    public GridPane documentsPane;

    private Node createDocumentCard(DocumentRequest documentRequest) {
        VBox card = new VBox() {{
            setBackground(new Background(new BackgroundFill(Color.PINK, CornerRadii.EMPTY, Insets.EMPTY)));
        }};

        Label templateName = new Label(documentRequest.getTemplate().getName());
        Label status = new Label(documentRequest.getStatus().name());

        card.setOnMouseClicked(event -> {
            try {
                // TODO navigate to document or document request ?
                Router.getInstance().navigateTo("client/document" + documentRequest.getDocument().getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        card.getChildren().addAll(templateName, status);

        return card;
    }

    @FXML
    public void initialize() {
        loadDocuments();
    }

    private void loadDocuments() {
        DocumentServiceGrpc.DocumentServiceBlockingStub stub = StubsManager.getInstance().getDocumentServiceBlockingStub();

        List<DocumentRequest> documentRequests = stub.getAllMineDocumentRequests(Empty.newBuilder().build()).getDocumentRequestsList();

        documentRequests.forEach(document ->
                documentsPane.getChildren().add(createDocumentCard(document))
        );
    }
}
