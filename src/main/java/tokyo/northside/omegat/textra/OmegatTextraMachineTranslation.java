/**************************************************************************
 * TexTra Machine Translation plugin for OmegaT(http://www.omegat.org/)
 *
 * Copyright 2016-2023  Hiroshi Miura
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 **************************************************************************/
package tokyo.northside.omegat.textra;

import org.omegat.core.Core;
import org.omegat.core.machinetranslators.BaseCachedTranslate;
import org.omegat.gui.exttrans.IMachineTranslation;
import org.omegat.util.Language;
import org.omegat.util.Log;
import org.omegat.util.Preferences;

import tokyo.northside.omegat.textra.dialog.TextraOptionDialogController;

import java.awt.Window;
import java.io.IOException;

import javax.swing.*;

import static tokyo.northside.omegat.textra.TextraOptions.Mode.generalNT;

/**
 * Support TexTra powered by NICT machine translation.
 *
 * @author Hiroshi Miura
 */
public class OmegatTextraMachineTranslation extends BaseCachedTranslate implements IMachineTranslation {
    protected TextraOptions options;

    protected TextraApiClient client;

    /**
     * Preparation for OmegaT Menu.
     */
    public static final String OPTION_ALLOW_TEXTRA_TRANSLATE = "allow_textra_translate";

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
            Log.log("Textra Machine Translation plugin enabled.");
        } else {
            Log.log("Textra Machine Translation plugin disabled.");
        }
        client = new TextraApiClient();
    }

    public void saveCredential(final TextraOptions textraOptions) {
        setCredential(OPTION_TEXTRA_USERNAME, textraOptions.getUsername(), false);
        setCredential(OPTION_TEXTRA_APIKEY, textraOptions.getApikey(), false);
        setCredential(OPTION_TEXTRA_SECRET, textraOptions.getSecret(), false);
        Preferences.setPreference(OPTION_TEXTRA_SERVER, textraOptions.getProvider());
        Preferences.setPreference(OPTION_TEXTRA_TRANSLATE_MODE, textraOptions.getMode());
        if (textraOptions.getCustomId() != null) {
            Preferences.setPreference(OPTION_TEXTRA_CUSTOM_ID, textraOptions.getCustomId());
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
        TextraOptionDialogController.show(parent, options);
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
    @SuppressWarnings("unused")
    public static void loadPlugins() {
        Core.registerMachineTranslationClass(OmegatTextraMachineTranslation.class);
    }

    /**
     * Unregister plugin.
     * Currently not supported.
     */
    @SuppressWarnings("unused")
    public static void unloadPlugins() {}

    /**
     * Call Web API to translate.
     * @param text source text.
     * @return translated test.
     */
    @Override
    protected String translate(Language sLang, final Language tLang, final String text) throws Exception {
        if (!checkConfig()) {
            enabled = false;
            return null;
        }
        // Set TexTra access options
        options.setLang(sLang, tLang);
        if (!options.isCombinationValid()) {
            Log.log(String.format(
                    "Textra:Invalid language combination" + " for %s with source %s, and target %s on %s.",
                    options.getModeName(),
                    options.getSourceLang(),
                    options.getTargetLang(),
                    options.getProvider().name()));
            return null;
        }
        return client.executeTranslation(options, text);
    }

    protected boolean checkConfig() {
        String apiUsername = options.getUsername();
        String apiKey = options.getApikey();
        String apiSecret = options.getSecret();
        if (apiUsername == null
                || apiUsername.isEmpty()
                || apiKey == null
                || apiKey.isEmpty()
                || apiSecret == null
                || apiSecret.isEmpty()) {
            SwingUtilities.invokeLater(
                    () -> TextraOptionDialogController.show(Core.getMainWindow().getApplicationFrame(), options));
            return false;
        }
        return true;
    }
}
