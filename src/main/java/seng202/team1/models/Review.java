package seng202.team1.models;

/**
 * The review class that contains the attributes of a user review.
 * Used by {@link seng202.team1.repository.LogWineDao} to return a user's
 * review.
 * @Author Wen Sheng Thong
 */
public class Review {
    private int uid;
    private int wid;
    private int rating;
    private String reviewDescription;
    private String reviewDate;

    /**
     * Constructor for the review class.
     * @param uid The user id of the user that made the review.
     * @param wid The wine id of the wine that was reviewed.
     * @param rating The rating given by the user.
     * @param reviewDescription The description of the review.
     * @param reviewDate The date the review was made.
     */
    public Review(int uid, int wid, int rating, String reviewDescription, String reviewDate) {
        this.uid = uid;
        this.wid = wid;
        this.rating = rating;
        this.reviewDescription = reviewDescription;
        this.reviewDate = reviewDate;
    }

    /**
     * Getter for uid.
     * @return The user id of the user that made the review.
     */
    public int getUid() {
        return uid;
    }

    /**
     * Setter for uid.
     * @param uid The user id of the user that made the review.
     */
    public void setUid(int uid) {
        this.uid = uid;
    }

    /**
     * Getter for wid.
     * @return The wine id of the wine that was reviewed.
     */
    public int getWid() {
        return wid;
    }

    /**
     * Setter for wid.
     * @param wid The wine id of the wine that was reviewed.
     */
    public void setWid(int wid) {
        this.wid = wid;
    }

    /**
     * Getter for rating.
     * @return The rating given by the user.
     */
    public int getRating() {
        return rating;
    }

    /**
     * Setter for rating.
     * @param rating The rating given by the user.
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Getter for reviewDescription.
     * @return The description of the review.
     */
    public String getReviewDescription() {
        return reviewDescription;
    }

    /**
     * Setter for reviewDescription.
     * @param reviewDescription The description of the review.
     */
    public void setReviewDescription(String reviewDescription) {
        this.reviewDescription = reviewDescription;
    }

    /**
     * Getter for reviewDate.
     * @return The date the review was made.
     */
    public String getReviewDate() {
        return reviewDate;
    }

    /**
     * Setter for reviewDate.
     * @param reviewDate The date the review was made.
     */
    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    /**
     * Returns a string representation of the review.
     * @return A string representation of the review.
     */
    @Override
    public String toString() {
        return "User " + uid + " reviewed wine " + wid + " and rated it " + rating + " with description: {"
                + reviewDescription + "} at " + reviewDate;
    }
}
