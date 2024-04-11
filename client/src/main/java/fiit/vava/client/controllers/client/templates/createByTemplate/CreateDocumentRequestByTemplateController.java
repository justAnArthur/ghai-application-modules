package fiit.vava.client.controllers.client.templates.createByTemplate;

import fiit.vava.client.Router;
import fiit.vava.client.StubsManager;
import fiit.vava.server.*;
import io.github.palexdev.mfxcore.utils.fx.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateDocumentRequestByTemplateController {

    @FXML
    public VBox fieldsPane;
    @FXML
    public ImageView pdfPreview;
    @FXML
    Label label;

    private final List<Node> fields = new ArrayList<>();

    // to link with filled inputs
    private List<DocumentTemplateField> documentTemplateFields = new ArrayList<>();

    private Node createFieldGroup(DocumentTemplateField templateField) {
        VBox fieldGroup = new VBox();

        Label label = new Label(templateField.getName());
        // todo select the correct type of field by `templateField.getType()`.
        // now - just text field.
        TextField valueField = new TextField();

        // todo Might be created callback function to change a value in Map
        // when several fields of different types without one shared interface to access a value
        fields.add(valueField);

        // todo handle required, not required fields

        fieldGroup.getChildren().addAll(label, valueField);

        return fieldGroup;
    }

    private void addFieldGroups() throws IOException {
        String id = Router.getInstance().getParameter("templateId");

        DocumentServiceGrpc.DocumentServiceBlockingStub stub = StubsManager.getInstance().getDocumentServiceBlockingStub();

        DocumentTemplateWithFields template = stub.getDocumentTemplateById(GetDocumentTemplateByIdRequest.newBuilder()
                .setId(id).build());

        template.getFieldsList().forEach(templateField -> {
            fieldsPane.getChildren().add(createFieldGroup(templateField));
            documentTemplateFields.add(templateField);
        });

        byte[] file = stub.getFileByPath(GetFileByPathRequest.newBuilder()
                        .setPath(template.getTemplate().getPath()).build())
                .getFile()
                .toByteArray();

        PDDocument document = PDDocument.load(file);

        PDFRenderer renderer = new PDFRenderer(document);

        BufferedImage image = renderer.renderImageWithDPI(0, 100);
        Image fxImage = SwingFXUtils.toFXImage(image, null);
        pdfPreview.setImage(fxImage);
    }

    @FXML
    public void initialize() throws IOException {
        addFieldGroups();
    }

    @FXML
    public void handleCreateDocumentRequest(ActionEvent actionEvent) {
        DocumentServiceGrpc.DocumentServiceBlockingStub stub = StubsManager.getInstance().getDocumentServiceBlockingStub();

        String id = Router.getInstance().getParameter("templateId");

        CreateDocumentRequest request = CreateDocumentRequest.newBuilder()
                .setTemplateId(id)
                .build();

        for (int i = 0; i < fields.size(); i++) {
            TextField textField = (TextField) fields.get(i);
            DocumentField documentField = DocumentField.newBuilder()
                    .setField(documentTemplateFields.get(i))
                    .setValue(textField.getText())
                    .build();

            request = request.toBuilder().addFields(documentField).build();
        }

        try {
            stub.createDocumentRequest(request);

            Router.getInstance().navigateTo("client/documents");
        } catch (Exception e) {
            // handle the error
            e.printStackTrace();
        }
    }
}
