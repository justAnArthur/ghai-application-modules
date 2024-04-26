package fiit.vava.client.controllers.auth.registration;

import com.google.protobuf.ByteString;
import fiit.vava.client.Router;
import fiit.vava.client.StubsManager;
import fiit.vava.client.bundles.XMLResourceBundle;
import fiit.vava.client.bundles.XMLResourceBundleProvider;
import fiit.vava.client.controllers._components.FileUploadController;
import fiit.vava.client.controllers.auth.Validation;
import fiit.vava.server.*;
import javafx.scene.control.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;

public class ClientRegistrationController {
    public Label labelHeaderRegistration;
    public Label labelFirstName;
    public TextField firstName;
    public Label labelLastName;
    public TextField lastName;
    public Label labelCountry;
    public TextField country;
    public Label labelRegion;
    public TextField region;
    public Label labelDateOfBirth;
    public DatePicker dateOfBirth;
    public Label labelEmail;
    public TextField email;
    public Label labelPassword;
    public PasswordField password;
    public Label labelPasswordAgain;
    public PasswordField passwordAgain;
    public Button signUp;
    public Label labelError;
    public Label labelSuccess;
    public Label labelGoToLogin;
    public Button goToLogin;

    XMLResourceBundleProvider instance;

    public ClientRegistrationController() {
        this.instance = XMLResourceBundleProvider.getInstance();
    }

    public void handleGoToLogin() throws IOException {
        Router.getInstance().navigateTo("auth/login");
    }

    public void handleSignUp() {
        labelError.setText(null);

        try {
            checkFields();

            File uploadedFile = (File) Router.getInstance().getSharedData(FileUploadController.FILE_SHARED_KEY);

            if (uploadedFile == null)
                throw new Exception("Uploaded document is missing");

            UserServiceGrpc.UserServiceBlockingStub stub = StubsManager.getInstance().getUserServiceBlockingStub();

            RegisterClientRequest request = RegisterClientRequest.newBuilder()
                    .setClient(Client.newBuilder()
                            .setUser(User.newBuilder()
                                    .setEmail(email.getText())
                                    .setPassword(/*password.getText()*/"client")
                                    .build()
                            )
                            .setFirstName(firstName.getText())
                            .setLastName(lastName.getText())
                            .setDateOfBirth(dateOfBirth.getValue().toString())
//                            .setRegion(region.getText())
                            .build())
                    .setDocumentFile(ByteString.copyFrom(Files.readAllBytes(uploadedFile.toPath())))
                    .build();

            Response response = stub.registerClient(request);

            Router.getInstance().navigateTo("auth/awaitingOnConfirmation");

        } catch (Exception e) {
            labelError.setText(e.getMessage());
        }
    }

    public void initialize() {
        loadTexts(instance);

        instance.subscribe(language -> loadTexts(instance));
    }

    private void loadTexts(XMLResourceBundleProvider instance) {
        XMLResourceBundle bundle = instance.getBundle("fiit.vava.client.bundles.auth");

        if (bundle == null)
            return;

        labelHeaderRegistration.setText(bundle.getString("header.registration"));
        labelFirstName.setText(bundle.getString("label.firstname"));
        labelLastName.setText(bundle.getString("label.lastname"));
        // labelCountry.setText(bundle.getString("label.country"));
        // labelRegion.setText(bundle.getString("label.region"));
        labelDateOfBirth.setText(bundle.getString("label.date"));
        labelEmail.setText(bundle.getString("label.email"));
        // labelPassword.setText(bundle.getString("label.password"));
        // labelPasswordAgain.setText(bundle.getString("label.passwordAgain"));
        signUp.setText(bundle.getString("button.signUp"));
        labelGoToLogin.setText(bundle.getString("label.goToLogin"));
        goToLogin.setText(bundle.getString("button.goToLogin"));

        firstName.setPromptText(bundle.getString("label.firstname"));
        lastName.setPromptText(bundle.getString("label.lastname"));
        // country.setPromptText(bundle.getString("label.country"));
        // region.setPromptText(bundle.getString("label.region"));
        dateOfBirth.setPromptText(bundle.getString("label.date"));
        email.setPromptText(bundle.getString("label.email"));
        // password.setPromptText(bundle.getString("label.password"));
        // passwordAgain.setPromptText(bundle.getString("label.passwordAgain"));
    }

    private void checkFields() throws Exception {
        String date = "";

        if (dateOfBirth.getValue() != null)
            date = dateOfBirth.getValue().toString();

        String error = Validation.validateFields(
                Validation.pair("First name might be filled correctly", Validation.notNull, Validation.name)
                        .set(firstName.getText()),
                Validation.pair("Last name might be filled correctly", Validation.notNull, Validation.name)
                        .set(lastName.getText()),
                Validation.pair("Email is not valid", Validation.notNull, Validation.email)
                        .set(email.getText()),
//                Validation.pair("Password is not valid", Validation.notNull, Validation.password)
//                        .set(password.getText()),
                Validation.pair("Date is minimum 18 years", Validation.notNull,
                                _date -> LocalDate.now().minusYears(18).isAfter(LocalDate.parse(_date)))
                        .set(date)
        );

        if (error != null)
            throw new Exception(error);
    }
}
