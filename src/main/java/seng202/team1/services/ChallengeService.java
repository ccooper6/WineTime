package seng202.team1.services;

import javafx.scene.layout.AnchorPane;
import seng202.team1.gui.FXWrapper;
import seng202.team1.gui.controllers.NavigationController;
import seng202.team1.models.User;
import seng202.team1.models.Wine;
import seng202.team1.repository.DAOs.ChallengeDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Service class for the challenge tracker feature.
 * @author Lydia Jackson
 */
public class ChallengeService {

    private List<String> varities = List.of("merlot", "pinot gris", "chardonnay", "red blend", "rose");
    private List<String> years = List.of("1989", "1990", "2000", "2010", "2005");
    private List<String> reds = List.of("red", "red", "red", "red", "red");
    private List<String> whites = List.of("whites", "whites", "whites", "whites", "whites");
    private List<String> rose = List.of("rose", "rose", "rose", "rose", "rose");

    private final ChallengeDAO chalDao = new ChallengeDAO();

    /**
     * Calls the challengeDAO to update the database so that the user has the variety challenge as an active challenge.
     */
    public void startChallengeVariety()
    {
        ArrayList<Integer> wineids = getWinesforChallenge(varities);
        chalDao.userActivatesChallenge(User.getCurrentUser().getId(), "Variety Challenge", wineids);
    }

    public void startChallengeYears()
    {
        ArrayList<Integer> wineids = getWinesforChallenge(years);
        chalDao.userActivatesChallenge(User.getCurrentUser().getId(), "Time Travelling Challenge", wineids);
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
    public ArrayList<Integer> getWinesforChallenge(List<String> types) {
        ArrayList<Integer> wines = new ArrayList<>();
        for (int i = 0; i < types.size(); i ++) {
            Random random = new Random();
            SearchWineService.getInstance().searchWinesByTags(types.get(i), 50);
            wines.add(SearchWineService.getInstance().getWineList().get(random.nextInt(SearchWineService.getInstance().getWineList().size())).getWineId());
            System.out.print(wines);
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
        return Objects.equals(chalDao.getChallengeForUser(User.getCurrentUser().getId()), "Variety Challenge");
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






