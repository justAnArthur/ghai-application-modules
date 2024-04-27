package fiit.vava.client.controllers.coworker.documentRequests;

import fiit.vava.client.bundles.XMLResourceBundle;
import fiit.vava.client.bundles.XMLResourceBundleProvider;
import fiit.vava.client.Router;
import fiit.vava.client.StubsManager;
import fiit.vava.server.DocumentRequest;
import fiit.vava.server.DocumentServiceGrpc;
import fiit.vava.server.Empty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class DocumentRequestsController {
    @FXML 
    public Label placeholderLabel; 
    @FXML 
    public Label documentsText; 
    @FXML 
    public Label documentsLabel; 
    @FXML
    public TableView<DocumentRequest> nonApprovedDocumentRequests;
    @FXML
    public TableColumn<DocumentRequest, String> templateName;
    @FXML
    public TableColumn<DocumentRequest, String> clientsName;
    @FXML
    public TableColumn<DocumentRequest, String> clientsEmail;
    @FXML
    public TableColumn<DocumentRequest, String> createdAt;
    @FXML
    public TableColumn<DocumentRequest, String> actionsColumn;

    public void goToApproving(DocumentRequest documentRequest) {
        try {
            Router.getInstance().navigateTo("coworker/documentRequests/" + documentRequest.getId() + "/approve");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    XMLResourceBundleProvider instance;

    public DocumentRequestsController() {
        this.instance = XMLResourceBundleProvider.getInstance();
    }

    public void initialize() {
        loadTexts();

        instance.subscribe(language -> loadTexts());
        templateName.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTemplate().getName()));
        clientsName.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getClient().getFirstName() + " " + cellData.getValue().getClient().getLastName()));
        clientsEmail.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getClient().getUser().getEmail()));
        /*createdAt.setCellValueFactory(cellData -> {
            Instant instant = Instant.ofEpochSecond(cellData.getValue().getCreatedAt().getSeconds(), cellData.getValue().getCreatedAt().getNanos());
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            return new ReadOnlyStringWrapper(localDateTime.toString());
        });*/

        actionsColumn.setCellFactory(param -> new TableCell<>() {
            final Button btn = new Button("Approve");

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

        List<DocumentRequest> documentRequests = stub.getAllDocumentRequestsToApprove(Empty.newBuilder().build()).getDocumentRequestsList().stream()
                .filter(documentRequest -> !documentRequest.getTemplate().getPrivate())
                .collect(Collectors.toList());

        nonApprovedDocumentRequests.getItems().addAll(documentRequests);
    }
    private void loadTexts() {
        XMLResourceBundle bundle = instance.getBundle("fiit.vava.client.bundles.coworker");

        if (bundle == null)
            return;
        
        documentsLabel.setText(bundle.getString("documents.label.requests"));
        documentsText.setText(bundle.getString("documents.text.requests"));
        placeholderLabel.setText(bundle.getString("documents.label.placeholder"));
        templateName.setText(bundle.getString("documents.label.template"));
        clientsName.setText(bundle.getString("documents.label.clients"));
        clientsEmail.setText(bundle.getString("documents.label.clients.email"));
        actionsColumn.setText(bundle.getString("documents.label.approve"));
    } 
}


