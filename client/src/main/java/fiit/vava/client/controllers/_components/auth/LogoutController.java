package fiit.vava.client.controllers._components.auth;

import fiit.vava.client.bundles.XMLResourceBundle;
import fiit.vava.client.bundles.XMLResourceBundleProvider;
import fiit.vava.client.CredentialsManager;
import fiit.vava.client.Router;

import javafx.scene.control.Button;

import java.io.IOException;

public class LogoutController {
    
    public Button logoutButton;

    public void handleLogout() throws IOException {
        CredentialsManager.removeCredentials();

        Router.getInstance().removeSidebar();
        Router.getInstance().navigateTo("auth/login");
    }

    XMLResourceBundleProvider instance;

    public LogoutController() {
        this.instance = XMLResourceBundleProvider.getInstance();
    }

    public void initialize() {
      loadTexts();

      instance.subscribe(language -> loadTexts());
    }

    private void loadTexts() {
        XMLResourceBundle bundle = instance.getBundle("fiit.vava.client.bundles.components");

        if (bundle == null)
            return;
        logoutButton.setText(bundle.getString("sidebar.button.logout")); 
    }
}
