package tokyo.northside.omegat.textra

import org.testng.annotations.Test;

import static org.testng.Assert.*;


class TextraOptionsTest {

    @Test
    void testIsCombinationValid() {
        assertTrue(new TextraOptions().setMode("generalN").setLang("ja", "en").isCombinationValid())
        assertFalse(new TextraOptions().setMode("generalN").setLang("fr", "en").isCombinationValid())
        assertTrue(new TextraOptions().setMode("patentN").setLang("en", "ja").isCombinationValid())
    }

    @Test
    void testUsername() {
        TextraOptions options = new TextraOptions().setUsername("hoge")
        assertEquals(options.getUsername(), "hoge")
    }

    @Test
    void testApikey() {
        TextraOptions options = new TextraOptions().setApikey("hoge")
        assertEquals(options.getApikey(), "hoge")
    }

    @Test
    void testSecret() {
        TextraOptions options = new TextraOptions().setSecret("hoge")
        assertEquals(options.getSecret(), "hoge")
    }

    @Test
    void testMode() {
        TextraOptions options = new TextraOptions().setMode(TextraOptions.Mode.generalN)
        assertEquals(options.getMode(), TextraOptions.Mode.generalN)
    }

    @Test
    void testGetModeName() {
        TextraOptions options = new TextraOptions().setMode(TextraOptions.Mode.generalN)
        assertEquals(options.getModeName(), "generalN")
    }

    @Test
    void testSetMode() {
        TextraOptions options = new TextraOptions().setMode("generalN")
        assertEquals(options.getModeName(), "generalN")
    }

    @Test
    void testIsMode() {
        TextraOptions options = new TextraOptions().setMode(TextraOptions.Mode.generalN)
        assertTrue(options.isMode("generalN"))
        assertFalse(options.isMode(null))
        assertFalse(options.isMode("patentN"))
    }
}
