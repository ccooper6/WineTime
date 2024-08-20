package seng202.team0.services;

import java.util.ArrayList;
import java.util.Arrays;

public class QuizService {

    ArrayList<String> questions = new ArrayList<>(Arrays.asList(
            "You just robbed a chocolate store, what was your choice of poison?",
            "It’s a Saturday night. What are you doing?",
            "If you could only pick one fruit for the rest of your life what would it be?",
            "What burger are you getting at McDonalds?",
            "What Hogwarts house are you?"
    ));

    ArrayList<String> answer1answers = new ArrayList<>(Arrays.asList(
            "White chocolate",
            "In bed with a book",
            "Banana",
            "Quarter pounder",
            "Hufflepuff"
    ));
    ArrayList<String> answer2answers = new ArrayList<>(Arrays.asList(
            "Milk chocolate",
            "Asleep",
            "Pear",
            "McChicken",
            "Ravenclaw"
    ));
    ArrayList<String> answer3answers = new ArrayList<>(Arrays.asList(
            "Dark chocolate",
            "Out on the town",
            "Plum",
            "Big Mac",
            "Gryffindor"

    ));
    ArrayList<String> answer4answers = new ArrayList<>(Arrays.asList(
            "The till",
            "Casino",
            "Peach",
            "Filet o' Fish",
            "Slytherin"
    ));

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
}
