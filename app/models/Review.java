package models;

import java.util.*;

/**
 * Created by Gaylor on 24.06.15.
 * A comment drops by an user for an application or something else
 */
public class Review {

    private ID _id;
    private String permalink;
    private String reviewBody;
    private int starRatings;
    private Date reviewDate;
    private String authorName;
    private String authorUrl;
    private String reviewTitle;
    private List<Sentence> parsed;
    private List<Sentence> parsedBody;
    private List<Sentence> parsedTitle;
    private Map<Integer, List<Opinion>> opinions;

    public Review() {

    }

    public String getID() {
        return _id.getValue();
    }

    public void setID(ID value) {
        _id = value;
    }

    public String getUrl() {
        return permalink;
    }

    public void setPermalink(String value) {
        permalink = value;
    }

    public String getReviewBody() {
        return reviewBody;
    }

    public void setReviewBody(String value) {
        reviewBody = value;
    }

    public String getTitle() {
        return reviewTitle;
    }

    public void setTitle(String value) {
        reviewTitle = value;
    }

    public int getScore() {
        return starRatings;
    }

    public void setScore(int value) {
        starRatings = value;
    }

    public String getDate() {
        return reviewDate.toString();
    }

    public void setDate(Date value) {
        reviewDate = value;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String value) {
        authorName = value;
    }

    public Map<Integer, List<Opinion>> getOpinions() {
        return opinions;
    }

    public void setOpinions(Map<Integer, List<Opinion>> value) {
        opinions = value;
    }

    public void setParsed(List<Sentence> value) {
        parsed = value;
    }

    public void setParsedBody(List<Sentence> value) {
        parsedBody = value;
    }

    public void setParsedTitle(List<Sentence> value) {
        parsedTitle = value;
    }

    public String getBody(int topicID) {

        List<Opinion> opinions = this.opinions.get(topicID);
        if (opinions != null) {

            Map<Integer, List<Opinion>> children = new TreeMap<>();
            for (Opinion child : opinions) {
                if (!children.containsKey(child.getSentenceID())) {
                    children.put(child.getSentenceID(), new ArrayList<>());
                }

                children.get(child.getSentenceID()).add(child);
            }

            StringBuilder builder = new StringBuilder();
            for (Sentence sentence : parsed != null ? parsed : parsedBody) {
                boolean hasOpinions = children.containsKey(sentence.getID());

                for (Clause clause : sentence.getChildren()) {

                    if (clause.getType() == Clause.ClauseType.PARAGRAPH) {

                        builder.append("<br />");
                    } else {

                        if (hasOpinions) {
                            String polarity = "neutral";
                            String text = clause.getText();
                            for (Opinion child : children.get(sentence.getID())) {
                                if (child.getAspectID() == clause.getID() || child.getPolarityID() == clause.getID()) {
                                    polarity = child.getPolarity().toString().toLowerCase();
                                    text = text.replaceAll("("+child.getAspect()+"|"+child.getWord()+")", "<strong>$1</strong>");
                                }
                            }

                            builder.append("<span class='clause clause-").append(polarity).append("'>");
                            builder.append(text).append("</span>");
                        } else {
                            builder.append(clause.getText()).append("<span class='clause'>");
                        }

                        builder.append("</span>");
                    }
                }
            }

            return builder.toString();

        } else {
            return getReviewBody();
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
}
