package models;

import java.io.Serializable;

/**
 * Created by gaylor on 02.07.15.
 * Clause in a sentence
 */
public class Clause implements Serializable {

    private static final long serialVersionUID = 3481812850343249114L;

    public enum ClauseType { CLAUSE, CONNECTOR, PUNCTUATION, PARAGRAPH }

    private String text;
    private int id;
    private int groupId;
    private ClauseType type;
    private boolean isHidden;

    public int getID() {
        return id;
    }

    public String getText() {
        return text;
    }

    public ClauseType getType() {
        return type;
    }
}
