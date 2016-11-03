package tokyo.northside.omegat.textra

import org.testng.annotations.BeforeClass
import org.testng.annotations.Test;

import static org.testng.Assert.*;


class TextraOptionsTest {
    TextraOptions options;

    @BeforeClass
    void setUp() {
        options = new TextraOptions()
    }

    @Test
    void testIsCombinationValid() {
        options.setMode("GENERIC")
        assertTrue(options.isCombinationValid("en", "ja"))
        assertFalse(options.isCombinationValid("fr", "en"))
        options.setMode("PATENT")
        assertTrue(options.isCombinationValid("en", "zh-CN"))
    }

    @Test
    void testUsername() {
        options.setUsername("hoge")
        assertEquals(options.getUsername(), "hoge")
    }

    @Test
    void testApikey() {
        options.setApikey("hoge")
        assertEquals(options.getApikey(), "hoge")
    }

    @Test
    void testSecret() {
        options.setSecret("hoge")
        assertEquals(options.getSecret(), "hoge")
    }

    @Test
    void testMode() {
        options.setMode(TextraOptions.Mode.GENERIC)
        assertEquals(options.getMode(), TextraOptions.Mode.GENERIC)
    }

    @Test
    void testGetModeName() {
        options.setMode(TextraOptions.Mode.GENERIC)
        assertEquals(options.getModeName(), "GENERIC")
    }

    @Test
    void testSetMode() {
        options.setMode(TextraOptions.Mode.GENERIC)
        assertEquals(options.getModeName(), "GENERIC")
    }

    @Test
    void testIsMode() {
        options.setMode(TextraOptions.Mode.GENERIC)
        assertTrue(options.isMode("GENERIC"))
        assertFalse(options.isMode(null))
        assertFalse(options.isMode("PATENT"))
    }
}
