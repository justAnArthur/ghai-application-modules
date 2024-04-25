package fiit.vava.client.controllers.coworker.documentRequests.approve;

import fiit.vava.client.Router;
import fiit.vava.client.StubsManager;
import fiit.vava.client.controllers._components.page.card.DocumentCardController;
import fiit.vava.server.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class ApproveDocumentRequestController {
    @FXML
    public Label creationDateLabel;
    @FXML
    public Label creatorLabel;
    @FXML
    public Label templateName;
    @FXML
    public TextArea comment;
    @FXML
    public VBox dataInputs;
    @FXML
    public GridPane clientsDocumentRequests;

    @FXML
    public void handleApprove() throws IOException {
        DocumentServiceGrpc.DocumentServiceBlockingStub stub = StubsManager.getInstance().getDocumentServiceBlockingStub();

        stub.approveDocumentRequest(ApproveRejectDocumentRequestRequest.newBuilder()
                .setDocumentRequestId(Router.getInstance().getParameter("documentRequestId")).build());

        Router.getInstance().navigateTo("coworker/documentRequests");
    }

    @FXML
    public void handleReject() throws IOException {
        DocumentServiceGrpc.DocumentServiceBlockingStub stub = StubsManager.getInstance().getDocumentServiceBlockingStub();

        stub.rejectDocumentRequest(ApproveRejectDocumentRequestRequest.newBuilder()
                .setDocumentRequestId(Router.getInstance().getParameter("documentRequestId")).build());

        Router.getInstance().navigateTo("coworker/documentRequests");
    }

    public void initialize() {
        loadDetails(Router.getInstance().getParameter("documentRequestId"));
    }

    private void loadDetails(String documentRequestId) {
        DocumentServiceGrpc.DocumentServiceBlockingStub stub = StubsManager.getInstance().getDocumentServiceBlockingStub();

        DocumentRequest documentRequest = stub.getDocumentRequestById(GetDocumentRequestByIdRequest.newBuilder()
                .setId(documentRequestId).build());

        // todo fix
        // creationDateLabel.setText(LocalDateTime.ofInstant(Instant.ofEpochSecond(documentRequest.getCreatedAt().getSeconds()), ZoneId.systemDefault()).toString());
        creationDateLabel.setText(LocalDateTime.now().toString());
        creatorLabel.setText(documentRequest.getClient().getFirstName() + " " + documentRequest.getClient().getLastName());
        //requirements.setText(documentRequest.getTemplate().getRequirements());

        List<DocumentField> documentFields = stub.getAllDocumentFieldsByDocumentRequestId(GetDocumentFieldsByDocumentRequestIdRequest.newBuilder()
                .setDocumentRequestId(documentRequestId).build()).getFieldsList();

        documentFields.forEach(documentField -> {
            HBox group = new HBox() {{
                setSpacing(5);
            }};

            Label label = new Label(documentField.getField().getName() + ":") {{
                setStyle("-fx-font-size: 1em; -fx-font-weight: medium; -fx-text-fill: #2d3748;");
            }};
            Label value = new Label(documentField.getValue()) {{
                setStyle("-fx-font-weight: bold");
            }};

            group.getChildren().addAll(label, value);
            dataInputs.getChildren().add(group);
        });

        List<DocumentRequest> documentRequests = stub.getAllDocumentRequestsToApprove(Empty.newBuilder().build()).getDocumentRequestsList();

        documentRequests.stream()
                .filter(_documentRequest -> _documentRequest.getStatus().equals(DocumentRequestStatus.GENERATED) || _documentRequest.getStatus().equals(DocumentRequestStatus.VALIDATED))
                .forEach(_documentRequest -> {
                    Pair<Node, DocumentCardController> pair = DocumentCardController.generateNodeFromDocument(_documentRequest);

                    Node node = pair.getKey();

                    node.setOnMouseClicked(event -> {
                        try {
                            Router.getInstance().navigateTo("document/" + _documentRequest.getDocument().getId());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                    int size = clientsDocumentRequests.getChildren().size();

                    clientsDocumentRequests.add(node, (int) size % 3, (int) size / 3);
                });
    }
}
