package fiit.vava.client.controllers.coworker.clients;

import fiit.vava.client.StubsManager;
import fiit.vava.server.Client;
import fiit.vava.server.CoworkerServiceGrpc;
import fiit.vava.server.Empty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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

    public void handleApprove(Client client) {
        CoworkerServiceGrpc.CoworkerServiceBlockingStub stub = StubsManager.getInstance().getCoworkerServiceBlockingStub();

        stub.approveClient(client);

        nonApprovedClientsTable.getItems().clear();
        loadData();
    }

    public void initialize() {
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

        System.out.println(nonApprovedClients.size());

        nonApprovedClientsTable.getItems().addAll(nonApprovedClients);
    }
}
