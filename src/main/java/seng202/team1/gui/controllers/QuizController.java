package seng202.team1.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import seng202.team1.services.QuizService;

import java.util.ArrayList;

/**
 * Controller class for the quiz.fxml page.
 *
 * @author Isaac Macdonald
 */
public class QuizController {
    @FXML
    private Label questionLabel;
    @FXML
    private Button answer1Button;
    @FXML
    private Button answer2Button;
    @FXML
    private Button answer3Button;
    @FXML
    private Button answer4Button;
    @FXML
    private Button showWineButton;
    @FXML
    private Button backButton;

    private final QuizService quizService = new QuizService();

    private ArrayList<String> questions;
    private ArrayList<String> answer1answers;
    private ArrayList<String> answer2answers;
    private ArrayList<String> answer3answers;
    private ArrayList<String> answer4answers;

    private int questionNumber = 0;

    /**
     * Sets the labels for the quiz.
     */
    public void setLabels() {
        if (questionNumber < questions.size()) {
            if (questionNumber == 0) {
                backButton.setDisable(true);
                backButton.setOpacity(0);
            } else {
                backButton.setDisable(false);
                backButton.setOpacity(1);
            }
            questionLabel.setText(questions.get(questionNumber));
            answer1Button.setText(answer1answers.get(questionNumber));
            answer2Button.setText(answer2answers.get(questionNumber));
            answer3Button.setText(answer3answers.get(questionNumber));
            answer4Button.setText(answer4answers.get(questionNumber));
        } else {
            backButton.setDisable(true);
            backButton.setOpacity(0);
            questionLabel.setText("Your wine has been carefully selected.");
            showWineButton.setDisable(false);
            answer1Button.setDisable(true);
            answer2Button.setDisable(true);
            answer3Button.setDisable(true);
            answer4Button.setDisable(true);
            showWineButton.setOpacity(1);
            answer1Button.setOpacity(0);
            answer2Button.setOpacity(0);
            answer3Button.setOpacity(0);
            answer4Button.setOpacity(0);

        }
    }

    /**
     * Initializes the controller.
     */
    public void initialize() {
        questionLabel.setOpacity(1);
        showWineButton.setDisable(true);
        answer1Button.setDisable(false);
        answer2Button.setDisable(false);
        answer3Button.setDisable(false);
        answer4Button.setDisable(false);
        showWineButton.setOpacity(0);
        answer1Button.setOpacity(1);
        answer2Button.setOpacity(1);
        answer3Button.setOpacity(1);
        answer4Button.setOpacity(1);

        this.questions = quizService.getQuestions();
        this.answer1answers = quizService.getAnswer1answers();
        this.answer2answers = quizService.getAnswer2answers();
        this.answer3answers = quizService.getAnswer3answers();
        this.answer4answers = quizService.getAnswer4answers();

        setLabels();
    }

    /**
     * Sends the user to the profile screen using quizService and displays their optimal wine.
     */
    @FXML
    public void onShowWineClicked() {
        questionLabel.setOpacity(0);
        showWineButton.setDisable(true);
        quizService.launchWinePopup();
    }

    /**
     * Handles the event when the first answer is clicked.
     */
    @FXML
    public void onAnswer1Clicked() {
        quizService.getRecordOfAnswers().set(questionNumber++, 1);
        setLabels();
    }

    /**
     * Handles the event when the second answer is clicked.
     */
    @FXML
    public void onAnswer2Clicked() {
        quizService.getRecordOfAnswers().set(questionNumber++, 2);
        setLabels();
    }

    /**
     * Handles the event when the third answer is clicked.
     */
    @FXML
    public void onAnswer3Clicked() {
        quizService.getRecordOfAnswers().set(questionNumber++, 3);
        setLabels();
    }

    /**
     * Handles the event when the fourth answer is clicked.
     */
    @FXML
    public void onAnswer4Clicked() {
        quizService.getRecordOfAnswers().set(questionNumber++, 4);
        setLabels();
    }

    /**
     * Handles what to do when the back button is clicked
     */
    @FXML
    public void onBackButtonClicked() {
        if (questionNumber > 0) {
            questionNumber--;
            setLabels();
        }
    }



}
