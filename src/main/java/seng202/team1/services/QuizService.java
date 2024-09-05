package seng202.team1.services;

import seng202.team1.gui.FXWrapper;
import seng202.team1.gui.NavigationController;
import seng202.team1.models.Wine;
import seng202.team1.repository.SearchDAO;
import seng202.team1.repository.WineDAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

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
            "The Incredibles",
            "Fish",
            "Pear",
            "Kiwi",
            "Ravenclaw"
    ));
    ArrayList<String> answer3answers = new ArrayList<>(Arrays.asList(
            "The Dark Knight",
            "Cheesecake",
            "Plum",
            "Iberial Imperial Eagle",
            "Gryffindor"

    ));
    ArrayList<String> answer4answers = new ArrayList<>(Arrays.asList(
            "Whiplash",
            "Sushi",
            "Peach",
            "Gallic Rooster",
            "Slytherin"
    ));

    ArrayList<Integer> recordOfAnswers = new ArrayList<>(Arrays.asList(null, null, null, null, null));
    String earliestYear;
    String type;
    String country;

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

        Wine wine = null;
        ArrayList<Wine> possibleWines = new ArrayList<>();

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
        }

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

        }

        if (!possibleWines.isEmpty()) {
            Random random = new Random();
            wine = possibleWines.get(random.nextInt(possibleWines.size()));
        }
        FXWrapper.getInstance().launchSubPage("profile");
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.initPopUp(wine);

    }
}
