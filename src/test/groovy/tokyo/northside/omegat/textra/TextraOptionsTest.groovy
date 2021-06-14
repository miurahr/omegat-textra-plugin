package tokyo.northside.omegat.textra

import org.junit.Test

import org.omegat.util.Language

import static org.junit.Assert.*

class TextraOptionsTest {

    @Test
    void testIsCombinationValid() {
        assertTrue(new TextraOptions().setMode("generalNT").setLang(new Language("ja-JP"), new Language("en-US")).isCombinationValid())
        assertFalse(new TextraOptions().setMode("generalNT").setLang(new Language("fr"), new Language("es")).isCombinationValid())
        assertFalse(new TextraOptions().setMode("generalNT").setLang(new Language("zh-CN"), new Language("zh-TW")).isCombinationValid())
        assertTrue(new TextraOptions().setMode("generalNT").setLang(new Language("ja"), new Language("zh-CN")).isCombinationValid())
        assertTrue(new TextraOptions().setMode("generalNT").setLang("JA", "EN").isCombinationValid())
        assertTrue(new TextraOptions().setMode("patentNT").setLang("en", "ja").isCombinationValid())
        assertTrue(new TextraOptions().setMode("voicetraNT").setLang("ja", "en").isCombinationValid())
        assertTrue("test KI personal", new TextraOptions().setProvider(TextraOptions.Provider.minna_personal).setMode("generalNT").setLang(new Language("en"), new Language("ja")).isCombinationValid())
        TextraOptions options = new TextraOptions()
        options.setProvider(TextraOptions.Provider.minna_personal)
        options.setMode("generalNT")
        options.setLang("en", "ja")
        assertTrue(options.isCombinationValid())
        options = new TextraOptions().setMode("custom")
        options.setCustomId("c-999999")
        assertTrue(options.isCombinationValid())
        options.setCustomId("a-999999")
        assertTrue(options.isCombinationValid())
    }

    @Test
    void testUsername() {
        TextraOptions options = new TextraOptions().setUsername("hoge")
        assertEquals("hoge", options.getUsername())
    }

    @Test
    void testApikey() {
        TextraOptions options = new TextraOptions().setApikey("hoge")
        assertEquals("hoge", options.getApikey())
    }

    @Test
    void testSecret() {
        TextraOptions options = new TextraOptions().setSecret("hoge")
        assertEquals("hoge", options.getSecret())
    }

    @Test
    void testSetModePatentN() {
        TextraOptions options = new TextraOptions().setMode("patentNT")
        assertEquals("patentNT", options.getModeName())
    }

    @Test
    void testSetModeGeneralNT() {
        TextraOptions options = new TextraOptions().setMode("generalNT")
        assertEquals("generalNT", options.getModeName())
    }

    @Test
    void testIsMode() {
        TextraOptions options = new TextraOptions().setMode(TextraOptions.Mode.generalNT)
        assertTrue(options.isMode("generalNT"))
        assertFalse(options.isMode(null))
        assertFalse(options.isMode("patentNT"))
    }
}
