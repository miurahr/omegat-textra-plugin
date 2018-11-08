package tokyo.northside.omegat.textra

import org.omegat.util.Language
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test;

import static org.testng.Assert.*;

class OmegatTextraMachineTranslationTest  {
    OmegatTextraMachineTranslationMock mock;

    class OmegatTextraMachineTranslationMock extends OmegatTextraMachineTranslation {
        protected void initEnabled() {
            enabled = true
        }

        protected void initMenus() {
            // do nothing
        }

        protected void initOptions() {
            options = new TextraOptions().setMode("genericN")
        }

        @Override
        protected String translate(final String text)
                throws Exception {
            return "Translated " + text
        }
    }

    @BeforeClass
    void setUp() {
        mock = new OmegatTextraMachineTranslationMock();
    }

    @Test
    void testIsEnabled() {
        assertTrue(mock.isEnabled())
    }

    @Test
    void testGetName() {
        assertEquals(mock.getName(), "TexTra Powered by NICT")
    }

    @Test
    void testGetTranslation() {
        Language sLang = new Language("en")
        Language tLang = new Language("ja")
        String text = "Source Text."
        assertEquals(mock.getTranslation(sLang, tLang, text), "Translated " + text)
    }

    @Test(dependsOnMethods = "testGetTranslation")
    void testGetCachedTranslation() {
        Language sLang = new Language("en")
        Language tLang = new Language("ja")
        String text = "Source Text." // Same as testGetTranslation()
        assertEquals(mock.getCachedTranslation(sLang, tLang, text), "Translated " + text)
    }
}
