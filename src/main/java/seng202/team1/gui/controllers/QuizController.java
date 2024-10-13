package seng202.team1.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.models.User;
import seng202.team1.services.QuizService;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Controller class for the quiz.fxml page.
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

    private final QuizService QUIZSERVICE = new QuizService();
    private ArrayList<String> questions;
    private ArrayList<String> answer1answers;
    private ArrayList<String> answer2answers;
    private ArrayList<String> answer3answers;
    private ArrayList<String> answer4answers;
    private int questionNumber = 0;

    private static final Logger LOG = LogManager.getLogger(QuizController.class);

    /**
     * Default constructor for QuizController.
     */
    public QuizController() {}

    /**
     * Initializes the quiz controller.
     */
    @FXML
    public void initialize() {
        questionLabel.setOpacity(1);
        showWineButton.setDisable(true);
        showWineButton.setOpacity(0);

        ArrayList<Button> answerButtons = new ArrayList<>(Arrays.asList(answer1Button, answer2Button, answer3Button, answer4Button));
        for (Button button : answerButtons) {
            button.setDisable(false);
            button.setOpacity(1);
        }

        this.questions = QUIZSERVICE.getQuestions();
        this.answer1answers = QUIZSERVICE.getQuestion1Answers();
        this.answer2answers = QUIZSERVICE.getQuestion2Answers();
        this.answer3answers = QUIZSERVICE.getQuestion3Answers();
        this.answer4answers = QUIZSERVICE.getQuestion4Answers();

        setLabels();
    }

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
            ArrayList<Button> answerButtons = new ArrayList<>(Arrays.asList(answer1Button, answer2Button, answer3Button, answer4Button));
            for (Button button : answerButtons) {
                button.setDisable(true);
                button.setOpacity(0);
            }
            showWineButton.setOpacity(1);
        }
    }

    /**
     * Sends the user to the profile screen using quizService and displays the relevant wine.
     */
    @FXML
    public void onShowWineClicked() {
        LOG.info("Showing quiz wine for user {}", User.getCurrentUser().getName());
        questionLabel.setOpacity(0);
        showWineButton.setDisable(true);
        QUIZSERVICE.launchWinePopup();
    }

    /**
     * Handles the event when the first answer is clicked.
     */
    @FXML
    public void onAnswer1Clicked() {
        QUIZSERVICE.getUserAnswers().set(questionNumber++, 1);
        setLabels();
    }

    /**
     * Handles the event when the second answer is clicked.
     */
    @FXML
    public void onAnswer2Clicked() {
        QUIZSERVICE.getUserAnswers().set(questionNumber++, 2);
        setLabels();
    }

    /**
     * Handles the event when the third answer is clicked.
     */
    @FXML
    public void onAnswer3Clicked() {
        QUIZSERVICE.getUserAnswers().set(questionNumber++, 3);
        setLabels();
    }

    /**
     * Handles the event when the fourth answer is clicked.
     */
    @FXML
    public void onAnswer4Clicked() {
        QUIZSERVICE.getUserAnswers().set(questionNumber++, 4);
        setLabels();
    }

    /**
     * Handles what to do when the back button is clicked, alters question number, resets labels.
     */
    @FXML
    public void onBackButtonClicked() {
        if (questionNumber > 0) {
            questionNumber--;
            setLabels();
        }
    }
}
