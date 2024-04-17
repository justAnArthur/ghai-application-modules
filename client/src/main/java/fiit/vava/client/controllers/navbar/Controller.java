package fiit.vava.client.controllers.navbar;

import fiit.vava.client.Router;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.util.Locale;
import fiit.vava.client.bundles.SupportedLanguages;
import fiit.vava.client.bundles.XMLResourceBundle;
import fiit.vava.client.bundles.XMLResourceBundleProvider;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label firstBtn;

    @FXML
    private Label secondBtn;

    @FXML
    private Label settingsBtn;

    @FXML
    private Label profileBtn;

    @FXML
    void initialize() {
      switch (Router.getRole()) {
        case "client":
            firstBtn.setText("Documents");
            firstBtn.setOnMouseClicked(event -> {
              noHighlight(event);
              try {
                Router.getInstance().navigateTo("client/documents");
              } catch (IOException e) {
                e.printStackTrace();
              }
            });
            secondBtn.setText("Templates");
            secondBtn.setOnMouseClicked(event -> {
              noHighlight(event);
              try {
                Router.getInstance().navigateTo("client/templates");
              } catch (IOException e) {
                e.printStackTrace();
              }
            });
          break;

        case "admin":
            firstBtn.setText("Templates");
            firstBtn.setOnMouseClicked(event -> {
              noHighlight(event);
              try {
                Router.getInstance().navigateTo("admin/templates");
              } catch (IOException e) {
                e.printStackTrace();
              }
            });
            secondBtn.setText("Users");
            secondBtn.setOnMouseClicked(event -> {
              noHighlight(event);
              try {
                Router.getInstance().navigateTo("admin/users");
              } catch (IOException e) {
                e.printStackTrace();
              }
            });
          break;
        case "coworker":
            firstBtn.setText("Document Verification");
            // firstBtn.setOnMouseClicked(event -> {
            //   noHighlight();
            //   Router.getInstance().navigateTo("coworker/templates");
            // });
            secondBtn.setText("User Verification");
            // secondBtn.setOnMouseClicked(event -> {
            //   noHighlight();
            //   Router.getInstance().navigateTo("admin/users");
            // });
          break;
        default:
          break;
      } 
    }
    @FXML
    public void handleProfileBtn(MouseEvent event) throws IOException {
        noHighlight(event);
        profileBtn.setStyle("-fx-background-color: #555;");
        Router.getInstance().navigateTo("profile");
    }

    @FXML
    public void handleSettingsBtn(MouseEvent event) {
        if(XMLResourceBundleProvider.getInstance().getCurrentLanguage().getLocale().equals(Locale.ENGLISH)){
            XMLResourceBundleProvider.getInstance().changeLanguage(SupportedLanguages.SLOVAK);
        }else {
            XMLResourceBundleProvider.getInstance().changeLanguage(SupportedLanguages.ENGLISH);
        }
        System.out.println(XMLResourceBundleProvider.getInstance().getCurrentLanguage().getLocale());
    }

    private void noHighlight(MouseEvent event) {
        firstBtn.setStyle("-fx-background-color: transparent;");
        secondBtn.setStyle("-fx-background-color: transparent;");
        settingsBtn.setStyle("-fx-background-color: transparent;");
        profileBtn.setStyle("-fx-background-color: transparent;");
        Label label = (Label) event.getSource();
        label.setStyle("-fx-background-color: #555;");

    }
}
