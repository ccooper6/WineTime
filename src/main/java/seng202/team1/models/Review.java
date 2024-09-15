package seng202.team1.models;

/**
 * The review class that contains the attributes of a user review.
 * Used by {@link seng202.team1.repository.LogWineDao} to return a user's
 * review.
 *
 * @Author Wen Sheng Thong
 */
public class Review {
    int uid;
    int wid;
    int rating;
    String reviewDescription;
    String reviewDate;

    public Review(int uid, int wid, int rating, String reviewDescription, String reviewDate) {
        this.uid = uid;
        this.wid = wid;
        this.rating = rating;
        this.reviewDescription = reviewDescription;
        this.reviewDate = reviewDate;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getWid() {
        return wid;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReviewDescription() {
        return reviewDescription;
    }

    public void setReviewDescription(String reviewDescription) {
        this.reviewDescription = reviewDescription;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    @Override
    public String toString() {
        return "User " + uid + " reviewed wine " + wid + " and rated it " + rating + " with description: {"
                + reviewDescription + "} at " + reviewDate;
    }
}
