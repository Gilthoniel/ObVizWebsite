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
        throws JsonParseException {

        JsonObject obj = json.getAsJsonObject();
        Review review = new Review();
        review._id = MessageParser.fromJson(obj.get("_id"), ID.class);
        review.permalink = obj.get("permalink").getAsString();
        review.reviewBody = obj.get("reviewBody").getAsString();
        review.starRatings = obj.get("starRatings").getAsInt();
        review.reviewDate = MessageParser.fromJson(obj.get("reviewDate"), Date.class);
        review.authorName = obj.get("authorName").getAsString();
        review.authorUrl = obj.get("authorUrl") != null ? obj.get("authorUrl").getAsString() : "";
        review.reviewTitle = obj.get("reviewTitle").getAsString();

        review.isQuestionable = obj.get("isQuestionable") != null && obj.get("isQuestionable").getAsBoolean();

        try {
            review.parsed = obj.get("parsed") != null && obj.get("parsed").getAsBoolean();
        } catch (Exception e) {
            review.parsed = false;
        }

        if (review.parsed) {

            review.parsedBody = parseSentences(obj.getAsJsonArray("parsedBody"));
            review.parsedTitle = parseSentences(obj.getAsJsonArray("parsedTitle"));

            JsonArray opinions = obj.getAsJsonArray("opinions");
            if (opinions != null && opinions.size() > 0) {
                review.opinions = MessageParser.fromJson(opinions.get(0), Opinion.class);
            } else {


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
