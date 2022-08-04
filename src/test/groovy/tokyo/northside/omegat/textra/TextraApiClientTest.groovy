package tokyo.northside.omegat.textra

import org.junit.Test

import static org.junit.Assert.*

class TextraApiClientTest {

    @Test
    void testResponseParser() {
        URL url = Thread.currentThread().getContextClassLoader().getResource("response.json")
        assertEquals('原因だからです。', TextraApiClient.parseResponse(new FileInputStream(new File(url.getPath()))))
    }
}
