package webservice;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import play.Logger;

import java.lang.reflect.Type;

/**
 * Created by gaylor on 25.06.15.
 * Json parser
 */
public class MessageParser {

    private static Gson gson = new Gson();

    /**
     * Convert a string json content into a Java Class
     * @param json the content
     * @param type Java reflect type
     * @param <T> type of the return
     * @return the instance of the class
     */
    public static <T> T fromJson(String json, Type type) {
        try {
            return gson.fromJson(json, type);

        } catch (JsonSyntaxException e) {
            Logger.error("Parsing error : " + e.getMessage());
            return null;
        }
    }

    /**
     * Convert a string into a integer if the string is correct
     * @param number The integer under String format
     * @return the number or -1
     */
    public static int parseInt(String number) {
        if (number.matches("^[\\-0-9]+$")) {
            return Integer.parseInt(number);
        } else {
            return -1;
        }
    }
}
