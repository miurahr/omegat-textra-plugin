package tokyo.northside.omegat.textra;

import org.omegat.util.HttpConnectionUtils;

import java.io.IOException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * TexTra access API client.
 *
 * @author Hiroshi Miura
 */
public class TextraApiClient {
    public static final String BASE_URL = "https://mt-auto-minhon-mlt.ucri.jgn-x.jp";
    public static final String KI_BASE_URL = "https://minna-mt.k-intl.jp";
    private static final String API_URL_PREFIX = "/api/mt/";

    private final ObjectMapper mapper;

    private String accessToken = null;
    private LocalTime expire = null;

    public TextraApiClient() {
        mapper = new ObjectMapper();
    }

    public static boolean checkAuth(String authUrl, String apiKey, String apiSecret) {
        try {
            return getToken(authUrl, apiKey, apiSecret).contains("access_token");
        } catch (IOException ignored) {
        }
        return false;
    }

    private static String getToken(String authUrl, String apiKey, String apiSecret) throws IOException {
        Map<String, String> postParameters = new LinkedHashMap<>(3);
        postParameters.put("grant_type", "client_credentials");
        postParameters.put("client_id", apiKey);
        postParameters.put("client_secret", apiSecret);
        return HttpConnectionUtils.post(authUrl, postParameters, null);
    }

    /**
     * Execute translation on Web API.
     * @param options connectivity options.
     * @param text source text for translation.
     * @return translated text when success, otherwise return null.
     */
    public String executeTranslation(final TextraOptions options, final String text) throws Exception {
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

        if (expire == null || expire.isAfter(LocalTime.now()) || options.isChanged()) {
            try {
                String authUrl = options.getOAuth2Url();
                String json = getToken(authUrl, apiKey, apiSecret);
                JsonNode jsonNode = mapper.readTree(json);
                JsonNode tokenNode = jsonNode.get("access_token");
                if (tokenNode == null) {
                    throw new Exception("Authentication error!");
                }
                accessToken = tokenNode.asText();
                long expireIn = jsonNode.get("expires_in").asLong();
                LocalTime localTime = LocalTime.now();
                expire = localTime.plus(expireIn, ChronoUnit.SECONDS).minus(1, ChronoUnit.SECONDS);
            } catch (Exception e) {
                throw new Exception("Authentication error!");
            }
        }

        String accessUrl = getAccessUrl(options);
        String api_param = getApiParam(options);

        Map<String, String> postParameters = new HashMap<>(5);
        postParameters.put("key", apiKey);
        postParameters.put("name", apiUsername);
        postParameters.put("type", "json");
        postParameters.put("text", text);
        postParameters.put("access_token", accessToken);
        postParameters.put("api_name", "mt");
        postParameters.put("api_param", api_param);

        String response = HttpConnectionUtils.post(accessUrl, postParameters, null);
        return parseResponse(response);
    }

    private String getApiParam(TextraOptions options) {
        return String.format("%s_%s_%s", getApiEngine(options), options.getSourceLang(), options.getTargetLang());
    }

    protected String parseResponse(String response) throws Exception {
        TextraResponse root = mapper.readValue(response, TextraResponse.class);
        if (root == null || root.resultset == null) {
            return null;
        }
        if (root.resultset.code != 0 || root.resultset.result == null) {
            String message;
            if (BundleMessageUtil.hasErrorMessage(root.resultset.code)) {
                message = String.format(
                        "%d %s", root.resultset.code, BundleMessageUtil.getErrorMessage(root.resultset.code));
            } else {
                message = String.format("%d %s", root.resultset.code, root.resultset.message);
            }
            throw new Exception(message);
        }
        if (root.resultset.result.blank == 1) {
            return "";
        }
        return root.resultset.result.text;
    }

    private static String getAccessUrl(final TextraOptions options) {
        String apiEngine = getApiEngine(options);
        return options.getBaseUrl() + API_URL_PREFIX + apiEngine + "_" + options.getSourceLang() + "_"
                + options.getTargetLang() + "/";
    }

    private static String getApiEngine(TextraOptions options) {
        if (options.getMode() == TextraOptions.Mode.custom) {
            return options.getCustomId();
        }
        return options.getModeName().replace("_", "-");
    }
}
