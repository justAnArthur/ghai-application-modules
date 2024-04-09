package fiit.vava.client.controllers.admin.templates.create;

import com.google.protobuf.ByteString;
import fiit.vava.client.Router;
import fiit.vava.client.StubsManager;
import fiit.vava.server.CreateDocumentTemplateRequest;
import fiit.vava.server.DocumentServiceGrpc;
import fiit.vava.server.DocumentTemplateField;
import io.github.palexdev.mfxcore.utils.fx.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    @FXML
    public VBox fieldsPane;
    @FXML
    public ImageView pdfPreview;

    private byte[] fileBytes = null;
    private final List<List<Control>> fields = new ArrayList<>();

    private Node createFieldGroup() {
        VBox fieldGroup = new VBox();

        TextField nameField = new TextField();
        TextField typeField = new TextField();
        Control requiredCheckbox = new CheckBox();

        List<Control> fieldsGroupMap = List.of(
                nameField,
                typeField,
                requiredCheckbox
        );

        fields.add(fieldsGroupMap);

        Button removeButton = new Button("Remove") {{
            setOnAction(event -> {
                int index = fields.indexOf(fieldsGroupMap);

                fields.remove(index);
                fieldsPane.getChildren().remove(index + 1); // there's a button
            });
        }};

        fieldGroup.getChildren().addAll(fieldsGroupMap);
        fieldGroup.getChildren().add(removeButton);

        return fieldGroup;
    }

    @FXML
    public void handleUploadPdf() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files", "*.pdf"));

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            fileBytes = Files.readAllBytes(selectedFile.toPath());

            PDDocument document = PDDocument.load(selectedFile);
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 300);
            document.close();

            Image previewImage = SwingFXUtils.toFXImage(bim, null);
            pdfPreview.setImage(previewImage);
        }
    }

    @FXML
    public void handleAddField() {
        fieldsPane.getChildren().add(createFieldGroup());
    }

    @FXML
    public void handleCreateTemplate() {
        DocumentServiceGrpc.DocumentServiceBlockingStub stub = StubsManager.getInstance().getDocumentServiceBlockingStub();

        CreateDocumentTemplateRequest request = CreateDocumentTemplateRequest.newBuilder()
                .setFile(ByteString.copyFrom(fileBytes))
                .addAllFields(fields.stream()
                        .map(field -> DocumentTemplateField.newBuilder()
                                .setName(((TextField) field.get(0)).getText())
                                .setType(((TextField) field.get(1)).getText())
                                .setRequired(((CheckBox) field.get(2)).isSelected())
                                .build())
                        .toList())
                .build();

        try {
            stub.createDocumentTemplate(request);

            Router.getInstance().navigateTo("admin/templates");
        } catch (Exception e) {
            e.printStackTrace();
            // TODO print text to label
        }
    }
}
