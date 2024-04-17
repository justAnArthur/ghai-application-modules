package fiit.vava.client.controllers.client;

import fiit.vava.client.Router;
import javafx.fxml.FXML;

import java.io.IOException;

public class Controller {
    
    @FXML
    void initialize() throws IOException {
      Router.getInstance().changeNavBar("navbar");
    }
}
