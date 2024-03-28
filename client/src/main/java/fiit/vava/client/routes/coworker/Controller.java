package fiit.vava.client.routes.coworker;

import fiit.vava.client.Router;
import javafx.fxml.FXML;

import java.io.IOException;

public class Controller {

    @FXML
    public void handleGoToApproveClients() throws IOException {
        Router.getInstance().navigateTo("coworker/clients/approve");
    }
}
