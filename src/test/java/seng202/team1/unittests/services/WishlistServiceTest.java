package seng202.team1.unittests.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.models.User;
import seng202.team1.models.Wine;
import seng202.team1.models.WineBuilder;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.repository.WishlistDAO;
import seng202.team1.services.UserLoginService;
import seng202.team1.services.WishlistService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class WishlistServiceTest {
    public static WishlistDAO wishlistDAO;
    public static DatabaseManager databaseManager;
    @BeforeEach
    void setUp() throws InstanceAlreadyExistsException {
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        databaseManager.forceReset();
        wishlistDAO = new WishlistDAO();
        UserLoginService userLoginService = new UserLoginService();
        userLoginService.storeLogin("test", "test", "test");
    }
    /**
     * The following tests are assuming checkWine(), checkUserID and checkWineID work
     */
    @Test
    public void testAddWineBadUserID() {
        assertFalse(wishlistDAO.checkWine(3,2));
        WishlistService.addToWishlist(3, 2);
        assertFalse(wishlistDAO.checkWine(3,2));
    }
    @Test
    public void testAddWineBadWineID() {
        assertFalse(wishlistDAO.checkWine(999999999,1));
        WishlistService.addToWishlist(999999999, 1);
        assertFalse(wishlistDAO.checkWine(999999999,1));
    }
    @Test
    public void testAddWineAlreadyExists() {
        WishlistService.addToWishlist(2, 1);
        assertTrue(wishlistDAO.checkWine(2,1));
        WishlistService.addToWishlist(2, 1);
        assertTrue(wishlistDAO.checkWine(2,1));
    }
    @Test
    public void testAddWineDoesntExist() {
        assertFalse(wishlistDAO.checkWine(3,1));
        WishlistService.addToWishlist(3, 1);
        assertTrue(wishlistDAO.checkWine(3,1));
    }
    @Test
    public void testRemoveWineDoesntExist() {
        assertFalse(wishlistDAO.checkWine(4, 1));
        try {
            WishlistService.removeFromWishlist(4,1);
            assertFalse(true);
        } catch (SQLException e) {
            assertTrue(true);
        }
    }
    @Test
    public void testRemoveWineExists() throws SQLException {
        WishlistService.addToWishlist(2, 1);
        assertTrue(wishlistDAO.checkWine(2, 1));
        WishlistService.removeFromWishlist(2,1);
        assertFalse(wishlistDAO.checkWine(2,1));
    }

    @Test
    public void testGetWishlistWinesEmpty() throws SQLException {
        if (wishlistDAO.checkWine(2, 1)) {
            WishlistService.removeFromWishlist(2, 1);
        }
        ArrayList<Wine> testNull = new ArrayList<>();
        ArrayList<Wine> myWines = WishlistService.getWishlistWines(1);
        assertEquals(testNull, myWines);
    }
    @Test
    public void testGetWishlistWinesFull() throws SQLException {
        if (wishlistDAO.checkWine(2, 1)) {
            WishlistService.removeFromWishlist(2, 1);
        }
        //make sure wishlist empty, then add 3 wines
        WishlistService.addToWishlist(4, 1);
        WishlistService.addToWishlist(5, 1);
        WishlistService.addToWishlist(6, 1);
        ArrayList<Wine> myWines = WishlistService.getWishlistWines(1);
        assertEquals(myWines.get(0).getWineId(), 4);
        assertEquals(myWines.get(1).getWineId(), 5);
        assertEquals(myWines.get(2).getWineId(), 6);
    }
}

