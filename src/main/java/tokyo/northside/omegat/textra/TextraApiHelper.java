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
 * TexTra access API helper class.
 * <p>
 *     query() returns HttpClient and HttpPost object pair.
 *     getResponse(pair) returns result.
 * </p>
 * @Author Hiroshi Miura
 */
class TextraApiHelper {
    private static final int CONNECTION_TIMEOUT = 2 * 60 * 1000;
    private static final int SO_TIMEOUT = 10 * 60 * 1000;
    private static final Logger logger = LoggerFactory.getLogger(TextraApiHelper.class);
    private static final String API_URL = "https://mt-auto-minhon-mlt.ucri.jgn-x.jp/api/mt/";

    private static String getAccessUrl(final TextraOptions options) {
        String apiEngine = options.getModeName().replace("_", "-").toLowerCase();
        String apiUrl = API_URL + apiEngine + "_" + options.getSourceLang() +
                "_" + options.getTargetLang() + "/";
        logger.debug("Access URL:" + apiUrl);
        return apiUrl;
    }

    static HttpRequestPair query(final TextraOptions options, final String text) {
        return query(getAccessUrl(options), options.getUsername(), options.getApikey(), options.getSecret(), text);
    }

    static HttpRequestPair query(final String url, final String apiUsername, final String apiKey, final String apiSecret,
                  final String text) {
        HttpPost httpPost = new HttpPost(url);
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(apiKey, apiSecret);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(CONNECTION_TIMEOUT)
                .setSocketTimeout(SO_TIMEOUT)
                .build();
        httpPost.setConfig(requestConfig);
        HttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler(new DefaultHttpRequestRetryHandler(3, true))
                .build();

        List<BasicNameValuePair> postParameters = new ArrayList<>(5);
        postParameters.add(new BasicNameValuePair("key", apiKey));
        postParameters.add(new BasicNameValuePair("name", apiUsername));
        postParameters.add(new BasicNameValuePair("type", "json"));
        postParameters.add(new BasicNameValuePair("text", text));
        try {
            new UrlEncodedFormEntity(postParameters, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            logger.info("Encoding error.");
            return null;
        }

        try {
            consumer.sign(httpPost);
        } catch (OAuthMessageSignerException | OAuthExpectationFailedException
                | OAuthCommunicationException ex) {
            logger.info("OAuth error: " + ex.getMessage());
            return null;
        }
        return new HttpRequestPair(httpClient, httpPost);
    }

    static String getResponse(final HttpRequestPair pair) {
        int respStatus;
        InputStream respBodyStream;
        try {
            HttpResponse httpResponse = pair.getHttpClient().execute(pair.getHttpPost());
            respBodyStream = httpResponse.getEntity().getContent();
            respStatus = httpResponse.getStatusLine().getStatusCode();
        } catch (IOException ex) {
            logger.info("http access error: " + ex.getMessage());
            return null;
        }

        if (respStatus != 200) {
            System.err.println(String.format("Get response: %d", respStatus));
            return null;
        }

        String result;
        try (BufferedInputStream bis = new BufferedInputStream(respBodyStream)) {
            System.out.println("Http response status: " + respStatus);
            String rsp = IOUtils.toString(bis);
            JSONObject jobj = new JSONObject(rsp);
            JSONObject resultset = jobj.getJSONObject("resultset");
            result = resultset.getJSONObject("result").getString("text");
        } catch (IOException ex) {
            logger.info("Invalid http response: " + ex.getMessage());
            return null;
        }
        return result;
    }
}
