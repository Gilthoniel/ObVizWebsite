package models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gaylor on 02.07.15.
 * A sentence in a review
 */
public class Sentence implements Serializable {

    private static final long serialVersionUID = 6850252668426980459L;
    private List<Clause> children;
    private boolean isSpecialSentence;
    private boolean isHidden;
    private int id;

    public List<Clause> getChildren() {
        return children;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public int getID() {
            return id;
        }
}
