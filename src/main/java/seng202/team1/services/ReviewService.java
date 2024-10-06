package seng202.team1.services;

import org.jetbrains.annotations.NotNull;
import seng202.team1.models.Review;
import seng202.team1.models.Wine;
import seng202.team1.repository.DAOs.LogWineDao;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Contains methods for handling reviews. Used by the controllers to interact with the database.
 * @author Caleb Cooper
 */
public class ReviewService {
    private static final LogWineDao logWineDao = new LogWineDao();
    private static WineLoggingPopupService wineLoggingPopupService = new WineLoggingPopupService();
    private static Review currentReview;

    /**
     * Gets all the reviews for the current user.
     * @param currentUserUid The user id of the current user.
     * @return An ArrayList of all the reviews for the current user.
     */
    public ArrayList<Review> getUserReviews(int currentUserUid) {
        return logWineDao.getUserReview(currentUserUid, true);
    }

    /**
     * Checks if a review exists for the current user and wine.
     * @param currentUserUid The user id of the current user.
     * @param wineToCheck The wine id of the wine to check.
     * @return True if a review exists, false otherwise.
     */
    public boolean reviewExists(int currentUserUid, int wineToCheck) {
        return logWineDao.alreadyReviewExists(currentUserUid, wineToCheck);
    }

    /**
     * Sets the current review.
     * @param currentReview The review to set as the current review.
     */
    public void setCurrentReview(Review currentReview) {
        ReviewService.currentReview = currentReview;
    }

    /**
     * Gets the current review.
     * @return The current review.
     */
    public static Review getCurrentReview() {
        return currentReview;
    }

    /**
     * Gets the wine associated with the current review.
     * @return The wine associated with the current review.
     */
    public static Wine getCurrentWine() {
        return logWineDao.getWine(currentReview.getWid());
    }

    /**
     * Deletes the current review.
     * @param review The review to delete.
     */
    public void deleteReview(Review review) {
        int uid = review.getUid();
        int wid = review.getWid();
        ArrayList<String> tagsToRemove = review.getTagsLiked();
        int oldRating = review.getRating();

        updateTagLikes(uid, new ArrayList<>(), tagsToRemove, 0, oldRating);
        logWineDao.deleteReview(uid, wid);
    }

    /**
     * Uses {@link LogWineDao} to submit the liked tags and review to the database.
     * If no tags have been selected, it will add all the tags to the 'Likes' table. A rating of 1-2 will add a negative
     * value to the tag, whilst a 4-5 will add a positive value to the tag.
     * @param rating rating of the log
     * @param currentUserUid the user's int id
     * @param currentWine the wine's int id
     * @param selectedTags an ArrayList of strings, containing tag names
     * @param description the text description entered by the user
     * @param noneSelected a boolean value indicating if no tags have been selected
     */
    public void submitLog(int rating, int currentUserUid, int currentWine, @NotNull ArrayList<String> selectedTags, @NotNull ArrayList<String> tagsLiked, boolean noneSelected, String description) {
        if (!description.isBlank()) {
            String desc = description.replaceAll("\\s+", " ");
            logWineDao.reviews(currentUserUid, currentWine, rating, desc, getCurrentTimeStamp(), selectedTags, tagsLiked, noneSelected);
        } else {
            logWineDao.reviews(currentUserUid, currentWine, rating, "", getCurrentTimeStamp(), selectedTags, tagsLiked, noneSelected);
        }
    }

    /**
     * Obtain the date time stamp of the review in "YYYY-MM-DD HH:mm:SS" format.
     * @return the string date time stamp in "YYYY-MM-DD HH:mm:SS" format
     */
    public String getCurrentTimeStamp() {
        return ZonedDateTime.now( ZoneId.systemDefault() ).format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss"));
    }

    /**
     * Uses {@link LogWineDao} to get the review of a wine.
     * @param uid the user id
     * @param wid the wine id
     * @return the corresponding review object
     */
    public Review getReview(int uid, int wid) {
        return logWineDao.getReview(uid, wid);
    }

    /**
     * Uses {@link LogWineDao} to update the tags liked by the user.
     * @param uid the user id
     * @param tagsToAdd an ArrayList of strings, containing tag names to add
     * @param tagsToRemove an ArrayList of strings, containing tag names to remove
     * @param newRating the new rating of the log
     * @param oldRating the old rating of the log
     */
    public void updateTagLikes(int uid, ArrayList<String> tagsToAdd, ArrayList<String> tagsToRemove, int newRating, int oldRating) {
        int scaledNewRating = newRating - 3;
        int scaledOldRating = oldRating - 3;

        for (String tag : tagsToRemove) {
            logWineDao.likes(uid, tag, -scaledOldRating);
        }

        for (String tag : tagsToAdd) {
            logWineDao.likes(uid, tag, scaledNewRating);
        }

    }
}
