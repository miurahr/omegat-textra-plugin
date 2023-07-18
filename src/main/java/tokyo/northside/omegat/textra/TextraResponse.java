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
        public Information information;
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Information {
        @JsonProperty("text-s")
        String sourceText;

        @JsonProperty("text-t")
        String targetText;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Sentence {
        @JsonProperty("item")
        SentenceItem item;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class SentenceItem {
        @JsonProperty("text-s")
        String sourceText;

        @JsonProperty("text-t")
        String targetText;
    }
}
