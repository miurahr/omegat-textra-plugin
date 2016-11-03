package tokyo.northside.omegat.textra

import org.testng.annotations.Test;

import static org.testng.Assert.*;


class TextraOptionsTest {

    @Test
    void testIsCombinationValid() {
        assertTrue(new TextraOptions().setMode("GENERIC").setLang("ja", "en").isCombinationValid())
        assertFalse(new TextraOptions().setMode("GENERIC").setLang("fr", "en").isCombinationValid())
        assertTrue(new TextraOptions().setMode("PATENT").setLang("en", "zh-CN").isCombinationValid())
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
        TextraOptions options = new TextraOptions().setMode(TextraOptions.Mode.GENERIC)
        assertEquals(options.getMode(), TextraOptions.Mode.GENERIC)
    }

    @Test
    void testGetModeName() {
        TextraOptions options = new TextraOptions().setMode(TextraOptions.Mode.GENERIC)
        assertEquals(options.getModeName(), "GENERIC")
    }

    @Test
    void testSetMode() {
        TextraOptions options = new TextraOptions().setMode("GENERIC")
        assertEquals(options.getModeName(), "GENERIC")
    }

    @Test
    void testIsMode() {
        TextraOptions options = new TextraOptions().setMode(TextraOptions.Mode.GENERIC)
        assertTrue(options.isMode("GENERIC"))
        assertFalse(options.isMode(null))
        assertFalse(options.isMode("PATENT"))
    }
}
