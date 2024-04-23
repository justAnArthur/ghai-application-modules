package fiit.vava.client.controllers.coworker.documentRequests.approve;

import fiit.vava.client.Router;
import fiit.vava.client.StubsManager;
import fiit.vava.server.*;
import io.github.palexdev.mfxcore.controls.Text;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class ApproveDocumentRequestController {
    @FXML
    public Label creationDateLabel;
    @FXML
    public Label creatorLabel;
    @FXML
    public Text requirements;
    @FXML
    public GridPane dataInputs;

    @FXML
    public void handleApprove(ActionEvent actionEvent) throws IOException {
        DocumentServiceGrpc.DocumentServiceBlockingStub stub = StubsManager.getInstance().getDocumentServiceBlockingStub();

        stub.approveDocumentRequest(ApproveRejectDocumentRequestRequest.newBuilder()
                .setDocumentRequestId(Router.getInstance().getParameter("documentRequestId")).build());

        Router.getInstance().navigateTo("coworker/documentRequests");
    }

    @FXML
    public void handleReject(ActionEvent actionEvent) throws IOException {
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

        creationDateLabel.setText(LocalDateTime.ofInstant(Instant.ofEpochSecond(documentRequest.getCreatedAt().getSeconds()), ZoneId.systemDefault()).toString());
        creatorLabel.setText(documentRequest.getClient().getFirstName() + " " + documentRequest.getClient().getLastName());
        requirements.setText(documentRequest.getTemplate().getRequirements());

        List<DocumentField> documentFields = stub.getAllDocumentFieldsByDocumentRequestId(GetDocumentFieldsByDocumentRequestIdRequest.newBuilder()
                .setDocumentRequestId(documentRequestId).build()).getFieldsList();

        documentFields.forEach(documentField -> {
            VBox group = new VBox();

            Label label = new Label(documentField.getField().getName());
            Label value = new Label(documentField.getValue());

            group.getChildren().addAll(label, value);
            dataInputs.add(group, 0, dataInputs.getRowCount());
        });
    }
}
