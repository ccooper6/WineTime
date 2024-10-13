package seng202.team1.repository.DAOs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.models.Review;
import seng202.team1.repository.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * The class containing the functions to add and retrieve entries to the "Likes" and "Reviews" table,
 * mainly called by the WineLoggingPopupController when the user logs a wine.
 */
public class LogWineDAO {
    private final DatabaseManager DATABASEMANAGER = DatabaseManager.getInstance();
    private static final Logger LOG = LogManager.getLogger(LogWineDAO.class);

    /**
     * Default constructor for LogWineDAO.
     */
    public LogWineDAO() {}

    /**
     * Calls {@link LogWineDAO#alreadyLikeExists(int, String)} to see if the user has already liked the tag. If so
     * calls {@link LogWineDAO#updateLikesValue(int, String, int)} to update the liked tag's value. Else if it doesn't
     * exist, add a new entry to the 'LIKES' table
     *
     * @param uid     the UID of the current user
     * @param tagName the string tag name of the added tag
     * @param value   the value which determines the ranking of the users liked tags
     */
    public void likes(int uid, String tagName, int value) {
        if (alreadyLikeExists(uid, tagName)) {
            updateLikesValue(uid, tagName, value);
        } else {
            String likesSql = "INSERT INTO likes (uid, tname, value) VALUES (?,?,?)";
            try (Connection conn = DATABASEMANAGER.connect()) {
                try (PreparedStatement likesPs = conn.prepareStatement(likesSql)) {
                    likesPs.setInt(1, uid);
                    likesPs.setString(2, tagName);
                    likesPs.setInt(3, value);
                    likesPs.executeUpdate();
                    LOG.info("Successfully added user's tag preferences");
                }
            } catch (SQLException e) {
                LOG.error("Error: Could not add user's tag preferences, {}", e.getMessage());
            }
        }
    }

    /**
     * Updates the like value of the current relationship between the uid and tagName by adding the argument value onto
     * the already existing value.
     *
     * @param uid     user id
     * @param tagName tag name
     * @param value   value to be added to prev value
     */
    public void updateLikesValue(int uid, String tagName, int value) {
        int prevValue;
        String getPrevValueSql = "SELECT value FROM likes WHERE uid = ? AND tname = ?";
        String updateValueSql = "UPDATE likes SET value = ? WHERE uid = ? AND tname = ?";
        try (Connection conn = DATABASEMANAGER.connect()) {
            try (PreparedStatement getPrevValuePS = conn.prepareStatement(getPrevValueSql)) {
                getPrevValuePS.setInt(1, uid);
                getPrevValuePS.setString(2, tagName);
                ResultSet rs = getPrevValuePS.executeQuery();
                prevValue = rs.getInt(1);
            }
            try (PreparedStatement updateValuePS = conn.prepareStatement(updateValueSql)) {
                updateValuePS.setInt(1, prevValue + value);
                updateValuePS.setInt(2, uid);
                updateValuePS.setString(3, tagName);
                updateValuePS.executeUpdate();
            }
            LOG.info("Successfully updated user's tag preferences");
        } catch (SQLException e) {
            LOG.error("Error: Could not update user's tag preferences, {}", e.getMessage());
        }

    }

    /**
     * Returns a boolean indicating if the user is already in a 'like' relationship with the specified tag.
     * @param uid     the user id
     * @param tagName the tag name
     * @return Boolean indicating if the user has already liked the tag
     */
    public Boolean alreadyLikeExists(int uid, String tagName) {
        String sql = "SELECT * FROM likes WHERE uid = ? AND tname = ?";
        try (Connection conn = DATABASEMANAGER.connect()) {
            try (PreparedStatement loggingPS = conn.prepareStatement(sql)) {
                loggingPS.setInt(1, uid);
                loggingPS.setString(2, tagName);
                ResultSet rs = loggingPS.executeQuery();
                return rs.next();
            }
        } catch (SQLException e) {
            LOG.error("Error: Could not check if tag preferences exists, {}", e.getMessage());
            return false;
        }
    }

    /**
     * Returns a list of tags that the user has selected for a specific wine.
     * @param uid the user id
     * @param wid the wine id
     * @return ArrayList of selected tags (Strings)
     */
    public ArrayList<String> getSelectedTags(int uid, int wid) {
        String tags;
        String getSelectedTags = "SELECT selectedtags FROM reviews WHERE uid = ? AND wid = ?";
        try (Connection conn = DATABASEMANAGER.connect()) {
            try (PreparedStatement loggingPS = conn.prepareStatement(getSelectedTags)) {
                loggingPS.setInt(1, uid);
                loggingPS.setInt(2, wid);
                ResultSet rs = loggingPS.executeQuery();
                if (rs.next()) {
                    tags = rs.getString(1);
                    if (tags != null && !tags.isEmpty()) {
                        String[] tagArray = tags.split(",");
                        return new ArrayList<>(Arrays.asList(tagArray));
                    }
                }
            }
        } catch (SQLException e) {
            LOG.error("Error: Could not get selected tags, {}", e.getMessage());
        }
        return null;
    }

