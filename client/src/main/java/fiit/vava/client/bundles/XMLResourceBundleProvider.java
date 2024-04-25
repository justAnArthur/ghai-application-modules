package fiit.vava.client.bundles;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.spi.ResourceBundleProvider;

/**
 * Xml resource bundle control
 *
 * @author qvadym
 */
public class XMLResourceBundleProvider implements ResourceBundleProvider {

    private static XMLResourceBundleProvider instance = null;

    private SupportedLanguages currentLanguage;

    private List<OnChangeValueListener> listeners = new ArrayList<>();

    private XMLResourceBundleProvider() {
        this.currentLanguage = SupportedLanguages.ENGLISH;
    }

    public static synchronized XMLResourceBundleProvider getInstance() {
        if (instance == null) {
            instance = new XMLResourceBundleProvider();
        }

        return instance;
    }

    // intl

    public XMLResourceBundle getBundle(String baseName) {
        return getBundle(baseName, currentLanguage.getLocale());
    }

    @Override
    public XMLResourceBundle getBundle(String baseName, Locale locale) {
        String bundleName = toBundleName(baseName, locale);
        String resourceName = toResourceName(bundleName, "xml");

        URL url = XMLResourceBundleProvider.class.getResource("/" + resourceName);

        if (url == null)
            return null;

        try (InputStream stream = url.openStream();
             BufferedInputStream bis = new BufferedInputStream(stream)) {
            return new XMLResourceBundle(bis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load resource bundle", e);
        }
    }

    private String toBundleName(String baseName, Locale locale) {
        return baseName + "." + locale;
    }

    public void changeLanguage(SupportedLanguages locale) {
        this.currentLanguage = locale;
        notifyListeners();
    }

    public SupportedLanguages getCurrentLanguage() {
        return currentLanguage;
    }

    private String toResourceName(String bundleName, String format) {
        return bundleName.replace('.', '/') + "." + format;
    }

    // observable

    public void subscribe(OnChangeValueListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        listeners.forEach(listener -> listener.onChangeValue(this.currentLanguage));
    }

    public interface OnChangeValueListener {
        void onChangeValue(SupportedLanguages language);
    }
}