package fiit.vava.client.routes.coworker.clients.approve;

import fiit.vava.client.StubsManager;
import fiit.vava.server.Client;
import fiit.vava.server.Empty;
import fiit.vava.server.UserServiceGrpc;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class Controller {
    @FXML
    public TableView<Client> nonApprovedClientsTable;

    @FXML
    private TableColumn<Client, String> idColumn;

    @FXML
    private TableColumn<Client, String> emailColumn;

    @FXML
    private TableColumn<Client, String> firstNameColumn;

    @FXML
    private TableColumn<Client, String> lastNameColumn;

    @FXML
    private TableColumn<Client, String> actionsColumn;

    public void initialize() {
        idColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getId()));
        emailColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getUser().getEmail()));
        firstNameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getLastName()));

        actionsColumn.setCellFactory(param -> new TableCell<>() {
            final Button btn = new Button("approve");

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    btn.setOnAction(event -> handleApprove(getTableView().getItems().get(getIndex())));
                    setGraphic(btn);
                }
            }
        });

        loadData();
    }

    public void loadData() {
        UserServiceGrpc.UserServiceBlockingStub stub = StubsManager.getInstance().getUserServiceBlockingStub();

        List<Client> nonApprovedClients = stub.getNonApprovedClients(Empty.newBuilder().build()).getClientList();

        System.out.println(nonApprovedClients.size());

        nonApprovedClientsTable.getItems().addAll(nonApprovedClients);
    }

    public void handleApprove(Client client) {
        UserServiceGrpc.UserServiceBlockingStub stub = StubsManager.getInstance().getUserServiceBlockingStub();

        stub.approveClient(client);
        loadData();
    }
}
