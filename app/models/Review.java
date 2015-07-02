package models;

import play.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Gaylor on 24.06.15.
 * A comment drops by an user for an application or something else
 */
public class Review {

    private ID _id;
    private String permalink;
    private Date timestamp;
    private String reviewBody;
    private int starRatings;
    private Date reviewDate;
    private String authorName;
    private String authorUrl;
    private String reviewTitle;
    private List<Sentence> parsed;
    private List<Opinion> opinions;

    @Override
    public boolean equals(Object object) {

        return object.getClass() == Review.class && ((Review) object).getID().equals(_id.getValue());
    }

    @Override
    public int hashCode() {

        return getID().hashCode();
    }

    public String getID() {
        return _id.getValue();
    }

    public List<Opinion> getOpinions() {
        return opinions;
    }

    public String getReviewBody() {
        return reviewBody;
    }

    public String getParsedBody(int topicID) {
        Opinion opinion = null;
        for (Opinion op : opinions) {
            if (op.getTopicID() == topicID) {
                opinion = op;
            }
        }

        if (opinion != null) {

            Map<Integer, List<Opinion.OpinionChild>> children = new TreeMap<>();
            for (Opinion.OpinionChild child : opinion.getChildren()) {
                if (!children.containsKey(child.getSentenceID())) {
                    children.put(child.getSentenceID(), new ArrayList<>());
                }

                children.get(child.getSentenceID()).add(child);
            }

            StringBuilder builder = new StringBuilder();
            for (Sentence sentence : parsed) {
                boolean hasOpinions = children.containsKey(sentence.getID());

                for (Clause clause : sentence.getClauses()) {

                    if (clause.getType() == Clause.ClauseType.PARAGRAPH) {

                        builder.append("<br />");
                    } else {

                        if (hasOpinions) {
                            String polarity = "neutral";
                            String text = clause.getText();
                            for (Opinion.OpinionChild child : children.get(sentence.getID())) {
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
}
