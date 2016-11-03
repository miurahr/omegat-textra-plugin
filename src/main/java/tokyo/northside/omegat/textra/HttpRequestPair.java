package tokyo.northside.omegat.textra;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;


public class HttpRequestPair {

        private HttpPost httpPost;
        private HttpClient httpClient;

        public HttpRequestPair(final HttpClient httpClient, final HttpPost httpPost) {
            this.httpClient = httpClient;
            this.httpPost = httpPost;
        }

        public HttpPost getHttpPost() {
            return httpPost;
        }

        public HttpClient getHttpClient() {
            return httpClient;
        }
}
