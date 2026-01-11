/*
  TexTra Machine Translation plugin for OmegaT(http://www.omegat.org/)

  Copyright 2016-2023  Hiroshi Miura

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
*/
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

import javax.swing.SwingUtilities;

/**
 * Support TexTra powered by NICT machine translation.
 *
 * @author Hiroshi Miura
 */
public class OmegatTextraMachineTranslation extends BaseCachedTranslate implements IMachineTranslation {

    /**
     * TexTra API client.
     */
    protected TextraApiClient client;
    /**
     * Option dialog controller.
     */
    protected TextraOptionDialogController optionDialogController;
    /**
     * Factory of TexTra options.
     */
    private final TextraOptionsFactory textraOptionsFactory;

    /**
     * Preferences to enable/disable TexTra plugin.
     */
    public static final String OPTION_ALLOW_TEXTRA_TRANSLATE = "allow_textra_translate";

    /**
     * Preferences key of TexTra API credential option: username.
     */
    private static final String OPTION_TEXTRA_USERNAME = "mt_textra_username";
    /**
     * Preferences key of TexTra API credential option: api key.
     */
    private static final String OPTION_TEXTRA_APIKEY = "mt_textra_apikey";
    /**
     * Preferences key of TexTra API credential option: secret.
     */
    private static final String OPTION_TEXTRA_SECRET = "mt_textra_secret";
    /**
     * Preferences key of TexTra API option: translate mode.
     */
    private static final String OPTION_TEXTRA_TRANSLATE_MODE = "mt_textra_translate_mode";
    /**
     * Preferences key of TexTra API option: server.
     */
    private static final String OPTION_TEXTRA_SERVER = "mt_textra_server";
    /**
     * Preferences key of TexTra API option: custom id.
     */
    private static final String OPTION_TEXTRA_CUSTOM_ID = "mt_textra_custom_id";
    /**
     * Menu name of TexTra.
     */
    private static final String MENU_TEXTRA = "TexTra Powered by NICT";

    /**
     * Constructor.
     */
    public OmegatTextraMachineTranslation() {
        super();
        enabled = Preferences.isPreferenceDefault(OPTION_ALLOW_TEXTRA_TRANSLATE, true);
        if (enabled) {
            Log.log("Textra Machine Translation plugin enabled.");
        } else {
            Log.log("Textra Machine Translation plugin disabled.");
        }
        client = new TextraApiClient();
        optionDialogController = new TextraOptionDialogController();
        textraOptionsFactory = new TextraOptionsFactory();
    }

    /**
     * Save TexTra access credential to credential store.
     * @param textraOptions TexTra options.
     */
    public void saveCredential(TextraOptions textraOptions) {
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
        try {
            optionDialogController.show(parent, textraOptionsFactory, getOptions());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    public static void unloadPlugins() {
        // nothing to do
    }

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
        TextraOptions options = getOptions();
        options.setLang(sLang, tLang);
        return client.executeTranslation(options, text);
    }

    /**
     * Check configuration.
     * @return true if configuration is valid.
     */
    protected boolean checkConfig() throws IOException {
        TextraOptions options = getOptions();
        String apiUsername = options.getUsername();
        String apiKey = options.getApikey();
        String apiSecret = options.getSecret();
        if (apiUsername == null
                || apiUsername.isEmpty()
                || apiKey == null
                || apiKey.isEmpty()
                || apiSecret == null
                || apiSecret.isEmpty()) {
            SwingUtilities.invokeLater(() -> optionDialogController.show(
                    Core.getMainWindow().getApplicationFrame(), textraOptionsFactory, options));
            return false;
        }
        return true;
    }

    /**
     * TexTra options.
     */
    private TextraOptions textraOptions;

    /**
     * Get TexTra options.
     * @return TexTraOptions object generated from preferences.
     */
    private synchronized TextraOptions getOptions() throws IOException {
        if (textraOptions == null) {
            textraOptions = TextraOptions.builder()
                    .setTextraOptionsFactory(textraOptionsFactory)
                    .setProvider(Preferences.getPreferenceDefault(OPTION_TEXTRA_SERVER, "nict"))
                    .setUsername(getCredential(OPTION_TEXTRA_USERNAME))
                    .setApikey(getCredential(OPTION_TEXTRA_APIKEY))
                    .setSecret(getCredential(OPTION_TEXTRA_SECRET))
                    .setCustomId(Preferences.getPreferenceDefault(OPTION_TEXTRA_CUSTOM_ID, null))
                    .setMode(Preferences.getPreferenceDefault(OPTION_TEXTRA_TRANSLATE_MODE, "generalNT"))
                    .setOmegatTextraMachineTranslation(this)
                    .build();
        }
        return textraOptions;
    }
}
