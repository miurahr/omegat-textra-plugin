package tokyo.northside.omegat.textra;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tokyo.northside.omegat.textra.TextraOptions.Mode;
import tokyo.northside.omegat.textra.TextraOptions.Server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class TextraOptionCombinations {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(TextraOptionCombinations.class);
    private Set<Combination> combination;

    public TextraOptionCombinations() {
        combination = new HashSet<>();
        InputStream is = getClass().getClassLoader().getResourceAsStream("combinations.json");
        if (is != null) {
            JSONObject jsonObj = new JSONObject(new BufferedReader(new InputStreamReader(is))
                    .lines().collect(Collectors.joining(" ")));
            for (Server server : Arrays.asList(Server.nict, Server.minna_personal)) {
                JSONArray jsonArray = jsonObj.getJSONArray(server.name());
                int bound = jsonArray.length();
                IntStream.range(0, bound).mapToObj(jsonArray::getJSONArray).forEach(record -> {
                    Mode mode = Mode.valueOf(record.getString(0));
                    String sourceLang = record.getString(1);
                    String targetLang = record.getString(2);
                    this.combination.add(new Combination(server, mode, sourceLang, targetLang));
                });
            }
        }
    }

    /**
     * Check if parameter combination is valid or not.
     * @return true is combination is valid, otherwise false.
     */
    public boolean isCombinationValid(Server service, Mode mode,
                                      String sourceLang, String targetLang) {
        return combination.contains(new Combination(service, mode, sourceLang, targetLang));
    }

    /**
     * Class for check mode, language combination.
     */
    private static class Combination {
        private Server service;
        private TextraOptions.Mode mode;
        private String sLang;
        private String tLang;

        /**
         * Constructor.
         * @param mode mode.
         * @param sLang source language.
         * @param tLang target language.
         */
        Combination(final Server service, final TextraOptions.Mode mode,
                    final String sLang, final String tLang) {
            this.service = service;
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
            return other.service.equals(this.service) &&
                    other.mode.equals(this.mode) && other.sLang.toLowerCase().equals(this.sLang
                    .toLowerCase()) && other.tLang.toLowerCase().equals(this.tLang.toLowerCase());
        }

        /**
         * Return hashCode based on triplet strings.
         * @return hash code.
         */
        @Override
        public int hashCode() {
            return (service.name() + mode.name()
                    + sLang.toLowerCase() + tLang.toLowerCase()).hashCode();
        }
    }
}
