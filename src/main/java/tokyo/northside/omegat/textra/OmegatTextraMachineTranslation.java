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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tokyo.northside.omegat.textra.dialog.TextraOptionDialog;

import org.omegat.core.Core;
import org.omegat.gui.exttrans.IMachineTranslation;
import org.omegat.util.Language;

import org.omegat.util.Preferences;
import org.openide.awt.Mnemonics;

import static tokyo.northside.omegat.textra.TextraOptions.Mode.generalN;


/**
 * Support TexTra powered by NICT machine translation.
 *
 * @author Hiroshi Miura
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class OmegatTextraMachineTranslation implements IMachineTranslation, ActionListener {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(OmegatTextraMachineTranslation.class);
    protected boolean enabled;
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
    private final JMenu menuItem = new JMenu();
    private final JMenuItem enableMenuItem = new JMenuItem();
    private final JMenuItem optionMenuItem = new JMenuItem();

    private static final String OPTION_ALLOW_TEXTRA_TRANSLATE = "allow_textra_translate";
    private static final String OPTION_TEXTRA_USERNAME = "mt_textra_username";
    private static final String OPTION_TEXTRA_APIKEY = "mt_textra_apikey";
    private static final String OPTION_TEXTRA_SECRET = "mt_textra_secret";
    private static final String OPTION_TEXTRA_TRANSLATE_MODE = "mt_textra_translate_mode";

    private static final String ACTION_MENU_ENABLE = "menu_enable";
    private static final String ACTION_MENU_DISABLE = "menu_disable";
    private static final String ACTION_MENU_OPTIONS = "set_options";

    private static final String MENU_TEXTRA_ENABLED = "TexTra(Enabled)";
    private static final String MENU_TEXTRA_DISABLED = "TexTra(Disabled)";
    private static final String MENU_SET_OPTIONS = "Set Options";
    private static final String MENU_DISABLE = "Disable";
    private static final String MENU_ENABLE = "Enable";

    /**
     * Construct menu items and get options.
     */
    public OmegatTextraMachineTranslation() {
        initOptions();
        initEnabled();
        initMenus();
    }

    protected void initEnabled() {
        enabled = Preferences.isPreference(OPTION_ALLOW_TEXTRA_TRANSLATE);
    }

    protected void initOptions() {
        options = new TextraOptions()
                .setUsername(Preferences.getPreference(OPTION_TEXTRA_USERNAME))
                .setApikey(Preferences.getPreference(OPTION_TEXTRA_APIKEY))
                .setSecret(Preferences.getPreference(OPTION_TEXTRA_SECRET))
                .setMode(Preferences.getPreferenceEnumDefault(OPTION_TEXTRA_TRANSLATE_MODE,
                        generalN));
    }

    protected void initMenus() {
        Mnemonics.setLocalizedText(optionMenuItem, MENU_SET_OPTIONS);
        optionMenuItem.setActionCommand(ACTION_MENU_OPTIONS);
        updateMenuItem();
        menuItem.add(enableMenuItem);
        menuItem.add(optionMenuItem);
        enableMenuItem.addActionListener(this);
        optionMenuItem.addActionListener(this);
        Core.getMainWindow().getMainMenu().getMachineTranslationMenu().add(menuItem);
    }

    private void updateMenuItem() {
        if (enabled) {
            Mnemonics.setLocalizedText(menuItem, MENU_TEXTRA_ENABLED);
            Mnemonics.setLocalizedText(enableMenuItem, MENU_DISABLE);
            enableMenuItem.setActionCommand(ACTION_MENU_DISABLE);
        } else {
            Mnemonics.setLocalizedText(menuItem, MENU_TEXTRA_DISABLED);
            Mnemonics.setLocalizedText(enableMenuItem, MENU_ENABLE);
            enableMenuItem.setActionCommand(ACTION_MENU_ENABLE);
        }
    }

    /**
     * Menu action definitions.
     * @param e action event from menu.
     */
    @SuppressWarnings("StatementWithEmptyBody")
    public void actionPerformed(final ActionEvent e) {
        String action = e.getActionCommand();
        if (ACTION_MENU_DISABLE.equals(action)) {
            enabled = false;
            updateMenuItem();
            Preferences.setPreference(OPTION_ALLOW_TEXTRA_TRANSLATE, enabled);
        } else if (ACTION_MENU_ENABLE.equals(action)) {
            enabled = true;
            updateMenuItem();
            Preferences.setPreference(OPTION_ALLOW_TEXTRA_TRANSLATE, enabled);
        } else if (ACTION_MENU_OPTIONS.equals(action)) {
            TextraOptionDialog dialog = new TextraOptionDialog();
            dialog.pack();
            dialog.setData(options);
            dialog.setVisible(true);
            if (dialog.isModified(options)) {
                dialog.getData(options);
            }
            savePreferences();
        }
    }

    private void savePreferences() {
        Preferences.setPreference(OPTION_TEXTRA_USERNAME, options.getUsername());
        Preferences.setPreference(OPTION_TEXTRA_APIKEY, options.getApikey());
        Preferences.setPreference(OPTION_TEXTRA_SECRET, options.getSecret());
        Preferences.setPreference(OPTION_TEXTRA_TRANSLATE_MODE, options.getMode());
        Preferences.save();
    }

    /**
     * Return status.
     * @return true if enabled, otherwise false.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Return MT name.
     * @return MT engine name.
     */
    public String getName() {
        // TexTra service terms demand to show "Powered by NICT" on every application screen.
        // You may keep it to be compliant with TexTra terms.
        // Because it is showed on MT pane.
        return "TexTra Powered by NICT";
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
    protected String translate(final String text)
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
            options.setLang(sLang.getLanguageCode(), tLang.getLanguageCode());
            if (!options.isCombinationValid()) {
                LOGGER.info(String.format("Invalid language combination for %s with source %s, "
                                + " and target %s.",
                        options.getModeName(), options.getSourceLang(), options.getTargetLang()));
                return null;
            }

            String result = translate(text);
            if (result != null) {
                putToCache(sLang, tLang, text, result);
            }
            return result;
        } else {
            return null;
        }
    }

    /**
     * Get translated result from cache.
     * @param sLang source language.
     * @param tLang target language.
     * @param text source text.
     * @return translated text if cache exist, otherwise null.
     */
    public String getCachedTranslation(final Language sLang, final Language tLang,
                                       final String text) {
        if (enabled) {
            return getFromCache(sLang, tLang, text);
        } else {
            return null;
        }
    }

    private String getFromCache(final Language sLang, final Language tLang, final String text) {
        return cache.get(sLang + "/" + tLang + "/" + text);
    }

    private String putToCache(final Language sLang, final Language tLang, final String text,
                              final String result) {
        return cache.put(sLang + "/" + tLang + "/" + text, result);
    }
}

