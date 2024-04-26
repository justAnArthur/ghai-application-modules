package fiit.vava.client.controllers.admin;

import fiit.vava.client.bundles.XMLResourceBundle;
import fiit.vava.client.bundles.XMLResourceBundleProvider;
import fiit.vava.client.Router;
import javafx.scene.control.Button;

import java.io.IOException;

public class AdminSideBarController {

    public Button templates;

    XMLResourceBundleProvider instance;

    public AdminSideBarController() {
        this.instance = XMLResourceBundleProvider.getInstance();
    }


    public void handleTemplates() throws IOException {
        Router.getInstance().navigateTo("admin/templates");
    }

    public void initialize() {
        loadTexts();
        
        instance.subscribe(language -> loadTexts());
    }

    public void loadTexts() {
        XMLResourceBundle bundle = instance.getBundle("fiit.vava.client.bundles.admin");

        if (bundle == null)
            return;
        templates.setText(bundle.getString("sidebar.button.documents")); 
    }
}
