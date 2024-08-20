package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import seng202.team0.services.QuizService;

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

    QuizService quizService = new QuizService();

    private ArrayList<String> questions;
    private ArrayList<String> answer1answers;
    private ArrayList<String> answer2answers;
    private ArrayList<String> answer3answers;
    private ArrayList<String> answer4answers;

    int questionNumber = 0;

    public void setLabels() {
        if (questionNumber < questions.size()) {
            questionLabel.setText(questions.get(questionNumber));
            answer1Button.setText(answer1answers.get(questionNumber));
            answer2Button.setText(answer2answers.get(questionNumber));
            answer3Button.setText(answer3answers.get(questionNumber));
            answer4Button.setText(answer4answers.get(questionNumber));
        } else {
            questionLabel.setOpacity(0);
            answer1Button.setDisable(true);
            answer2Button.setDisable(true);
            answer3Button.setDisable(true);
            answer4Button.setDisable(true);
            answer1Button.setOpacity(0);
            answer2Button.setOpacity(0);
            answer3Button.setOpacity(0);
            answer4Button.setOpacity(0);
        }
    }
    @FXML
    public void initialize() {

        questionLabel.setOpacity(1);
        answer1Button.setDisable(false);
        answer2Button.setDisable(false);
        answer3Button.setDisable(false);
        answer4Button.setDisable(false);
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

    @FXML
    public void onAnswer1Clicked() {
        questionNumber++;
        setLabels();
    }

    @FXML
    public void onAnswer2Clicked() {
        questionNumber++;
        setLabels();
    }

    @FXML
    public void onAnswer3Clicked() {
        questionNumber++;
        setLabels();
    }

    @FXML
    public void onAnswer4Clicked() {
        questionNumber++;
        setLabels();
    }

}
