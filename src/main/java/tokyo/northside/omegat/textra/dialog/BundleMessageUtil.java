package tokyo.northside.omegat.textra.dialog;

import java.util.ResourceBundle;

public final class BundleMessageUtil {

    private BundleMessageUtil() {
    }

    /** Resource bundle that contains all the strings */
    private static final ResourceBundle bundle = ResourceBundle.getBundle("tokyo.northside.omegat.textra.TextraMachineTranslation");

    public static String getString(String key) {
        return bundle.getString(key);
    }
}
