package fiit.vava.client.bundles;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * XML Resource Bundle
 *
 * @author slabbe
 */
public class XMLResourceBundle extends ResourceBundle {

    private final Properties properties;

    public XMLResourceBundle(InputStream stream) throws IOException {
        properties = new Properties();

        if (stream != null)
            properties.loadFromXML(stream);
    }

    @Override
    protected Object handleGetObject(String key) {
        return properties.getProperty(key);
    }

    @Override
    public Enumeration<String> getKeys() {
        Set<String> handleKeys = properties.stringPropertyNames();
        return Collections.enumeration(handleKeys);
    }
}
