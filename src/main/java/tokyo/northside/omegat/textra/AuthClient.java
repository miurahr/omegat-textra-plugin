package tokyo.northside.omegat.textra;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static tokyo.northside.omegat.textra.TextraApiClient.CONNECTION_TIMEOUT;
import static tokyo.northside.omegat.textra.TextraApiClient.SO_TIMEOUT;

public class AuthClient {
    private final String authUrl;
    private final String apiKey;
    private final String apiSecret;

    private String accessToken;
    private int expireIn;
    private Date expire;
    private String tokenType;

    public AuthClient(String authUrl, String apiKey, String apiSecret) {
        this.authUrl = authUrl;  // baseUrl + "/oauth2/token.php";
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public boolean isValid() {
        Calendar calendar = Calendar.getInstance();
        return calendar.before(expire);
    }

    public boolean requestAuth() {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        HttpClient httpClient = HttpClientBuilder.create()
                .setRetryHandler(new DefaultHttpRequestRetryHandler(3, true))
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(CONNECTION_TIMEOUT)
                .setSocketTimeout(SO_TIMEOUT)
                .setRedirectsEnabled(true)
                .build();
        HttpPost httpPost = new HttpPost(authUrl);
        List<BasicNameValuePair> postParameters = new ArrayList<>(3);
        postParameters.add(new BasicNameValuePair("grant_type", "client_credentials"));
        postParameters.add(new BasicNameValuePair("client_id", apiKey));
        postParameters.add(new BasicNameValuePair("client_secret", apiSecret));
        httpPost.setConfig(requestConfig);
        HttpResponse httpResponse;
        InputStream respBodyStream;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));
            httpResponse = httpClient.execute(httpPost);
            respBodyStream = httpResponse.getEntity().getContent();
        } catch (IOException e) {
            Log.log("Failed to connect to Textra server.");
            return false;
        }
        int respStatus = httpResponse.getStatusLine().getStatusCode();
        if (respStatus != 200) {
            Log.log("Failed to connect to Textra server.");
            return false;
        }
        String rsp;
        try (BufferedInputStream bis = new BufferedInputStream(respBodyStream)) {
            rsp = IOUtils.toString(bis, StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode response = mapper.readTree(rsp);
            accessToken = response.get("access_token").asText();
            expireIn = response.get("expires_in").asInt();
            tokenType = response.get("token_type").asText();
        } catch (IOException e) {
            Log.log("Failed to get authorization from Textra server.");
            return false;
        }
        calendar.add(Calendar.SECOND, expireIn);
        expire = calendar.getTime();
        return true;
    }
}
