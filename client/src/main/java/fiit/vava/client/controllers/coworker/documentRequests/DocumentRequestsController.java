package fiit.vava.client.controllers.coworker.documentRequests;

import fiit.vava.client.Router;
import fiit.vava.client.StubsManager;
import fiit.vava.server.DocumentRequest;
import fiit.vava.server.DocumentServiceGrpc;
import fiit.vava.server.Empty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.util.List;

public class DocumentRequestsController {

    @FXML
    public TableView<DocumentRequest> nonApprovedDocumentRequests;
    @FXML
    public TableColumn<DocumentRequest, String> templateName;
    @FXML
    public TableColumn<DocumentRequest, String> clientsName;
    @FXML
    public TableColumn<DocumentRequest, String> createdAt;
    @FXML
    public TableColumn<DocumentRequest, String> actionsColumn;

    public void goToApproving(DocumentRequest documentRequest) {
        try {
            Router.getInstance().navigateTo("coworker/documentRequests/" + documentRequest.getId() + "/approve");
        } catch (IOException ignored) {
        }
    }

    public void initialize() {
        templateName.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTemplate().getName()));
        clientsName.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getClient().getFirstName() + " " + cellData.getValue().getClient().getLastName()));
        createdAt.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCreatedAt().toString()));

        actionsColumn.setCellFactory(param -> new TableCell<>() {
            final Button btn = new Button("approve");

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    btn.setOnAction(event -> goToApproving(getTableView().getItems().get(getIndex())));
                    setGraphic(btn);
                }
            }
        });

        loadData();
    }

    private void loadData() {
        DocumentServiceGrpc.DocumentServiceBlockingStub stub = StubsManager.getInstance().getDocumentServiceBlockingStub();

        List<DocumentRequest> documentRequests = stub.getAllDocumentRequestsToApprove(Empty.newBuilder().build()).getDocumentRequestsList();

        nonApprovedDocumentRequests.getItems().addAll(documentRequests);
    }
}


