package tokyo.northside.omegat.textra;

import org.omegat.util.Language;

import java.io.IOException;

/**
 * Data class for TexTra configuration Options.
 * Also have combination limitation knowledge.
 * @author Hiroshi Miura
 */
public class TextraOptions {
    private String username;
    private String apikey;
    private String secret;
    private String customId;
    private Mode mode;

    private Provider provider;

    private OmegatTextraMachineTranslation omegatTextraMachineTranslation;
    private TextraOptionCombinations textraOptionCombinations;

    private String sourceLang;
    private String targetLang;


    public TextraOptions(Provider provider, String username, String apikey, String secret, String customId,
                         Mode mode, OmegatTextraMachineTranslation omegatTextraMachineTranslation) throws IOException {
        this.username = username;
        this.apikey = apikey;
        this.secret = secret;
        this.customId = customId;
        this.mode = mode;
        this.omegatTextraMachineTranslation = omegatTextraMachineTranslation;
        this.provider = provider;

        this.textraOptionCombinations = new TextraOptionCombinations();
    }

    /**
     * Dummy constructor for test.
     */
    public TextraOptions() throws IOException {
        this(Provider.nict, "", "", "", null, Mode.generalNT, null);
    }

    public void saveCredentials() {
        this.omegatTextraMachineTranslation.saveCredential(this);
    }

    public boolean isCombinationValid() {
        if (mode != Mode.custom) {
            return textraOptionCombinations.isCombinationValid(provider, mode, sourceLang, targetLang);
        } else {
            if (customId != null && (customId.startsWith("c-") || customId.startsWith("a-"))) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Translation provider
     *
     * There are known three services;
     * 1. NiCT TexTra for nonprofit purpose
     * 2. Kawamura-Internaltional TexTra for personal business.
     * 3. Kawamura-Internaltional TexTra for business.
     */
    public enum Provider {
        nict,
        minna_personal,
    }

    /**
     * Translation mode.
     * These name is as same as an part of API URL.
     */
    public enum Mode {
       /** general NT mode.
         */
        generalNT,
       /** Patent NT mode.
         * Next generation of Patent NMT
         */
        patentNT,
       /** Taiwa NT mode.
         * Next generation of Taiwa NMT
         */
        voicetraNT,
        /** Financial NT mode.
         *
         */
        fsaNT,
        /** Minna NT mode.
         * Aka. Generic NT+
         */
        minnaNT,
        /**
         * science mode.
         * only supported with KI personal edition.
         */
        science,
        /**
         * Custom mode.
         */
        custom,
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
    public TextraOptions setUsername(final String username) {
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
    public TextraOptions setApikey(final String apiKey) {
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
    public TextraOptions setSecret(final String secret) {
        this.secret = secret;
        return this;
    }

    /**
     * Getter of custom id.
     * @return custom id when configured, otherwise "custom id"
     */
    public String getCustomId() {
        if (customId == null) {
            return "CustomID(ex. c-12345)";
        }
        return customId;
    }

    /**
     * Setter of custom id.
     * @param customId  id string format 'c999999'
     */
    @SuppressWarnings("HiddenField")
    public void setCustomId(String customId) {
        this.customId = customId;
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
    public TextraOptions setMode(final Mode mode) {
        this.mode = mode;
        return this;
    }

    public boolean isServer(final Provider provider) {
        return this.provider.equals(provider);
    }

    public Provider getProvider() {
        return provider;
    }

    public TextraOptions setProvider(final Provider provider) {
        this.provider = provider;
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
    public TextraOptions setMode(final String name) {
        if (name != null) {
            mode = Mode.valueOf(name);
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
    public TextraOptions setLang(final String sLang, final String tLang) {
        this.sourceLang = formatLang(sLang, "");
        this.targetLang = formatLang(tLang, "");
        return this;
    }

    public TextraOptions setLang(final Language sLang, final Language tLang) {
        this.sourceLang = formatLang(sLang);
        this.targetLang = formatLang(tLang);
        return this;
    }

    private static String formatLang(final String lang, final String country) {
        String result;
        if (country.equals("CN")) {
            result = lang.toLowerCase() + "-" + country.toUpperCase();
        } else {
            result = lang.toLowerCase();
        }
        return result;
    }

    static String formatLang(final Language lang) {
        return formatLang(lang.getLanguageCode(), lang.getCountryCode());
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
