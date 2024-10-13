package seng202.team1.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.models.Review;
import seng202.team1.repository.DAOs.LogWineDao;
import seng202.team1.repository.DAOs.SearchDAO;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.services.ReviewService;
import seng202.team1.services.TagRankingService;

import java.util.ArrayList;
import java.util.HashMap;

public class ReviewStepDefs {
    static ReviewService reviewService;
    static LogWineDao logWineDao;
    static TagRankingService tagRankingService;
    private int currentUser;
    private ArrayList<String> wineTags;
    private ArrayList<String> selectedTags;
    private String description;
    private int wid;
    private boolean isEditing;
    private Review review;

    private void initialize() throws InstanceAlreadyExistsException {
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        DatabaseManager.getInstance().forceReset();
        reviewService = new ReviewService();
        logWineDao = new LogWineDao();
        tagRankingService = new TagRankingService();
        currentUser = 0;
        resetReviewValues();
    }

    public void resetReviewValues() {
        review = null;
        isEditing = false;
        wineTags = new ArrayList<>();
        selectedTags = new ArrayList<>();
        description = "";
    }

    @Given("User {int} is logged in")
    public void userLogin(int uid) throws InstanceAlreadyExistsException {
        initialize();
        currentUser = uid;
    }

    @When("User {int} reviews wine #{int} with tags {listOfTags}")
    public void userReviewingWine(int uid, int wid, ArrayList<String> tags) {
        this.currentUser = uid;
        this.wid = wid;
        wineTags = tags;
    }

    @When("selects the tags {listOfTags}")
    public void setSelectedTags(ArrayList<String> selectedTags) {
        this.selectedTags = selectedTags;
    }

    @When("rates it a {int} and submits the review")
    public void submitReview(int rating) {
        ArrayList<String> tagsToLiked = selectedTags;
        boolean noneSelected = selectedTags.isEmpty();
        if (noneSelected) {
            tagsToLiked = wineTags;
        }

        reviewService.updateTagLikes(currentUser, wid, tagsToLiked, rating);

        reviewService.submitLog(rating, currentUser, wid, selectedTags, tagsToLiked, noneSelected, description );
        resetReviewValues();
    }

    @When("later edits the review for wine #{int} with tags {listOfTags}")
    public void startEditingReview(int wid, ArrayList<String> tags) {
        resetReviewValues();
        this.wid = wid;
        this.wineTags = tags;
        isEditing = true;
    }

    @When("enters the description {string}")
    public void setDescription(String desc) {
        this.description = desc;
    }

    @Then("User {int} relationship with the tags {listOfTags} is {int}")
    public void checkTagRelation(int uid, ArrayList<String> tagsToCheck, int weight) {
        HashMap<String, Integer> userTags = logWineDao.getLikedTags(uid, SearchDAO.UNLIMITED, false);
        for (String tag : tagsToCheck) {
            Assertions.assertEquals(userTags.get(tag), weight);
        }
    }

    @Then("User {int} has no relationship with the tags {listOfTags}")
    public void checkNoTagRelation(int uid, ArrayList<String> tagsToCheck) {
        HashMap<String, Integer> userTags = logWineDao.getLikedTags(uid, SearchDAO.UNLIMITED, false);
        for (String tag : tagsToCheck) {
            Assertions.assertFalse(userTags.containsKey(tag));
        }
    }

    @Then("User {int} has 1 review with wine #{int} with description {string}")
    public void checkReviewExist(int uid, int wid, String desc) {
        Review review = reviewService.getReview(uid, wid);
        Assertions.assertNotNull(review);
        Assertions.assertEquals(desc, review.getDescription());
    }

    @Then("User {int} has no review with wine #{int}")
    public void checkNoReview(int uid, int wid) {
        Assertions.assertNull(reviewService.getReview(uid, wid));
    }

    @Then("User {int} can see the review for wine #{int} in the review page")
    public void viewingReview(int uid, int wid) {
        this.review = reviewService.getReview(uid, wid);
    }

    @Then("{int} out of five stars are filled")
    public void checkStars(int numStars) {
        Assertions.assertEquals(review.getRating(), numStars);
    }

    @Then("no tags are indicated to be liked")
    public void noTagsSelected() {
        Assertions.assertTrue(review.getTagsSelected().isEmpty());
    }

    @Then("tags liked are {listOfTags}")
    public void checkTags(ArrayList<String> tagsToCheck) {
        Assertions.assertArrayEquals(review.getTagsSelected().toArray(),tagsToCheck.toArray());
    }

    @Then("description says {string}")
    public void checkReviewDesc(String desc) {
        Assertions.assertEquals(review.getDescription(), desc);
    }
}
