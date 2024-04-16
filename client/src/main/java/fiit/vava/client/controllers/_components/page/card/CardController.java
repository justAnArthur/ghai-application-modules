package fiit.vava.client.controllers._components.page.card;

import fiit.vava.client.Router;
import fiit.vava.client.StubsManager;
import fiit.vava.server.DocumentServiceGrpc;
import fiit.vava.server.DocumentTemplate;
import fiit.vava.server.DocumentRequest;
import fiit.vava.server.Empty;
import fiit.vava.server.GetFileByPathRequest;
import io.github.palexdev.mfxcore.utils.fx.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.control.Pagination;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class CardController{

    @FXML 
    private BorderPane imageBorderPane;
    @FXML 
    private BorderPane card;
    @FXML
    private ImageView imageView;

    @FXML
    private Label nameLabel;

    @FXML
    private Label errorLabel;
    
    @FXML
    private Label statusLabel;
    
    public void setObject(Object obj, String mode){
        
        if (obj instanceof DocumentTemplate){
          DocumentTemplate template = (DocumentTemplate) obj;
          PDDocument document = null;
          try {
              DocumentServiceGrpc.DocumentServiceBlockingStub stub = StubsManager.getInstance().getDocumentServiceBlockingStub();

              byte[] file = stub.getFileByPath(GetFileByPathRequest.newBuilder()
                              .setPath(template.getPath()).build())
                      .getFile()
                      .toByteArray();

              document = PDDocument.load(file);

              PDFRenderer renderer = new PDFRenderer(document);

              BufferedImage image = renderer.renderImageWithDPI(0, 100);
              Image fxImage = SwingFXUtils.toFXImage(image, null);
              imageView.setImage(fxImage);

          } catch (IOException e) {
              errorLabel.setText("Unable to load preview");
          } finally{
            if (document != null) {
                    try {
                        document.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
          }
          nameLabel.setText(template.getName());
          statusLabel.setText(template.getId());
          if(mode.equals("ClientTemplates")){

            card.setOnMouseClicked(event -> {
                try {
                    Router.getInstance().navigateTo("client/templates/createBy/" + template.getId());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
          }
        } else if(obj instanceof DocumentRequest){
          DocumentRequest document = (DocumentRequest) obj;
          switch (document.getStatus().name()) {
            case "CREATED":
              imageBorderPane.setBackground(new Background(new BackgroundFill(Color.PINK, CornerRadii.EMPTY, Insets.EMPTY)));
              break;

            default:
              break;
          }
          nameLabel.setText(document.getTemplate().getName());
          statusLabel.setText(document.getStatus().name());
          // card.setOnMouseClicked(event -> {
          //     try {
          //         // TODO navigate to document or document request ?
          //         Router.getInstance().navigateTo("client/documents" + document.getDocument().getId());
          //     } catch (IOException e) {
          //         e.printStackTrace();
          //     }
          // });
        }
    }
}
