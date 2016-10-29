package tokyo.northside.omegat.textra;

public class TextraOptions {
    private String username;
    private String apikey;
    private String secret;
    private TranslationMode mode;

    public enum TranslationMode {
        GENERIC,
        MINNA, // GENERIC+
        ADDRESS,
        PATENT,
        JPO,
        JPO_CLAIM,
        JPO_NICT,
        JPO_NICT_CLAIM;
    }

	public String getModeString() {
	    return mode.name().replace("_", "-").toLowerCase();
	}

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(final String apikey) {
        this.apikey = apikey;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(final String secret) {
        this.secret = secret;
    }

    public TranslationMode getMode() {
        return mode;
    }

    public void setMode(TranslationMode mode) {
        this.mode = mode;
    }
}