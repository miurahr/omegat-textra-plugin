package tokyo.northside.omegat.textra;

import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;

public class TextraOptionsFactory {

    private final ResourceBundle resources;

    public TextraOptionsFactory() {
        resources = ResourceBundle.getBundle("tokyo/northside/omegat/textra/TextraApiConfiguration");
    }

    private String[] tokenize(String input) {
        return input == null ? new String[0] : input.split("\\s");
    }

    private String getResourceString(String name) {
        String str;
        try {
            str = Objects.nonNull(resources) ? resources.getString(name) : null;
        } catch (MissingResourceException ex) {
            str = null;
        }
        return str;
    }

    public String[] getServices() {
        return tokenize(getResourceString("services"));
    }

    public String[] getModes(String service) {
        return tokenize(getResourceString(service + "Mode"));
    }

    public String getName(String v) {
        return getResourceString(v + "Name");
    }

    public String getURL(String service) {
        return getResourceString(service + "Url");
    }

    public String getURLPath() {
        return getResourceString("apiPathPrefix");
    }
}
