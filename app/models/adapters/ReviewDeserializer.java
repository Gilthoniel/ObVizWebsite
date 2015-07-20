package models.adapters;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import models.*;
import webservice.MessageParser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by gaylor on 20.07.15.
 * Create to avoid writing some useless models for the deserialization
 */
public class ReviewDeserializer implements JsonDeserializer<Review> {

    @Override
    public Review deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException
    {

        JsonObject obj = json.getAsJsonObject();
        Review review = new Review();
        review.setID(MessageParser.fromJson(obj.get("_id"), ID.class));
        review.setPermalink(obj.get("permalink").getAsString());
        review.setReviewBody(obj.get("reviewBody").getAsString());
        review.setScore(obj.get("starRatings").getAsInt());
        review.setDate(MessageParser.fromJson(obj.get("reviewDate"), Date.class));
        review.setAuthorName(obj.get("authorName").getAsString());
        review.setTitle(obj.get("reviewTitle").getAsString());

        if (obj.get("opinions") != null) {
            Map<Integer, List<Opinion>> opinions = new TreeMap<>();
            Type type = new TypeToken<List<Opinion>>(){}.getType();

            for (JsonElement jsonElement : obj.get("opinions").getAsJsonArray()) {
                JsonObject item = jsonElement.getAsJsonObject();

                opinions.put(item.get("topicID").getAsInt(), MessageParser.fromJson(item.get("opinions"), type));
            }

            review.setOpinions(opinions);
        }

        JsonElement parsed = obj.get("parsed");
        if (parsed != null) {

            if (parsed.isJsonPrimitive() && parsed.getAsBoolean()) {

                JsonArray body = obj.get("parsedBody").getAsJsonArray();
                review.setParsedBody(parseSentences(body));

                JsonArray title = obj.get("parsedTitle").getAsJsonArray();
                review.setParsedTitle(parseSentences(title));

            } else {

                review.setParsed(parseSentences(parsed.getAsJsonArray()));
            }
        }

        return review;
    }

    private List<Sentence> parseSentences(JsonArray array) {
        List<Sentence> sentences = new ArrayList<>();

        for (JsonElement element : array) {
            JsonObject item = element.getAsJsonObject();

            sentences.add(MessageParser.fromJson(item.get("sentenceClauses"), Sentence.class));
        }

        return sentences;
    }
}
