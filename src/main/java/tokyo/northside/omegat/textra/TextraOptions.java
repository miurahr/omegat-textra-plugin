package tokyo.northside.omegat.textra;

import org.omegat.util.Language;

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
    private String provider;
    private String mode;

    private final OmegatTextraMachineTranslation omegatTextraMachineTranslation;

    private String sourceLang;
    private String targetLang;

    private boolean changed;

    private String baseUrl = null;
    private String path = null;

    private final TextraOptionsFactory textraOptionsFactory;

    private TextraOptions(
            final String provider,
            final String username,
            final String apikey,
            final String secret,
            final String customId,
            final String mode,
            final OmegatTextraMachineTranslation omegatTextraMachineTranslation,
            final TextraOptionsFactory factory) {
        this.username = username;
        this.apikey = apikey;
        this.secret = secret;
        this.customId = customId;
        this.mode = mode;
        this.omegatTextraMachineTranslation = omegatTextraMachineTranslation;
        this.provider = provider;
        this.textraOptionsFactory = factory;
        this.changed = false;
    }

    public void saveCredentials() {
        omegatTextraMachineTranslation.saveCredential(this);
        changed = true;
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
        return customId;
    }

    /**
     * Setter of custom id.
     * @param customId  id string format 'c999999'
     */
    @SuppressWarnings("HiddenField")
    public void setCustomId(final String customId) {
        this.customId = customId;
    }

    /**
     * Getter of mode.
     * @return TranslateMode enum value.
     */
    public String getMode() {
        return mode;
    }

    public String getProvider() {
        return provider;
    }

    @SuppressWarnings("HiddenField")
    public TextraOptions setProvider(final String provider) {
        this.provider = provider;
        return this;
    }

    /**
     * Get mode name.
     * @return API mode string for target URL.
     */
    public String getModeName() {
        return mode;
    }

    /**
     * Setter of mode by String.
     * @param name String.
     * @return this object.
     */
    public TextraOptions setMode(final String name) {
        mode = name;
        return this;
    }

    /**
     * Ask mode.
     * @param name Mode name.
     * @return true if mode name equals.
     */
    public boolean isMode(final String name) {
        return mode.equals(name);
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

    public String getOAuth2Url() {
        return getBaseUrl() + "/oauth2/token.php";
    }

    public String getBaseUrl() {
        if (baseUrl == null) {
            baseUrl = textraOptionsFactory.getURL(getProvider());
        }
        return baseUrl;
    }

    public String getPath() {
        if (path == null) {
            path = textraOptionsFactory.getURLPath();
        }
        return path;
    }

    public boolean isChanged() {
        final boolean result = changed;
        changed = false;
        return result;
    }

    public static TextraOptionsBuilder builder() {
        return new TextraOptionsBuilder();
    }

    public static class TextraOptionsBuilder {
        private String provider;
        private String username;
        private String apikey;
        private String secret;
        private String customId;
        private String mode;
        private OmegatTextraMachineTranslation omegatTextraMachineTranslation;
        private TextraOptionsFactory textraOptionsFactory;

        public TextraOptionsBuilder() {}

        public TextraOptionsBuilder setProvider(String provider) {
            this.provider = provider;
            return this;
        }

        public TextraOptionsBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public TextraOptionsBuilder setApikey(String apikey) {
            this.apikey = apikey;
            return this;
        }

        public TextraOptionsBuilder setSecret(String secret) {
            this.secret = secret;
            return this;
        }

        public TextraOptionsBuilder setCustomId(String customId) {
            this.customId = customId;
            return this;
        }

        public TextraOptionsBuilder setMode(String mode) {
            this.mode = mode;
            return this;
        }

        public TextraOptionsBuilder setOmegatTextraMachineTranslation(
                OmegatTextraMachineTranslation omegatTextraMachineTranslation) {
            this.omegatTextraMachineTranslation = omegatTextraMachineTranslation;
            return this;
        }

        public TextraOptionsBuilder setTextraOptionsFactory(TextraOptionsFactory textraOptionsFactory) {
            this.textraOptionsFactory = textraOptionsFactory;
            return this;
        }

        public TextraOptions build() {
            return new TextraOptions(
                    provider,
                    username,
                    apikey,
                    secret,
                    customId,
                    mode,
                    omegatTextraMachineTranslation,
                    textraOptionsFactory);
        }
    }
}
