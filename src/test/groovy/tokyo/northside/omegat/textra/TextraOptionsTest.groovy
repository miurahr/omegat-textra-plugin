package tokyo.northside.omegat.textra

import org.testng.annotations.Test;

import static org.testng.Assert.*;

class TextraOptionsTest {

    @Test
    void testIsCombinationValid() {
        assertTrue(new TextraOptions().setMode("generalN").setLang("ja", "en").isCombinationValid())
        assertFalse(new TextraOptions().setMode("generalN").setLang("fr", "es").isCombinationValid())
        assertFalse(new TextraOptions().setMode("generalN").setLang("zh-CN", "zh-TW").isCombinationValid())
        assertTrue(new TextraOptions().setMode("generalN").setLang("ja", "zh-CN").isCombinationValid())
        assertTrue(new TextraOptions().setMode("generalN").setLang("JA", "EN").isCombinationValid())
        assertTrue(new TextraOptions().setMode("patentN").setLang("en", "ja").isCombinationValid())
        assertTrue(new TextraOptions().setMode("patent_claimN").setLang("ja", "en").isCombinationValid())
        assertTrue(new TextraOptions().setMode("generalNT").setLang("ja", "en").isCombinationValid())
        assertTrue(new TextraOptions().setMode("voicetraNT").setLang("ja", "en").isCombinationValid())
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
    void testSetModeGeneralN() {
        TextraOptions options = new TextraOptions().setMode("generalN")
        assertEquals(options.getModeName(), "generalN")
    }

    @Test
    void testSetModePatentN() {
        TextraOptions options = new TextraOptions().setMode("patentN")
        assertEquals(options.getModeName(), "patentN")
    }

    @Test
    void testSetModePatentClaimN() {
        TextraOptions options = new TextraOptions().setMode("patent_claimN")
        assertEquals(options.getModeName(), "patent_claimN")
    }

    @Test
    void testSetModeGeneralNT() {
        TextraOptions options = new TextraOptions().setMode("generalNT")
        assertEquals(options.getModeName(), "generalNT")
    }

    @Test
    void testIsMode() {
        TextraOptions options = new TextraOptions().setMode(TextraOptions.Mode.generalN)
        assertTrue(options.isMode("generalN"))
        assertFalse(options.isMode(null))
        assertFalse(options.isMode("patentN"))
    }
}
