package seng202.team1.unittests.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.repository.WishlistDAO;
import seng202.team1.services.WishlistService;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WishlistServiceTest {
    public static WishlistService wishlistService;
    public static WishlistDAO wishlistDAO;
    public static DatabaseManager databaseManager;
    @BeforeEach
    void setUp() throws InstanceAlreadyExistsException {
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        wishlistService = new WishlistService();
        wishlistDAO = new WishlistDAO();
    }
    /**
     * The following tests are assuming checkWine(), checkUserID and checkWineID work
     */
    @Test
    public void testAddWineBadUserID() {
        assertFalse(wishlistDAO.checkWine(3,2));
        wishlistService.addToWishlist(3, 2);
        assertFalse(wishlistDAO.checkWine(3,2));
    }
    @Test
    public void testAddWineBadWineID() {
        assertFalse(wishlistDAO.checkWine(999999999,1));
        wishlistService.addToWishlist(999999999, 1);
        assertFalse(wishlistDAO.checkWine(999999999,1));

    }
    @Test
    public void testAddWineAlreadyExists() {
        assertTrue(wishlistDAO.checkWine(2,1));
        wishlistService.addToWishlist(2, 1);
        assertTrue(wishlistDAO.checkWine(2,1));
    }

    @Test
    public void testAddWineDoesntExist() {
        assertFalse(wishlistDAO.checkWine(3,1));
        wishlistService.addToWishlist(3, 1);
        assertTrue(wishlistDAO.checkWine(3,1));
    }
}
