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
        public Request request;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Result {
        public String text;
        public int blank;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Request {
        public String url;
        public String text;
        public String split;
        public String history;
        public String xml;
        public String term_id;
        public String bilingual_id;
        public String log_use;
        public String editor_use;
        public String data;
    }
}
