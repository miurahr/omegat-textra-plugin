package tokyo.northside.omegat.textra;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;


/**
 * Container for HttpClient and HttpPost pair.
 * @author Hiroshi Miura
 */
class HttpRequestPair {

        private HttpPost httpPost;
        private HttpClient httpClient;

        HttpRequestPair(final HttpClient httpClient, final HttpPost httpPost) {
            this.httpClient = httpClient;
            this.httpPost = httpPost;
        }

        HttpPost getHttpPost() {
            return httpPost;
        }

        HttpClient getHttpClient() {
            return httpClient;
        }
}
