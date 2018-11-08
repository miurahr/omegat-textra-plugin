package tokyo.northside.omegat.textra

import org.testng.annotations.Test;

import static org.testng.Assert.*;


class TextraOptionsTest {

    @Test
    void testIsCombinationValid() {
        assertTrue(new TextraOptions().setMode("genericN").setLang("ja", "en").isCombinationValid())
        assertFalse(new TextraOptions().setMode("genericN").setLang("fr", "en").isCombinationValid())
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
        TextraOptions options = new TextraOptions().setMode(TextraOptions.Mode.genericN)
        assertEquals(options.getMode(), TextraOptions.Mode.genericN)
    }

    @Test
    void testGetModeName() {
        TextraOptions options = new TextraOptions().setMode(TextraOptions.Mode.genericN)
        assertEquals(options.getModeName(), "genericN")
    }

    @Test
    void testSetMode() {
        TextraOptions options = new TextraOptions().setMode("genericN")
        assertEquals(options.getModeName(), "genericN")
    }

    @Test
    void testIsMode() {
        TextraOptions options = new TextraOptions().setMode(TextraOptions.Mode.genericN)
        assertTrue(options.isMode("genericN"))
        assertFalse(options.isMode(null))
        assertFalse(options.isMode("patentN"))
    }
}
