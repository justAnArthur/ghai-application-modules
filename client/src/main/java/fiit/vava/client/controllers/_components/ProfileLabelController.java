package fiit.vava.client.controllers._components;

import fiit.vava.client.bundles.XMLResourceBundle;
import fiit.vava.client.bundles.XMLResourceBundleProvider;
import fiit.vava.client.StubsManager;
import fiit.vava.server.Empty;
import fiit.vava.server.User;
import fiit.vava.server.UserServiceGrpc;
import javafx.scene.control.Label;

public class ProfileLabelController {
    public Label profileNameLabel;
    public Label profileName;


    XMLResourceBundleProvider instance;

    public ProfileLabelController() {
        this.instance = XMLResourceBundleProvider.getInstance();
    }
    public void initialize() {
        loadTexts();

        instance.subscribe(language -> loadTexts());
        UserServiceGrpc.UserServiceBlockingStub stub = StubsManager.getInstance().getUserServiceBlockingStub();

        User user = stub.me(Empty.newBuilder().build());

        // TODO display name by switch loading correct client or coworker

        profileName.setText(user.getEmail());
    }

    private void loadTexts() {
        XMLResourceBundle bundle = instance.getBundle("fiit.vava.client.bundles.components");

        if (bundle == null)
            return;
        profileNameLabel.setText(bundle.getString("sidebar.label.yourProfile")); 
    }
}
