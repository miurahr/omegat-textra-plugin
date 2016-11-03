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
            // do nothing
        }

        @Override
        protected String translate(final Language sLang, final Language tLang, final String text)
                throws Exception {
            return "Translated."
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
        assertEquals(mock.getTranslation(sLang, tLang, "Source text."), "Translated.")
    }

    @Test(dependsOnMethods = "testGetTranslation")
    void testGetCachedTranslation() {
        Language sLang = new Language("en")
        Language tLang = new Language("ja")
        assertEquals(mock.getCachedTranslation(sLang, tLang, "Source text."), "Translated.")
    }
}
