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
        switch (service) {
            case "nict":
                return tokenize(getResourceString("nictMode"));
            case "minna_personal":
                return tokenize(getResourceString("minna_personalMode"));
            default:
                return tokenize(getResourceString(service + "Mode"));
        }
    }

    public String getName(String v) {
        return BundleMessageUtil.getString(v + "Name");
    }

    public String getURL(String service) {
        switch(service) {
            case "nict":
                return getResourceString("nictUrl");
            case "minna_personal":
                return getResourceString("minna_personalUrl");
            default:
                return getResourceString(service + "Url");
        }
    }

    public String getURLPath() {
        return getResourceString("apiPathPrefix");
    }
}
