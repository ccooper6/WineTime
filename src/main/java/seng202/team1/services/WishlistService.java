package seng202.team1.services;

import seng202.team1.models.User;
import seng202.team1.models.Wine;
import seng202.team1.repository.ChallengeDAO;
import seng202.team1.repository.WishlistDAO;

import java.util.ArrayList;

public class WishlistService {
    public static boolean checkInWishlist(int wineID, int userID) {
        return WishlistDAO.getInstance().checkWine(wineID, userID);
    }

    public static void addToWishlist(int wineID, int userID) {
        WishlistDAO.getInstance().addWine(wineID, userID);
    }

    public static void removeFromWishlist(int wineID, int userID) {
        WishlistDAO.getInstance().removeWine(wineID, userID);
    }

    public static int getUserID(User user) {
        return WishlistDAO.getInstance().getUId(user);
    }

    /**
     * Forwards the wineList from the DAO to the WishlistController
     *
     * @param userId is the id of the active user
     * @return wineList array of wines from the user's wishlist
     */
    public static ArrayList<Wine> getWishlistWines(int userId) {
        return WishlistDAO.getInstance().fetchWines(userId);
    }
}
