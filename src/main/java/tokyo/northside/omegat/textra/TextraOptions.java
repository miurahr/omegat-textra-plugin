package tokyo.northside.omegat.textra;

import java.util.HashSet;
import java.util.Set;

/**
 * Data class for TexTra configuration Options.
 * Also have combination limitation knowledge.
 * @author Hiroshi Miura
 */
public class TextraOptions {
    private String username;
    private String apikey;
    private String secret;
    private Mode mode;
    private String sourceLang;
    private String targetLang;

    private Set<Combination> combination = new HashSet<>();

    {
        combination.add(new Combination(Mode.GENERIC, "ja", "en"));
        combination.add(new Combination(Mode.GENERIC, "en", "ja"));
        combination.add(new Combination(Mode.GENERIC, "ka", "ja"));
        combination.add(new Combination(Mode.GENERIC, "ja", "ka"));
        combination.add(new Combination(Mode.GENERIC, "ka", "en"));
        combination.add(new Combination(Mode.GENERIC, "en", "ka"));
        combination.add(new Combination(Mode.GENERIC, "en", "zh-CN"));
        combination.add(new Combination(Mode.GENERIC, "en", "zh-TW"));
        combination.add(new Combination(Mode.GENERIC, "ja", "zh-CN"));
        combination.add(new Combination(Mode.GENERIC, "ja", "zh-TW"));
        combination.add(new Combination(Mode.GENERIC, "zh-CN", "en"));
        combination.add(new Combination(Mode.GENERIC, "zh-TW", "en"));
        combination.add(new Combination(Mode.GENERIC, "zh-CN", "ja"));
        combination.add(new Combination(Mode.GENERIC, "zh-TW", "ja"));
        combination.add(new Combination(Mode.ADDRESS, "ja", "en"));
        combination.add(new Combination(Mode.ADDRESS, "en", "ja"));

        combination.add(new Combination(Mode.JPO_CLAIM, "zh-CN", "ja"));
        combination.add(new Combination(Mode.JPO_CLAIM, "zh-TW", "ja"));
        combination.add(new Combination(Mode.JPO_CLAIM, "ja", "zh-CN"));
        combination.add(new Combination(Mode.JPO_CLAIM, "ja", "zh-TW"));

        combination.add(new Combination(Mode.JPO_NICT_CLAIM, "ja", "en"));
        combination.add(new Combination(Mode.JPO_NICT_CLAIM, "en", "ja"));

        combination.add(new Combination(Mode.JPO_NICT, "en", "ja"));
        combination.add(new Combination(Mode.JPO_NICT, "ka", "ja"));
        combination.add(new Combination(Mode.JPO_NICT, "ja", "en"));
        combination.add(new Combination(Mode.JPO_NICT, "ja", "ka"));

        combination.add(new Combination(Mode.JPO, "zh-CN", "ja"));
        combination.add(new Combination(Mode.JPO, "zh-TW", "ja"));
        combination.add(new Combination(Mode.JPO, "ja", "zh-CN"));
        combination.add(new Combination(Mode.JPO, "ja", "zh-TW"));

        combination.add(new Combination(Mode.MINNA, "ja", "en"));
        combination.add(new Combination(Mode.MINNA, "en", "ja"));
        combination.add(new Combination(Mode.MINNA, "ja", "ka"));
        combination.add(new Combination(Mode.MINNA, "ka", "ja"));
        combination.add(new Combination(Mode.MINNA, "ja", "zh-CN"));
        combination.add(new Combination(Mode.MINNA, "ja", "zh-TW"));
        combination.add(new Combination(Mode.MINNA, "zh-CN", "ja"));
        combination.add(new Combination(Mode.MINNA, "zh-TW", "ja"));

        combination.add(new Combination(Mode.PATENT, "zh-CN", "en"));
        combination.add(new Combination(Mode.PATENT, "zh-TW", "en"));
        combination.add(new Combination(Mode.PATENT, "en", "zh-CN"));
        combination.add(new Combination(Mode.PATENT, "en", "zh-TW"));
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
        GENERIC,
        /** generic plus translation.
         * English/Japanese/Chinese/Korean
         */
        MINNA,
        /** address translation.
         * Japanese/English
         */
        ADDRESS,
        /** patent translation.
         * Chinese/English
         */
        PATENT,
        /** Japan patent office translation.
         * Chinese/Japanese
         */
        JPO,
        /** JPO claim translation.
         * Chinese/Japanese
         */
        JPO_CLAIM,
        /** JPO+NICT patent translation.
         * Japanese/English/Korean
         */
        JPO_NICT,
        /** JPO+NICT patent claim translation.
         * Japanese/English
         */
        JPO_NICT_CLAIM
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
     * @param key to set.
     * @return this object.
     */
    @SuppressWarnings("HiddenField")
    public TextraOptions setApikey(final String key) {
        this.apikey = key;
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
        return name != null && mode == Enum.valueOf(Mode.class, name);
    }

    /**
     * Set language options.
     * @param sLang source language.
     * @param tLang target language.
     * @return this object.
     */
    public TextraOptions setLang(final String sLang, final String tLang) {
        this.sourceLang = sLang;
        this.targetLang = tLang;
        return this;
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
