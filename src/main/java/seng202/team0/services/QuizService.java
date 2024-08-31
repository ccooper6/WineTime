package seng202.team0.services;

import seng202.team0.gui.FXWrapper;
import seng202.team0.gui.NavigationController;
import seng202.team0.models.Wine;
import seng202.team0.models.testWines.wine1;
import seng202.team0.repository.WineDAO;

import java.util.ArrayList;
import java.util.Arrays;

public class QuizService {

    WineDAO wineDAO = new WineDAO();
    ArrayList<String> questions = new ArrayList<>(Arrays.asList(
            "Pick a movie from this great selection",
            "What is your go to food",
            "If you could only pick one fruit for the rest of your life what would it be?",
            "What is the coolest bird.",
            "What Hogwarts house are you?"
    ));

    ArrayList<String> answer1answers = new ArrayList<>(Arrays.asList(
            "The Shawshank Redemption",
            "Steak",
            "Banana",
            "Bald Eagle",
            "Hufflepuff"
    ));
    ArrayList<String> answer2answers = new ArrayList<>(Arrays.asList(
            "The Dark Knight",
            "Fish",
            "Pear",
            "Kiwi",
            "Ravenclaw"
    ));
    ArrayList<String> answer3answers = new ArrayList<>(Arrays.asList(
            "Parasite",
            "Cheesecake",
            "Plum",
            "Iberial Imperial Eagle",
            "Gryffindor"

    ));
    ArrayList<String> answer4answers = new ArrayList<>(Arrays.asList(
            "Dune: Part Two",
            "Sushi",
            "Peach",
            "Gallic Rooster",
            "Slytherin"
    ));

    ArrayList<Integer> recordOfAnswers = new ArrayList<>(Arrays.asList(null, null, null, null, null));

    /**
     * The getter for the question labels.
     * @return The question labels
     */
    public ArrayList<String> getQuestions() {
        return questions;
    }

    /**
     * The getter for the answer 1 labels.
     * @return The answer 1 labels
     */
    public ArrayList<String> getAnswer1answers() {
        return answer1answers;
    }

    /**
     * The getter for the answer 2 labels.
     * @return The answer labels
     */
    public ArrayList<String> getAnswer2answers() {
        return answer2answers;
    }

    /**
     * The getter for the answer 3 labels.
     * @return The answer 3 labels
     */
    public ArrayList<String> getAnswer3answers() {
        return answer3answers;
    }

    /**
     * The getter for the answer 4 labels.
     * @return The answer labels
     */
    public ArrayList<String> getAnswer4answers() {
        return answer4answers;
    }

    public ArrayList<Integer> getRecordOfAnswers() {
        return recordOfAnswers;
    }

    /**
     * The getter for the record of answers.
     */

    public void launchWinePopup() {

        Wine wine = new wine1();
        FXWrapper.getInstance().launchSubPage("profile");
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.initPopUp(wine);

    }
}
