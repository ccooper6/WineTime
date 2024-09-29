package seng202.team1.services;

import seng202.team1.models.User;
import seng202.team1.models.Wine;
import seng202.team1.repository.DAOs.WishlistDAO;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Service class for the wishlist feature.
 * @author Elise Newman, Caleb Cooper
 */
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
     * Matches a wine with a user in the wishlist table.
     * @param wineID int value repr wine object
     * @param userID int value repr active user
     */
    public static void addToWishlist(int wineID, int userID) {
        WishlistDAO wishlistDAO = WishlistDAO.getInstance();
        if (wishlistDAO.checkWineID(wineID) && wishlistDAO.checkUserID(userID) && !wishlistDAO.checkWine(wineID, userID)) {
            wishlistDAO.addWine(wineID, userID);
        }
    }

    /**
     * Deletes a pairing of a user and wine in the wishlist table.
     * @param wineID int value repr wine object
     * @param userID int value repr active user
     * @throws SQLException throws when trying to remove a wine from the wishlist that isn't there
     */
    public static void removeFromWishlist(int wineID, int userID) throws SQLException {
        if (WishlistDAO.getInstance().checkWine(wineID, userID)) {
            WishlistDAO.getInstance().removeWine(wineID, userID);
        } else {
            throw new SQLException("Can't remove element which doesn't exist");
        }

    }

    /**
     * Gets the wines which match with the current user in the wishlist table as an array.
     * @param userId is the id of the active user
     * @return wineList array of wines from the user's wishlist
     */
    public static ArrayList<Wine> getWishlistWines(int userId) {
        return WishlistDAO.getInstance().fetchWines(userId);
    }
}
