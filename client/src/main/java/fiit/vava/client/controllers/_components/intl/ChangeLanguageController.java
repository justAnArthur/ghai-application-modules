package fiit.vava.client.controllers._components.intl;

import fiit.vava.client.bundles.SupportedLanguages;
import fiit.vava.client.bundles.XMLResourceBundle;
import fiit.vava.client.bundles.XMLResourceBundleProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class ChangeLanguageController {

    public Label labelLanguage;
    public ComboBox language;

    public void initialize() {
        XMLResourceBundleProvider instance = XMLResourceBundleProvider.getInstance();

        loadTexts(instance);

        instance.subscribe(new XMLResourceBundleProvider.OnChangeValueListener() {
            @Override
            public void onChangeValue(SupportedLanguages language) {
                loadTexts(instance);
            }
        });

        // init values

        ObservableList<String> options =
                FXCollections.observableArrayList(SupportedLanguages.asList().stream().map(SupportedLanguages::name).toList());

        language.setItems(options);
        language.getSelectionModel().select(instance.getCurrentLanguage().name()); // Set default value

        language.getSelectionModel().selectedItemProperty().addListener((_options, oldValue, newValue) -> {
            instance.changeLanguage(SupportedLanguages.valueOf(newValue.toString()));
        });
    }

    private void loadTexts(XMLResourceBundleProvider instance) {
        XMLResourceBundle bundle = instance.getBundle("fiit.vava.client.bundles.components");

        if (bundle == null)
            return;

        labelLanguage.setText(bundle.getString("label.changeLanguage"));
    }
}
