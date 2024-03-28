package fiit.vava.client.controllers.client;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import fiit.vava.client.Router;
import fiit.vava.client.Routes;

public class MyDocumentsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Document> documentsTable;

    @FXML
    private Button toProfileBtn;

    @FXML
    private TableColumn<Document, String> createdAtColumn;

    @FXML
    private TableColumn<Document, String> dNameColumn;

    @FXML
    private TableColumn<Document, String> statusColumn;

    @FXML
    private TableColumn<Document, String> updatedAtColumn;
 
    @FXML
    void handleToProfileBtn(ActionEvent event) {
     Router.getInstance().navigateTo(Routes.Client.PROFILE); 
    }

    @FXML
    void handleAddPassport(ActionEvent event) {
     Router.getInstance().showModal(Routes.Utils.DIALOG); 
    }

    @FXML
    void initialize() {
        dNameColumn.setCellValueFactory(data -> data.getValue().documentNameProperty());
        createdAtColumn.setCellValueFactory(data -> data.getValue().createdAtProperty());
        updatedAtColumn.setCellValueFactory(data -> data.getValue().updatedAtProperty());
        statusColumn.setCellValueFactory(data -> data.getValue().statusProperty());
        ObservableList<Document> data = FXCollections.observableArrayList(
          new Document("Id", "12.12.2024", "13.12.2024", "Verified")
        );
        documentsTable.setItems(data);
    }
    

    public class Document {
        private final StringProperty documentName = new SimpleStringProperty();
        private final StringProperty createdAt = new SimpleStringProperty();
        private final StringProperty updatedAt = new SimpleStringProperty();
        private final StringProperty status = new SimpleStringProperty();

        public Document(String documentName, String createdAt, String updatedAt, String status) {
            setDocumentName(documentName);
            setCreatedAt(createdAt);
            setUpdatedAt(updatedAt);
            setStatus(status);
        }

        // Getters and setters
        public final String getDocumentName() {
            return documentName.get();
        }

        public final void setDocumentName(String documentName) {
            this.documentName.set(documentName);
        }

        public final StringProperty documentNameProperty() {
            return documentName;
        }

        public final String getCreatedAt() {
            return createdAt.get();
        }

        public final void setCreatedAt(String createdAt) {
            this.createdAt.set(createdAt);
        }

        public final StringProperty createdAtProperty() {
            return createdAt;
        }

        public final String getUpdatedAt() {
            return updatedAt.get();
        }

        public final void setUpdatedAt(String updatedAt) {
            this.updatedAt.set(updatedAt);
        }

        public final StringProperty updatedAtProperty() {
            return updatedAt;
        }

        public final String getStatus() {
            return status.get();
        }

        public final void setStatus(String status) {
            this.status.set(status);
        }

        public final StringProperty statusProperty() {
            return status;
        }
    }
}
