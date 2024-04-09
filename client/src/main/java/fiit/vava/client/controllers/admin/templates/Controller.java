package fiit.vava.client.controllers.admin.templates;

import fiit.vava.client.Router;
import fiit.vava.client.StubsManager;
import fiit.vava.server.DocumentServiceGrpc;
import fiit.vava.server.DocumentTemplate;
import fiit.vava.server.Empty;
import fiit.vava.server.GetFileByPathRequest;
import io.github.palexdev.mfxcore.utils.fx.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class Controller {

    @FXML
    private GridPane gridPane;

    /*
     * TODO import VBox from `.fxml` file:
     * ```java
     * FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/your/card.fxml"));
     * // Create the VBox
     * VBox card = loader.load();
     * ```
     */
    private VBox createCard(DocumentTemplate template) {
        VBox card = new VBox();
        card.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        try {
            DocumentServiceGrpc.DocumentServiceBlockingStub stub = StubsManager.getInstance().getDocumentServiceBlockingStub();

            byte[] file = stub.getFileByPath(GetFileByPathRequest.newBuilder()
                            .setPath(template.getPath()).build())
                    .getFile()
                    .toByteArray();

            PDDocument document = PDDocument.load(file);

            PDFRenderer renderer = new PDFRenderer(document);

            BufferedImage image = renderer.renderImageWithDPI(0, 100);
            Image fxImage = SwingFXUtils.toFXImage(image, null);
            ImageView imageView = new ImageView(fxImage);
            imageView.setFitWidth(100);
            imageView.setPreserveRatio(true);

            card.getChildren().add(imageView);
        } catch (IOException e) {
            card.getChildren().add(new Label("Unable to load preview"));
        }

        Label label = new Label(template.getId());

        card.getChildren().add(label);
        return card;
    }

    @FXML
    void initialize() {
        loadData();
    }

    public void loadData() {
        DocumentServiceGrpc.DocumentServiceBlockingStub stub = StubsManager.getInstance().getDocumentServiceBlockingStub();

        List<DocumentTemplate> templates = stub.getAllDocumentTemplates(Empty.newBuilder().build()).getTemplatesList();

        System.out.println(templates.size());

        if (templates.isEmpty()) {
            Label label = new Label("No templates found.");
            gridPane.add(label, 0, 0);
        } else {
            int rowIndex = 0, columnIndex = 0;
            for (DocumentTemplate template : templates) {
                VBox card = createCard(template);
                gridPane.add(card, columnIndex, rowIndex);

                if (++columnIndex > 3) {
                    columnIndex = 0;
                    rowIndex++;
                }
            }
        }
    }

    @FXML
    public void handleCreateAction(ActionEvent actionEvent) throws IOException {
        Router.getInstance().navigateTo("admin/templates/create");
    }
}
