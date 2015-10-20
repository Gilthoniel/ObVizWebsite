package models.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import models.admin.Report;

import java.lang.reflect.Type;

/**
 * Created by gaylor on 10/13/2015.
 * Deserializer for the Reports in the admin panel
 */
public class ReportDeserializer implements JsonDeserializer<Report> {
    @Override
    public Report deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        return new Report(json.getAsJsonObject());
    }
}
