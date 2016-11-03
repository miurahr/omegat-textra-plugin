package tokyo.northside.omegat.textra;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Launch default browser and go to url.
 * @author Hiroshi Miura
 */
public final class WebBrowser {
    /**
     * Start default web browser with URL.
     * @param url open page of url.
     */
    public static void launch(final String url) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                desktop.browse(new URI(url));
            } else {
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("xdg-open " + url);
            }
        } catch (IOException | URISyntaxException ex) {
            // ignore me.
        }
    }

    /**
     * Avoid instantiation of utility class.
     */
    private WebBrowser() {
    }
}
