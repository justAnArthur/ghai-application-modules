package fiit.vava.client.controllers.coworker.clients;

import fiit.vava.client.bundles.XMLResourceBundle;
import fiit.vava.client.bundles.XMLResourceBundleProvider;
import fiit.vava.client.StubsManager;
import fiit.vava.server.Client;
import fiit.vava.server.CoworkerServiceGrpc;
import fiit.vava.server.Empty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ClientsToApproveController {
    @FXML
    public TableView<Client> nonApprovedClientsTable;

    @FXML
    private TableColumn<Client, String> emailColumn;

    @FXML
    private TableColumn<Client, String> firstNameColumn;

    @FXML
    private TableColumn<Client, String> lastNameColumn;
    @FXML
    public TableColumn<Client, String> createdAtColumn;

    @FXML
    private TableColumn<Client, String> approveColumn;

    @FXML
    private Label pendingLabel;

    @FXML
    private Label clientsText;

    @FXML
    private Label placeholderLabel;

    XMLResourceBundleProvider instance;

    private static final Logger logger = LoggerFactory.getLogger("client." + ClientsToApproveController.class);

    public ClientsToApproveController() {
        this.instance = XMLResourceBundleProvider.getInstance();
    }

    public void handleApprove(Client client) {
        CoworkerServiceGrpc.CoworkerServiceBlockingStub stub = StubsManager.getInstance().getCoworkerServiceBlockingStub();

        stub.approveClient(client);

        nonApprovedClientsTable.getItems().clear();
        loadData();
    }

    public void initialize() {
        loadTexts();

        instance.subscribe(language -> loadTexts());

        emailColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getUser().getEmail()));
        firstNameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getLastName()));
        createdAtColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getRegistrationDate()));

        approveColumn.setCellFactory(param -> new TableCell<>() {
            final Button btn = new Button("approve");

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    btn.setOnAction(event -> {
                        handleApprove(getTableView().getItems().get(getIndex()));
//      todo (make page better)                  try {
//                            Router.getInstance().navigateTo("coworker/clients/" + getTableView().getItems().get(getIndex()).getId() + "/approve");
//                        } catch (IOException ignored) {
//                        }
                    });
                    setGraphic(btn);
                }
            }
        });

        loadData();
    }

    public void loadData() {
        CoworkerServiceGrpc.CoworkerServiceBlockingStub stub = StubsManager.getInstance().getCoworkerServiceBlockingStub();

        List<Client> nonApprovedClients = stub.getNonApprovedClients(Empty.newBuilder().build()).getClientList();

        logger.debug(nonApprovedClients.size() + " non approved clients found.");

        nonApprovedClientsTable.getItems().addAll(nonApprovedClients);
    }
    private void loadTexts() {
        XMLResourceBundle bundle = instance.getBundle("fiit.vava.client.bundles.coworker");

        if (bundle == null)
            return;
        pendingLabel.setText(bundle.getString("client.label.pending"));
        clientsText.setText(bundle.getString("client.label.text"));
        placeholderLabel.setText(bundle.getString("client.label.placeholder"));
        firstNameColumn.setText(bundle.getString("client.label.firstname"));
        lastNameColumn.setText(bundle.getString("client.label.lastname"));
        emailColumn.setText(bundle.getString("client.label.email"));
        createdAtColumn.setText(bundle.getString("client.label.created"));
        approveColumn.setText(bundle.getString("client.label.approve"));
    }
}
