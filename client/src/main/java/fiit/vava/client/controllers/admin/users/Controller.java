package fiit.vava.client.controllers.admin.users;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.scene.Node;
import fiit.vava.client.controllers._components.table.TableController;

public class Controller{

    @FXML
    private Tab clientsTab;

    @FXML
    private MFXTextField coworkerName;

    @FXML
    private BorderPane coworkersBorderPane;

    @FXML
    private Tab coworkersTab;

    @FXML
    private MFXTextField preferedCountries;
    
    @FXML 
    public void initialize(){
      loadClientsTable();
      loadCoworkersTable();
    }
    
    private void loadClientsTable(){
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fiit/vava/client/fxml/_components/table/table.fxml"));   
        Node table = loader.load();
        TableController controller = (TableController) loader.getController();
        controller.setupTable("client");
        clientsTab.setContent(table);
      } catch (Exception e) {
          e.printStackTrace();
      } 
    }

    private void loadCoworkersTable(){
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fiit/vava/client/fxml/_components/table/table.fxml"));   
        Node table = loader.load();
        TableController controller = (TableController) loader.getController();
        controller.setupTable("coworker");
        coworkersBorderPane.setCenter(table);
      } catch (Exception e) {
          e.printStackTrace();
      } 
    }

    @FXML 
    private void createCoworker(){
        // put logic here    
    }
}
