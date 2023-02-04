package tokyo.northside.omegat.textra;

import java.util.ResourceBundle;

public class StringUtil {
    /** Resource bundle that contains all the strings */
    private static ResourceBundle bundle;

    static {
        bundle = ResourceBundle.getBundle("tokyo.northside.omegat.textra.TextraMachineTranslation");
    }

    public static String getString(String key) {
        return bundle.getString(key);
    }
}
