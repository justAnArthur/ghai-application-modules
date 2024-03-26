package fiit.vava.client.bundles;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.spi.ResourceBundleProvider;
/**
 * Xml resource bundle control
 * @author qvadym
 */

public class XMLResourceBundleProvider implements ResourceBundleProvider {
    
    @Override
    public XMLResourceBundle getBundle(String baseName, Locale locale) {
        String bundleName = toBundleName(baseName, locale);
        String resourceName = toResourceName(bundleName, "xml");
        
        URL url = XMLResourceBundleProvider.class.getResource("/" + resourceName);
        if (url == null) {
            return null;
        }
        
        try (InputStream stream = url.openStream();
             BufferedInputStream bis = new BufferedInputStream(stream)) {
            return new XMLResourceBundle(bis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load resource bundle", e);
        }
    }

    private String toBundleName(String baseName, Locale locale) {
        return baseName + "_" + locale;
    }

    private String toResourceName(String bundleName, String format) {
        return bundleName.replace('.', '/') + "." + format;
    }
}