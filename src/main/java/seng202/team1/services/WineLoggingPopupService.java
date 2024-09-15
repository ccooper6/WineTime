package seng202.team1.services;

import org.jetbrains.annotations.NotNull;
import seng202.team1.models.User;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.repository.LogWineDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Runs the logic for {@link seng202.team1.gui.WineLoggingPopupController} such as interacting with
 * the database and processing the data from the gui interface of the wine logging popup.
 *
 * @author Wen Sheng Thong
 */
public class WineLoggingPopupService {
    private DatabaseManager databaseManager;
    private LogWineDao logWineDao;
    public WineLoggingPopupService() {
        this.databaseManager = DatabaseManager.getInstance();
        this.logWineDao = new LogWineDao();
    }

    /**
     * Uses {@link LogWineDao} to submit the liked tags and review to the database.
     * <p></p>
     * If no tags have been selected, it will add all the tags to the 'Likes' table. A rating of 1-2 will add a negative
     * value to the tag, whilst a 4-5 will add a positive value to the tag.
     * @param rating rating of the log
     * @param currentUserUid the user's int id
     * @param currentWine the wine's int id
     * @param selectedTags an ArrayList<String> of tag names
     * @param description the text description entered by the user
     */
    public void submitLog(int rating, int currentUserUid, int currentWine, @NotNull ArrayList<String> selectedTags, String description) {
        for (String tag : selectedTags) {
            logWineDao.likes(currentUserUid, tag, rating - 3);
        }
        if (!description.isBlank()) {
            String desc = description.replaceAll("\\s+", " ");
            logWineDao.reviews(currentUserUid, currentWine, rating, desc, getCurrentTimeStamp());
        } else {
            logWineDao.reviews(currentUserUid, currentWine, rating, "", getCurrentTimeStamp());
        }
    }

    /**
     * Called by {@link WineLoggingPopupService#submitLog(int, int, int, ArrayList, String)}
     * to obtain the date time stamp of the review in "YYYY-MM-DD HH:mm:SS" format
     * @return the string date time stamp in "YYYY-MM-DD HH:mm:SS" format
     */
    public String getCurrentTimeStamp() {
        return ZonedDateTime.now( ZoneId.systemDefault() ).format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss"));
    }

    /**
     * Returns the int user id of the current user. Called during initialization of
     * {@link seng202.team1.gui.WineLoggingPopupController}
     * @param currentUser the current user
     * @return int uid
     */
    public int getUId(User currentUser) {
        int uid = 0;
        String uidSql = "SELECT id FROM user WHERE username = ? AND name = ?";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement uidPs = conn.prepareStatement(uidSql)) {
                uidPs.setString(1, currentUser.getEncryptedUserName());
                uidPs.setString(2, currentUser.getName());
                uid = uidPs.executeQuery().getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return uid;
    }
}
