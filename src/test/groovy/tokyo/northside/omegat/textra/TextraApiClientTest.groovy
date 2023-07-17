package tokyo.northside.omegat.textra

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.matching.StringValuePattern
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import org.apache.commons.io.FileUtils

import org.junit.jupiter.api.*

import org.omegat.util.Preferences
import org.omegat.util.PreferencesImpl
import org.omegat.util.PreferencesXML
import org.omegat.util.RuntimePreferences

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

import static org.junit.Assert.*

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.containing
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo
import static com.github.tomakehurst.wiremock.client.WireMock.post
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo


@WireMockTest
class TextraApiClientTest {

    private static File tmpDir;
    private static final ObjectMapper mapper = new ObjectMapper()

    @BeforeAll
    static final void setUpCore() throws Exception {
        tmpDir = Files.createTempDirectory("omegat").toFile();
    }

    @AfterAll
    static final void tearDownCore() throws Exception {
        FileUtils.deleteDirectory(tmpDir);
    }

    @Test
    void testResponseParser() {
        URL url = Thread.currentThread().getContextClassLoader().getResource("response.json")
        String response = Files.lines(Paths.get(url.toURI())).collect(Collectors.joining())
        TextraApiClient client = new TextraApiClient()
        assertEquals('原因だからです。', client.parseResponse(response))
    }

    @Test
    void testResponse(WireMockRuntimeInfo wireMockRuntimeInfo) {
        File prefsFile = new File(tmpDir, Preferences.FILE_PREFERENCES);
        Preferences.IPreferences prefs = new PreferencesImpl(new PreferencesXML(null, prefsFile));
        prefs.setPreference(OmegatTextraMachineTranslation.OPTION_ALLOW_TEXTRA_TRANSLATE, true)
        init(prefsFile.getAbsolutePath())

        String username = 'username'
        String apiKey = '329033940ADEF'
        String secret = '9B3ABC1234ABDD'
        String sourceText = 'source text'
        String targetText = 'Hello World!'
        String tokenPath = '/oauth2/token.php'
        String engine = 'generalNT'
        String sourceLanguage = 'en'
        String targetLanguage = 'ja'
        String apiParam = String.format("%s_%s_%s", engine, sourceLanguage, targetLanguage)

        // Define expectations
        WireMock wireMock = wireMockRuntimeInfo.getWireMock()
        wireMock.register(post(urlPathEqualTo(tokenPath))
                // .withHeader('Content-Type', equalTo('application/x-www-form-urlencoded'))
                // .withRequestBody(containing("client_id=" + apiKey + "&client_secret=" + secret + "&grant_type=" + "client_credentials"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody('{"access_token":"MTQ0NjJkZmQ5OTM2NDE1ZTZjNGZmZjI3", "token_type":"Bearer", "expires_in":3600}')
                )
        )
        JsonNode expected = mapper.readTree('{ "resultset": { "result": { "text": "' + targetText + '", "code": 200, "message": ""} } }')
        WireMock.stubFor(post(urlPathEqualTo("/api/mt/" + apiParam + "/"))
                .withRequestBody(containing("key=" + apiKey))
                .withRequestBody(containing("name=" + username))
                .withRequestBody(containing("type=json"))
                .withRequestBody(containing("text=" + URLEncoder.encode(sourceText, StandardCharsets.UTF_8)))
                .withRequestBody(containing("api_name=mt"))
                .withRequestBody(containing("api_param=" + apiParam))
                .withRequestBody(containing("access_token=MTQ0NjJkZmQ5OTM2NDE1ZTZjNGZmZjI3"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withJsonBody(expected)
                )
        )

        // Create test instance
        TextraApiClient client = new TextraApiClient()
        int port = wireMockRuntimeInfo.getHttpPort()
        String url = String.format("http://localhost:%d", port)
        TextraOptions options = new TextraOptions(url);
        options.setUsername(username)
        options.setApikey(apiKey)
        options.setLang("EN", "JA")
        options.setSecret(secret)

        // examine
        String result = client.executeTranslation(options, sourceText);
        assertEquals(targetText, result)
    }

    /**
     * Initialize preferences for test.
     * @param configDir to create omegat.prefs.
     */
    static synchronized void init(String configDir) {
        RuntimePreferences.setConfigDir(configDir);
        Preferences.init();
        Preferences.initFilters();
        Preferences.initSegmentation();
    }
}