    /**
     * Returns a list of tags that the user has liked for a specific wine.
     * @param uid the user id
     * @param wid the wine id
     * @return an ArrayList of liked tags (Strings)
     */
    public ArrayList<String> getWineLikedTags(int uid, int wid) {
        String tags;
        String getSelectedTags = "SELECT tagsliked FROM reviews WHERE uid = ? AND wid = ?";
        try (Connection conn = DATABASEMANAGER.connect()) {
            try (PreparedStatement ps = conn.prepareStatement(getSelectedTags)) {
                ps.setInt(1, uid);
                ps.setInt(2, wid);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    tags = rs.getString(1);
                    if (tags != null && !tags.isEmpty()) {
                        String[] tagArray = tags.split(",");
                        return new ArrayList<>(Arrays.asList(tagArray));
                    }
                }
            }
        } catch (SQLException e) {
            LOG.error("Error in LogWineDAO.getWineLikedTags(): Could not access database.");
        }
        return null;
    }

    /**
     * Returns a hashmap of tagName, tagValue of the likedTags by the user.
     * @param uid          the current user int id
     * @param maximumTag   the maximum number of tags to return
     * @param orderByValue set to true to return the highest valued tags
     * @return HashMap of likedTags
     */
    public HashMap<String, Integer> getLikedTags(int uid, int maximumTag, boolean orderByValue) {
        HashMap<String, Integer> likedTags = new HashMap<>();
        String sql = "SELECT tname, value FROM likes WHERE uid = ?";
        if (orderByValue) {
            sql = "SELECT tname, value FROM likes WHERE uid = ? ORDER BY value DESC";
        }
        try (Connection conn = DATABASEMANAGER.connect()) {
            try (PreparedStatement loggingPS = conn.prepareStatement(sql)) {
                loggingPS.setInt(1, uid);
                ResultSet rs = loggingPS.executeQuery();
                for (int i = 0; i < maximumTag; i++) {
                    if (!rs.next()) {
                        break;
                    }
                    likedTags.put(rs.getString(1), rs.getInt(2));
                }

                return likedTags;
            }
        } catch (SQLException e) {
            LOG.error("Error: Could not get liked tags, {}", e.getMessage());
            return likedTags;
        }
    }

    /**
     * Returns the top tags that have a positive rating by the user.
     * @param uid the user uid
     * @param maximumTag the maximum number of tags to return
     * @return An ArrayList of tag names
     */
    public ArrayList<String> getFavouritedTags(int uid, int maximumTag) {
        ArrayList<String> likedTags = new ArrayList<>();
        String sql = "SELECT tname, value FROM likes WHERE uid = ? AND value >= 1 ORDER BY value DESC";
        try (Connection conn = DATABASEMANAGER.connect()) {
            try (PreparedStatement loggingPS = conn.prepareStatement(sql)) {
                loggingPS.setInt(1, uid);
                ResultSet rs = loggingPS.executeQuery();
                for (int i = 0; i < maximumTag; i++) {
                    if (!rs.next()) {
                        break;
                    }

                    likedTags.add(rs.getString(1));
                }

                return likedTags;
            }
        } catch (SQLException e) {
            LOG.error("Error: Could not get favourited tags, {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns all the negatively rated tags
     * @param uid the current user uid
     * @return An ArrayList of disliked tag names
     */
    public ArrayList<String> getDislikedTags(int uid) {
        ArrayList<String> dislikedTags = new ArrayList<>();
        String sql = "SELECT tname FROM likes WHERE uid = ? AND value < 0";
        try (Connection conn = DATABASEMANAGER.connect()) {
            try (PreparedStatement loggingPS = conn.prepareStatement(sql)) {
                loggingPS.setInt(1, uid);
                ResultSet rs = loggingPS.executeQuery();
                while (rs.next()) {
                    dislikedTags.add(rs.getString(1));
                }
                return dislikedTags;
            }
        } catch (SQLException e) {
            LOG.error("Error: Could not get disliked tags, {}", e.getMessage());
        }
        return dislikedTags;
    }

    /**
     * Returns a hashmap of &lt;tagName, tagValue&gt; of the most negatively rated tags belonging to the user.
     * @param uid user id
     * @param limit number of tags to return
     * @return  a hashmap of &lt;tagName, tagValue&gt;
     */
    public HashMap<String, Integer> getMostDislikedTags(int uid, int limit) {
        HashMap<String, Integer> likedTags = new HashMap<>();
        String likePs = "SELECT tname, value FROM likes WHERE uid = ? AND value < 0 ORDER BY value LIMIT ?";
        try (Connection conn = DATABASEMANAGER.connect()) {
            try (PreparedStatement ps = conn.prepareStatement(likePs)) {
                ps.setInt(1, uid);
                ps.setInt(2, limit);
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
     * Returns a boolean indicating if the user has already reviewed the specified wine.
     * @param uid the user id
     * @param wid the wine id
     * @return Boolean indicating if the user has already reviewed the wine
     */
    public Boolean reviewAlreadyExists(int uid, int wid) {
        String sql = "SELECT * FROM reviews WHERE uid = ? AND wid = ?";
        try (Connection conn = DATABASEMANAGER.connect()) {
            try (PreparedStatement loggingPS = conn.prepareStatement(sql)) {
                loggingPS.setInt(1, uid);
                loggingPS.setInt(2, wid);
                ResultSet rs = loggingPS.executeQuery();
                return rs.next();
            }
        } catch (SQLException e) {
            LOG.error("Error: Could not check if review exists, {}", e.getMessage());
            return true; // if there is an error don't put it in database
        }
    }

    /**
     * Checks to see if the user has already reviewed the wine by calling {@link LogWineDAO#reviewAlreadyExists(int, int)}.
     * If it does, it updates the current review, otherwise inserts a new review into the database
     * @param uid          the int user id
     * @param wid          the int wine id
     * @param rating       the int rating given by the user
     * @param description  the string description of the review
     * @param date         the string date of the time the review was made in "YYYY-MM-DD HH:mm:ss"
     * @param selectedTags the ArrayList of tags selected by the user
     * @param tagsLiked    the ArrayList of tags liked/disliked by this review
     * @param noneSelected a boolean value to indicate if no tags were selected
     */
    public void doReview(int uid, int wid, int rating, String description, String date, ArrayList<String> selectedTags, ArrayList<String> tagsLiked, boolean noneSelected) {
        if (!reviewAlreadyExists(uid, wid)) {
            createReview(uid, wid, rating, description, date, selectedTags, tagsLiked, noneSelected);
        } else {
            updateReview(uid, wid, rating, description, date, selectedTags, tagsLiked, noneSelected);
        }
    }

    /**Creates a review for a given user and wine.
     * @param uid          the int user id
     * @param wid          the int wine id
     * @param rating       the int rating given by the user
     * @param description  the string description of the review
     * @param date         the string date of the time the review was made in "YYYY-MM-DD HH:mm:ss"
     * @param selectedTags the ArrayList of tags selected by the user
     * @param tagsLiked the ArrayList of tags liked by this review
     * @param noneSelected a boolean value to indicate if no tags were selected
     */
    private void createReview(int uid, int wid, int rating, String description, String date, ArrayList<String> selectedTags, ArrayList<String> tagsLiked, boolean noneSelected)
    {
        String sql = "INSERT INTO reviews (uid, wid, rating, description, date, selectedtags, tagsLiked) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DATABASEMANAGER.connect()) {
            try (PreparedStatement loggingPS = conn.prepareStatement(sql)) {
                loggingPS.setInt(1, uid);
                loggingPS.setInt(2, wid);
                loggingPS.setInt(3, rating);
                loggingPS.setString(4, description);
                loggingPS.setString(5, date);
                if (noneSelected) {
                    selectedTags = new ArrayList<>();
                }
                loggingPS.setString(6, String.join(",", selectedTags));
                loggingPS.setString(7, String.join(",", tagsLiked));
                loggingPS.executeUpdate();
            }
            LOG.info("Successfully created review for user");
        } catch (SQLException e) {
            LOG.error("Error: Could not create review for user, {}", e.getMessage());
        }
    }

    /**
     * Updates the rating, date and description of an already existing review made by the user.
     * @param uid            the int user id
     * @param wid            the int wine id
     * @param rating         the int rating given by the user
     * @param newDescription the string description of the review
     * @param date           the string date of the time the review was made in "YYYY-MM-DD HH:mm:ss"
     * @param selectedTags   the ArrayList of tags selected by the user
     * @param tagsLiked      the ArrayList of tags liked by this review
     * @param noneSelected   a boolean value to indicate if no tags were selected
     */
    private void updateReview(int uid, int wid, int rating, String newDescription, String date, ArrayList<String> selectedTags, ArrayList<String> tagsLiked, boolean noneSelected) {
        String sql = "UPDATE reviews SET description = ?, rating = ?, date = ?, selectedtags = ?, tagsliked = ? WHERE uid = ? AND wid = ?";
        try (Connection conn = DATABASEMANAGER.connect()) {
            try (PreparedStatement loggingPS = conn.prepareStatement(sql)) {
                loggingPS.setString(1, newDescription);
                loggingPS.setInt(2, rating);
                loggingPS.setString(3, date);
                if (noneSelected) {
                    selectedTags = new ArrayList<>();
                }
                loggingPS.setString(4, String.join(",", selectedTags));
                loggingPS.setString(5, String.join(",", tagsLiked));
                loggingPS.setInt(6, uid);
                loggingPS.setInt(7, wid);
                loggingPS.executeUpdate();
                LOG.info("Successfully updated review for user");
            }
        } catch (SQLException e) {
            LOG.error("Error: Could not update review for user, {}", e.getMessage());
        }
    }

    /**
     * Returns a certain number of user reviews specified by maxNumbers and returns the most recent reviews if specified.
     * @param uid         the int user id
     * @param maxNumbers  the maximum number of reviews to return
     * @param orderByDate a boolean value to return the most recent reviews
     * @return an ArrayList of Review
     */
    public ArrayList<Review> getUserReview(int uid, int maxNumbers, Boolean orderByDate) {
        ArrayList<Review> userReviews = new ArrayList<>();
        String sql = "SELECT * FROM reviews WHERE uid = ?";
        if (orderByDate) {
            sql = "SELECT * FROM reviews WHERE uid = ? ORDER BY date DESC";
        }
        try (Connection conn = DATABASEMANAGER.connect()) {
            try (PreparedStatement loggingPS = conn.prepareStatement(sql)) {
                loggingPS.setInt(1, uid);
                ResultSet rs = loggingPS.executeQuery();
                for (int i = 0; i < maxNumbers; i++) {
                    if (!rs.next()) {
                        break;
                    }

                    userReviews.add(new Review(
                            rs.getInt(1),
                            rs.getInt(2),
                            rs.getInt(3),
                            rs.getString(4),
                            rs.getString(5),
                            getSelectedTags(uid, rs.getInt(2)),
                            getWineLikedTags(uid, rs.getInt(2))));
                }

                return userReviews;
            }
        } catch (SQLException e) {
            LOG.error("Error: Could not get user review, {}", e.getMessage());
            return userReviews;
        }
    }

    /**
     * Returns an array of all the wine id that the user has reviewed before.
     * @param uid the current user's uid
     * @return an ArrayList&lt;Integer&gt; of the user's reviewed wine ids
     */
    public ArrayList<Integer> getReviewedWines(int uid) {
        ArrayList<Integer> userReviews = new ArrayList<>();
        String sql = "SELECT wid FROM reviews WHERE uid = ?";
        try (Connection conn = DATABASEMANAGER.connect()) {
            try (PreparedStatement loggingPS = conn.prepareStatement(sql)) {
                loggingPS.setInt(1, uid);
                ResultSet rs = loggingPS.executeQuery();
                while (rs.next()) {
                    userReviews.add(rs.getInt(1));
                }
                return userReviews;
            }
        } catch (SQLException e) {
            LOG.error("Error: Could not get reviewed wines, {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes a review from the database.
     * @param uid the user id
     * @param wid the wine id
     */
    public void deleteReview(int uid, int wid) {
        String sql = "DELETE FROM reviews WHERE uid = ? AND wid = ?";
        try (Connection conn = DATABASEMANAGER.connect()) {
            try (PreparedStatement loggingPS = conn.prepareStatement(sql)) {
                loggingPS.setInt(1, uid);
                loggingPS.setInt(2, wid);
                loggingPS.executeUpdate();
            }
        } catch (SQLException e) {
            LOG.error("Error: Could not delete review, {}", e.getMessage());
        }
    }

    /**
     * Returns a review object using the specified user id and wine id.
     * @param uid the user id
     * @param wid the wine id
     * @return a Review object
     */
    public Review getReview(int uid, int wid) {
        String sql = "SELECT * FROM reviews WHERE uid = ? AND wid = ?";
        try (Connection conn = DATABASEMANAGER.connect();
                PreparedStatement loggingPS = conn.prepareStatement(sql)) {
            loggingPS.setInt(1, uid);
            loggingPS.setInt(2, wid);
            try (ResultSet rs = loggingPS.executeQuery()) {
                if (rs.next()) {
                    return new Review(
                            rs.getInt(1),
                            rs.getInt(2),
                            rs.getInt(3),
                            rs.getString(4),
                            rs.getString(5),
                            getSelectedTags(uid, rs.getInt(2)),
                            getWineLikedTags(uid, rs.getInt(2))
                    );
                }
            }
        } catch (SQLException e) {
            LOG.error("Error: Could not get review, {}", e.getMessage());
        }
        return null;
    }
}
