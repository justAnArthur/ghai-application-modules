package fiit.vava.client.controllers._components.page.card;

import fiit.vava.client.Router;
import fiit.vava.client.StubsManager;
import fiit.vava.server.*;
import io.github.palexdev.mfxcore.utils.fx.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class DocumentCardController {

    public VBox card;

    public ImageView imageView;
    public Label nameLabel;
    public Label sizeLabel;
    public Label documentTypeLabel;
    public Label statusLabel;

    private void setImageView(byte[] file) throws IOException {
        PDDocument document = PDDocument.load(file);
        PDFRenderer renderer = new PDFRenderer(document);
        BufferedImage image = renderer.renderImageWithDPI(0, 100);
        Image fxImage = SwingFXUtils.toFXImage(image, null);
        imageView.setImage(fxImage);
    }

    private void loadImage(String path) {
        DocumentServiceGrpc.DocumentServiceBlockingStub stub = StubsManager.getInstance().getDocumentServiceBlockingStub();

        try {
            byte[] file = stub.getFileByPath(GetFileByPathRequest.newBuilder()
                            .setPath(path).build())
                    .getFile()
                    .toByteArray();

            setImageView(file);
        } catch (Exception ignored) {
        }
    }

    public void setData(Object obj) {
        if (obj instanceof DocumentTemplate) {
            DocumentTemplate template = (DocumentTemplate) obj;

            nameLabel.setText(template.getName());
            statusLabel.setText(template.getType().name());

            if (template.getPath() != null && !template.getPath().isEmpty())
                loadImage(template.getPath());

        } else if (obj instanceof DocumentRequest) {
            DocumentRequest documentRequest = (DocumentRequest) obj;

            nameLabel.setText(documentRequest.getTemplate().getName());
            statusLabel.setText(documentRequest.getStatus().name());

            if (documentRequest.getStatus().equals(DocumentRequestStatus.GENERATED) && documentRequest.getDocument() != null && documentRequest.getDocument().getPath() != null && !documentRequest.getDocument().getPath().isEmpty()) {
                loadImage(documentRequest.getDocument().getPath());

                card.setOnMouseClicked(event -> {
                    try {
                        Router.getInstance().navigateTo("document/" + documentRequest.getDocument().getId());
                    } catch (IOException ignored) {
                    }
                });
            }
        }
    }

    public static Pair<Node, DocumentCardController> generateNodeFromDocument(Object document) {
        try {
            FXMLLoader loader = new FXMLLoader(DocumentCardController.class.getResource("/fiit/vava/client/fxml/_components/page/card/card.fxml"));
            Node node = loader.load();

            DocumentCardController controller = loader.getController();
            controller.setData(document);

            return new Pair<>(node, controller);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
