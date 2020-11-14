package tokyo.northside.omegat.textra;

import org.json.JSONArray;
import org.json.JSONObject;
import org.omegat.util.Language;
import tokyo.northside.omegat.textra.TextraOptions.Mode;
import tokyo.northside.omegat.textra.TextraOptions.Server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class TextraOptionCombinations {

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
        private Language sLang;
        private Language tLang;

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
            this.sLang = new Language(sLang);
            this.tLang = new Language(tLang);
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
            return other.service.equals(this.service) && other.mode.equals(this.mode) &&
                   leq(other.sLang, this.sLang) && leq(other.tLang, this.tLang);
        }

        private static boolean leq(Language langA, Language langB) {
            String a = TextraOptions.formatLang(langA);
            String b = TextraOptions.formatLang(langB);
            return a.equals(b);
        }

        /**
         * Return hashCode based on triplet strings.
         * @return hash code.
         */
        @Override
        public int hashCode() {
            return (service.name() + mode.name()
                    + sLang.getDisplayName() + tLang.getDisplayName()).hashCode();
        }
    }
}
