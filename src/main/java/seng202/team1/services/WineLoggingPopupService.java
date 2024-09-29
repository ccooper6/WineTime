package seng202.team1.services;

import org.jetbrains.annotations.NotNull;
import seng202.team1.gui.controllers.WineLoggingPopupController;
import seng202.team1.models.Review;
import seng202.team1.repository.DAOs.LogWineDao;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Runs the logic for {@link WineLoggingPopupController} such as interacting with
 * the database and processing the data from the gui interface of the wine logging popup.
 *
 * @author Wen Sheng Thong, Caleb Cooper
 */
public class WineLoggingPopupService {
    private final LogWineDao logWineDao;

    /**
     * Constructor for WineLoggingPopupService.
     */
    public WineLoggingPopupService() {
        this.logWineDao = new LogWineDao();
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
    public void submitLog(int rating, int currentUserUid, int currentWine, @NotNull ArrayList<String> selectedTags, boolean noneSelected, String description) {
        if (!description.isBlank()) {
            String desc = description.replaceAll("\\s+", " ");
            logWineDao.reviews(currentUserUid, currentWine, rating, desc, getCurrentTimeStamp(), selectedTags, noneSelected);
        } else {
            logWineDao.reviews(currentUserUid, currentWine, rating, "", getCurrentTimeStamp(), selectedTags, noneSelected);
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
     * @param tag the tag name
     * @param definedRating the rating of the tag
     */
    public void updateTagLikes(int uid, String tag, int definedRating) {
        logWineDao.likes(uid, tag, definedRating);
    }

    /**
     * Uses {@link LogWineDao} to update the tags liked by the user.
     * @param uid the user id
     * @param tagsToAdd an ArrayList of strings, containing tag names to add
     * @param tagsToRemove an ArrayList of strings, containing tag names to remove
     * @param existingTags an ArrayList of strings, containing existing tag names
     * @param newRating the new rating of the log
     * @param oldRating the old rating of the log
     */
    public void updateTagLikes(int uid, ArrayList<String> tagsToAdd, ArrayList<String> tagsToRemove, ArrayList<String> existingTags, int newRating, int oldRating) {
        int ratingDifference = newRating - oldRating;

        for (String tag : tagsToAdd) {
            updateTagLikes(uid, tag, newRating - 3); // Add the like
        }
        for (String tag : tagsToRemove) {
            updateTagLikes(uid, tag, 3 - oldRating); // Reverse the like
        }
        for (String tag : existingTags) {
            if (!tagsToRemove.contains(tag)) {
                updateTagLikes(uid, tag, ratingDifference); // Update the like
            }
        }
    }
}
