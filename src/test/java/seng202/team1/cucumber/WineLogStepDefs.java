package seng202.team1.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.models.Review;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.repository.DAOs.LogWineDao;
import seng202.team1.services.WineLoggingPopupService;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Step operations for the scenario tests for when user logs a wine.
 *
 * @author Wen Sheng Thong
 */
public class WineLogStepDefs {
    private DatabaseManager databaseManager;
    static WineLoggingPopupService wineLoggingPopupService;
    static LogWineDao logWineDao;
    private int uid;
    private String description;
    private ArrayList<String> wineTags;
    private ArrayList<String> selectedTags;
    private int wid;
    private int rating;
    private boolean selectedTag;
    private int oldrating;

    private void initialise() throws InstanceAlreadyExistsException {
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        DatabaseManager.getInstance().forceReset();
        wineLoggingPopupService = new WineLoggingPopupService();
        logWineDao = new LogWineDao();
        this.wid = 0;
        this.rating = 0;
        this.uid = 0;
        this.description = "";
        this.wineTags = new ArrayList<>();
        this.selectedTags = new ArrayList<>();
        this.selectedTag = false;
    }

    @Given("I am viewing a wine #{int} with tags {string}, {string} and {string} as user {int}")
    public void iViewWineWithTags(Integer wid, String tag1, String tag2, String tag3, Integer uid) throws InstanceAlreadyExistsException {
        initialise();
        this.wid = wid;
        this.uid = uid;
        wineTags.add(tag1);
        wineTags.add(tag2);
        wineTags.add(tag3);
    }

    @When("I have previously rated it a {int} with the description {string}")
    public void previouslyLoggedWine(Integer oldRating, String oldDescription) {
        this.oldrating = oldRating;
        this.description = oldDescription;
        for (String tag : wineTags) {
            logWineDao.likes(this.uid, tag, this.oldrating - 3);
        }
        wineLoggingPopupService.submitLog(this.oldrating,this.uid,this.wid,this.wineTags,false,this.description);
    }

    @When("I rate it a {int}")
    public void iRateWine(Integer rating) {
        this.rating = rating;
    }

    @When("I select the tag {string}")
    public void selectedTag(String tag) {
        this.selectedTag = true;
        this.selectedTags.add(tag);
    }
    @When("I leave the review description {string}")
    public void leaveReview(String review) {
        this.description = review.replaceAll("\\s+", " ");
    }
    @When("I click submit log")
    public void submitedLog() {
        if (this.selectedTag) {
            for (String tag : selectedTags) {
                logWineDao.likes(this.uid, tag, this.rating - 3);
            }
            wineLoggingPopupService.submitLog(this.rating,this.uid,this.wid,this.selectedTags,false,this.description);
        } else {
            for (String tag : wineTags) {
                logWineDao.likes(this.uid, tag, this.rating - 3);
            }
            wineLoggingPopupService.submitLog(this.rating,this.uid,this.wid,this.wineTags,true,this.description);
        }
    }

    /**
     * Checks that the review and likes value are correctly submitted into the database
     */
    @Then("The log is submitted successfully")
    public void checkSubmittedLog() {
        ArrayList<String> tagsToCheck;
        if (selectedTag) {
            tagsToCheck = selectedTags;
        } else {
            tagsToCheck = wineTags;
        }
        HashMap<String, Integer> result = logWineDao.getLikedTags(this.uid, true);
        for (String checkTag: tagsToCheck) {
            Assertions.assertTrue(result.containsKey(checkTag));
            Assertions.assertEquals(rating - 3 , result.get(checkTag));
        }
        ArrayList<Review> reviews = logWineDao.getUserReview(this.uid,1, true);
        Assertions.assertEquals(rating,reviews.getFirst().getRating());
        Assertions.assertEquals(wid, reviews.getFirst().getWid());
        Assertions.assertEquals(uid, reviews.getFirst().getUid());
        Assertions.assertEquals(description, reviews.getFirst().getReviewDescription());
    }

    @Then("The log is properly updated")
    public void checkUpdatedLog() {
        ArrayList<String> tagsToCheck = wineTags;
        HashMap<String, Integer> result = logWineDao.getLikedTags(this.uid, true);
        for (String checkTag: tagsToCheck) {
            Assertions.assertTrue(result.containsKey(checkTag));
            Assertions.assertEquals((rating + oldrating) - 3 - 3, result.get(checkTag));
        }
        ArrayList<Review> reviews = logWineDao.getUserReview(this.uid,1, true);
        Assertions.assertEquals(rating,reviews.getFirst().getRating());
        Assertions.assertEquals(wid, reviews.getFirst().getWid());
        Assertions.assertEquals(uid, reviews.getFirst().getUid());
        Assertions.assertEquals(description, reviews.getFirst().getReviewDescription());
    }
}
