package seng202.team1.models;

import seng202.team1.repository.DAOs.LogWineDao;

import java.util.ArrayList;
import java.util.Objects;

/**
 * The review class that contains the attributes of a user review.
 * Used by {@link LogWineDao} to return a user's
 * review.
 */
public class Review {
    private int uid;
    private int wid;
    private int rating;
    private String description;
    private String date;
    private ArrayList<String> tagsSelected;
    private ArrayList<String> tagsLiked;

    /**
     * Constructor for the review class.
     * @param uid The user id of the user that made the review.
     * @param wid The wine id of the wine that was reviewed.
     * @param rating The rating given by the user.
     * @param reviewDescription The description of the review.
     * @param reviewDate The date the review was made.
     * @param tagsSelected The tags selected by the user.
     * @param tagsLiked The tags liked by the user.
     */
    public Review(int uid, int wid, int rating, String reviewDescription, String reviewDate, ArrayList<String> tagsSelected, ArrayList<String> tagsLiked) {
        this.uid = uid;
        this.wid = wid;
        this.rating = rating;
        this.description = reviewDescription;
        this.date = reviewDate;
        this.tagsSelected = Objects.requireNonNullElseGet(tagsSelected, ArrayList::new);
        this.tagsLiked = Objects.requireNonNullElseGet(tagsLiked, ArrayList::new);
    }

    /**
     * Getter for user id.
     * @return The user id of the user that made the review.
     */
    public int getUid() {
        return uid;
    }

    /**
     * Setter for user id.
     * @param uid The user id of the user that made the review.
     */
    public void setUid(int uid) {
        this.uid = uid;
    }

    /**
     * Getter for wine id.
     * @return The wine id of the wine that was reviewed.
     */
    public int getWid() {
        return wid;
    }

    /**
     * Setter for wine id.
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
    public String getDescription() {
        return description;
    }

    /**
     * Getter for reviewDate.
     * @return The date the review was made.
     */
    public String getDate() {
        return date;
    }

    /**
     * Getter for tagsSelected.
     * @return The tags selected by the user.
     */
    public ArrayList<String> getTagsSelected() {
        return tagsSelected;
    }

    /**
     * Returns the liked tags of the review
     *
     * @return An ArrayList<String> of tags
     */
    public ArrayList<String> getTagsLiked() {
        return tagsLiked;
    }
}
