package tokyo.northside.omegat.textra

import org.testng.annotations.Test;

import static org.testng.Assert.*;

class TextraOptionsTest {

    @Test
    void testIsCombinationValid() {
        assertTrue(new TextraPreferenceController().setMode("generalN").setLang("ja", "en").isCombinationValid())
        assertFalse(new TextraPreferenceController().setMode("generalN").setLang("fr", "es").isCombinationValid())
        assertFalse(new TextraPreferenceController().setMode("generalN").setLang("zh-CN", "zh-TW").isCombinationValid())
        assertTrue(new TextraPreferenceController().setMode("generalN").setLang("ja", "zh-CN").isCombinationValid())
        assertTrue(new TextraPreferenceController().setMode("generalN").setLang("JA", "EN").isCombinationValid())
        assertTrue(new TextraPreferenceController().setMode("patentN").setLang("en", "ja").isCombinationValid())
        assertTrue(new TextraPreferenceController().setMode("patent_claimN").setLang("ja", "en").isCombinationValid())
    }

    @Test
    void testUsername() {
        TextraPreferenceController options = new TextraPreferenceController().setUsername("hoge")
        assertEquals(options.getUsername(), "hoge")
    }

    @Test
    void testApikey() {
        TextraPreferenceController options = new TextraPreferenceController().setApikey("hoge")
        assertEquals(options.getApikey(), "hoge")
    }

    @Test
    void testSecret() {
        TextraPreferenceController options = new TextraPreferenceController().setSecret("hoge")
        assertEquals(options.getSecret(), "hoge")
    }

    @Test
    void testMode() {
        TextraPreferenceController options = new TextraPreferenceController().setMode(TextraPreferenceController.Mode.generalN)
        assertEquals(options.getMode(), TextraPreferenceController.Mode.generalN)
    }

    @Test
    void testGetModeName() {
        TextraPreferenceController options = new TextraPreferenceController().setMode(TextraPreferenceController.Mode.generalN)
        assertEquals(options.getModeName(), "generalN")
    }

    @Test
    void testSetModeGeneralN() {
        TextraPreferenceController options = new TextraPreferenceController().setMode("generalN")
        assertEquals(options.getModeName(), "generalN")
    }

    @Test
    void testSetModePatentN() {
        TextraPreferenceController options = new TextraPreferenceController().setMode("patentN")
        assertEquals(options.getModeName(), "patentN")
    }

    @Test
    void testSetModePatentClaimN() {
        TextraPreferenceController options = new TextraPreferenceController().setMode("patent_claimN")
        assertEquals(options.getModeName(), "patent_claimN")
    }

    @Test
    void testIsMode() {
        TextraPreferenceController options = new TextraPreferenceController().setMode(TextraPreferenceController.Mode.generalN)
        assertTrue(options.isMode("generalN"))
        assertFalse(options.isMode(null))
        assertFalse(options.isMode("patentN"))
    }
}
