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

import java.awt.Desktop;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.omegat.core.Core;
import org.omegat.gui.preferences.BasePreferencesController;
import org.omegat.gui.preferences.IPreferencesController;
import org.omegat.gui.preferences.view.GlossaryPreferencesController;
import org.omegat.util.OStrings;
import org.omegat.util.Preferences;

import tokyo.northside.omegat.textra.OmegatTextraMachineTranslation;
import tokyo.northside.omegat.textra.WebBrowser;

import static tokyo.northside.omegat.textra.TextraPreferenceController.Mode.generalN;


/**
 * Data class for TexTra configuration Options.
 * Also have combination limitation knowledge.
 * @author Hiroshi Miura
 */
public class TextraPreferenceController extends BasePreferencesController {
    private String username;
    private String apikey;
    private String secret;
    private Mode mode;
    private String sourceLang;
    private String targetLang;

    private TextraPreferencePanel panel;

    private static final String API_KEY_URL = "https://mt-auto-minhon-mlt.ucri.jgn-x.jp/content/mt/";
    private static final String REGISTRATION_URL = "https://mt-auto-minhon-mlt.ucri.jgn-x.jp/content/register/";

    /**
     * Preparation for OmegaT Menu.
     */
    private static final String OPTION_ALLOW_TEXTRA_TRANSLATE = "allow_textra_translate";
    private static final String OPTION_TEXTRA_USERNAME = "mt_textra_username";
    private static final String OPTION_TEXTRA_APIKEY = "mt_textra_apikey";
    private static final String OPTION_TEXTRA_SECRET = "mt_textra_secret";
    private static final String OPTION_TEXTRA_TRANSLATE_MODE = "mt_textra_translate_mode";
    private static final String MENU_TEXTRA = "TexTra";

    private Set<Combination> combination = new HashSet<>();

    {
        combination.addAll(createCombinations(
                Arrays.asList(Mode.generalN, Mode.patentN, Mode.patent_claimN),
                Arrays.asList("ja", "en", "zh-CN", "zh-TW")));
        combination.addAll(createCombinations(
                Arrays.asList(Mode.generalN, Mode.patentN, Mode.patent_claimN),
                Arrays.asList("ko", "ja")));
        combination.addAll(createCombinations(Mode.generalN, "en",
                Arrays.asList("fr", "pt", "fr", "id", "my", "th", "vi", "es")));
    }

    @Override
    public JComponent getGui() {
        if (panel == null) {
            initGui();
            initFromPrefs();
        }
        return panel;
    }

    @Override
    public String toString() {
        return OStrings.getString("PREFS_TITLE_TEXTRA");
    }

