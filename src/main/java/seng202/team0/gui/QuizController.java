package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.Arrays;

public class QuizController {

    @FXML
    Label questionLabel;
    @FXML
    Button answer1Button;
    @FXML
    Button answer2Button;
    @FXML
    Button answer3Button;
    @FXML
    Button answer4Button;

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

    int questionNumber = 0;

    public void setLabels(){
        questionLabel.setText(questions.get(questionNumber));
        answer1Button.setText(answer1answers.get(questionNumber));
        answer2Button.setText(answer2answers.get(questionNumber));
        answer3Button.setText(answer3answers.get(questionNumber));
        answer4Button.setText(answer4answers.get(questionNumber));
    }
    @FXML
    public void initialize() {
        setLabels();
    }

    @FXML
    public void onAnswer1Clicked(){
        questionNumber++;
        setLabels();
    }
    @FXML
    public void onAnswer2Clicked(){
        questionNumber++;
        setLabels();
    }
    @FXML
    public void onAnswer3Clicked(){
        questionNumber++;
        setLabels();
    }
    @FXML
    public void onAnswer4Clicked(){
        questionNumber++;
        setLabels();
    }

}
