package fiit.vava.client.controllers.admin.templates.create;

import fiit.vava.client.bundles.XMLResourceBundle;
import fiit.vava.client.bundles.XMLResourceBundleProvider;
import com.google.protobuf.ByteString;
import fiit.vava.client.Router;
import fiit.vava.client.StubsManager;
import fiit.vava.client.controllers._components.FileUploadController;
import fiit.vava.client.controllers.auth.Validation;
import fiit.vava.server.CreateDocumentTemplateRequest;
import fiit.vava.server.DocumentServiceGrpc;
import fiit.vava.server.DocumentTemplateField;
import fiit.vava.server.DocumentTemplateType;
import io.github.palexdev.mfxcore.utils.fx.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateTemplateController {

    @FXML
    public VBox fieldsPane;
    @FXML
    public TextField templateName;
    @FXML
    public TextField templateRequirements;
    public Label templateNameLabel;
    public Label labelError;
    public Label typeLabel;
    public ComboBox type;
    public Button addField;
    public Button submitTemplate;

    XMLResourceBundleProvider instance;

    public CreateTemplateController() {
        this.instance = XMLResourceBundleProvider.getInstance();
    }

    private final List<List<Control>> fields = new ArrayList<>();

    private Node createFieldGroup() {
        VBox fieldGroup = new VBox() {{
            setStyle("-fx-border-width: 1; -fx-border-width: 1 0 0 0; -fx-border-color: #a0aec0; -fx-padding: 5 0");
        }};

        TextField nameField = new TextField() {{
            setPromptText("Name");
            setStyle("-fx-font-size: 1em; -fx-font-weight: medium; -fx-text-fill: #2d3748;");
        }};
        ComboBox<String> typeField = new ComboBox<String>() {{
            setPromptText("Select an option");
            getItems().addAll(
                    "STRING",
                    "DATE",
                    "NUMBER"
            );
            setStyle("-fx-font-size: 1em; -fx-font-weight: medium; -fx-text-fill: #2d3748;");
            setValue("String");
        }};
        Control requiredCheckbox = new CheckBox() {{
            setText("Is required");
            setStyle("-fx-font-size: 1em; -fx-font-weight: medium; -fx-text-fill: #2d3748;");
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
        return fieldGroup;
    }

    @FXML
    public void handleAddField() {
        fieldsPane.getChildren().add(createFieldGroup());
    }

    @FXML
    public void handleCreateTemplate() {
        try {
            checkFields();

            DocumentServiceGrpc.DocumentServiceBlockingStub stub = StubsManager.getInstance().getDocumentServiceBlockingStub();

            File uploadedFile = (File) Router.getInstance().getSharedData(FileUploadController.FILE_SHARED_KEY);

            if (uploadedFile == null)
                throw new Exception("Uploaded document is missing");

            CreateDocumentTemplateRequest request = CreateDocumentTemplateRequest.newBuilder()
                    .setName(templateName.getText())
                    .setType(DocumentTemplateType.valueOf((String) type.getValue()))
                    //.setRequirements(templateRequirements.getText())
                    .setFile(ByteString.copyFrom(Files.readAllBytes(uploadedFile.toPath())))
                    .addAllFields(fields.stream()
                            .map(field -> DocumentTemplateField.newBuilder()
                                    .setName(((TextField) field.get(0)).getText())
                                    .setType(((ComboBox<String>) field.get(1)).getValue())
                                    .setRequired(((CheckBox) field.get(2)).isSelected())
                                    .build())
                            .toList())
                    .build();


            stub.createDocumentTemplate(request);

            Router.getInstance().navigateTo("admin/templates");
        } catch (Exception e) {
            e.printStackTrace();
            labelError.setText(e.getMessage());
        }
    }

    public void initialize() {
        loadTexts();

        instance.subscribe(language -> loadTexts());
        type.getItems().addAll(Arrays.stream(DocumentTemplateType.values()).map(Enum::name).toList());

        fieldsPane.getChildren().add(createFieldGroup());

        // Link to FileUploadController

        FileUploadController fileUploadController = (FileUploadController) Router.getInstance().getSharedData(FileUploadController.FILE_SHARED_CONTROLLER);

        if (fileUploadController == null)
            return;

        fileUploadController.setAllowedFileFormats(new String[]{"*.pdf"});
        fileUploadController.getFileFormatInfo().setText("PDF files (*.pdf)");

        fileUploadController.setFromFileToImage(file -> {
            try {
                PDDocument document = PDDocument.load(file);
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 300);
                document.close();

                double aspectRatio = (double) bim.getWidth() / bim.getHeight();
                fileUploadController.getCoverPhotoPlaceholder().setFitHeight(400);

                double width = 400 * aspectRatio;

                if (width > 300)
                    width = 300;

                fileUploadController.getCoverPhotoPlaceholder().setFitWidth(width);

                return SwingFXUtils.toFXImage(bim, null);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    private void checkFields() throws Exception {
        String error = Validation.validateFields(
                Validation.pair("Name is required", Validation.notNull)
                        .set(templateName.getText()),
                Validation.pair("Type is required", Validation.notNull)
                        .set((String) type.getValue())
        );

        if (error != null)
            throw new Exception(error);
    }

    private void loadTexts() {
        XMLResourceBundle bundle = instance.getBundle("fiit.vava.client.bundles.admin");

        if (bundle == null)
            return;
        templateNameLabel.setText(bundle.getString("templates.label.name")); 
        templateName.setPromptText(bundle.getString("templates.label.name")); 
        typeLabel.setText(bundle.getString("templates.label.type")); 
        addField.setText(bundle.getString("templates.button.add")); 
        submitTemplate.setText(bundle.getString("templates.button.submit")); 
    }
}
