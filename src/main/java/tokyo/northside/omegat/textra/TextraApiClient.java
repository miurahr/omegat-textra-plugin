package tokyo.northside.omegat.textra;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


/**
 * TexTra access API client.
 *
 * @author Hiroshi Miura
 */
public class TextraApiClient {
    private static final int CONNECTION_TIMEOUT = 2 * 60 * 1000;
    private static final int SO_TIMEOUT = 10 * 60 * 1000;
    private static final Logger LOGGER = LoggerFactory.getLogger(TextraApiClient.class);
    private static final String API_URL = "https://mt-auto-minhon-mlt.ucri.jgn-x.jp/api/mt/";

    private HttpClient httpClient;
    private HttpPost httpPost;
    private RequestConfig requestConfig;

    /**
     * Try authenticate OAuth with given key/secret.
     * @param options connectivity options.
     * @param text source text for translation.
     */
    public void authenticate(final TextraOptions options, final String text) {
        authenticate(getAccessUrl(options), options.getUsername(), options.getApikey(),
                options.getSecret(), text);
    }

    private void authenticate(final String url, final String apiUsername, final String apiKey,
                      final String apiSecret, final String text) {
        httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler(new DefaultHttpRequestRetryHandler(3, true))
                .build();
        requestConfig = RequestConfig.custom()
                .setConnectTimeout(CONNECTION_TIMEOUT)
                .setSocketTimeout(SO_TIMEOUT)
                .build();
        httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(apiKey, apiSecret);

        List<BasicNameValuePair> postParameters = new ArrayList<>(5);
        postParameters.add(new BasicNameValuePair("key", apiKey));
        postParameters.add(new BasicNameValuePair("name", apiUsername));
        postParameters.add(new BasicNameValuePair("type", "json"));
        postParameters.add(new BasicNameValuePair("text", text));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            LOGGER.info("Encoding error.");
        }

        try {
            consumer.sign(httpPost);
        } catch (OAuthMessageSignerException | OAuthExpectationFailedException
                | OAuthCommunicationException ex) {
            LOGGER.info("OAuth error: " + ex.getMessage());
        }
    }

    /**
     * Execute translation on Web API.
     * @return translated text when success, otherwise return null.
     */
    public String executeTranslation() {
        int respStatus;
        InputStream respBodyStream;
        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            respBodyStream = httpResponse.getEntity().getContent();
            respStatus = httpResponse.getStatusLine().getStatusCode();
        } catch (IOException ex) {
            LOGGER.info("http access error: " + ex.getMessage());
            return null;
        }

        if (respStatus != 200) {
            LOGGER.info(String.format("Get response: %d", respStatus));
            return null;
        }

        String result;
        try (BufferedInputStream bis = new BufferedInputStream(respBodyStream)) {
            LOGGER.debug("Http response status: " + respStatus);
            String rsp = IOUtils.toString(bis);
            LOGGER.info("response body: " + rsp);
            JSONObject jobj = new JSONObject(rsp);
            JSONObject resultset = jobj.getJSONObject("resultset");
            result = resultset.getJSONObject("result").getString("text");
        } catch (IOException ex) {
            LOGGER.info("Invalid http response: " + ex.getMessage());
            return null;
        }
        return result;
    }

    private static String getAccessUrl(final TextraOptions options) {
        String apiEngine = options.getModeName().replace("_", "-").toLowerCase();
        String apiUrl = API_URL + apiEngine + "_" + options.getSourceLang()
                + "_" + options.getTargetLang() + "/";
        LOGGER.debug("Access URL:" + apiUrl);
        return apiUrl;
    }
}
