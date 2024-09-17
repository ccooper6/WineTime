package seng202.team1.services;

import org.w3c.dom.ranges.RangeException;
import seng202.team1.models.User;
import seng202.team1.models.Wine;
import seng202.team1.repository.ChallengeDAO;
import seng202.team1.repository.WishlistDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class WishlistService {
    /**
     * Checks the existence of a wine in the wishlist.
     * @param wineID int value repr wine object
     * @param userID int value repr active user
     * @return true if in wishlist, else false
     */
    public static boolean checkInWishlist(int wineID, int userID) {
        return WishlistDAO.getInstance().checkWine(wineID, userID);
    }

    /**
     * Matches a wine with a user in the wishlist table
     * @param wineID int value repr wine object
     * @param userID int value repr active user
     */
    public static void addToWishlist(int wineID, int userID) {
        if (WishlistDAO.getInstance().checkWineID(wineID) && WishlistDAO.getInstance().checkUserID(userID) && !WishlistDAO.getInstance().checkWine(wineID, userID)) {
            WishlistDAO.getInstance().addWine(wineID, userID);
        }
    }

    /**
     * Deletes a pairing of a user and wine in the wishlist table.
     * @param wineID int value repr wine object
     * @param userID int value repr active user
     */
    public static void removeFromWishlist(int wineID, int userID) throws SQLException{
        if (WishlistDAO.getInstance().checkWine(wineID, userID)){
            WishlistDAO.getInstance().removeWine(wineID, userID);
        } else {
            throw new SQLException("Can't remove element which doesn't exist");
        }

    }

    /**
     * Gets the user id from the active user object
     * @param user User object repr the active user
     * @return int value to identify the active user
     */
    public static int getUserID(User user) {
        return WishlistDAO.getInstance().getUId(user);
    }

    /**
     * Gets the wines which match with the current user in the wishlist table as an array
     * @param userId is the id of the active user
     * @return wineList array of wines from the user's wishlist
     */
    public static ArrayList<Wine> getWishlistWines(int userId) {
        return WishlistDAO.getInstance().fetchWines(userId);
    }
}
