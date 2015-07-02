package models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by gaylor on 02.07.15.
 * Opinion in a clause
 */
public class Opinion {

    public enum Polarity {
        @SerializedName("negative")
        NEGATIVE,
        @SerializedName("positive")
        POSITIVE
    }

    private int topicID;
    private List<OpinionChild> opinions;

    public int getTopicID() {
        return topicID;
    }

    public List<OpinionChild> getChildren() {
        return opinions;
    }

    public class OpinionChild {

        private int id;
        private Polarity polarity;
        private boolean negation;
        private String polarityWord;
        private String aspect;
        private int polWordPosition;
        private int aspectPosition;
        private String phrase;
        private int sentenceId;
        private int polarityWordClauseId;
        private int aspectClauseId;

        public Polarity getPolarity() {
            return polarity;
        }

        public int getAspectID() {
            return aspectClauseId;
        }

        public int getPolarityID() {
            return polarityWordClauseId;
        }

        public int getSentenceID() {
            return sentenceId;
        }

        public String getWord() {
            return polarityWord;
        }

        public String getAspect() {
            return aspect;
        }
    }
}
