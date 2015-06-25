package models;

/**
 * Created by Gaylor on 24.06.15.
 * A comment drops by an user for an application or something else
 */
public class Review {

    private String documentVersion;
    private long timestampMsec;
    private double starRating;
    private String title;
    private String comment;
    private String commentId;

    public String getDocumentVersion() {
        return documentVersion;
    }

    public void setDocumentVersion(String documentVersion) {
        this.documentVersion = documentVersion;
    }

    public long getTimestampMsec() {
        return timestampMsec;
    }

    public void setTimestampMsec(long timestampMsec) {
        this.timestampMsec = timestampMsec;
    }

    public double getStarRating() {
        return starRating;
    }

    public void setStarRating(double starRating) {
        this.starRating = starRating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }
}
