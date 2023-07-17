package tokyo.northside.omegat.textra;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Schema class of Textra API response data.
 */
class TextraResponse {
    @JsonProperty("resultset")
    public ResultSet resultset;

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class ResultSet {
        public Result result;
        public int code;
        public String message;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Result {
        public String text;
        public int blank;
    }
}
