package tokyo.northside.omegat.textra;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.omegat.util.Language;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class TextraOptionCombinations {

    private Set<Combination> combination = new HashSet<>();

    public TextraOptionCombinations() throws IOException {
        InputStream is = TextraOptionCombinations.class.getResourceAsStream("combinations.json");
        ObjectMapper mapper = new ObjectMapper();
        String json = new BufferedReader(new InputStreamReader(is)).lines()
                .collect(Collectors.joining());
        for (Definition definition : mapper.readValue(json, Definition[].class)) {
            TextraOptions.Provider provider = definition.getProvider();
            for (Service record : definition.getServices()) {
                TextraOptions.Mode mode = TextraOptions.Mode.valueOf(record.getMode());
                String sourceLang = record.getSource();
                String targetLang = record.getTarget();
                combination.add(new Combination(provider, mode, sourceLang, targetLang));
            }
        }
    }


    /**
     * Check if parameter combination is valid or not.
     * @return true is combination is valid, otherwise false.
     */
    public boolean isCombinationValid(TextraOptions.Provider service, TextraOptions.Mode mode,
                                      String sourceLang, String targetLang) {
        return combination.contains(new Combination(service, mode, sourceLang, targetLang));
    }

    /**
     * Class for check mode, language combination.
     */
    private static class Combination {
        private TextraOptions.Provider provider;
        private TextraOptions.Mode mode;
        private Language sLang;
        private Language tLang;

        /**
         * Constructor.
         * @param mode mode.
         * @param sLang source language.
         * @param tLang target language.
         */
        Combination(final TextraOptions.Provider provider, final TextraOptions.Mode mode,
                    final String sLang, final String tLang) {
            this.provider = provider;
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
            return other.provider.equals(this.provider) && other.mode.equals(this.mode) &&
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
            return (provider.name() + mode.name()
                    + sLang.getDisplayName() + tLang.getDisplayName()).hashCode();
        }
    }

    public static class Service {
        private String mode;
        private String source;
        private String target;

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        @Override
        public String toString() {
            return "Service{" +
                    "mode='" + mode + '\'' +
                    ", source='" + source + '\'' +
                    ", target='" + target + '\'' +
                    '}';
        }
    }

    public static class Definition {
        private TextraOptions.Provider provider;
        private List<Service> services;

        public TextraOptions.Provider getProvider() {
            return provider;
        }

        public void setProvider(TextraOptions.Provider provider) {
            this.provider = provider;
        }

        public List<Service> getServices() {
            return services;
        }

        public void setServices(List<Service> services) {
            this.services = services;
        }

        @Override
        public String toString() {
            return "Definition{" +
                    "provider=" + provider +
                    ", services=" + services +
                    '}';
        }
    }
}
