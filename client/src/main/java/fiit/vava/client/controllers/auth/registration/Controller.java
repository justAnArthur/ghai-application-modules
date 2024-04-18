package fiit.vava.client.controllers.auth.registration;

import fiit.vava.client.bundles.SupportedLanguages;
import fiit.vava.client.bundles.XMLResourceBundle;
import fiit.vava.client.bundles.XMLResourceBundleProvider;
import fiit.vava.client.CredentialsManager;
import fiit.vava.client.Router;
import fiit.vava.client.StubsManager;
import fiit.vava.server.*;
import fiit.vava.client.Router;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import fiit.vava.client.controllers.auth.Validation;
import java.util.function.Predicate;
import java.io.File;
import java.io.IOException;
import fiit.vava.client.controllers._components.ImageReceiver;
public class Controller implements ImageReceiver {
    @FXML
    private MFXTextField countryField;

    @FXML
    private DatePicker dateOfBirthPicker;

    @FXML
    private MFXTextField emailField;

    @FXML
    private Label errorMessageLabel;

    @FXML
    private MFXTextField fNameField;

    @FXML
    private MFXTextField lNameField;

    @FXML
    private MFXComboBox<String> langSelectCombo;

    @FXML
    private MFXPasswordField passwordField;

    @FXML
    private MFXPasswordField passwordAgainField;
    
    @FXML
    private MFXTextField regionField;

    @FXML
    private Button signInBtn;

    @FXML
    private Button signUpBtn;
    
    private File frontsideImage = null;
    private File backsideImage = null;
    
    @Override 
    public void setImages(File frontsideImage, File backsideImage){
      this.frontsideImage = frontsideImage;
      this.backsideImage = backsideImage;
    } 

    @FXML
    public void initialize() {
        initValidators();
        langSelectCombo.getItems().addAll(SupportedLanguages.asList().stream().map(SupportedLanguages::name).toList());
        langSelectCombo.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            // TODO exchange on @FXML controller method
            XMLResourceBundleProvider.getInstance().changeLanguage(SupportedLanguages.valueOf(newValue));
            // loadTexts();
        });
        // loadTexts();
    }

    private void initValidators(){
      emailField.textProperty().addListener((observable,oldValue, newValue) -> Validation.validateField(newValue, emailField, Validation.emailValidation));
      passwordField.textProperty().addListener((observable,oldValue, newValue) -> Validation.validateField(newValue, passwordField, Validation.passwordValidation));
      passwordAgainField.textProperty().addListener((observable,oldValue, newValue) -> { 
      Validation.validateField(newValue, passwordAgainField, Validation.passwordValidation);
      if(newValue!=passwordField.getText()){
        passwordAgainField.setStyle("-fx-border-color: red;");
      } else {
        passwordAgainField.setStyle("-fx-border-color: green;");
      }
    });
      countryField.textProperty().addListener((observable, oldValue, newValue) -> Validation.validateField(newValue, countryField, Validation.nameValidation)); 
      regionField.textProperty().addListener((observable, oldValue, newValue) -> Validation.validateField(newValue, regionField, Validation.nameValidation)); 
      fNameField.textProperty().addListener((observable, oldValue, newValue) -> Validation.validateField(newValue, fNameField, Validation.nameValidation)); 
      lNameField.textProperty().addListener((observable, oldValue, newValue) -> Validation.validateField(newValue, lNameField, Validation.nameValidation));
    }
    // private void loadTexts() {
    //     XMLResourceBundle bundle = XMLResourceBundleProvider.getInstance().getBundle("fiit.vava.client.bundles.auth.messages");
    //
    // }
    @FXML
    private void goToLogin(ActionEvent event) throws IOException {
      Router.getInstance().navigateTo("auth/login");
    }

    // Doesn't work
    @FXML
    private void handleRegistration(ActionEvent event) throws IOException {
        errorMessageLabel.setText(null);
        if (fNameField.getText() == null || lNameField.getText() == null || countryField.getText() == null || regionField.getText() == null || emailField.getText() == null || passwordField.getText() == null || passwordAgainField.getText() == null || dateOfBirthPicker.getValue() == null) {
          errorMessageLabel.setText("All fields must be filled.");
          return;
        }
        if (passwordField.getText() != passwordAgainField.getText()){
          errorMessageLabel.setText("Passwords must match.");
          return;
        }
        if(!Validation.emailValidation.test(emailField.getText())){
          errorMessageLabel.setText("Email must be valid");
          return;
        } else if (!Validation.passwordValidation.test(passwordField.getText())){
          errorMessageLabel.setText("Password must contain at least 8 symbols");
          return;
        } else if (!Validation.nameValidation.test(lNameField.getText()) || !Validation.nameValidation.test(fNameField.getText()) || !Validation.nameValidation.test(countryField.getText()) || !Validation.nameValidation.test(regionField.getText())){
          errorMessageLabel.setText("Name and region fields must contain letters only");
          return;
        }
        if(frontsideImage == null || backsideImage == null){
          Router.getInstance().showModal("/fiit/vava/client/fxml/_components/fileUpload.fxml", this);
        } else { 
          UserServiceGrpc.UserServiceBlockingStub stub = StubsManager.getInstance().getUserServiceBlockingStub();

          Client request = Client.newBuilder()
                  .setUser(User.newBuilder()
                          .setEmail(emailField.getText())
                          .setPassword(passwordField.getText())
                          .build()
                  )
                  .setFirstName(fNameField.getText())
                  .setLastName(lNameField.getText())
                  .setDateOfBirth(dateOfBirthPicker.getValue().toString())
                  .setRegion(regionField.getText())
                  .setCountry(countryField.getText())
                  .build();

          Response response = stub.registerClient(request);

          if (response.getUser() != null) {
              errorMessageLabel.setText("Status: " + response.getUser().getStatus().name());
          } else {
              errorMessageLabel.setText(response.getError());
          }
        }
    }
}
