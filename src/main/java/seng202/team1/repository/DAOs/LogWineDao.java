package seng202.team1.repository.DAOs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.models.Review;
import seng202.team1.models.Wine;
import seng202.team1.models.WineBuilder;
import seng202.team1.repository.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * The class containing the functions to add and retrieve entries to the "Likes" and "Reviews" table,
 * mainly called by the WineLoggingPopupController when the user logs a wine.
 *
 * @author Wen Sheng Thong, Caleb Cooper
 */
public class LogWineDao {
    private final DatabaseManager databaseManager = DatabaseManager.getInstance();
    private static final Logger LOG = LogManager.getLogger(LogWineDao.class);

    /**
     * Calls {@link LogWineDao#alreadyLikeExists(int, String)} to see if the user has already liked the tag. If so
     * calls {@link LogWineDao#updateLikesValue(int, String, int)} to update the liked tag's value. Else if it doesn't
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
            try (Connection conn = databaseManager.connect()) {
                try (PreparedStatement likesPs = conn.prepareStatement(likesSql)) {
                    likesPs.setInt(1, uid);
                    likesPs.setString(2, tagName);
                    likesPs.setInt(3, value);
                    likesPs.executeUpdate();
                }
            } catch (SQLException e) {
                LOG.error(e.getMessage());
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
            LOG.error(e.getMessage());
        }

    }

    /**
     * Returns a boolean indicating if the user is already in a 'like' relationship with the specified tag.
     *
     * @param uid     the user id
     * @param tagName the tag name
     * @return Boolean indicating if the user has already liked the tag
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
     * Returns a list of tags that the user has selected for a specific wine.
     * @param uid the user id
     * @param wid the wine id
     * @return ArrayList of selected tags
     */
    public ArrayList<String> getSelectedTags(int uid, int wid) {
        String tags;
        String getSelectedTags = "SELECT selectedtags FROM reviews WHERE uid = ? AND wid = ?";
        try (Connection conn = databaseManager.connect()) {
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
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Returns a hashmap of tagName, tagValue of the likedTags by the user.
     *
     * @param uid          the current user int id
     * @param maximumTag   the maximum number of tags to return
     * @param orderByValue set to true to return the highest valued tags
     * @return HashMap of likedTags
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
                int index = 0;
                while (index < maximumTag && rs.next()) {
                    likedTags.put(rs.getString(1), rs.getInt(2));
                    index++;
                }
                return likedTags;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a hashmap of &lt;tagName, tagValue&gt; of all the likedTags by the user.
     *
     * @param uid          the current user int id
     * @param orderByValue set to true to return the highest valued tags
     * @return HashMap of likedTags
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
     * Returns the top tags that have a positive value by the user
     * @param uid the user uid
     * @param maximumTag the maximum number of tags to return
     * @return An {@link ArrayList<String>} of tag names
     */
    public ArrayList<String> getFavouritedTags(int uid, int maximumTag) {
        ArrayList<String> likedTags = new ArrayList<>();
        String likePs = "SELECT tname, value FROM likes WHERE uid = ? AND value >= 1 ORDER BY value DESC";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement ps = conn.prepareStatement(likePs)) {
                ps.setInt(1, uid);
                ResultSet rs = ps.executeQuery();
                int index = 0;
                while (index < maximumTag && rs.next()) {
                    likedTags.add(rs.getString(1));
                    index++;
                }
                return likedTags;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns all the negatively valued tags
     * @param uid the current user uid
     * @return An {@link ArrayList<String>} of disliked tag names
     */
    public ArrayList<String> getDislikedTags(int uid) {
        ArrayList<String> dislikedTags = new ArrayList<>();
        String dislikePs = "SELECT tname FROM likes WHERE uid = ? AND value < 0";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement ps = conn.prepareStatement(dislikePs)) {
                ps.setInt(1, uid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    dislikedTags.add(rs.getString(1));
                }
                return dislikedTags;
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
        return dislikedTags;
    }

    /**
     * Returns a hashmap of &lt;tagName, tagValue&gt; of the most negatively rated tags belonging to the user.
     * @param uid user id
     * @param limit number of tags to retunr
     * @return  a hashmap of &lt;tagName, tagValue&gt
     */
    public HashMap<String, Integer> getMostDislikedTags(int uid, int limit) {
        HashMap<String, Integer> likedTags = new HashMap<>();
        String likePs = "SELECT tname, value FROM likes WHERE uid = ? AND value < 0 ORDER BY value ASC LIMIT ?";
        try (Connection conn = databaseManager.connect()) {
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
     *
     * @param uid the user id
     * @param wid the wine id
     * @return Boolean indicating if the user has already reviewed the wine
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
     * If it does, it updates the current review, otherwise inserts a new review into the database
     *
     * @param uid          the int user id
     * @param wid          the int wine id
     * @param rating       the int rating given by the user
     * @param description  the string description of the review
     * @param date         the string date of the time the review was made in "YYYY-MM-DD HH:mm:ss"
     * @param selectedTags the ArrayList of tags selected by the user
     * @param noneSelected a boolean value to indicate if no tags were selected
     */
    public void reviews(int uid, int wid, int rating, String description, String date, ArrayList<String> selectedTags, boolean noneSelected) {
        if (!alreadyReviewExists(uid, wid)) {
            String reviewSql = "INSERT INTO reviews (uid, wid, rating, description, date, selectedtags) VALUES (?,?,?,?,?,?)";
            try (Connection conn = databaseManager.connect()) {
                try (PreparedStatement ps = conn.prepareStatement(reviewSql)) {
                    ps.setInt(1, uid);
                    ps.setInt(2, wid);
                    ps.setInt(3, rating);
                    ps.setString(4, description);
                    ps.setString(5, date);
                    if (noneSelected) {
                        selectedTags = new ArrayList<>();
                    }
                    ps.setString(6, String.join(",", selectedTags));
                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            updateReview(uid, wid, rating, description, date, selectedTags, noneSelected);
        }
    }

    /**
     * Updates the rating, date and description of an already existing review made by the user.
     *
     * @param uid            the int user id
     * @param wid            the int wine id
     * @param rating         the int rating given by the user
     * @param newDescription the string description of the review
     * @param date           the string date of the time the review was made in "YYYY-MM-DD HH:mm:ss"
     * @param selectedTags   the ArrayList of tags selected by the user
     * @param noneSelected   a boolean value to indicate if no tags were selected
     */
    public void updateReview(int uid, int wid, int rating, String newDescription, String date, ArrayList<String> selectedTags, boolean noneSelected) {
        String updateSql = "UPDATE reviews SET description = ?, rating = ?, date = ?, selectedtags = ? WHERE uid = ? AND wid = ?";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement updateValuePs = conn.prepareStatement(updateSql)) {
                updateValuePs.setString(1, newDescription);
                updateValuePs.setInt(2, rating);
                updateValuePs.setString(3, date);
                if (noneSelected) {
                    selectedTags = new ArrayList<>();
                }
                updateValuePs.setString(4, String.join(",", selectedTags));
                updateValuePs.setInt(5, uid);
                updateValuePs.setInt(6, wid);
                updateValuePs.executeUpdate();
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
    }

    /**
     * Returns a certain number of user reviews specified by maxNumbers and returns the most recent reviews if specified.
     *
     * @param uid         the int user id
     * @param maxNumbers  the maximum number of reviews to return
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
                int index = 0;
                while (index < maxNumbers && rs.next()) {
                    userReviews.add(new Review(rs.getInt(1), rs.getInt(2),
                            rs.getInt(3), rs.getString(4), rs.getString(5),
                            getSelectedTags(uid, rs.getInt(2))));
                    index++;
                }
                return userReviews;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns an array of all the wine id that the user has reviewed before
     * @param uid the current user's uid
     * @return an {@link ArrayList<Integer>} of the user's reviewed wine ids
     */
    public ArrayList<Integer> getReviewedWines(int uid) {
        ArrayList<Integer> userReviews = new ArrayList<Integer>();
        String getReview = "SELECT wid FROM reviews WHERE uid = ?";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement ps = conn.prepareStatement(getReview)) {
                ps.setInt(1, uid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    userReviews.add(rs.getInt(1));
                }
                return userReviews;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns all the user reviews and returns the most recent reviews if specified.
     *
     * @param uid         the int user id
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
                    Review review = new Review(
                            rs.getInt(1),
                            rs.getInt(2),
                            rs.getInt(3),
                            rs.getString(4),
                            rs.getString(5),
                            getSelectedTags(uid, rs.getInt(2)));
                    userReviews.add(review);
                }
                return userReviews;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a specific wine object using the specified wine id.
     * @param wid the wine id
     * @return a {@link Wine} object
     */
    public Wine getWine(int wid) {
        String getWine = "SELECT id, wine.name as wine_name, description, price, tag.type as tag_type, tag.name as tag_name FROM wine "
                + "JOIN owned_by ON id = owned_by.wid "
                + "JOIN tag ON owned_by.tname = tag.name WHERE wine.id = ?;";
        WineBuilder wineBuilder = null;
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(getWine)) {
            ps.setInt(1, wid);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    if (wineBuilder == null) {
                        wineBuilder = WineBuilder.genericSetup(rs.getInt("id"),
                                rs.getString("wine_name"),
                                rs.getString("description"),
                                rs.getInt("price"));
                    }
                    String tagType = rs.getString("tag_type");
                    String tagName = rs.getString("tag_name");
                    setWineAttri(tagType, wineBuilder, tagName);
                }
                return wineBuilder.build();
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
        return null;
    }

    /**
     * Sets the wine attributes based on the fed tag types using a {@link WineBuilder}
     * @param tagType the string tag type
     * @param wineBuilder the {@link WineBuilder}
     * @param tagName the name of the tag
     */
    private static void setWineAttri(String tagType, WineBuilder wineBuilder, String tagName) {
        switch (tagType) {
            case "Variety":
                wineBuilder.setVariety(tagName);
                break;
            case "Province":
                wineBuilder.setProvince(tagName);
                break;
            case "Region":
                wineBuilder.setRegion(tagName);
                break;
            case "Vintage":
                wineBuilder.setVintage(Integer.valueOf(tagName));
                break;
            case "Country":
                wineBuilder.setCountry(tagName);
                break;
            case "Winery":
                wineBuilder.setWinery(tagName);
                break;
            default:
                LOG.error("Tag type {} is not supported!", tagType);
        }
    }

    /**
     * Deletes a review from the database.
     * @param uid the user id
     * @param wid the wine id
     */
    public void deleteReview(int uid, int wid) {
        String deleteReview = "DELETE FROM reviews WHERE uid = ? AND wid = ?";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement ps = conn.prepareStatement(deleteReview)) {
                ps.setInt(1, uid);
                ps.setInt(2, wid);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
    }

    /**
     * Returns a review object using the specified user id and wine id.
     * @param uid the user id
     * @param wid the wine id
     * @return a {@link Review} object
     */
    public Review getReview(int uid, int wid) {
        String getReviewSQL = "SELECT * FROM reviews WHERE uid = ? AND wid = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(getReviewSQL)) {
            ps.setInt(1, uid);
            ps.setInt(2, wid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Review(
                            rs.getInt(1),
                            rs.getInt(2),
                            rs.getInt(3),
                            rs.getString(4),
                            rs.getString(5),
                            getSelectedTags(uid, rs.getInt(2))
                    );
                }
            }
        } catch (SQLException e) {
            LOG.error("SQL Exception while retrieving review with UID: " + uid + " and WID: " + wid, e);
        }
        return null;
    }
}
