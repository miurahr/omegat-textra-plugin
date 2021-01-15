package tokyo.northside.omegat.textra

import org.testng.annotations.Test

import org.omegat.util.Language


import static org.testng.Assert.*


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
        assertTrue(new TextraOptions().setProvider(TextraOptions.Provider.minna_personal).setMode("generalNT").setLang(new Language("en"), new Language("ja")).isCombinationValid(), "test KI personal")
        TextraOptions options = new TextraOptions()
        options.setProvider(TextraOptions.Provider.minna_personal)
        options.setMode("generalNT")
        options.setLang("en", "ja")
        assertTrue(options.isCombinationValid())
        options = new TextraOptions().setMode("custom");
        options.setCustomId("c999999");
        assertTrue(options.isCombinationValid())
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
    void testSetModePatentN() {
        TextraOptions options = new TextraOptions().setMode("patentNT")
        assertEquals(options.getModeName(), "patentNT")
    }

    @Test
    void testSetModeGeneralNT() {
        TextraOptions options = new TextraOptions().setMode("generalNT")
        assertEquals(options.getModeName(), "generalNT")
    }

    @Test
    void testIsMode() {
        TextraOptions options = new TextraOptions().setMode(TextraOptions.Mode.generalNT)
        assertTrue(options.isMode("generalNT"))
        assertFalse(options.isMode(null))
        assertFalse(options.isMode("patentNT"))
    }
}
