package seng202.team1.services;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import seng202.team1.gui.FXWrapper;
import seng202.team1.gui.NavigationController;
import seng202.team1.models.Wine;
import seng202.team1.repository.SearchDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Service class for the quiz feature.
 * @author Isaac Macdonald, Caleb Cooper, Yuhao Zhang
 */
public class QuizService {
    private Wine wine = null;
    private Stage loadingStage;

    private final ArrayList<String> questions = new ArrayList<>(Arrays.asList(
            "Pick a movie from this great selection",
            "What is your go to food",
            "If you could only pick one fruit for the rest of your life what would it be?",
            "What is the coolest bird.",
            "What Hogwarts house are you?"
    ));
    private final ArrayList<String> answer1answers = new ArrayList<>(Arrays.asList(
            "The Shawshank Redemption",
            "Steak",
            "Banana",
            "Bald Eagle",
            "Hufflepuff"
    ));
    private final ArrayList<String> answer2answers = new ArrayList<>(Arrays.asList(
            "The Incredibles",
            "Fish",
            "Pear",
            "Kiwi",
            "Ravenclaw"
    ));
    private final ArrayList<String> answer3answers = new ArrayList<>(Arrays.asList(
            "The Dark Knight",
            "Cheesecake",
            "Plum",
            "Iberial Imperial Eagle",
            "Gryffindor"

    ));
    private final ArrayList<String> answer4answers = new ArrayList<>(Arrays.asList(
            "Whiplash",
            "Sushi",
            "Peach",
            "Gallic Rooster",
            "Slytherin"
    ));

    private ArrayList<Integer> recordOfAnswers = new ArrayList<>(Arrays.asList(null, null, null, null, null));
    private String earliestYear;
    private String type;
    private String country;

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

    /**
     * The setter for the record of answers.
     * @param answers The record of answers
     */
    public void setRecordOfAnswers(ArrayList<Integer> answers) {
        recordOfAnswers = answers;
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
     * @return The record of answers
     */
    public ArrayList<Integer> getRecordOfAnswers() {
        return recordOfAnswers;
    }

    /**
     * Method to launch the wine popup. Implements a loading screen to show that the app is working
     * in the background even with large wait times.
     */
    public void launchWinePopup() {
        showLoadingScreen();
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                wineAlgorithm();
                Platform.runLater(() -> {
                    FXWrapper.getInstance().launchSubPage("profile");
                    NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
                    navigationController.initPopUp(wine);
                });
                return null;
            }

            @Override
            protected void succeeded() {
                hideLoadingScreen();
            }
        };

