package seng202.team1.services;

import seng202.team1.models.Review;
import seng202.team1.repository.DAOs.LogWineDao;

import java.util.ArrayList;

public class LogsService {
    private static LogWineDao logWineDao = new LogWineDao();
    private Review currentReview;

    public static ArrayList<Review> getUserLogs(int currentUserUid) {
        return logWineDao.getUserReview(currentUserUid, true);
    }

    public boolean logExists(int currentUserUid, int wineToCheck) {
        return logWineDao.logExists(currentUserUid, wineToCheck);
    }

    public void setCurrentReview(Review currentReview) {
        this.currentReview = currentReview;
    }

    public Review getCurrentReview() {
        return currentReview;
    }
}
