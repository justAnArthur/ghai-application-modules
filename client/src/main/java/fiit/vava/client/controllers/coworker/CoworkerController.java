package fiit.vava.client.controllers.coworker;

import fiit.vava.client.Router;
import javafx.fxml.FXML;

import java.io.IOException;

public class CoworkerController {

    @FXML
    void initialize() throws IOException {
        Router.getInstance().setSideBar("coworker/sideBar");
        Router.getInstance().navigateTo("coworker/documentRequests");
    }
}