        new Thread(task).start();
    }

    /**
     * Method to show the loading screen.
     */
    private void showLoadingScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/loadingScreen.fxml"));
            Parent root = loader.load();
            loadingStage = new Stage();
            loadingStage.setScene(new Scene(root));
            loadingStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to hide the loading screen.
     */
    private void hideLoadingScreen() {
        if (loadingStage != null) {
            loadingStage.close();
        }
    }

    /**
     * Method to run the wine algorithm.
     */
    public void wineAlgorithm() {

        switch (getRecordOfAnswers().get(0)) {
            case 1:
                earliestYear = "1990";
                break;
            case 2:
                earliestYear = "2000";
                break;
            case 3:
                earliestYear = "2005";
                break;
            case 4:
                earliestYear = "2010";
                break;
            default:
                earliestYear = "1990";
                break;
        }

        switch (getRecordOfAnswers().get(1)) {
            case 1:
                type = "Pinot Noir";
                break;
            case 2:
                type = "Sauvignon Blanc";
                break;
            case 3:
                type = "Rosé";
                break;
            case 4:
                type = "Prosecco";
                break;
            default:
                type = "Pinot Noir";
        }
        switch (getRecordOfAnswers().get(3)) {
            case 1:
                country = "US";
                break;
            case 2:
                country = "New Zealand";
                break;
            case 3:
                country = "Spain";
                break;
            case 4:
                country = "France";
                break;
            default:
                country = "US";
        }

        ArrayList<Wine> possibleWines = new ArrayList<>();
        switch (earliestYear) {
            case "1990":
                SearchWineService.getInstance().searchWinesByTags("1990, " + country, SearchDAO.UNLIMITED);
                possibleWines.addAll(SearchWineService.getInstance().getWineList());
                SearchWineService.getInstance().searchWinesByTags("1991, " + country, SearchDAO.UNLIMITED);
                possibleWines.addAll(SearchWineService.getInstance().getWineList());
                SearchWineService.getInstance().searchWinesByTags("1992, " + country, SearchDAO.UNLIMITED);
                possibleWines.addAll(SearchWineService.getInstance().getWineList());
                SearchWineService.getInstance().searchWinesByTags("1993, " + country, SearchDAO.UNLIMITED);
                possibleWines.addAll(SearchWineService.getInstance().getWineList());
                SearchWineService.getInstance().searchWinesByTags("1994, " + country, SearchDAO.UNLIMITED);
                possibleWines.addAll(SearchWineService.getInstance().getWineList());
                SearchWineService.getInstance().searchWinesByTags("1995, " + country, SearchDAO.UNLIMITED);
                possibleWines.addAll(SearchWineService.getInstance().getWineList());
                SearchWineService.getInstance().searchWinesByTags("1996, " + country, SearchDAO.UNLIMITED);
                possibleWines.addAll(SearchWineService.getInstance().getWineList());
                SearchWineService.getInstance().searchWinesByTags("1997, " + country, SearchDAO.UNLIMITED);
                possibleWines.addAll(SearchWineService.getInstance().getWineList());
                SearchWineService.getInstance().searchWinesByTags("1998, " + country, SearchDAO.UNLIMITED);
                possibleWines.addAll(SearchWineService.getInstance().getWineList());
                SearchWineService.getInstance().searchWinesByTags("1999, " + country, SearchDAO.UNLIMITED);
                possibleWines.addAll(SearchWineService.getInstance().getWineList());
                break;

            case "2000":
                SearchWineService.getInstance().searchWinesByTags("2000, " + country, SearchDAO.UNLIMITED);
                possibleWines.addAll(SearchWineService.getInstance().getWineList());
                SearchWineService.getInstance().searchWinesByTags("2001, " + country, SearchDAO.UNLIMITED);
                possibleWines.addAll(SearchWineService.getInstance().getWineList());
                SearchWineService.getInstance().searchWinesByTags("2002, " + country, SearchDAO.UNLIMITED);
                possibleWines.addAll(SearchWineService.getInstance().getWineList());
                SearchWineService.getInstance().searchWinesByTags("2003, " + country, SearchDAO.UNLIMITED);
                possibleWines.addAll(SearchWineService.getInstance().getWineList());
                SearchWineService.getInstance().searchWinesByTags("2004, " + country, SearchDAO.UNLIMITED);
                possibleWines.addAll(SearchWineService.getInstance().getWineList());
                break;

            case "2005":
                SearchWineService.getInstance().searchWinesByTags("2005, " + country, SearchDAO.UNLIMITED);
                possibleWines.addAll(SearchWineService.getInstance().getWineList());
                SearchWineService.getInstance().searchWinesByTags("2006, " + country, SearchDAO.UNLIMITED);
                possibleWines.addAll(SearchWineService.getInstance().getWineList());
                SearchWineService.getInstance().searchWinesByTags("2007, " + country, SearchDAO.UNLIMITED);
                possibleWines.addAll(SearchWineService.getInstance().getWineList());
                SearchWineService.getInstance().searchWinesByTags("2008, " + country, SearchDAO.UNLIMITED);
                possibleWines.addAll(SearchWineService.getInstance().getWineList());
                SearchWineService.getInstance().searchWinesByTags("2009, " + country, SearchDAO.UNLIMITED);
                possibleWines.addAll(SearchWineService.getInstance().getWineList());
                break;

            case "2010":
                SearchWineService.getInstance().searchWinesByTags("2010, " + country, SearchDAO.UNLIMITED);
                possibleWines.addAll(SearchWineService.getInstance().getWineList());
                SearchWineService.getInstance().searchWinesByTags("2011, " + country, SearchDAO.UNLIMITED);
                possibleWines.addAll(SearchWineService.getInstance().getWineList());
                SearchWineService.getInstance().searchWinesByTags("2012, " + country, SearchDAO.UNLIMITED);
                possibleWines.addAll(SearchWineService.getInstance().getWineList());
                SearchWineService.getInstance().searchWinesByTags("2013, " + country, SearchDAO.UNLIMITED);
                possibleWines.addAll(SearchWineService.getInstance().getWineList());
                SearchWineService.getInstance().searchWinesByTags("2014, " + country, SearchDAO.UNLIMITED);
                possibleWines.addAll(SearchWineService.getInstance().getWineList());
                break;
            default:
                break;

        }

        if (!possibleWines.isEmpty()) {
            Random random = new Random();
            wine = possibleWines.get(random.nextInt(possibleWines.size()));
        }
    }
}
