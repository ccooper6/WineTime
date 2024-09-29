package seng202.team1.services;

import seng202.team1.models.Review;
import seng202.team1.models.Wine;
import seng202.team1.repository.DAOs.LogWineDao;

import java.util.ArrayList;

/**
 * Contains methods for handling reviews. Used by the controllers to interact with the database.
 * @author Caleb Cooper
 */
public class ReviewService {
    private static final LogWineDao logWineDao = new LogWineDao();
    private static Review currentReview;

    /**
     * Gets all the reviews for the current user.
     * @param currentUserUid The user id of the current user.
     * @return An ArrayList of all the reviews for the current user.
     */
    public static ArrayList<Review> getUserReviews(int currentUserUid) {
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
     * @param rating The rating of the review.
     */
    public static void deleteReview(int rating) {
        int uid = currentReview.getUid();
        logWineDao.deleteReview(uid, currentReview.getWid());
        ArrayList<String> selectedTags = currentReview.getTagsSelected();
        if (selectedTags != null && !selectedTags.isEmpty()) {
            for (String tag : selectedTags) {
                logWineDao.likes(uid, tag, 3 - rating); // Reverse the like
            }
        }
    }
}
