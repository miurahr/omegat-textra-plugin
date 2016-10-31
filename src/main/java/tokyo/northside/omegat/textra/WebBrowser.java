package tokyo.northside.omegat.textra;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Launch default browser and go to url.
 */
public final class WebBrowser {
    /**
     * Start default web browser with URL.
     * @param url open page of url.
     * @throws IOException when fails to start browser.
     * @throws URISyntaxException when URL is invalid.
     */
    public static void launch(final String url) throws IOException, URISyntaxException {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(new URI(url));
        } else {
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("xdg-open " + url);
        }
    }

    /**
     * Avoid instantiation of utility class.
     */
    private WebBrowser() {
    }
}
