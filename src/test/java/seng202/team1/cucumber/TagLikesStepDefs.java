package seng202.team1.cucumber;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.models.Wine;
import seng202.team1.repository.DAOs.LogWineDao;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.services.ReviewService;
import seng202.team1.services.UserLoginService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TagLikesStepDefs {
    private UserLoginService userLoginService;
    private ReviewService reviewService;
    private LogWineDao logWineDao;
    private Wine wine;
    private ArrayList<String> wineTags;
    private int wineId;
    private int currentUserId;
    private int currentRating;

    public void initialise() throws InstanceAlreadyExistsException {
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        DatabaseManager.getInstance().forceReset();
        userLoginService = new UserLoginService();
        reviewService = new ReviewService();
        logWineDao = new LogWineDao();
    }

    @Given("The user with name {string}, username {string} and password {string} is currently logged in. Their user id is {int}")
    public void iAmLoggedIn(String name, String username, String password, int userId) throws InstanceAlreadyExistsException {
        initialise();
        assertEquals(1, userLoginService.storeLogin(name, username, password));
        this.currentUserId = userId;
    }

    @When("The user is in the wine logging popup with the wine with id {int} which has the tags {listOfTags}")
    public void iAmInWineLoggingPopup(int wineId, ArrayList<String> tags) {
        this.wineId = wineId;
        this.wineTags = tags;
    }

    @And("The user likes or dislikes the tag {string} with a review rating of {int}")
    public void iLikeASingleTag(String tag, int newRating) {
        this.currentRating = newRating;
        reviewService.updateTagLikes(currentUserId, new ArrayList<>(List.of(tag)), new ArrayList<>(), newRating, 0); // Only that tag is liked/disliked
    }

    @And("The user likes no tags with a review rating of {int}")
    public void iLikeNoTags(int newRating) {
        this.currentRating = newRating;
        reviewService.updateTagLikes(currentUserId, this.wineTags, new ArrayList<>(), newRating, 0); // All tags are therefore liked/disliked
    }

    @And("The user likes the tags {listOfTags} with a review rating of {int}")
    public void iLikeMultipleTags(List<String> tags, int newRating) {
        this.currentRating = newRating;
        reviewService.updateTagLikes(currentUserId, new ArrayList<>(tags), new ArrayList<>(), newRating, 0); // All tags selected are liked/disliked
    }

    @And("The user edits the review to only like the tag {string} with a review rating of {int}")
    public void iEditReviewToLikeSingleTag(String tag, int newRating) {
        ArrayList<String> previouslyLikedTags = logWineDao.getWineLikedTags(this.currentUserId, this.wineId);
        int previousRating = this.currentRating;
        reviewService.updateTagLikes(currentUserId, new ArrayList<>(List.of(tag)), previouslyLikedTags, newRating, previousRating); // Only that tag is now liked/disliked
        this.currentRating = newRating;
    }

    @Then("The {string} tag is liked or disliked successfully within the database")
    public void tagIsLikedSuccessfully(String tag) {
        HashMap<String, Integer> likedTags = logWineDao.getLikedTags(currentUserId, wineId, true);
        assertEquals(likedTags.get(tag), reviewService.getRatingWeight(currentRating)); // have to get rating weight as rating isn't 1:1, we alter slightly for the recommendations algorithm
    }

    @Then("All the tags are liked successfully within the database")
    public void allTagsAreLikedSuccessfully() {
    }

    @Then("Only the liked tags are liked successfully within the database")
    public void onlyLikedTagsAreLikedSuccessfully() {
    }

    @Then("Only the latest liked tag is liked successfully within the database")
    public void onlyLatestLikedTagIsLikedSuccessfully() {
    }
}
