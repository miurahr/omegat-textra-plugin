package tokyo.northside.omegat.textra;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Launch default browser and go to url.
 */
public class WebBrowser {
    public static void launch(String url) throws IOException, URISyntaxException {
        if(Desktop.isDesktopSupported()){
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(new URI(url));
        }else{
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("xdg-open " + url);
        }
    }
}
