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

    private static class Combination {
        static Mode mode;
        static String sLang;
        static String tLang;

        Combination (final Mode mode, final String sLang, final String tLang) {
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
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (!(o instanceof Combination)) {
                return false;
            }
            Combination other = (Combination) o;
            if (!other.mode.equals(this.mode)) {
                return false;
            }
            if (!other.sLang.toLowerCase().equals(this.sLang.toLowerCase())) {
                return false;
            }
            if (!other.tLang.toLowerCase().equals(this.tLang.toLowerCase())) {
                return false;
            }
            return true;
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
        JPO_NICT_CLAIM;
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
     */
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
     * @param apikey to set.
     */
    public TextraOptions setApikey(final String apikey) {
        this.apikey = apikey;
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
     */
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
     */
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
     */
    public TextraOptions setMode(final String name) {
        if (name != null) {
            mode = Enum.valueOf(Mode.class, name);
        }
        return this;
    }

    /**
     * Ask mode.
     * @return true if mode name equals.
     */
    public boolean isMode(final String name) {
        if (name == null) return false;
        return mode==Enum.valueOf(Mode.class, name);
    }

    public TextraOptions setLang(final String sLang, final String tLang) {
        this.sourceLang = sLang;
        this.targetLang = tLang;
        return this;
    }

    public String getSourceLang() {
        return sourceLang;
    }

    public void setSourceLang(String sLang) {
        this.sourceLang = sLang;
    }

    public String getTargetLang() {
        return targetLang;
    }

    public void setTargetLang(String tLang) {
        this.targetLang = tLang;
    }
}
