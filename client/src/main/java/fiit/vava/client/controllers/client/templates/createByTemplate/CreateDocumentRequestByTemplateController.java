package fiit.vava.client.controllers.client.templates.createByTemplate;

import fiit.vava.client.Router;
import fiit.vava.client.StubsManager;
import fiit.vava.client.bundles.XMLResourceBundle;
import fiit.vava.client.bundles.XMLResourceBundleProvider;
import fiit.vava.client.controllers.auth.Validation;
import fiit.vava.server.*;
import io.github.palexdev.mfxcore.utils.fx.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CreateDocumentRequestByTemplateController {

    @FXML
    public VBox fieldsPane;
    @FXML
    public ImageView pdfPreview;
    @FXML
    Label errorLabel;
    @FXML
    Label templateName;
    @FXML
    Button submit;

    XMLResourceBundleProvider instance;


    public CreateDocumentRequestByTemplateController() {
        this.instance = XMLResourceBundleProvider.getInstance();
    }

    private final List<Node> fields = new ArrayList<>();

    private List<DocumentTemplateField> documentTemplateFields = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger("client." + CreateDocumentRequestByTemplateController.class);

    private Node createField(DocumentTemplateField templateField) {
        VBox fieldGroup = new VBox();

        Label label = new Label(templateField.getName()) {{
            setStyle("-fx-font-size: 1em; -fx-font-weight: medium; -fx-text-fill: #2d3748;");
        }};
        fieldGroup.getChildren().add(label);

        if (templateField.getRequired()) {
            Label required = new Label("Required") {{
                setStyle("-fx-font-size: 1em; -fx-font-weight: bold; -fx-text-fill: #ff0000;");
            }};
            fieldGroup.getChildren().add(required);
        }

        Node field = null;

        switch (templateField.getType()) {
            case "DATE":
                field = new DatePicker();
                break;
            default:
                field = new TextField();
                break;
        }

        fieldGroup.getChildren().add(field);
        fields.add(field);

        return fieldGroup;
    }

    @FXML
    public void handleCreateDocumentRequest(ActionEvent actionEvent) {
        try {
            DocumentServiceGrpc.DocumentServiceBlockingStub stub = StubsManager.getInstance().getDocumentServiceBlockingStub();

            String id = Router.getInstance().getParameter("templateId");

            CreateDocumentRequest request = CreateDocumentRequest.newBuilder()
                    .setTemplateId(id)
                    .build();

            for (int i = 0; i < fields.size(); i++) {
                DocumentTemplateField templateField = documentTemplateFields.get(i);

                Node textField = fields.get(i);

                // handle different types of fields
                String serializedValue = null;

                Predicate<String>[] validationPairs = new Predicate[]{Validation.notNull};

                switch (templateField.getType()) {
                    case "DATE":
                        LocalDate date = ((DatePicker) textField).getValue();
                        if (date != null)
                            serializedValue = date.toString();
                        break;
                    case "NUMBER":
                        validationPairs = new Predicate[]{Validation.notNull, Validation.number};
                    default:
                        serializedValue = ((TextField) textField).getText();
                        break;
                }

                if (templateField.getRequired()) {
                    logger.debug("help" + templateField.getType() + " " + serializedValue);

                    String errored = Validation.validateFields(
                            Validation.pair(templateField.getName() + " is not valid", validationPairs)
                                    .set(serializedValue)
                    );

                    if (errored != null)
                        throw new Exception(errored);
                }

                DocumentField documentField = DocumentField.newBuilder()
                        .setField(documentTemplateFields.get(i))
                        .setValue(serializedValue)
                        .build();

                request = request.toBuilder().addFields(documentField).build();
            }

            stub.createDocumentRequest(request);

            Router.getInstance().navigateTo("client/documents");
        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        loadTexts();
        instance.subscribe(language -> loadTexts());
        loadData();
    }

    private void loadData() {
        String id = Router.getInstance().getParameter("templateId");

        DocumentServiceGrpc.DocumentServiceBlockingStub stub = StubsManager.getInstance().getDocumentServiceBlockingStub();

        DocumentTemplateWithFields template = stub.getDocumentTemplateById(GetDocumentTemplateByIdRequest.newBuilder()
                .setId(id).build());

        templateName.setText(template.getTemplate().getName());

        template.getFieldsList().forEach(templateField -> {
            fieldsPane.getChildren().add(createField(templateField));
            documentTemplateFields.add(templateField);
        });

        byte[] file = stub.getFileByPath(GetFileByPathRequest.newBuilder()
                        .setPath(template.getTemplate().getPath()).build())
                .getFile()
                .toByteArray();

        try {
            PDDocument document = PDDocument.load(file);

            PDFRenderer renderer = new PDFRenderer(document);

            BufferedImage bim = renderer.renderImageWithDPI(0, 100);

            double aspectRatio = (double) bim.getWidth() / bim.getHeight();

            double width = 400 * aspectRatio;

            if (width > 300)
                width = 300;

            pdfPreview.setFitWidth(width);
            pdfPreview.setFitHeight(400);

            Image fxImage = SwingFXUtils.toFXImage(bim, null);
            pdfPreview.setImage(fxImage);
        } catch (IOException e) {
            logger.warn("Failed to load pdf");
            e.printStackTrace();
        }
    }

    private void loadTexts() {
        XMLResourceBundle bundle = instance.getBundle("fiit.vava.client.bundles.client");

        if (bundle == null)
            return;

        submit.setText(bundle.getString("templates.button.submit"));
    }
}
