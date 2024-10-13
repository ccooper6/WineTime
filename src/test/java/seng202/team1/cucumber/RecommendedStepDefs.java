package seng202.team1.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.models.Wine;
import seng202.team1.repository.DAOs.SearchDAO;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.services.ReviewService;
import seng202.team1.services.SearchWineService;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class RecommendedStepDefs {
    private HashMap<Integer, ArrayList<Wine>> recommendedWines = new HashMap<>();

    @Given("The database is reset")
    public void reset()
    {
        DatabaseManager.REMOVE_INSTANCE();
        try {
            DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        } catch (InstanceAlreadyExistsException e) {
            fail("Could not initialise database");
        }
        DatabaseManager.getInstance().forceReset();

        recommendedWines = new HashMap<>();
    }

    @Given("User {int} reviews tags {listOfTags} from wine {int} with rating {int}")
    public void theUserLikesTags(int userID, ArrayList<String> tags, int wineID, int rating)
    {
        ReviewService.getInstance().updateTagLikes(userID, wineID, tags, rating);
        ReviewService.getInstance().submitLog(rating, userID, wineID, tags, tags, false, "");
    }

    @Given("User {int} has tried wines {listOfInts}")
    public void theUserHasTriedWines(int userID, ArrayList<Integer> wineIDs)
    {
        ArrayList<String> empty = new ArrayList<>();

        for (int wineID : wineIDs)
            ReviewService.getInstance().submitLog(3, userID, wineID, empty, empty, false, "");
    }

    @When("User {int} gets their recommended wines")
    public void userGetsRecommendedWine(int userID)
    {
        SearchWineService.getInstance().searchWinesByRecommend(userID, SearchDAO.UNLIMITED); // top few have all tags rest have some tags

        recommendedWines.put(userID, SearchWineService.getInstance().getWineList());
    }

    private boolean hasLikedTag(Wine wine, ArrayList<String> tags)
    {
        for (String tag : tags) {
            if (wine.hasTag(tag))
                return true;
        }

        return false;
    }

    private int countTags(Wine wine, ArrayList<String> tags)
    {
        int count = 0;

        for (String tag : tags) {
            count += wine.hasTag(tag) ? 1 : 0;
        }

        return count;
    }

    @Then("User {int}'s recommended wines should contain tags {listOfTags} but not wines {listOfInts}")
    public void theWinesShouldBe(int userID, ArrayList<String> tags, ArrayList<Integer> wineIDs)
    {
        assertTrue(recommendedWines.get(userID).stream().allMatch(wine -> hasLikedTag(wine, tags)));

        for (int wineID : wineIDs) {
            assertTrue(recommendedWines.get(userID).stream().noneMatch(wine -> wine.getID() == wineID));
        }
    }

    @Then("User {int}'s recommended wines should contain tags {listOfTags} but not tags {listOfTags}")
    public void hasDislikedTags(int userID, ArrayList<String> likedTags, ArrayList<String> dislikedTags)
    {

        assertTrue(recommendedWines.get(userID).stream().allMatch(wine -> hasLikedTag(wine, likedTags)));

        for (String tag : dislikedTags) {
            assertTrue(recommendedWines.get(userID).stream().noneMatch(wine -> wine.hasTag(tag)));
        }
    }

    @Then("User {int}'s recommended wines should be empty")
    public void emptyRecommended(int userID)
    {
        assertEquals(0, recommendedWines.get(userID).size());
    }

    @Then("User {int}'s recommended wines should be sorted by number of liked tags: {listOfTags}")
    public void correctlySortedWines(int userID, ArrayList<String> tags)
    {
        int numLikedTags = 6; // max num of tags

        for (Wine wine : recommendedWines.get(userID)) {
            assertTrue(countTags(wine, tags) <= numLikedTags);

            numLikedTags = countTags(wine, tags);
        }
    }
}
