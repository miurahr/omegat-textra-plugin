package tokyo.northside.omegat.textra

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo
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
import static com.github.tomakehurst.wiremock.client.WireMock.post
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo


@WireMockTest
class TextraApiClientTest {

    private static File tmpDir
    private static final ObjectMapper mapper = new ObjectMapper()

    private static final String USERNAME = 'username'
    private static final String API_KEY = '329033940ADEF'
    private static final String SECRET = '9B3ABC1234ABDD'
    private static final String SOURCE_TEXT = 'source text'
    private static final String TARGET_TEXT = 'Hello World!'
    private static final String TOKEN_PATH = '/oauth2/token.php'
    private static final String SOURCE_LANGUAGE = 'en'
    private static final String TARGET_LANGUAGE = 'ja'

    @BeforeAll
    static final void setUpCore() throws Exception {
        tmpDir = Files.createTempDirectory("omegat").toFile()
    }

    @AfterAll
    static final void tearDownCore() throws Exception {
        FileUtils.deleteDirectory(tmpDir)
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
        File prefsFile = new File(tmpDir, Preferences.FILE_PREFERENCES)
        Preferences.IPreferences prefs = new PreferencesImpl(new PreferencesXML(null, prefsFile))
        prefs.setPreference(OmegatTextraMachineTranslation.OPTION_ALLOW_TEXTRA_TRANSLATE, true)
        init(prefsFile.getAbsolutePath())

        String engine = 'generalNT'
        String apiParam = String.format("%s_%s_%s", engine, SOURCE_LANGUAGE, TARGET_LANGUAGE)

        // Define expectations
        WireMock wireMock = wireMockRuntimeInfo.getWireMock()
        wireMock.register(post(urlPathEqualTo(TOKEN_PATH))
                .withHeader('Content-Type', containing('application/x-www-form-urlencoded'))
                .withRequestBody(containing("client_id=" + API_KEY))
                .withRequestBody(containing("client_secret=" + SECRET))
                .withRequestBody(containing("grant_type=client_credentials"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody('{"access_token":"MTQ0NjJkZmQ5OTM2NDE1ZTZjNGZmZjI3", "token_type":"Bearer", "expires_in":3600}')
                )
        )
        JsonNode expected = mapper.readTree('{ "resultset": { "code": 0, "message": "Succeeded", "result": { "blank": 0, "text": "' + TARGET_TEXT + '"} } }')
        WireMock.stubFor(post(urlPathEqualTo("/api/mt/" + apiParam + "/"))
                .withRequestBody(containing("key=" + API_KEY))
                .withRequestBody(containing("name=" + USERNAME))
                .withRequestBody(containing("type=json"))
                .withRequestBody(containing("text=" + URLEncoder.encode(SOURCE_TEXT, StandardCharsets.UTF_8)))
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
        TextraOptions options = new TextraOptions(url)
        options.setUsername(USERNAME)
        options.setApikey(API_KEY)
        options.setLang("EN", "JA")
        options.setSecret(SECRET)

        // examine
        String result = client.executeTranslation(options, SOURCE_TEXT)
        assertEquals(TARGET_TEXT, result)
    }

    @Test
    void testAuthErrorResponse(WireMockRuntimeInfo wireMockRuntimeInfo) {
        File prefsFile = new File(tmpDir, Preferences.FILE_PREFERENCES)
        Preferences.IPreferences prefs = new PreferencesImpl(new PreferencesXML(null, prefsFile))
        prefs.setPreference(OmegatTextraMachineTranslation.OPTION_ALLOW_TEXTRA_TRANSLATE, true)
        init(prefsFile.getAbsolutePath())

        // Define expectations
        JsonNode json = mapper.readTree('{ "error": "incorrect_request", ' +
                '"error_description": "The client_id and/or client_secret passed are incorrect.", ' +
                '"error_uri": "/oauth-access-token-request-errors/#incorrect-request" }')
        WireMock wireMock = wireMockRuntimeInfo.getWireMock()
        wireMock.register(post(urlPathEqualTo(TOKEN_PATH))
                .withHeader('Content-Type', containing('application/x-www-form-urlencoded'))
                .withRequestBody(containing("client_id=" + API_KEY))
                .withRequestBody(containing("client_secret=" + SECRET))
                .withRequestBody(containing("grant_type=client_credentials"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withJsonBody(json)
                )
        )

        // Create test instance
        TextraApiClient client = new TextraApiClient()
        int port = wireMockRuntimeInfo.getHttpPort()
        String url = String.format("http://localhost:%d", port)
        TextraOptions options = new TextraOptions(url)
        options.setUsername(USERNAME)
        options.setApikey(API_KEY)
        options.setLang("EN", "JA")
        options.setSecret(SECRET)

        Exception exc = Assertions.assertThrows(Exception.class, () -> {
            client.executeTranslation(options, SOURCE_TEXT)
        })
        assertEquals("Authentication error!", exc.getMessage())
    }

    @Test
    void testApiKeyErrorResponse(WireMockRuntimeInfo wireMockRuntimeInfo) {
        File prefsFile = new File(tmpDir, Preferences.FILE_PREFERENCES)
        Preferences.IPreferences prefs = new PreferencesImpl(new PreferencesXML(null, prefsFile))
        prefs.setPreference(OmegatTextraMachineTranslation.OPTION_ALLOW_TEXTRA_TRANSLATE, true)
        init(prefsFile.getAbsolutePath())

        String engine = 'generalNT'
        String apiParam = String.format("%s_%s_%s", engine, SOURCE_LANGUAGE, TARGET_LANGUAGE)

        // Define expectations
        WireMock wireMock = wireMockRuntimeInfo.getWireMock()
        wireMock.register(post(urlPathEqualTo(TOKEN_PATH))
                .withHeader('Content-Type', containing('application/x-www-form-urlencoded'))
                .withRequestBody(containing("client_id=" + API_KEY))
                .withRequestBody(containing("client_secret=" + SECRET))
                .withRequestBody(containing("grant_type=client_credentials"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody('{"access_token":"MTQ0NjJkZmQ5OTM2NDE1ZTZjNGZmZjI3", "token_type":"Bearer", "expires_in":3600}')
                )
        )
        JsonNode expected = mapper.readTree('{ "resultset": { "code": 500, "message": "API Key error" } }')
        WireMock.stubFor(post(urlPathEqualTo("/api/mt/" + apiParam + "/"))
                .withRequestBody(containing("key=" + API_KEY))
                .withRequestBody(containing("name=" + USERNAME))
                .withRequestBody(containing("type=json"))
                .withRequestBody(containing("text=" + URLEncoder.encode(SOURCE_TEXT, StandardCharsets.UTF_8)))
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
        TextraOptions options = new TextraOptions(url)
        options.setUsername(USERNAME)
        options.setApikey(API_KEY)
        options.setLang("EN", "JA")
        options.setSecret(SECRET)

        Exception exc = Assertions.assertThrows(Exception.class, () -> {
            client.executeTranslation(options, SOURCE_TEXT)
        })
        assertEquals("500 " + ErrorMessages.messages.get(500), exc.getMessage())
    }

    /**
     * Initialize preferences for test.
     * @param configDir to create omegat.prefs.
     */
    static synchronized void init(String configDir) {
        RuntimePreferences.setConfigDir(configDir)
        Preferences.init()
        Preferences.initFilters()
        Preferences.initSegmentation()
    }
}
