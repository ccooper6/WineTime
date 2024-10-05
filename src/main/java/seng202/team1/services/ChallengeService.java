package seng202.team1.services;

import javafx.scene.layout.AnchorPane;
import seng202.team1.gui.FXWrapper;
import seng202.team1.gui.controllers.NavigationController;
import seng202.team1.models.User;
import seng202.team1.models.Wine;
import seng202.team1.repository.DAOs.ChallengeDAO;
import seng202.team1.repository.DAOs.SearchDAO;

import java.util.*;

/**
 * Service class for the challenge tracker feature.
 * @author Lydia Jackson
 */
public class ChallengeService {

    private ArrayList<String> varities = new ArrayList<>(Arrays.asList("merlot", "pinot gris", "chardonnay", "red blend", "rose"));
    private ArrayList<String> years = new ArrayList<>(Arrays.asList("1989", "1990", "2000", "2010", "2005"));
    private ArrayList<String> reds = new ArrayList<>(Arrays.asList("red", "red", "red", "red", "red"));
    private ArrayList<String> whites = new ArrayList<>(Arrays.asList("whites", "whites", "whites", "whites", "whites"));
    private ArrayList<String> rose = new ArrayList<>(Arrays.asList("rose", "rose", "rose", "rose", "rose"));

    private final ChallengeDAO chalDao = new ChallengeDAO();

    private SearchDAO searchDAO = new SearchDAO();

    /**
     * Calls the challengeDAO to update the database so that the user has the variety challenge as an active challenge.
     */
    public void startChallengeVariety()
    {
        ArrayList<Integer> wineids = getWinesforChallenge(varities, "tags");
        chalDao.userActivatesChallenge(User.getCurrentUser().getId(), "Variety Challenge", wineids);
    }

    /**
     * calls challengeDAO to make the time traveeliing challenge and active challenge.
     */
    public void startChallengeYears()
    {
        ArrayList<Integer> wineids = getWinesforChallenge(years, "tags");
        chalDao.userActivatesChallenge(User.getCurrentUser().getId(), "Time Travelling Challenge", wineids);
    }

    public void startChallengeReds()
    {
        ArrayList<Integer> wineids = getWinesforChallenge(reds, "name");
        chalDao.userActivatesChallenge(User.getCurrentUser().getId(), "Red Roulette Challenge", wineids);
    }

    public void startChallengeWhites()
    {
        ArrayList<Integer> wineids = getWinesforChallenge(whites, "name");
        chalDao.userActivatesChallenge(User.getCurrentUser().getId(), "Great White Challenge", wineids);
    }

    public void startChallengeRose()
    {
        ArrayList<Integer> wineids = getWinesforChallenge(rose, "name");
        chalDao.userActivatesChallenge(User.getCurrentUser().getId(), "Rose challenge", wineids);
    }

    /**
     * calls the challenge complete method in the challenge dao.
     * @param cname the name of the challenge.
     */
    public void challengeCompleted(String cname)
    {
        chalDao.challengeCompleted(User.getCurrentUser().getId(), cname);
    }

    /**
     * chose 5 random wines of different variety and returns there ids in an array list.
     * @return ArrayList<Integer> list of wine ids </Integer>
     */
    public ArrayList<Integer> getWinesforChallenge(ArrayList<String> types, String searchtype) {
        ArrayList<Integer> wines = new ArrayList<>();
        if (searchtype == "name") {
            SearchWineService.getInstance().searchWinesByName(types.get(0), 100);
        }
        for (int i = 0; i < types.size(); i ++) {
            int wine = 0;
            boolean wineInvalid = true;
            if (searchtype == "tags") {
                SearchWineService.getInstance().searchWinesByTags(types.get(i), 100);
            }
            while (wineInvalid) {
                Random random = new Random();
                wine = SearchWineService.getInstance().getWineList().get(random.nextInt(SearchWineService.getInstance().getWineList().size())).getWineId();
                boolean winefound = false;
                for (int l = 0; l < wines.size(); l++) {
                    if (wine == wines.get(l)) {
                        winefound = true;
                    }
                }
                if (!winefound) {
                    wineInvalid = false;
                }
            }
            wines.add(wine);
            }
        return wines;
    }

    /**
     * Launches/resets the profile.
     */
    public void launchProfile() { // take this out
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.closePopUp();
        navigationController.loadPageContent("profile");
    }

    /**
     * Launches the select challenge pop up.
     */
    public void launchSelectChallenge() { ///take this out
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.closePopUp();
        navigationController.loadSelectChallengePopUpContent();
    }

    /**
     * Closes the select challenge pop up.
     */
    public void closeSelectChallenge() {
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.closePopUp();
    }


    /**
     * Checks to see if user has the variety challenge as an active challenge.
     * @return true if the user has the variety challenge.
     */
    public boolean activeChallenge() {
        if (chalDao.getChallengeForUser(User.getCurrentUser().getId()) != null) {
            return true;
        }
        return false;
    }

    /**
     * Gets the wines for the challenge that the user has active.
     * @return arraylist of the wines for the challenge the user is participating in.
     */
    public ArrayList<Wine> challengeWines() {
        return chalDao.getWinesForChallenge(chalDao.getChallengeForUser(User.getCurrentUser().getId()), User.getCurrentUser().getId());
    }

    /**
     * Gets the name of the challenge the user has active.
     * @return string name of users active challenge.
     */
    public String usersChallenge() {
        return chalDao.getChallengeForUser(User.getCurrentUser().getId());
    }


}






