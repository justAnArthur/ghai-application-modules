package fiit.vava.client.bundles;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public enum SupportedLanguages {
    ENGLISH(Locale.ENGLISH),
    SLOVAK(new Locale("sk"));

    private final Locale code;

    SupportedLanguages(Locale code) {
        this.code = code;
    }

    public Locale getLocale() {
        return code;
    }

    public static List<SupportedLanguages> asList() {
        return Arrays.asList(values());
    }
}