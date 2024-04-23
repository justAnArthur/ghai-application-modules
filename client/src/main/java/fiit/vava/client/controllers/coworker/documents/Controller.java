package fiit.vava.client.controllers.coworker.documents;

import fiit.vava.client.Router;
import fiit.vava.client.StubsManager;
import fiit.vava.server.*;
import io.github.palexdev.mfxcore.utils.fx.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.image.Image;
import javafx.scene.control.DatePicker;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Controller {

    @FXML
    public VBox fieldsPane;
    @FXML
    public ImageView pdfPreview;
    @FXML
    Label errorLabel;
    @FXML
    Label templateName;
    private final List<Node> fields = new ArrayList<>();
    private final Map<Node, Boolean> requiredMap = new HashMap<>();  
    // to link with filled inputs
    private List<DocumentTemplateField> documentTemplateFields = new ArrayList<>();

    private Node createFieldGroup(/*DocumentRequestField*/ DocumentTemplateField requestField) {
        VBox fieldGroup = new VBox();

        if(requestField.getRequired()){
          Label required = new Label("Required");  
          fieldGroup.getChildren().add(required); 
        }
        Label name = new Label(requestField.getName());
        fieldGroup.getChildren().add(name);
        // Label value = new Label(requestField.getValue());

        return fieldGroup;
    }
    private void loadNextDocument() throws IOException {

        // DocumentServiceGrpc.DocumentServiceBlockingStub stub = StubsManager.getInstance().getDocumentServiceBlockingStub();
        // TODO getFirstDocumentRequest 
        // DocumentRequest request = stub.getFirstDocumentRequest(GetDocumentTemplateByIdRequest.newBuilder()
        //         .setId().build());
        //
        // request.getFieldsList().forEach(templateField -> {
        //     fieldsPane.getChildren().add(createFieldGroup(templateField));
        //     documentTemplateFields.add(templateField);
        // });

        // byte[] file = stub.getFileByPath(GetFileByPathRequest.newBuilder()
        //                 .setPath(template.getTemplate().getPath()).build())
        //         .getFile()
        //         .toByteArray();
        //
        // PDDocument document = PDDocument.load(file);
        //
        // PDFRenderer renderer = new PDFRenderer(document);
        //
        // BufferedImage image = renderer.renderImageWithDPI(0, 100);
        // Image fxImage = SwingFXUtils.toFXImage(image, null);
        // pdfPreview.setImage(fxImage);
    }

    @FXML
    public void initialize() throws IOException {
        //loadNextDocument();
    }
    
    @FXML 
    private void handleApprove(){
      //TODO handleApprove on server side 
      //loadNextDocument();
    }

    @FXML 
    private void handleDisapprove(){
      //TODO handleDisapprove on server side 
      //loadNextDocument();
    }
}
