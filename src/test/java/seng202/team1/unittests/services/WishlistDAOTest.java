package seng202.team1.unittests.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.models.Wine;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.repository.DAOs.WishlistDAO;
import seng202.team1.services.UserLoginService;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WishlistDAOTest {
    public static WishlistDAO wishlistDAO;
    public static DatabaseManager databaseManager;
    @BeforeEach
    void setUp() throws InstanceAlreadyExistsException {
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        wishlistDAO = new WishlistDAO();
        UserLoginService userLoginService = new UserLoginService();
        userLoginService.storeLogin("test", "test", "test");
        wishlistDAO.addWine(2,1);
    }
    @Test
    public void testCheckWineBadWineID() {
        assertFalse(wishlistDAO.checkWine(999999999, 1));
    }
    @Test
    public void testCheckWineBadUserID() {
        assertFalse(wishlistDAO.checkWine(1, 2));
    }
    @Test
    public void testCheckWineDoesntExist() {
        assertFalse(wishlistDAO.checkWine(1, 1));
    }
    @Test
    public void testCheckWineExists() {
        assertTrue(wishlistDAO.checkWine(2, 1));
    }

    /**
     * There are 500 wines in the wine table. 501 does not exist.
     */
    @Test
    public void testCheckWineIDBad() {
        assertFalse(Wine.checkWineExists(999999999));
    }
    @Test
    public void testCheckWineIDGood() {
        assertTrue(Wine.checkWineExists(1));
    }
}
