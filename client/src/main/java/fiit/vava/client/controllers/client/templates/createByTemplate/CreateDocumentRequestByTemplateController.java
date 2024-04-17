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

public class CreateDocumentRequestByTemplateController {

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

    private Node createFieldGroup(DocumentTemplateField templateField) {
        VBox fieldGroup = new VBox();

        if(templateField.getRequired()){
          Label required = new Label("Required");  
          fieldGroup.getChildren().add(required); 
        }
        Label label = new Label(templateField.getName());
        // todo select the correct type of field by `templateField.getType()`.
        // now - just text field.
        fieldGroup.getChildren().add(label); 
        
        TextField valueField = null; // Declare outside the switch statement
        switch (templateField.getType()) {
            case "String":
                valueField = new TextField();
                fieldGroup.getChildren().add(valueField);
                fields.add(valueField);
                break;
            case "Number":
                valueField = new TextField();
                valueField.setTextFormatter(new TextFormatter<>(this::validateNumberInput));
                fieldGroup.getChildren().add(valueField);
                fields.add(valueField);
                break;
            case "Date":
                DatePicker datePicker = new DatePicker();
                fieldGroup.getChildren().add(datePicker);
                fields.add(datePicker);
                break;
            default:
                break;
        } 
        requiredMap.put(fields.get(fields.size()-1),templateField.getRequired()); 
        // todo Might be created callback function to change a value in Map
        // when several fields of different types without one shared interface to access a value
        // fields.add(valueField);

        // todo handle required, not required fields

        // fieldGroup.getChildren().addAll(label, valueField);

        return fieldGroup;
    }
  private TextFormatter.Change validateNumberInput(TextFormatter.Change change) {
      String newText = change.getControlNewText();
      if (newText.matches("-?\\d*\\.?\\d*")) { // Allow digits, optional negative sign, optional decimal point
          return change;
      }
      return null; // Reject the change
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
        errorLabel.setText("");
        CreateDocumentRequest request = null;
        DocumentServiceGrpc.DocumentServiceBlockingStub stub = StubsManager.getInstance().getDocumentServiceBlockingStub();
        try {
          request = createRequest(stub); 
        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
            return;
        }
        try {
            stub.createDocumentRequest(request);

            Router.getInstance().navigateTo("client/documents");
        } catch (Exception e) {
            // handle the error
            e.printStackTrace();
        }
    }


    private CreateDocumentRequest createRequest(DocumentServiceGrpc.DocumentServiceBlockingStub stub ) throws RequiredFieldIsEmpty {


        String id = Router.getInstance().getParameter("templateId");

        CreateDocumentRequest request = CreateDocumentRequest.newBuilder()
                .setTemplateId(id)
                .build();

        for (int i = 0; i < fields.size(); i++) {
            String value = null;
            DocumentField documentField = null;
            Node node = fields.get(i);
            if (node instanceof TextField){
              TextField textField = (TextField) node;
              String text = textField.getText().trim();
              System.out.println(text);
              if(text.equals("") && requiredMap.get(textField)){
                throw new RequiredFieldIsEmpty("Text field is empty"); 
              }else if(text.equals("")){
                System.out.println("Field not required, not filled, skipped");
                continue;
              }
              value = text;
            } else if (node instanceof DatePicker){
              System.out.println("DATE");
              DatePicker datePicker = (DatePicker) node;
              LocalDate selectedDate = datePicker.getValue();
              System.out.println(selectedDate);
              if (selectedDate == null && requiredMap.get(datePicker)){
                throw new RequiredFieldIsEmpty("Date field is empty"); 
              } else if(selectedDate == null) {
                System.out.println("Field not required, not filled, skipped");
                continue;
              }

              DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
              value = selectedDate.format(formatter);
            } 
            documentField = DocumentField.newBuilder()
                    .setField(documentTemplateFields.get(i))
                    .setValue(value)
                    .build();
            request = request.toBuilder().addFields(documentField).build();
        }
        return request;
    }
}
