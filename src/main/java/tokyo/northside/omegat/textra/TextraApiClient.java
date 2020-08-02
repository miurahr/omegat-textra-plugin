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
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.omegat.util.CredentialsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static tokyo.northside.omegat.textra.OmegatTextraMachineTranslation.OPTION_TEXTRA_APIKEY;
import static tokyo.northside.omegat.textra.OmegatTextraMachineTranslation.OPTION_TEXTRA_SECRET;
import static tokyo.northside.omegat.textra.OmegatTextraMachineTranslation.OPTION_TEXTRA_USERNAME;

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
    private static final String BASE_URL = "https://mt-auto-minhon-mlt.ucri.jgn-x.jp/";
    private static final String MT_LIST_URL = "https://mt-auto-minhon-mlt.ucri.jgn-x.jp/content/mt/";
    private static final String API_INFO_URL = "https://mt-auto-minhon-mlt.ucri.jgn-x.jp/app/apiInfoController.php";

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
            String rsp = IOUtils.toString(bis, StandardCharsets.UTF_8);
            LOGGER.debug("response body: " + rsp);
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
        String apiEngine = options.getModeName().replace("_", "-");
        String apiUrl = API_URL + apiEngine + "_" + options.getSourceLang()
                + "_" + options.getTargetLang() + "/";
        LOGGER.debug("Access URL:" + apiUrl);
        return apiUrl;
    }

    public boolean TextraLogin(final String username, final String password) {
        try {
            LOGGER.debug("Access to " + BASE_URL);
            Connection.Response topPageResponse = Jsoup.connect(BASE_URL)
                    .referrer(BASE_URL)
                    .userAgent("Mozilla/5.0")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .timeout(10 * 1000)
                    .followRedirects(true)
                    .execute();
            Map<String, String> mapTopPageCookies = topPageResponse.cookies();
            Document topPageDocument = topPageResponse.parse();
            Elements tokenElement = topPageDocument.select("input[id='token_common']");
            String tokenCommon = tokenElement.attr("value");
            LOGGER.debug("Got token_common " + tokenCommon);
            Map<String, String> mapParams = new HashMap<>();
            mapParams.put("q_name", username);
            mapParams.put("q_password", password);
            mapParams.put("token_common", tokenCommon);
            LOGGER.debug("Login on " + BASE_URL);
            Connection.Response loginPageResponse = Jsoup.connect(BASE_URL)
                    .referrer(BASE_URL)
                    .userAgent("Mozilla/5.0")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .timeout(10 * 1000)
                    .data(mapParams)
                    .cookies(mapTopPageCookies)
                    .followRedirects(true)
                    .execute();
            Map<String, String> mapLoginPageCookies = loginPageResponse.cookies();
            Map<String, String> mapParamsCommon = new HashMap<>();
            mapParamsCommon.put("token_common", tokenCommon);
            LOGGER.debug("Go to " + MT_LIST_URL);
            Connection.Response mtListResponse = Jsoup.connect(MT_LIST_URL)
                    .referrer(BASE_URL)
                    .userAgent("Moziila/5.0")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .timeout(10 * 1000)
                    .data(mapParamsCommon)
                    .cookies(mapLoginPageCookies)
                    .followRedirects(true)
                    .execute();
            //
            Map<String, String> mapApiParams = new HashMap<>();
            mapApiParams.put("q_mode", "api_mt");
            mapApiParams.put("q_mt_id", "generalN_ja_en");
            //
            LOGGER.debug("Try to get Key and Secret.");
            String apiJson = Jsoup.connect(API_INFO_URL)
                    .referrer(MT_LIST_URL + "?q_lang_s=--&q_lang_t=--")
                    .userAgent("Mozilla/5.0")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .header("Accept", "application/json, text/javascript, */*; q=0.01")
                    .header("X-Requested-With", "XMLHttpRequest")
                    .timeout(10 * 1000)
                    .data(mapApiParams)
                    .cookies(mapLoginPageCookies)
                    .ignoreContentType(true)
                    .execute().body();
            //
            JSONObject jobj = new JSONObject(apiJson);
            LOGGER.debug("Got JSON data.");
            int result = jobj.getInt("result");
            if (result == 1) {
                String title = jobj.getString("title");
                String body = jobj.getString("data");
                String htmlData = "<html><head><title>"+ title + "</title></head>"
                        + "<body>" + body + "</body></html>";
                Document doc = Jsoup.parse(htmlData);
                Elements keyElement = doc.select("input[id='g_api_key']");
                Elements secretElement = doc.select("input[id='g_api_secret']");
                String key = keyElement.attr("value");
                String secret = keyElement.attr("value");
                LOGGER.info("Succeeded to get API key and secret.");
                //
                setCredential(OPTION_TEXTRA_USERNAME, username, false);
                setCredential(OPTION_TEXTRA_APIKEY, key, false);
                setCredential(OPTION_TEXTRA_SECRET, secret, false);
                LOGGER.info("Stored API key: " + key + "secret: " + secret);
            } else {
                LOGGER.info("GOt error when retrieving API key and secret.");
                return false;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected void setCredential(String id, String value, boolean temporary) {
        System.setProperty(id, value);
        CredentialsManager.getInstance().store(id, temporary ? "" : value);
    }
}
