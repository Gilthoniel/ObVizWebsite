package webservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import models.Review;
import models.adapters.ReviewDeserializer;
import play.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

/**
 * Created by gaylor on 25.06.15.
 * Json parser
 */
public class MessageParser {

    public static MessageParser instance = new MessageParser();
    private static Gson gson;

    private MessageParser() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Review.class, new ReviewDeserializer());

        gson = builder.create();
    }

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

    public static <T> T fromJson(JsonElement json, Type type) {
        try {

            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            Logger.error("Parsing error : " + e.getMessage());
            return null;
        }
    }

    public static <T> T fromJson(InputStream stream, Type type) {
        try {

            return gson.fromJson(new InputStreamReader(stream), type);
        } catch (JsonSyntaxException e) {
            Logger.error("Parsing error : " + e.getMessage());
            return null;
        }
    }

    /**
     * Convert a Java object into a json format
     * @param object the object
     * @return String of the parsing
     */
    public static String toJson(Object object) {

        return gson.toJson(object);
    }

    /**
     * Convert a string into a integer if the string is correct
     * @param number The integer under String format
     * @return the number or -1
     */
    public static int parseInt(String number) {
        if (number != null && number.matches("^[\\-0-9]+$")) {
            return Integer.parseInt(number);
        } else {
            return -1;
        }
    }
}
