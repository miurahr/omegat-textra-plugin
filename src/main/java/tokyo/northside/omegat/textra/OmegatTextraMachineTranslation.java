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
import java.io.IOException;
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

import static tokyo.northside.omegat.textra.TextraOptions.Mode.generalNT;


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
    private final Map<String, String> cache = Collections.synchronizedMap(new HashMap<>());
    /**
     * Preparation for OmegaT Menu.
     */
    private static final String OPTION_ALLOW_TEXTRA_TRANSLATE = "allow_textra_translate";
    private static final String OPTION_TEXTRA_USERNAME = "mt_textra_username";
    private static final String OPTION_TEXTRA_APIKEY = "mt_textra_apikey";
    private static final String OPTION_TEXTRA_SECRET = "mt_textra_secret";
    private static final String OPTION_TEXTRA_TRANSLATE_MODE = "mt_textra_translate_mode";
    private static final String OPTION_TEXTRA_SERVER = "mt_textra_server";
    private static final String OPTION_TEXTRA_CUSTOM_ID = "mt_textra_custom_id";
    private static final String MENU_TEXTRA = "TexTra Powered by NICT";

    /**
     * Construct options.
     */
    public OmegatTextraMachineTranslation() throws IOException {
        super();
        options = new TextraOptions(
            Preferences.getPreferenceEnumDefault(OPTION_TEXTRA_SERVER, TextraOptions.Provider.nict),
            getCredential(OPTION_TEXTRA_USERNAME),
            getCredential(OPTION_TEXTRA_APIKEY),
            getCredential(OPTION_TEXTRA_SECRET),
            Preferences.getPreferenceDefault(OPTION_TEXTRA_CUSTOM_ID, null),
            Preferences.getPreferenceEnumDefault(OPTION_TEXTRA_TRANSLATE_MODE, generalNT),
                this);
        enabled = Preferences.isPreferenceDefault(OPTION_ALLOW_TEXTRA_TRANSLATE, true);
        if (enabled) {
            LOGGER.info("Textra Machine Translation plugin enabled.");
        } else {
            LOGGER.info("Textra Machine Translation plugin disabled.");
        }
    }

    public void saveCredential(final TextraOptions options) {
        setCredential(OPTION_TEXTRA_USERNAME, options.getUsername(), false);
        setCredential(OPTION_TEXTRA_APIKEY, options.getApikey(), false);
        setCredential(OPTION_TEXTRA_SECRET, options.getSecret(), false);
        Preferences.setPreference(OPTION_TEXTRA_SERVER, options.getProvider());
        Preferences.setPreference(OPTION_TEXTRA_TRANSLATE_MODE, options.getMode());
        if (options.getCustomId() != null) {
            Preferences.setPreference(OPTION_TEXTRA_CUSTOM_ID, options.getCustomId());
        }
        Preferences.save();
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
        TextraOptionDialog dialog = new TextraOptionDialog(options);
        dialog.pack();
        dialog.setOptions(options);
        dialog.setVisible(true);
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
     */
    @Override
    protected String translate(final Language sLang, final Language tLang, final String text) {
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
     */
    public String getTranslation(final Language sLang, final Language tLang, final String text) {
        if (enabled) {
            // Set TexTra access options
            options.setLang(sLang, tLang);
            if (!options.isCombinationValid()) {
                LOGGER.info(String.format("Textra:Invalid language combination"
                                + " for %s with source %s, and target %s on %s.",
                        options.getModeName(), options.getSourceLang(), options.getTargetLang(),
                        options.getProvider().name()));
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

