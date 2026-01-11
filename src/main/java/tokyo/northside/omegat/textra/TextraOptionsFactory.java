package tokyo.northside.omegat.textra;

import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Factory for creating TextraOptions instances.
 */
public class TextraOptionsFactory {

    /** Resource bundle of configuration. */
    private final ResourceBundle resources;

    /**
     * Constructor.
     */
    public TextraOptionsFactory() {
        resources = ResourceBundle.getBundle("tokyo/northside/omegat/textra/TextraApiConfiguration");
    }

    /**
     * Tokenize input string by splitting it using whitespace as the delimiter.
     * @param input input string to tokenize.
     * @return array of tokens.
     */
    private String[] tokenize(String input) {
        if (input == null) {
            return new String[0];
        }
        return input.split("\\s");
    }

    /**
     * Retrieve a resource string from the configuration bundle.
     * @param name name of the resource to retrieve.
     * @return resource string or null if not found.
     */
    private String getResourceString(String name) {
        String str;
        try {
            str = Objects.nonNull(resources) ? resources.getString(name) : null;
        } catch (MissingResourceException ex) {
            str = null;
        }
        return str;
    }

    /**
     * Retrieve a list of services.
     * @return array of services.
     */
    public String[] getServices() {
        return tokenize(getResourceString("services"));
    }

    /**
     * Retrieve a list of modes for a given service.
     * @param service target service.
     * @return array of modes.
     */
    public String[] getModes(String service) {
        switch (service) {
            case "nict":
                return tokenize(getResourceString("nictMode"));
            case "minna_personal":
                return tokenize(getResourceString("minna_personalMode"));
            default:
                return tokenize(getResourceString(service + "Mode"));
        }
    }

    /**
     * Retrieve the localized name of a service or mode.
     * @param v service or mode identifier.
     * @return name of the service or mode.
     */
    public String getName(String v) {
        return BundleMessageUtil.getString(v + "Name");
    }

    /**
     * Retrieve the base URL for a given service.
     * @param service target service.
     * @return URL of the service.
     */
    public String getURL(String service) {
        switch (service) {
            case "nict":
                return getResourceString("nictUrl");
            case "minna_personal":
                return getResourceString("minna_personalUrl");
            default:
                return getResourceString(service + "Url");
        }
    }

    /**
     * Retrieve the base URL path for API requests.
     * @return URL path prefix.
     */
    public String getURLPath() {
        return getResourceString("apiPathPrefix");
    }
}
