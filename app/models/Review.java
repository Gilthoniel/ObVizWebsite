package models;

import java.io.Serializable;
import java.util.*;

import models.Opinion.OpinionDetail;

/**
 * Created by Gaylor on 24.06.15.
 * A comment drops by an user for an application or something else
 */
public class Review implements Serializable {

    private static final long serialVersionUID = -703878768818567597L;
    public ID _id;
    public String permalink;
    public String reviewBody;
    public String reviewTitle;
    public int starRatings;
    public Date reviewDate;
    public String authorName;
    public String authorUrl;
    public boolean parsed;
    public boolean isQuestionable;
    public List<Sentence> parsedBody;
    public List<Sentence> parsedTitle;
    public Opinion opinions;

    private String parsedBodyContent;
    private String parsedTitleContent;

    public String getID() {

        return _id.getValue();
    }

    public String getTitle() {

        if (parsedTitleContent != null) {

            return parsedTitleContent;
        }

        if (opinions != null && parsedTitle != null) {

            return (parsedTitleContent = parseContent(parsedTitle, true).toString());
        } else {
            return reviewTitle != null ? reviewTitle : "";
        }
    }

    public String getContent() {

        if (parsedBodyContent != null) {
            return parsedBodyContent;
        }

        if (opinions != null && parsedBody != null) {

            return (parsedBodyContent = parseContent(parsedBody, false).toString());

        } else {

            return reviewBody != null ? reviewBody : "";
        }
    }

    public int getDisplayType() {

        if (reviewBody != null && reviewBody.length() > 0 && reviewTitle != null && reviewTitle.length() > 0) {

            return 0;
        } else if (reviewBody != null && reviewBody.length() > 0) {

            return 1;
        } else {

            return 2;
        }
    }

    @Override
    public boolean equals(Object object) {

        return object.getClass() == Review.class && ((Review) object).getID().equals(_id.getValue());
    }

    @Override
    public int hashCode() {

        return getID().hashCode();
    }

    private StringBuilder parseContent(List<Sentence> sentences, boolean isInTitle) {

        // Sort the opinions by sentence and clause IDs
        Opinion.ParsedOpinion parsedOpinion = new Opinion.ParsedOpinion();
        parsedOpinion.put(opinions, isInTitle);

        // Build the body or the title sentence by sentence
        StringBuilder builder = new StringBuilder();
        for (Sentence sentence : sentences) {

            Iterator<Clause> it = sentence.getChildren().iterator();
            while (it.hasNext()) {
                Clause clause = it.next();

                if (clause.getType() == Clause.ClauseType.PARAGRAPH) {

                    if (it.hasNext()) {
                        builder.append("<br>");
                    }

                } else {

                    List<OpinionDetail> details = parsedOpinion.get(sentence.getID(), clause.getGroupID());
                    if (details.isEmpty()) {
                        // If there's no opinions, we check if there's a global for the entire sentence
                        details = parsedOpinion.get(sentence.getID(), 0);
                    }

                    if (details.size() > 0) {
                        String text = clause.getText();
                        for (OpinionDetail detail : details) {

                            if (detail.isGoodOpinion == null || detail.isGoodOpinion) {
                                for (String word : detail.getWords()) {
                                    text = text.replaceAll("(?i)\\b+(" + word + ")\\b+", "<strong>$1</strong>");
                                }
                            } else {
                                for (String word : detail.getWords()) {
                                    text = text.replaceAll("(?i)\\b+(" + word + ")\\b+", "<strong><span class='bad-opinion'>$1</span></strong>");
                                }
                            }
                        }

                        builder.append("<span class='clause clause-").append(details.get(0).polarity).append("'>");
                        builder.append(text);
                    } else {

                        builder.append("<span class='clause'>").append(clause.getText());
                    }

                    builder.append("</span>");
                }
            }
        }

        return builder;
    }

    public class ReviewContainer implements Serializable {

        private static final long serialVersionUID = -450393000052750910L;
        public List<Review> reviews;
        public int nbTotalPages;
    }
}
