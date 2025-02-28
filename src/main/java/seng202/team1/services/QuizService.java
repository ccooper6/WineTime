package seng202.team1.services;

import javafx.application.Platform;
import seng202.team1.gui.FXWrapper;
import seng202.team1.gui.controllers.NavigationController;
import seng202.team1.models.Wine;
import seng202.team1.repository.DAOs.SearchDAO;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Service class for the quiz feature.
 */
public class QuizService {
    private Wine wine = null;

    private final ArrayList<String> QUESTIONS = new ArrayList<>(Arrays.asList(
            "Pick a movie from this great selection",
            "What is your go to food",
            "If you could only pick one fruit for the rest of your life what would it be?",
            "What is the coolest bird.",
            "What Hogwarts house are you?"
    ));
    private final ArrayList<String> QUESTION1ANSWERS = new ArrayList<>(Arrays.asList(
            "The Shawshank Redemption",
            "Steak",
            "Banana",
            "Bald Eagle",
            "Hufflepuff"
    ));
    private final ArrayList<String> QUESTION2ANSWERS = new ArrayList<>(Arrays.asList(
            "The Incredibles",
            "Fish",
            "Pear",
            "Kiwi",
            "Ravenclaw"
    ));
    private final ArrayList<String> QUESTION3ANSWERS = new ArrayList<>(Arrays.asList(
            "The Dark Knight",
            "Cheesecake",
            "Plum",
            "Iberian Imperial Eagle",
            "Gryffindor"

    ));
    private final ArrayList<String> QUESTION4ANSWERS = new ArrayList<>(Arrays.asList(
            "Whiplash",
            "Sushi",
            "Peach",
            "Gallic Rooster",
            "Slytherin"
    ));

    private ArrayList<Integer> USERANSWERS = new ArrayList<>(Arrays.asList(null, null, null, null, null));

    /**
     * Default constructor for QuizService.
     */
    public QuizService() {}

    /**
     * The getter for the question labels.
     * @return The question labels
     */
    public ArrayList<String> getQuestions() {
        return QUESTIONS;
    }

    /**
     * The getter for the answer 1 labels.
     * @return The answer 1 labels
     */
    public ArrayList<String> getQuestion1Answers() {
        return QUESTION1ANSWERS;
    }

    /**
     * The getter for the answer 2 labels.
     * @return The answer labels
     */
    public ArrayList<String> getQuestion2Answers() {
        return QUESTION2ANSWERS;
    }

    /**
     * The getter for the answer 3 labels.
     * @return The answer 3 labels
     */
    public ArrayList<String> getQuestion3Answers() {
        return QUESTION3ANSWERS;
    }

    /**
     * The getter for the answer 4 labels.
     * @return The answer labels
     */
    public ArrayList<String> getQuestion4Answers() {
        return QUESTION4ANSWERS;
    }

    /**
     * The setter for the record of answers.
     * @param answers The record of answers
     */
    public void setUserAnswers(ArrayList<Integer> answers) {
        USERANSWERS = answers;
    }

    /**
     * Getter for the wine object.
     * @return The wine object
     */
    public Wine getWine() {
        return wine;
    }

    /**
     * The getter for the record of answers.
     * @return ArrayList&lt;Integer&gt; record of answers
     */
    public ArrayList<Integer> getUserAnswers() {
        return USERANSWERS;
    }

    /**
     * Method to launch the wine popup. Implements a loading screen to show that the app is working
     * in the background even with large wait times.
     */
    public void launchWinePopup() {
        NavigationController nav = FXWrapper.getInstance().getNavigationController();
        FXWrapper.getInstance().launchSubPage("profile");
        nav.executeWithLoadingScreen(() -> {
            getQuizWine();
            Platform.runLater(() -> {
                NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
                navigationController.initPopUp(wine);
            });
        });
    }

    /**
     * Method to run the wine algorithm.
     */
    public void getQuizWine() {

        String earliestYear = switch (getUserAnswers().getFirst()) {
            case 1 -> "1990";
            case 2 -> "2000";
            case 3 -> "2005";
            case 4 -> "2010";
            default -> "1990";
        };

        String type = switch (getUserAnswers().get(1)) {
            case 1 -> "pinot noir";
            case 2 -> "chardonnay";
            case 3 -> "rose";
            case 4 -> "sparkling blend";
            default -> "pinot noir";
        };

        String country = switch (getUserAnswers().get(3)) {
            case 1 -> "us";
            case 2 -> "new zealand";
            case 3 -> "spain";
            case 4 -> "france";
            default -> "us";
        };

        ArrayList<Wine> possibleWines = new ArrayList<>();
        switch (earliestYear) {
            case "1990":
                possibleWines = SearchDAO.getInstance().searchWineByTagsAndFilter(new ArrayList<>(List.of(country, type)),
                        0, 100, 1990, 1999, 0, 3300, "", "wine_name");
                break;

            case "2000":
                possibleWines = SearchDAO.getInstance().searchWineByTagsAndFilter(new ArrayList<>(List.of(country, type)),
                        0, 100, 2000, 2004,0, 3300, "", "wine_name");
                break;

            case "2005":
                possibleWines = SearchDAO.getInstance().searchWineByTagsAndFilter(new ArrayList<>(List.of(country, type)),
                        0, 100, 2005, 2010, 0, 3300,"", "wine_name");
                break;

            case "2010":
                possibleWines = SearchDAO.getInstance().searchWineByTagsAndFilter(new ArrayList<>(List.of(country, type)),
                        0, 100, 2010, 2014,0, 3300, null, "wine_name");
                break;
            default:
                break;

        }

        if (possibleWines.isEmpty()) {
            possibleWines = SearchDAO.getInstance().searchWineByTagsAndFilter(new ArrayList<>(),
                    0, 100, 2000, 2014,0, 3300, null, "wine_name");
        }

        Random random = new Random();
        wine = possibleWines.get(random.nextInt(possibleWines.size()));
    }
}