    private void initGui() {
        panel = new TextraPreferencePanel();
        panel.registerNewTexTraUserButton.addActionListener(actionEvent ->
                WebBrowser.launch(panel, REGISTRATION_URL));
        panel.checkTexTraAPIKeyButton.addActionListener(actionEvent ->
                WebBrowser.launch(panel, API_KEY_URL));

        Timer timer = new Timer(500, e -> {
            persistCredentials();
        });
        timer.setRepeats(false);

        panel.userNameTextField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            private void update() {
                if (panel.userNameTextField.hasFocus()) {
                    timer.restart();
                }
            }
        });

        panel.apikeyTextField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            private void update() {
                if (panel.apikeyTextField.hasFocus()) {
                    timer.restart();
                }
            }
        });

        panel.secretTextField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            private void update() {
                if (panel.apikeyTextField.hasFocus()) {
                    timer.restart();
                }
            }
        });
    }

    private void persistCredentials() {
        try {
            getData();
            // XXX: TaaSPlugin.getClient().setApiKey(key, temporary);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Override
    protected void initFromPrefs() {
        setUsername(Preferences.getPreference(OPTION_TEXTRA_USERNAME));
        setApikey(Preferences.getPreference(OPTION_TEXTRA_APIKEY));
        setSecret(Preferences.getPreference(OPTION_TEXTRA_SECRET));
        setMode(Preferences.getPreferenceEnumDefault(OPTION_TEXTRA_TRANSLATE_MODE, generalN));
    }

    @Override
    public void restoreDefaults() {
    }

    @Override
    public void persist() {
        Preferences.setPreference(OPTION_TEXTRA_USERNAME, getUsername());
        Preferences.setPreference(OPTION_TEXTRA_APIKEY, getApikey());
        Preferences.setPreference(OPTION_TEXTRA_SECRET, getSecret());
        Preferences.setPreference(OPTION_TEXTRA_TRANSLATE_MODE, getMode());
        Preferences.save();
    }

    @Override
    public Class<? extends IPreferencesController> getParentViewClass() {
        return GlossaryPreferencesController.class;
    }

    /**
     * Setter for Dialog data.
     *
     * @param data dialog data.
     */
    public void setData() {
        panel.userNameTextField.setText(getUsername());
        panel.apikeyTextField.setText(getApikey());
        panel.secretTextField.setText(getSecret());
        switch (getMode()) {
            case generalN:
                panel.generalModeRadioButton.setSelected(true);
                break;
            case patentN:
                panel.patentModeRadioButton.setSelected(true);
                break;
            case patent_claimN:
                panel.patentOrderModeRadioButton.setSelected(true);
                break;
            default:
                panel.generalModeRadioButton.setSelected(true);
                break;
        }
    }

    /**
     * Getter for dialog data.
     *
     * @param data dialog data.
     */
    public void getData() {
        setUsername(panel.userNameTextField.getText());
        setApikey(panel.apikeyTextField.getText());
        setSecret(panel.secretTextField.getText());
        setMode(panel.modeButtonGroup.getSelection().getActionCommand());
    }

    /**
     * utility function to create Set of combination from set of String.
     */
    private Set<Combination> createCombinations(final List<Mode> modes,
                                                final List<String> languages) {
        Set<Combination> outSet = new HashSet<>();
        for (String source: languages) {
            for (String target: languages) {
                if (source.startsWith("zh") && target.startsWith("zh")) {
                    continue;
                } else if (source.equals(target)) {
                    continue;
                }
                for (Mode m: modes) {
                    outSet.add(new Combination(m, source, target));
                }
            }
        }
        return outSet;
    }

    private Set<Combination> createCombinations(final Mode m,
                                                final String source,
                                                final List<String> languages) {
        Set<Combination> outSet = new HashSet<>();
        for (String target: languages) {
            if (source.equals(target)) {
                continue;
            }
            outSet.add(new Combination(m, source, target));
            outSet.add(new Combination(m, target, source));
        }
        return outSet;
    }

    /**
     * Class for check mode, language combination.
     */
    private class Combination {
        private Mode mode;
        private String sLang;
        private String tLang;

        /**
         * Constructor.
         * @param mode mode.
         * @param sLang source language.
         * @param tLang target language.
         */
        Combination(final Mode mode, final String sLang, final String tLang) {
            this.mode = mode;
            this.sLang = sLang;
            this.tLang = tLang;
        }

        /**
         * Override equals.
         * @param o other object.
         * @return true if three values are equals.
         */
        @Override
        public boolean equals(final Object o) {
            if (o == null) {
                return false;
            }
            if (!(o instanceof Combination)) {
                return false;
            }
            Combination other = (Combination) o;
            return other.mode.equals(this.mode) && other.sLang.toLowerCase().equals(this.sLang
                    .toLowerCase()) && other.tLang.toLowerCase().equals(this.tLang.toLowerCase());
        }

        /**
         * Return hashCode based on triplet strings.
         * @return hash code.
         */
        @Override
        public int hashCode() {
            return (mode.name() + sLang.toLowerCase() + tLang.toLowerCase()).hashCode();
        }
    }

    /**
     * Check if parameter combination is valid or not.
     * @return true is combination is valid, otherwise false.
     */
    public boolean isCombinationValid() {
        return combination.contains(new Combination(mode, sourceLang, targetLang));
    }

    /**
     * Translation mode.
     * These name is as same as an part of API URL.
     */
    public enum Mode {
        /** generic translation.
         * English/Japanese/Chinese/Korean
         */
        generalN,
        /** address translation.
         * Japanese/English
         */
        patentN,
        /** JPO claim translation.
         * Chinese/Japanese
         */
        patent_claimN,
    }

    /**
     * Getter of username.
     * @return username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter of username.
     * @param username to set.
     * @return this object.
     */
    @SuppressWarnings("HiddenField")
    public TextraPreferenceController setUsername(final String username) {
        this.username = username;
        return this;
    }

    /**
     * Getter of ApiKey.
     * @return apikey.
     */
    public String getApikey() {
        return apikey;
    }

    /**
     * Setter of Apikey.
     * @param apiKey to set.
     * @return this object.
     */
    @SuppressWarnings("HiddenField")
    public TextraPreferenceController setApikey(final String apiKey) {
        this.apikey = apiKey;
        return this;
    }

    /**
     * Getter of secret.
     * @return secret.
     */
    public String getSecret() {
        return secret;
    }

    /**
     * Setter of secret.
     * @param secret to set.
     * @return this object.
     */
    @SuppressWarnings("HiddenField")
    public TextraPreferenceController setSecret(final String secret) {
        this.secret = secret;
        return this;
    }

    /**
     * Getter of mode.
     * @return TranslateMode enum value.
     */
    public Mode getMode() {
        return mode;
    }

    /**
     * Setter of mode.
     * @param mode TranslateMode.
     * @return this object.
     */
    @SuppressWarnings("HiddenField")
    public TextraPreferenceController setMode(final Mode mode) {
        this.mode = mode;
        return this;
    }

    /**
     * Get mode name.
     * @return API mode string for target URL.
     */
    public String getModeName() {
        return mode.name();
    }

    /**
     * Setter of mode by String.
     * @param name String.
     * @return this object.
     */
    public TextraPreferenceController setMode(final String name) {
        if (name != null) {
            mode = Enum.valueOf(Mode.class, name);
        }
        return this;
    }

    /**
     * Ask mode.
     * @param name Mode name.
     * @return true if mode name equals.
     */
    public boolean isMode(final String name) {
        return mode.name().equals(name);
    }

    /**
     * Set language options.
     * <p>
     *     Format language string as like "zh-CN", "en", "ja"
     *     to be a proper case.
     *     Because OmegaT may give language in capital letter such as "EN".
     * </p>
     * @param sLang source language.
     * @param tLang target language.
     * @return this object.
     */
    public TextraPreferenceController setLang(final String sLang, final String tLang) {
        this.sourceLang = formatLang(sLang);
        this.targetLang = formatLang(tLang);
        return this;
    }

    private String formatLang(final String lang) {
        String result;
        int index = lang.indexOf("-");
        if (index != -1) {
            result = lang.substring(0, index).toLowerCase()
                    + lang.substring(index).toUpperCase();
        } else {
            result = lang.toLowerCase();
        }
        return result;
    }

    /**
     * Get source language string.
     * @return source language.
     */
    String getSourceLang() {
        return sourceLang;
    }

    /**
     * Get target language string.
     * @return target language.
     */
    String getTargetLang() {
        return targetLang;
    }
}
