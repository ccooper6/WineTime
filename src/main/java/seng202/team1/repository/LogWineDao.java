package seng202.team1.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.models.Review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The class containing the functions to add entries to the "Likes" and "Reviews" table
 * Mainly called by the WineLoggingPopupController when the user logs a wine.
 *
 * @author Wen Sheng Thong
 */
public class LogWineDao {
    /**
     * An instance of the databaseManager to setup the connection to the database
     */
    private final DatabaseManager databaseManager = DatabaseManager.getInstance();
    /**
     * A logger to handle the logging of any errors
     */
    private static final Logger log = LogManager.getLogger(LogWineDao.class);

    /**
     * Calls {@link LogWineDao#alreadyLikeExists(int, String)} to see if the user has already liked the tag. If so
     * calls {@link LogWineDao#updateLikesValue(int, String, int)} to update the liked tag's value. Else if it doesn't
     * exist, add a new entry to the 'LIKES' table
     * @param uid the UID of the current user
     * @param tagName the string tag name of the added tag
     * @param value the value which determines the ranking of the users liked tags
     */
    public void likes(int uid, String tagName, int value) {
        if (alreadyLikeExists(uid, tagName)) {
            updateLikesValue(uid, tagName, value);
        } else {
            String likesSql = "INSERT INTO likes (uid, tname, value) VALUES (?,?,?)";
            try (Connection conn = databaseManager.connect()) {
                try (PreparedStatement likesPs = conn.prepareStatement(likesSql)) {
                    likesPs.setInt(1, uid);
                    likesPs.setString(2, tagName);
                    likesPs.setInt(3, value);
                    likesPs.executeUpdate();
                }
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * Updates the like value of the current relationship between the uid and tagName by adding the argument value onto
     * the already existing value.
     * @param uid user id
     * @param tagName tag name
     * @param value value to be added to prev value
     */
    public void updateLikesValue(int uid, String tagName, int value) {
        int prevValue;
        String getPrevValueSql = "SELECT value FROM likes WHERE uid = ? AND tname = ?";
        String updateValueSql = "UPDATE likes SET value = ? WHERE uid = ? AND tname = ?";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement getPrevValuePs = conn.prepareStatement(getPrevValueSql)) {
                getPrevValuePs.setInt(1, uid);
                getPrevValuePs.setString(2, tagName);
                ResultSet rs = getPrevValuePs.executeQuery();
                prevValue = rs.getInt(1);
            }
            try (PreparedStatement updateValuePs = conn.prepareStatement(updateValueSql)) {
                updateValuePs.setInt(1, prevValue + value);
                updateValuePs.setInt(2, uid);
                updateValuePs.setString(3, tagName);
                updateValuePs.executeUpdate();
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

    }

    /**
     * Returns a boolean indicating if the user is already in a 'like' relationship with the specified tag
     * @param uid the user id
     * @param tagName the tag name
     * @return Boolean
     */
    public Boolean alreadyLikeExists(int uid, String tagName) {
        String test = "SELECT * FROM likes WHERE uid = ? AND tname = ?";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement ps = conn.prepareStatement(test)) {
                ps.setInt(1, uid);
                ps.setString(2, tagName);
                ResultSet rs = ps.executeQuery();
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a hashmap of <String tagName, int tagValue> of the likedTags by the user.
     * @param uid the current user int id
     * @param maximumTag the maximum number of tags to return
     * @param orderByValue set to true to return the highest valued tags
     * @return
     */
    public HashMap<String, Integer> getLikedTags(int uid, int maximumTag, boolean orderByValue) {
        HashMap<String, Integer> likedTags = new HashMap<>();
        String likePs = "SELECT tname, value FROM likes WHERE uid = ?";
        if (orderByValue) {
            likePs = "SELECT tname, value FROM likes WHERE uid = ? ORDER BY value DESC";
        }
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement ps = conn.prepareStatement(likePs)) {
                ps.setInt(1, uid);
                ResultSet rs = ps.executeQuery();
                int i = 0;
                while (i < maximumTag && rs.next()) {
                    likedTags.put(rs.getString(1), rs.getInt(2));
                    i++;
                }
                return likedTags;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Returns a hashmap of <String tagName, int tagValue> of all the likedTags by the user.
     * @param uid the current user int id
     * @param orderByValue set to true to return the highest valued tags
     * @return
     */
    public HashMap<String, Integer> getLikedTags(int uid, boolean orderByValue) {
        HashMap<String, Integer> likedTags = new HashMap<>();
        String likePs = "SELECT tname, value FROM likes WHERE uid = ?";
        if (orderByValue) {
            likePs = "SELECT tname, value FROM likes WHERE uid = ? ORDER BY value DESC";
        }
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement ps = conn.prepareStatement(likePs)) {
                ps.setInt(1, uid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    likedTags.put(rs.getString(1), rs.getInt(2));
                }
                return likedTags;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a boolean indicating if the user has already reviewed the specified wine
     * @param uid the user id
     * @param wid the wine id
     * @return Boolean
     */
    public Boolean alreadyReviewExists(int uid, int wid) {
        String test = "SELECT * FROM reviews WHERE uid = ? AND wid = ?";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement ps = conn.prepareStatement(test)) {
                ps.setInt(1, uid);
                ps.setInt(2, wid);
                ResultSet rs = ps.executeQuery();
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks to see if the user has already reviewed the wine by calling {@link LogWineDao#alreadyReviewExists(int, int)}.
     * If so, calls {@link LogWineDao#updateReview(int, int, int, String, String)}. Else inserts a new review into the
     * database
     * @param uid the int user id
     * @param wid the int wine id
     * @param rating the int rating given by the user
     * @param description the string description of the review
     * @param date the string date of the time the review was made in "YYYY-MM-DD HH:mm:ss"
     */
    public void reviews(int uid, int wid, int rating, String description, String date) {
        if (!alreadyReviewExists(uid, wid)) {
            String reviewSql = "INSERT INTO reviews (uid, wid, rating, description, date) VALUES (?,?,?,?,?)";
            try (Connection conn = databaseManager.connect()) {
                try (PreparedStatement ps = conn.prepareStatement(reviewSql)) {
                    ps.setInt(1, uid);
                    ps.setInt(2, wid);
                    ps.setInt(3, rating);
                    ps.setString(4, description);
                    ps.setString(5, date);
                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            updateReview(uid, wid, rating, description, date);
        }
    }

    /**
     * Updates the rating, date and description of an already existing review made by the user
     * @param uid the int user id
     * @param wid the int wine id
     * @param rating the int rating given by the user
     * @param newDescription the string description of the review
     * @param date the string date of the time the review was made in "YYYY-MM-DD HH:mm:ss"
     */
    public void updateReview(int uid, int wid, int rating, String newDescription, String date) {
        String updateSql = "UPDATE reviews SET description = ?, rating = ?, date = ? WHERE uid = ? AND wid = ?";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement updateValuePs = conn.prepareStatement(updateSql)) {
                updateValuePs.setString(1, newDescription);
                updateValuePs.setInt(2, rating);
                updateValuePs.setString(3, date);
                updateValuePs.setInt(4, uid);
                updateValuePs.setInt(5, wid);
                updateValuePs.executeUpdate();
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Returns a certain number of user reviews specified by maxNumbers and returns the most recent reviews if specified
     * @param uid the int user id
     * @param maxNumbers the maximum number of reviews to return
     * @param orderByDate a boolean value to return the most recent reviews
     * @return an ArrayList of {@link Review}
     */
    public ArrayList<Review> getUserReview(int uid, int maxNumbers, Boolean orderByDate) {
        ArrayList<Review> userReviews = new ArrayList<Review>();
        String getReview = "SELECT * FROM reviews WHERE uid = ?";
        if (orderByDate) {
            getReview = "SELECT * FROM reviews WHERE uid = ? ORDER BY date DESC";
        }
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement ps = conn.prepareStatement(getReview)) {
                ps.setInt(1, uid);
                ResultSet rs = ps.executeQuery();
                int i = 0;
                while (i < maxNumbers && rs.next()) {
                    userReviews.add(new Review(rs.getInt(1),rs.getInt(2),
                            rs.getInt(3),rs.getString(4), rs.getString(5)));
                    i++;
                }
                return userReviews;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns all the user reviews and returns the most recent reviews if specified
     * @param uid the int user id
     * @param orderByDate a boolean value to return the most recent reviews
     * @return an ArrayList of {@link Review}
     */
    public ArrayList<Review> getUserReview(int uid, Boolean orderByDate) {
        ArrayList<Review> userReviews = new ArrayList<Review>();
        String getReview = "SELECT * FROM reviews WHERE uid = ?";
        if (orderByDate) {
            getReview = "SELECT * FROM reviews WHERE uid = ? ORDER BY date DESC";
        }
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement ps = conn.prepareStatement(getReview)) {
                ps.setInt(1, uid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Review review = new Review(rs.getInt(1),rs.getInt(2),
                            rs.getInt(3),rs.getString(4), rs.getString(5));
                    userReviews.add(review);
                }
                return userReviews;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
