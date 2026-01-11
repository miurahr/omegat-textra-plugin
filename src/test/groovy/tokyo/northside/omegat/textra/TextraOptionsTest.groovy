package tokyo.northside.omegat.textra

import org.junit.Test

import static org.junit.Assert.*

class TextraOptionsTest {

    @Test
    void testUsername() {
        TextraOptions options = TextraOptions.builder().setUsername("hoge").build()
        assertEquals("hoge", options.getUsername())
    }

    @Test
    void testApikey() {
        TextraOptions options = TextraOptions.builder().setApikey("hoge").build()
        assertEquals("hoge", options.getApikey())
    }

    @Test
    void testSecret() {
        TextraOptions options = TextraOptions.builder().setSecret("hoge").build()
        assertEquals("hoge", options.getSecret())
    }

    @Test
    void testSetModePatentN() {
        TextraOptions options = TextraOptions.builder().setMode("patentNT").build()
        assertEquals("patentNT", options.getModeName())
    }

    @Test
    void testSetModeGeneralNT() {
        TextraOptions options = TextraOptions.builder().setMode("generalNT").build()
        assertEquals("generalNT", options.getModeName())
    }

    @Test
    void testIsMode() {
        TextraOptions options = TextraOptions.builder().setMode("generalNT").build()
        assertTrue(options.isMode("generalNT"))
        assertFalse(options.isMode(null))
        assertFalse(options.isMode("patentNT"))
    }
}
