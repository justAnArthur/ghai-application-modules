package fiit.vava.client.controllers._components.table;

import javafx.scene.control.cell.PropertyValueFactory;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

public class TableController {

    @FXML
    private TableView<Object> table;
    @FXML
    private Pagination pagination;

    private static final int ROWS_PER_PAGE = 24;
    private String currentType;

    public void initialize() {
        pagination.setPageFactory(this::createPage);
    }

    public void setupTable(String type) {
        currentType = type;
        table.getColumns().clear();
        table.getItems().clear();

        switch (type) {
            case "client":
                configureColumnsClient();
                break;
            case "coworker":
                configureColumnsCoworker();
                break;
        }
        
        int totalDataSize = getTotalDataSize(type);
        int pageCount = (int) Math.ceil(totalDataSize / (double) ROWS_PER_PAGE);
        pagination.setPageCount(pageCount);
    }

    private Node createPage(int pageIndex) {
        updateTable(pageIndex);
        return table;
    }

    private void updateTable(int pageIndex) {
        List<?> itemsForPage = getDataForPage(currentType, pageIndex);
        table.setItems(FXCollections.observableArrayList(itemsForPage));
    }

    private List<?> getDataForPage(String type, int pageIndex) {
        // Calculate the range for the page
        int start = pageIndex * ROWS_PER_PAGE;
        int end = start + ROWS_PER_PAGE;

        List<Object> pageData = new ArrayList<>();

        // Generate data based on type
        if ("client".equals(type)) {
            // Generate Clients for this page
            for (int i = start; i < end; i++) {
                pageData.add(new Client(
                    "ID" + i,
                    "FirstName" + i,
                    "LastName" + i,
                    "email" + i + "@example.com",
                    "Country" + i
                ));
            }
        } else if ("coworker".equals(type)) {
            // Generate Coworkers for this page
            for (int i = start; i < end; i++) {
                pageData.add(new Coworker(
                    "ID" + i,
                    "Name" + i,
                    "Countries" + i
                ));
            }
        }
        return pageData;
    }

    private int getTotalDataSize(String type) {
        // Return the total data size for the given type
      return 10;
    }

    private void configureColumnsClient() {
        table.getColumns().clear();  // Clear existing columns before adding new ones

        TableColumn<Object, String> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setResizable(false);

        TableColumn<Object, String> firstnameColumn = new TableColumn<>("Firstname");
        firstnameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        firstnameColumn.setResizable(false);

        TableColumn<Object, String> lastnameColumn = new TableColumn<>("Lastname");
        lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        lastnameColumn.setResizable(false);

        TableColumn<Object, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailColumn.setResizable(false);

        TableColumn<Object, String> countryColumn = new TableColumn<>("Country");
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        countryColumn.setResizable(false);

        idColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.2)); 
        firstnameColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.2)); 
        lastnameColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.2)); 
        emailColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.2)); 
        countryColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.2)); 
        table.getColumns().addAll(idColumn, firstnameColumn, lastnameColumn, emailColumn, countryColumn);
    }

    private void configureColumnsCoworker() {
        table.getColumns().clear();  // Clear existing columns before adding new ones

        TableColumn<Object, String> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setResizable(false);

        TableColumn<Object, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setResizable(false);

        TableColumn<Object, String> countriesColumn = new TableColumn<>("Prefered Countries");
        countriesColumn.setCellValueFactory(new PropertyValueFactory<>("countries"));
        countriesColumn.setResizable(false);

        idColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.2)); 
        nameColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.2)); 
        countriesColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.6)); 

        table.getColumns().addAll(idColumn, nameColumn, countriesColumn);
    }

    public class Client {
        private final SimpleStringProperty id = new SimpleStringProperty();
        private final SimpleStringProperty firstname = new SimpleStringProperty();
        private final SimpleStringProperty lastname = new SimpleStringProperty();
        private final SimpleStringProperty email = new SimpleStringProperty();
        private final SimpleStringProperty country = new SimpleStringProperty();

        public Client(String id, String firstname, String lastname, String email, String country) {
            this.id.set(id);
            this.firstname.set(firstname);
            this.lastname.set(lastname);
            this.email.set(email);
            this.country.set(country);
        }

        public StringProperty idProperty() { return id; }
        public StringProperty firstnameProperty() { return firstname; }
        public StringProperty lastnameProperty() { return lastname; }
        public StringProperty emailProperty() { return email; }
        public StringProperty countryProperty() { return country; }
    }

    public class Coworker {
        private final SimpleStringProperty id = new SimpleStringProperty();
        private final SimpleStringProperty name = new SimpleStringProperty();
        private final SimpleStringProperty countries = new SimpleStringProperty();

        public Coworker(String id, String name, String countries) {
            this.id.set(id);
            this.name.set(name);
            this.countries.set(countries);
        }

        public StringProperty idProperty() { return id; }
        public StringProperty nameProperty() { return name; }
        public StringProperty countriesProperty() { return countries; }
    }
}
