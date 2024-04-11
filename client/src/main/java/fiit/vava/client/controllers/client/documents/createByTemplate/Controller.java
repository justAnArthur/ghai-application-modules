package fiit.vava.client.controllers.client.documents.createByTemplate;

import fiit.vava.client.Router;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Controller {

    @FXML
    Label label;

    @FXML
    public void initialize() {
        label.setText(Router.getInstance().getParameter("templateId"));
    }
}
