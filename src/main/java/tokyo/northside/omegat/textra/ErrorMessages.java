package tokyo.northside.omegat.textra;

import java.util.HashMap;
import java.util.Map;

public final class ErrorMessages {
    public static final Map<Integer, String> messages = new HashMap<>();

    static {
        messages.put(500, "API key error");
        messages.put(501, "name error");
        messages.put(502, "Request limit error(daily)");
        messages.put(504, "Request limit error(minutes)");
        messages.put(505, "Request limit error(concurrent)");
        messages.put(510, "OAuth authentication error");
        messages.put(511, "OAuth header error");
        messages.put(520, "Access URL error");
        messages.put(521, "Access URL error");
        messages.put(522, "Request key error");
        messages.put(523, "Request name error");
        messages.put(524, "Request parameter error");
        messages.put(525, "Request parameter error(maximum size exceeded)");
        messages.put(530, "Authorization error");
        messages.put(531, "Execution error");
        messages.put(533, "No data");
    }
}
