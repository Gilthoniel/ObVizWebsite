package models.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.inject.Inject;
import models.AndroidApp;
import service.TopicsManager;
import webservice.MessageParser;

import javax.inject.Provider;
import java.lang.reflect.Type;

/**
 * Created by gaylor on 09/09/2015.
 * Json deserializer for AndroidApp
 */
public class AndroidAppDeserializer implements JsonDeserializer<AndroidApp> {

    @Inject
    private Provider<TopicsManager> topics;
    @Inject Provider<MessageParser> parser;

    @Override
    public AndroidApp deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {

        return new AndroidApp(json.getAsJsonObject(), parser.get(), topics.get());
    }
}
