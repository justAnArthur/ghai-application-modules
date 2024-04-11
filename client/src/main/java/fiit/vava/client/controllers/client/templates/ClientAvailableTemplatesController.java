package fiit.vava.client.controllers.client.templates;

import fiit.vava.client.Router;
import fiit.vava.client.StubsManager;
import fiit.vava.server.DocumentServiceGrpc;
import fiit.vava.server.DocumentTemplate;
import fiit.vava.server.Empty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;

public class ClientAvailableTemplatesController {

    @FXML
    public GridPane gridPane;

    /*
     * Load cards from .fxml with styles
     */
    private Node createTemplateCard(DocumentTemplate template) {
        VBox card = new VBox() {{
            setBackground(new Background(new BackgroundFill(Color.PINK, CornerRadii.EMPTY, Insets.EMPTY)));
        }};

        card.setOnMouseClicked(event -> {
            try {
                Router.getInstance().navigateTo("client/templates/createBy/" + template.getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Label namelabel = new Label(template.getName());
        Label idlabel = new Label(template.getId());

        card.getChildren().addAll(namelabel, idlabel);

        return card;
    }

    @FXML
    public void initialize() {
        loadTemplates();
    }

    private void loadTemplates() {
        DocumentServiceGrpc.DocumentServiceBlockingStub stub = StubsManager.getInstance().getDocumentServiceBlockingStub();

        stub.getAllDocumentTemplates(Empty.newBuilder().build())
                .getTemplatesList()
                .forEach(template -> {
                    System.out.println("document template: " + template.getId());
                    gridPane.getChildren().add(createTemplateCard(template));
                });
    }
}
