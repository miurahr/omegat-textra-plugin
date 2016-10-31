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
    private TranslationMode mode;

    private Set<Combination> combination = new HashSet<>();

    {
        combination.add(new Combination(TranslationMode.GENERIC, "ja", "en"));
        combination.add(new Combination(TranslationMode.GENERIC, "en", "ja"));
        combination.add(new Combination(TranslationMode.GENERIC, "ka", "ja"));
        combination.add(new Combination(TranslationMode.GENERIC, "ja", "ka"));
        combination.add(new Combination(TranslationMode.GENERIC, "ka", "en"));
        combination.add(new Combination(TranslationMode.GENERIC, "en", "ka"));
        combination.add(new Combination(TranslationMode.GENERIC, "en", "zh-CN"));
        combination.add(new Combination(TranslationMode.GENERIC, "en", "zh-TW"));
        combination.add(new Combination(TranslationMode.GENERIC, "ja", "zh-CN"));
        combination.add(new Combination(TranslationMode.GENERIC, "ja", "zh-TW"));
        combination.add(new Combination(TranslationMode.GENERIC, "zh-CN", "en"));
        combination.add(new Combination(TranslationMode.GENERIC, "zh-TW", "en"));
        combination.add(new Combination(TranslationMode.GENERIC, "zh-CN", "ja"));
        combination.add(new Combination(TranslationMode.GENERIC, "zh-TW", "ja"));
        combination.add(new Combination(TranslationMode.ADDRESS, "ja", "en"));
        combination.add(new Combination(TranslationMode.ADDRESS, "en", "ja"));

        combination.add(new Combination(TranslationMode.JPO_CLAIM, "zh-CN", "ja"));
        combination.add(new Combination(TranslationMode.JPO_CLAIM, "zh-TW", "ja"));
        combination.add(new Combination(TranslationMode.JPO_CLAIM, "ja", "zh-CN"));
        combination.add(new Combination(TranslationMode.JPO_CLAIM, "ja", "zh-TW"));

        combination.add(new Combination(TranslationMode.JPO_NICT_CLAIM, "ja", "en"));
        combination.add(new Combination(TranslationMode.JPO_NICT_CLAIM, "en", "ja"));

        combination.add(new Combination(TranslationMode.JPO_NICT, "en", "ja"));
        combination.add(new Combination(TranslationMode.JPO_NICT, "ka", "ja"));
        combination.add(new Combination(TranslationMode.JPO_NICT, "ja", "en"));
        combination.add(new Combination(TranslationMode.JPO_NICT, "ja", "ka"));

        combination.add(new Combination(TranslationMode.JPO, "zh-CN", "ja"));
        combination.add(new Combination(TranslationMode.JPO, "zh-TW", "ja"));
        combination.add(new Combination(TranslationMode.JPO, "ja", "zh-CN"));
        combination.add(new Combination(TranslationMode.JPO, "ja", "zh-TW"));

        combination.add(new Combination(TranslationMode.MINNA, "ja", "en"));
        combination.add(new Combination(TranslationMode.MINNA, "en", "ja"));
        combination.add(new Combination(TranslationMode.MINNA, "ja", "ka"));
        combination.add(new Combination(TranslationMode.MINNA, "ka", "ja"));
        combination.add(new Combination(TranslationMode.MINNA, "ja", "zh-CN"));
        combination.add(new Combination(TranslationMode.MINNA, "ja", "zh-TW"));
        combination.add(new Combination(TranslationMode.MINNA, "zh-CN", "ja"));
        combination.add(new Combination(TranslationMode.MINNA, "zh-TW", "ja"));

        combination.add(new Combination(TranslationMode.PATENT, "zh-CN", "en"));
        combination.add(new Combination(TranslationMode.PATENT, "zh-TW", "en"));
        combination.add(new Combination(TranslationMode.PATENT, "en", "zh-CN"));
        combination.add(new Combination(TranslationMode.PATENT, "en", "zh-TW"));
    }

    private static class Combination {
        static TranslationMode mode;
        static String sLang;
        static String tLang;

        Combination (final TranslationMode mode, final String sLang, final String tLang) {
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
            String base = mode.name().toLowerCase() + "_" + sLang.toLowerCase() + "_" + tLang.toLowerCase();
            return base.hashCode();
        }
    }

    public boolean isCombinationValid(final String sLang, final String tLang) {
        return combination.contains(new Combination(mode, sLang, tLang));
    }

    /**
     * Translation mode.
     * These name is as same as an part of API URL.
     */
    public enum TranslationMode {
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
     * A part of URL string for the mode.
     * @return API mode string for target URL.
     */
    public String getModeString() {
        return mode.name().replace("_", "-").toLowerCase();
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
    public void setUsername(final String username) {
        this.username = username;
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
    public void setApikey(final String apikey) {
        this.apikey = apikey;
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
    public void setSecret(final String secret) {
        this.secret = secret;
    }

    /**
     * Getter of mode.
     * @return TranslateMode enum value.
     */
    public TranslationMode getMode() {
        return mode;
    }

    /**
     * Setter of mode.
     * @param mode TranslateMode.
     */
    public void setMode(final TranslationMode mode) {
        this.mode = mode;
    }
}
