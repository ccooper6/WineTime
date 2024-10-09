package seng202.team1.cucumber;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.List;

public class TagLikesStepDefs {
    @Given("The user with name {string}, username {string} and password {string} is currently logged in")
    public void iAmLoggedIn(String name, String username, String password) {

    }

    @When("The user is in the wine logging popup with the wine with id {int} which has the tags {string}, {string} and {string}")
    public void iAmInWineLoggingPopup(int wineId, String tag1, String tag2, String tag3) {
    }

    @And("The user likes the tag {string} with a review rating of {int}")
    public void iLikeASingleTag(String tag, int rating) {
    }

    @And("The user dislikes the tag {string} with a review rating of {int}")
    public void iDislikeASingleTag(String tag, int rating) {
    }

    @And("The user likes no tags with a review rating of {int}")
    public void iLikeNoTags(int rating) {
    }

    @And("The user likes the tags {tags} with a review rating of {int}")
    public void iLikeMultipleTags(List<String> tags, int rating) {

    }
}
