package models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by gaylor on 02.07.15.
 * Opinion in a clause
 */
public class Opinion implements Serializable {

    private static final long serialVersionUID = -4273595504109641382L;

    public enum Polarity {
        @SerializedName("negative")
        NEGATIVE,
        @SerializedName("positive")
        POSITIVE
    }

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
