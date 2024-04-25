package fiit.vava.client.controllers._components;

import fiit.vava.client.Router;
import fiit.vava.client.StubsManager;
import fiit.vava.server.DocumentServiceGrpc;
import fiit.vava.server.GetFileByDocumentId;
import io.github.palexdev.mfxcore.utils.fx.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class DocumentViewController {
    public ImageView documentImage;

    public void initialize() {
        loadData();
    }

    private void loadData() {
        DocumentServiceGrpc.DocumentServiceBlockingStub stub = StubsManager.getInstance().getDocumentServiceBlockingStub();

        byte[] file = stub.getFileByDocumentId(GetFileByDocumentId.newBuilder()
                .setDocumentId(Router.getInstance().getParameter("documentId")).build()).getFile().toByteArray();

        try {
            PDDocument document = PDDocument.load(file);

            PDFRenderer renderer = new PDFRenderer(document);

            BufferedImage bim = renderer.renderImageWithDPI(0, 100);

            double aspectRatio = (double) bim.getWidth() / bim.getHeight();

            double width = 400 * aspectRatio;

            if (width > 300)
                width = 300;

            documentImage.setFitWidth(width);
            documentImage.setFitHeight(400);

            Image fxImage = SwingFXUtils.toFXImage(bim, null);
            documentImage.setImage(fxImage);
        } catch (IOException e) {
            System.out.println("Failed to load pdf");
            e.printStackTrace();
        }
    }
}
