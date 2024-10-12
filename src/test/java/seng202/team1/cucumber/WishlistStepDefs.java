package seng202.team1.cucumber;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.models.Wine;
import seng202.team1.repository.DAOs.LogWineDao;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.services.ReviewService;
import seng202.team1.services.TagRankingService;
import seng202.team1.services.UserLoginService;
import seng202.team1.services.WishlistService;

import java.sql.SQLException;
import java.util.ArrayList;

public class WishlistStepDefs {

    ArrayList<Wine> wishlist;

    private void initialize() throws InstanceAlreadyExistsException {

        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        DatabaseManager.getInstance().forceReset();

    }

    @Given("The user {int} is logged in and has {int} wines in their wishlist")
    public void userLoggedInWithEmptyWishlist(int uid, int wishlistSize) throws InstanceAlreadyExistsException {

        initialize();
        UserLoginService userLoginService = new UserLoginService();
        userLoginService.storeLogin("test", "test", "test1234");
        Assertions.assertEquals(wishlistSize, WishlistService.getWishlistWines(uid).size());

    }

    @Given("The user {int} is logged in and has {int} wines in their wishlist, with wine {int} in their wishlist")
    public void userLoggedInWith1WineInWishlist(int uid, int wishlistSize, int wid) throws InstanceAlreadyExistsException {

        initialize();
        UserLoginService userLoginService = new UserLoginService();
        userLoginService.storeLogin("test", "test", "test1234");
        WishlistService.addToWishlist(wid, uid);
        Assertions.assertEquals(wishlistSize, WishlistService.getWishlistWines(uid).size());
        Assertions.assertTrue(WishlistService.checkInWishlist(wid, uid));

    }

    @Given("The user {int} is logged in and has wine {int}, wine {int}, wine {int} to their wishlist")
    public void userLoggedInAddsMultipleWines(int uid, int wid1, int wid2, int wid3) throws InstanceAlreadyExistsException {

        initialize();
        UserLoginService userLoginService = new UserLoginService();
        userLoginService.storeLogin("test", "test", "test1234");
        WishlistService.addToWishlist(wid1, uid);
        WishlistService.addToWishlist(wid2, uid);
        WishlistService.addToWishlist(wid3, uid);

    }

    @Given("The users 1 and 2 are logged in with empty wishlists")
    public void twoUsersInSameDatabase() throws InstanceAlreadyExistsException {

        initialize();
        UserLoginService userLoginService = new UserLoginService();
        userLoginService.storeLogin("test1", "test1", "test1234");
        userLoginService.storeLogin("test2", "test2", "test2234");


    }

    @Given("Both users {int} and {int} have the same wine {int} in their wishlist")
    public void twoUsersInSameDatabaseBothWithSameWineInWishlist(int uid1, int uid2, int wid) throws InstanceAlreadyExistsException {

        initialize();
        UserLoginService userLoginService = new UserLoginService();
        userLoginService.storeLogin("test1", "test1", "test1234");
        userLoginService.storeLogin("test2", "test2", "test2234");
        WishlistService.addToWishlist(wid, uid1);
        WishlistService.addToWishlist(wid, uid2);

    }

    @When("The user {int} adds a wine {int} to their wishlist")
    public void userAddsWineToWishlist(int uid, int wid) {

        WishlistService.addToWishlist(wid, uid);

    }

    @When("The user {int} removes a wine {int} from their wishlist")
    public void userRemovesWineFromWishlist(int uid, int wid) throws SQLException {

        WishlistService.removeFromWishlist(wid, uid);

    }

    @When("The user {int} gets their wishlist")
    public void userGetsTheirWishlist(int uid) {

        wishlist = WishlistService.getWishlistWines(uid);

    }

    @When("The user {int} adds wine {int} to their wishlist and user {int} adds wine {int} to their wishlist")
    public void twoUsersAddAWineEach(int uid1, int wid1, int uid2, int wid2) {

        WishlistService.addToWishlist(wid1, uid1);
        WishlistService.addToWishlist(wid2, uid2);

    }

    @When("User {int} removes wine {int} from their wishlist")
    public void user1DeletesWine1(int uid, int wid) throws SQLException {

        WishlistService.removeFromWishlist(wid, uid);

    }


    @Then("The user {int} wishlist will be {int} long, and contain the wine {int}")
    public void userHasWineInWishlist(int uid, int wishlistSize, int wid) {

        Assertions.assertTrue(WishlistService.checkInWishlist(wid, uid));
        Assertions.assertEquals(wishlistSize, WishlistService.getWishlistWines(uid).size());

    }

    @Then("The user {int} wishlist will be {int} long")
    public void userHasEmptyWishlist(int uid, int wishlistSize) {

        Assertions.assertEquals(wishlistSize, WishlistService.getWishlistWines(uid).size());

    }

    @Then("The user {int} wishlist will be of length {int} and contain wine {int}, wine {int}, wine {int}")
    public void userWishlistHasMultipleWines(int uid, int wishlistSize, int wid1, int wid2, int wid3) {

        Assertions.assertTrue(WishlistService.checkInWishlist(wid1, uid));
        Assertions.assertTrue(WishlistService.checkInWishlist(wid2, uid));
        Assertions.assertTrue(WishlistService.checkInWishlist(wid2, uid));
        Assertions.assertEquals(wishlistSize, WishlistService.getWishlistWines(uid).size());

    }

    @Then("User {int} wishlist will only contain wine {int} and user {int} wishlist will only contain wine {int}")
    public void usersWishlistsOnlyContainTheirWines(int uid1, int wid1, int uid2, int wid2) {

        Assertions.assertTrue(WishlistService.checkInWishlist(wid1, uid1));
        Assertions.assertTrue(WishlistService.checkInWishlist(wid2, uid2));
        Assertions.assertFalse(WishlistService.checkInWishlist(wid1, uid2));
        Assertions.assertFalse(WishlistService.checkInWishlist(wid2, uid1));
        Assertions.assertEquals(1, WishlistService.getWishlistWines(uid1).size());
        Assertions.assertEquals(1, WishlistService.getWishlistWines(uid2).size());

    }

    @Then("User {int} will still have wine {int} in their wishlist and User {int} won't")
    public void users1WishlistLostWineOnly(int uid2, int wid, int uid1) {

        Assertions.assertTrue(WishlistService.checkInWishlist(wid, uid2));
        Assertions.assertFalse(WishlistService.checkInWishlist(wid, uid1));
        Assertions.assertEquals(0, WishlistService.getWishlistWines(uid1).size());
        Assertions.assertEquals(1, WishlistService.getWishlistWines(uid2).size());

    }

}
