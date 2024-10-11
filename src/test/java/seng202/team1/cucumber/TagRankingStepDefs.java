package seng202.team1.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import org.junit.jupiter.api.Assertions;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.models.Review;
import seng202.team1.repository.DAOs.LogWineDao;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.services.ReviewService;
import seng202.team1.services.TagRankingService;

import java.util.ArrayList;

public class TagRankingStepDefs {
    static ReviewService reviewService;
    static LogWineDao logWineDao;
    static TagRankingService tagRankingService;
    private int currentUser;
    private void initialize() throws InstanceAlreadyExistsException {
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        DatabaseManager.getInstance().forceReset();
        reviewService = new ReviewService();
        logWineDao = new LogWineDao();
        tagRankingService = new TagRankingService();
        currentUser = 0;
    }

    /**
     * Gets the name of the tags from the pie chart data and returns it as an arraylist
     * @param tagData an observable list of {@link PieChart.Data}
     * @return an ArrayList of tag names
     */
    private ArrayList<String> getTagNamesFromPieData(ObservableList<PieChart.Data> tagData) {
        ArrayList<String> tagNames = new ArrayList<>();
        for (PieChart.Data data : tagData) {
            tagNames.add(data.getName());
        }
        return tagNames;
    }

    @Given("User {int} has no reviews")
    public void setUserNoReviews(int uid) throws InstanceAlreadyExistsException {
        currentUser = uid;
        initialize();
    }

    @Given("User {int} is on the app")
    public void userIsOnApp(int uid) throws InstanceAlreadyExistsException {
        currentUser  = uid;
        initialize();
    }

    @When("User {int} reviewed wine #{int} with tags {listOfTags} with a rating of {int}")
    public void userReviewsWine(int uid, int wid, ArrayList<String> tags, int rating) throws InstanceAlreadyExistsException {
        reviewService.updateTagLikes(uid, wid, tags, rating);
        reviewService.submitLog(rating, uid, wid, tags, tags, false, "Wine is good");
    }

    @When("The profile is viewed for User {int}")
    public void viewProfile(int uid) {
        currentUser = uid;
    }

    @When("User {int} deletes the review for wine #{int}")
    public void deleteReview(int uid, int wid) {
        Review review  = ReviewService.getInstance().getReview(uid, wid);
        ReviewService.getInstance().deleteReview(review);
    }

    @Then("The top 5 liked pie chart is shown with the tags {listOfTags}")
    public void checkTop5Tags(ArrayList<String> tagsToCheck) {
        Assertions.assertTrue(tagRankingService.hasEnoughLikedTags(currentUser));
        ArrayList<String> topTags = getTagNamesFromPieData(tagRankingService.getTopTagData(1, 5));
        Assertions.assertEquals(topTags.size(), tagsToCheck.size());
        for (String tag: tagsToCheck) {
            Assertions.assertTrue(topTags.contains(tag));
        }
    }

    @Then("The top 5 disliked pie chart is shown with the tags {listOfTags}")
    public void checkTop5DislikedTags(ArrayList<String> tagsToCheck) {
        Assertions.assertTrue(tagRankingService.hasEnoughDislikedTags(currentUser));
        ArrayList<String> topTags = getTagNamesFromPieData(tagRankingService.getLowestTagData(1, 5));
        Assertions.assertEquals(topTags.size(), tagsToCheck.size());
        for (String tag: tagsToCheck) {
            Assertions.assertTrue(topTags.contains(tag));
        }
    }

    @Then("The top 5 disliked pie chart is greyed out")
    public void dislikedPieGreyed() {
        Assertions.assertFalse(tagRankingService.hasEnoughDislikedTags(currentUser));
    }

    @Then("The top 5 liked pie chart is greyed out")
    public void likedPieGreyed() {
        Assertions.assertFalse(tagRankingService.hasEnoughLikedTags(currentUser));
    }

    @Then("Both pie charts are not displayed")
    public void checkNoPieChart() {
        Assertions.assertFalse(tagRankingService.hasEnoughLikedTags(currentUser));
        Assertions.assertFalse(tagRankingService.hasEnoughDislikedTags(currentUser));
    }

}
