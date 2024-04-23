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
import javafx.scene.control.*;
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

public class CreateTemplateController {

    @FXML
    public VBox fieldsPane;
    @FXML
    public ImageView pdfPreview;
    @FXML
    public TextField templateName;
    @FXML
    public TextField templateRequirements;

    private byte[] fileBytes = null;
    private final List<List<Control>> fields = new ArrayList<>();

    private Node createFieldGroup() {
        VBox fieldGroup = new VBox();

        TextField nameField = new TextField() {{
            setPromptText("Name");
        }};
        ComboBox<String> typeField = new ComboBox<String>() {{
            setPromptText("Select an option");
            getItems().addAll(
                    "String",
                    "Date",
                    "Number"
            );
            setValue("String");
        }};
        Control requiredCheckbox = new CheckBox() {{
            setText("Is required");
        }};

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
                fieldsPane.getChildren().remove(index);
            });
        }};

        fieldGroup.getChildren().addAll(fieldsGroupMap);
        fieldGroup.getChildren().add(removeButton);
        fieldGroup.getStyleClass().add("field-group");
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
                .setName(templateName.getText())
                .setRequirements(templateRequirements.getText())
                .setFile(ByteString.copyFrom(fileBytes))
                .addAllFields(fields.stream()
                        .map(field -> DocumentTemplateField.newBuilder()
                                .setName(((TextField) field.get(0)).getText())
                                .setType(((ComboBox<String>) field.get(1)).getValue())
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
