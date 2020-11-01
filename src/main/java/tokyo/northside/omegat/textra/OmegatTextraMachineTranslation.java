/**************************************************************************
 TexTra Machine Translation plugin for OmegaT(http://www.omegat.org/)

 Copyright 2016,  Hiroshi Miura

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 **************************************************************************/

package tokyo.northside.omegat.textra;

import java.awt.Window;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tokyo.northside.omegat.textra.dialog.TextraOptionDialog;

import org.omegat.core.Core;
import org.omegat.core.machinetranslators.BaseTranslate;
import org.omegat.gui.exttrans.IMachineTranslation;
import org.omegat.util.Language;
import org.omegat.util.Preferences;

import static tokyo.northside.omegat.textra.TextraOptions.Mode.generalN;


/**
 * Support TexTra powered by NICT machine translation.
 *
 * @author Hiroshi Miura
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class OmegatTextraMachineTranslation extends BaseTranslate implements IMachineTranslation {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(OmegatTextraMachineTranslation.class);
    protected TextraOptions options;

    /**
     * Machine translation implementation can use this cache for skip requests twice.
     * Cache will not be cleared during OmegaT work.
     * FIXME: add timeout and flush functionality.
     */
    private final Map<String, String> cache = Collections.synchronizedMap(new HashMap<String,
            String>());
    /**
     * Preparation for OmegaT Menu.
     */
    private static final String OPTION_ALLOW_TEXTRA_TRANSLATE = "allow_textra_translate";
    private static final String OPTION_TEXTRA_USERNAME = "mt_textra_username";
    private static final String OPTION_TEXTRA_APIKEY = "mt_textra_apikey";
    private static final String OPTION_TEXTRA_SECRET = "mt_textra_secret";
    private static final String OPTION_TEXTRA_TRANSLATE_MODE = "mt_textra_translate_mode";
    private static final String MENU_TEXTRA = "TexTra Powered by NICT";

    /**
     * Construct options.
     */
    public OmegatTextraMachineTranslation() {
        super();
        options = new TextraOptions()
                .setUsername(getCredential(OPTION_TEXTRA_USERNAME))
                .setApikey(getCredential(OPTION_TEXTRA_APIKEY))
                .setSecret(getCredential(OPTION_TEXTRA_SECRET))
                .setMode(Preferences.getPreferenceEnumDefault(OPTION_TEXTRA_TRANSLATE_MODE,
                        generalN));
        enabled = Preferences.isPreferenceDefault(OPTION_ALLOW_TEXTRA_TRANSLATE, true);
        if (enabled) {
            LOGGER.info("Textra Machine Translation plugin enabled.");
        } else {
            LOGGER.info("Textra Machine Translation plugin disabled.");
        }
    }

    @Override
    protected String getPreferenceName() {
        return OPTION_ALLOW_TEXTRA_TRANSLATE;
    }

    @Override
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
        Preferences.setPreference(OPTION_ALLOW_TEXTRA_TRANSLATE, enabled);
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    public void showConfigurationUI(final Window parent) {
        TextraOptionDialog dialog = new TextraOptionDialog(parent);
        dialog.pack();
        dialog.setData(options);
        dialog.setVisible(true);
        if (dialog.isModified(options)) {
            dialog.getData(options);
        }
        setCredential(OPTION_TEXTRA_USERNAME, options.getUsername(), false);
        setCredential(OPTION_TEXTRA_APIKEY, options.getApikey(), false);
        setCredential(OPTION_TEXTRA_SECRET, options.getSecret(), false);
        Preferences.setPreference(OPTION_TEXTRA_TRANSLATE_MODE, options.getMode());
        Preferences.save();
    }

    /**
     * Return MT name.
     * @return MT engine name.
     */
    @Override
    public String getName() {
        // TexTra service terms demand to show "Powered by NICT" on every application screen.
        // You may keep it to be compliant with TexTra terms.
        // Because it is showed on MT pane.
        return MENU_TEXTRA;
    }

    /**
     * Register plugin into OmegaT.
     */
    public static void loadPlugins() {
        Core.registerMachineTranslationClass(OmegatTextraMachineTranslation.class);
    }

    /**
     * Unregister plugin.
     * Currently not supported.
     */
    public static void unloadPlugins() {
    }

    /**
     * Call Web API to translate.
     * @param text source text.
     * @return translated test.
     * @throws Exception when error happened.
     */
    @Override
    protected String translate(final Language sLang, final Language tLang, final String text)
            throws Exception {
       // Access to TexTra Web API
        TextraApiClient client = new TextraApiClient();
        client.authenticate(options, text);
        return client.executeTranslation();
     }

    /**
     * Return machine translation result.
     *
     * {@link IMachineTranslation()#getTranslation}
     * @param sLang source language.
     * @param tLang target language.
     * @param text source text.
     * @return translated text.
     * @throws Exception when error happened.
     */
    public String getTranslation(final Language sLang, final Language tLang, final String text)
            throws Exception {
        if (enabled) {
            // Set TexTra access options
            options.setLang(sLang, tLang);
            if (!options.isCombinationValid()) {
                LOGGER.info(String.format("Textra:Invalid language combination"
                                + " for %s with source %s, and target %s.",
                        options.getModeName(), options.getSourceLang(), options.getTargetLang()));
                return null;
            }

            String result = translate(sLang, tLang, text);
            if (result != null) {
                putToCache(sLang, tLang, text, result);
            }
            return result;
        } else {
            return null;
        }
    }

}

