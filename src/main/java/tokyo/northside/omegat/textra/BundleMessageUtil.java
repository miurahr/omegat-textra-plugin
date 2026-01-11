package tokyo.northside.omegat.textra;

import java.util.ResourceBundle;

/**
 * Utility class for accessing resource bundle.
 */
public final class BundleMessageUtil {

    /**
     * Error numbers that are defined in resource bundle.
     */
    private static final int[] ERROR_NUMS = {
        500, 501, 502, 503, 504, 505, 510, 511, 520, 521, 522, 523, 524, 525, 530, 531, 532, 533
    };

    private BundleMessageUtil() {}

    /** Resource bundle that contains all the strings. */
    private static final ResourceBundle BUNDLE =
            ResourceBundle.getBundle("tokyo.northside.omegat.textra.TextraMachineTranslation");

    public static String getString(String key) {
        return BUNDLE.getString(key);
    }

    /**
     * Get localized error message.
     * @param num error number.
     * @return error message.
     */
    public static String getErrorMessage(int num) {
        String key = "ErrorMessage." + num;
        if (hasErrorMessage(num)) {
            return BUNDLE.getString(key);
        }
        return BUNDLE.getString("ErrorMessage.Unknown");
    }

    public static boolean hasErrorMessage(int num) {
        for (int i : ERROR_NUMS) {
            if (num == i) {
                return true;
            }
        }
        return false;
    }
}
