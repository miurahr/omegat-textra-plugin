package tokyo.northside.omegat.textra;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.omegat.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
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
    public static final String BASE_URL = "https://mt-auto-minhon-mlt.ucri.jgn-x.jp";
    private static final String API_URL = "https://mt-auto-minhon-mlt.ucri.jgn-x.jp/api/mt/";
    public static final String KI_BASE_URL = "https://minna-mt.k-intl.jp";
    private static final String KI_API_URL = "https://minna-mt.k-intl.jp/api/mt/";

    private TextraOptions options;
    private OAuthConsumer consumer = null;

    public TextraApiClient(final TextraOptions options) {
        this.options = options;
        String apiUsername = options.getUsername();
        String apiKey = options.getApikey();
        String apiSecret = options.getSecret();
        if (apiUsername == null || apiUsername.isEmpty()) {
            return;
        }
        if (apiKey == null || apiKey.isEmpty()) {
            return;
        }
        if (apiSecret == null || apiSecret.isEmpty()) {
            return;
        }
        consumer = new CommonsHttpOAuthConsumer(apiKey, apiSecret);
    }

    public static boolean checkAuth(String authUrl, String apiKey, String apiSecret) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(CONNECTION_TIMEOUT)
                .setSocketTimeout(SO_TIMEOUT)
                .setRedirectsEnabled(true)
                .build();
        HttpClient httpClient = HttpClientBuilder.create()
                .setRetryHandler(new DefaultHttpRequestRetryHandler(3, true))
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();
        HttpPost httpPost = new HttpPost(authUrl);
        List<BasicNameValuePair> postParameters = new ArrayList<>(3);
        postParameters.add(new BasicNameValuePair("grant_type", "client_credentials"));
        postParameters.add(new BasicNameValuePair("client_id", apiKey));
        postParameters.add(new BasicNameValuePair("client_secret", apiSecret));
        httpPost.setConfig(requestConfig);
        HttpResponse httpResponse;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));
            httpResponse = httpClient.execute(httpPost);
        } catch (IOException e) {
            Log.log("Failed to connect to Textra server.");
            return false;
        }
        int respStatus = httpResponse.getStatusLine().getStatusCode();
        if (respStatus != 200) {
            Log.log("Failed to connect to Textra server.");
            return false;
        }
        return true;
    }

    /**
     * Execute translation on Web API.
     * @param options connectivity options.
     * @param text source text for translation.
     * @return translated text when success, otherwise return null.
     */
    public String executeTranslation(final TextraOptions options, final String text) throws Exception {
        String url = getAccessUrl(options);
        String apiUsername = options.getUsername();
        String apiKey = options.getApikey();
        String apiSecret = options.getSecret();
        if (apiUsername == null || apiUsername.isEmpty()) {
            throw new Exception("TexTra Username is not found.");
        }
        if (apiKey == null || apiKey.isEmpty()) {
            throw new Exception("TexTra API key is not found.");
        }
        if (apiSecret == null || apiSecret.isEmpty()) {
            throw new Exception("TexTra API secret is not found.");
        }

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(CONNECTION_TIMEOUT)
                .setSocketTimeout(SO_TIMEOUT)
                .setRedirectsEnabled(true)
                .build();
        HttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler(new DefaultHttpRequestRetryHandler(3, true))
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        // when update options, recreate oauth consumer.
        if (!options.equals(this.options)) {
            consumer = new CommonsHttpOAuthConsumer(apiKey, apiSecret);
            this.options = options;
        }

        List<BasicNameValuePair> postParameters = new ArrayList<>(5);
        postParameters.add(new BasicNameValuePair("key", apiKey));
        postParameters.add(new BasicNameValuePair("name", apiUsername));
        postParameters.add(new BasicNameValuePair("type", "json"));
        postParameters.add(new BasicNameValuePair("text", text));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            throw new Exception("Translation source is not able to encode into UTF-8.");
        }

        try {
            consumer.sign(httpPost);
        } catch (OAuthMessageSignerException | OAuthExpectationFailedException
                | OAuthCommunicationException ex) {
            Log.log("OAuth error: " + ex.getMessage());
            throw new Exception("Authentication error!");
        }

        int respStatus;
        InputStream respBodyStream;
        HttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(httpPost);
            respBodyStream = httpResponse.getEntity().getContent();
            respStatus = httpResponse.getStatusLine().getStatusCode();
            if (respStatus == 302) {
                String newLocation = httpResponse.getHeaders("Location")[0].toString();
                Log.log("redirect(302): " + newLocation);
                httpPost.setURI(new URI(newLocation));
                httpResponse = httpClient.execute(httpPost);
                respBodyStream = httpResponse.getEntity().getContent();
                respStatus = httpResponse.getStatusLine().getStatusCode();
            }
        } catch (IOException | URISyntaxException ex) {
            Log.log("http access error: " + ex.getMessage());
            return null;
        }

        if (respStatus != 200) {
            Log.log(String.format("Get response: %d", respStatus));
            throw new Exception("");
        }

        String result;
        try (BufferedInputStream bis = new BufferedInputStream(respBodyStream)) {
            String rsp = IOUtils.toString(bis, StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jobj = mapper.readTree(rsp);
            JsonNode resultset = jobj.get("resultset");
            result = resultset.get("result").get("text").asText();
        } catch (IOException ex) {
            Log.log("Invalid http response: " + ex.getMessage());
            return null;
        }
        return result;
    }

    private static String getAccessUrl(final TextraOptions options) {
        String apiUrl;
        String apiEngine;
        if (options.getMode() == TextraOptions.Mode.custom) {
            apiEngine = options.getCustomId();
        } else {
            apiEngine = options.getModeName().replace("_", "-");
        }
        if (options.isServer(TextraOptions.Provider.nict)) {
            apiUrl = API_URL + apiEngine + "_" + options.getSourceLang()
                + "_" + options.getTargetLang() + "/";
        } else if (options.isServer(TextraOptions.Provider.minna_personal)) {
            apiUrl = KI_API_URL + apiEngine + "_" + options.getSourceLang()
                    + "_" + options.getTargetLang() + "/";
        } else {
            apiUrl = API_URL + apiEngine + "_" + options.getSourceLang()
                    + "_" + options.getTargetLang() + "/";
        }
        Log.log("Access URL:" + apiUrl);
        return apiUrl;
    }
}
