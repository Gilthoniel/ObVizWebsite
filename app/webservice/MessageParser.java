package webservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;
import models.AndroidApp;
import models.Review;
import models.adapters.AndroidAppDeserializer;
import models.adapters.ReviewDeserializer;
import play.Logger;
import service.TopicsManager;

import javax.inject.Singleton;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

/**
 * Created by gaylor on 25.06.15.
 * Json parser
 */
@Singleton
public class MessageParser {

    private Gson gson;

    @Inject
    private MessageParser(AndroidAppDeserializer android) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Review.class, new ReviewDeserializer(this));
        builder.registerTypeAdapter(AndroidApp.class, android);

        gson = builder.create();
    }

    /**
     * Convert a string json content into a Java Class
     * @param json the content
     * @param type Java reflect type
     * @param <T> type of the return
     * @return the instance of the class
     */
    public <T> T fromJson(String json, Type type) {
        try {
            return gson.fromJson(json, type);

        } catch (JsonSyntaxException e) {
            Logger.error("Parsing error : " + e.getMessage());
            return null;
        }
    }

    public <T> T fromJson(JsonElement json, Type type) {
        try {

            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            Logger.error("Parsing error : " + e.getMessage());
            return null;
        }
    }

    public <T> T fromJson(InputStream stream, Type type) {
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
    public String toJson(Object object) {

        return gson.toJson(object);
    }
}
