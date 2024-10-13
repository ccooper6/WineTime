package seng202.team1.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import seng202.team1.exceptions.InstanceAlreadyExistsException;
import seng202.team1.models.Wine;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.services.QuizService;

import java.util.ArrayList;
import java.util.Arrays;

public class QuizStepDefs {

    private ArrayList<Integer> answers;
    private Wine recommendedWine = null;
    private QuizService quizService;

    public void initialise() throws InstanceAlreadyExistsException {
        DatabaseManager.removeInstance();
        DatabaseManager.initialiseInstanceWithUrl("jdbc:sqlite:./src/test/resources/test_database.db");
        DatabaseManager.getInstance().forceReset();
        quizService = new QuizService();
    }

    @Given("The user has completed the quiz with answers {int}, {int}, {int}, {int}, {int}")
    public void iCompleteTheQuiz(int int1, int int2, int int3, int int4, int int5) throws InstanceAlreadyExistsException {
        initialise();
        answers = new ArrayList<>(Arrays.asList(int1, int2, int3, int4, int5));
    }

    @When("The user gets their recommended wine")
    public void iGetMyRecommendedWine() {
        quizService.setUserAnswers(answers);
        quizService.getQuizWine();
        recommendedWine = quizService.getWine();
    }

    @Then("The wine matches the users answers - vintage is between {int} and {int} and country is {string}")
    public void theWineMatchesTheUsersAnswers(int lowerBound, int upperBound, String country) {
        assert recommendedWine != null;
        assert recommendedWine.getVintage() >= lowerBound && recommendedWine.getVintage() <= upperBound;
        assert recommendedWine.getCountry().equals(country);
    }
}
