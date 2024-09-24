package seng202.team1.services;

import seng202.team1.models.Review;
import seng202.team1.models.Wine;
import seng202.team1.repository.DAOs.LogWineDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class LogsService {
    private static final LogWineDao logWineDao = new LogWineDao();
    private static Review currentReview;

    public static ArrayList<Review> getUserLogs(int currentUserUid) {
        return logWineDao.getUserReview(currentUserUid, true);
    }

    public boolean logExists(int currentUserUid, int wineToCheck) {
        return logWineDao.logExists(currentUserUid, wineToCheck);
    }

    public void setCurrentReview(Review currentReview) {
        LogsService.currentReview = currentReview;
    }

    public static Review getCurrentReview() {
        return currentReview;
    }

    public static Wine getCurrentWine() {
        return logWineDao.getWine(currentReview.getWid());
    }

    public static ArrayList<String> getSelectedTags() {
        String tags = logWineDao.getSelectedTags(currentReview.getUid(), currentReview.getWid());
        ArrayList<String> selectedTags = new ArrayList<>();
        if (tags != null) {
            String[] tagArray = tags.split(",");
            selectedTags.addAll(Arrays.asList(tagArray));
        }
        return selectedTags;
    }
}
