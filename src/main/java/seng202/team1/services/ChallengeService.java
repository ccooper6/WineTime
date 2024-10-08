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
    private ArrayList<String> reds = new ArrayList<>();
    private ArrayList<String> whites = new ArrayList<>();
    private ArrayList<String> rose = new ArrayList<>();

    private final ChallengeDAO chalDao = new ChallengeDAO();
    private WineVarietyService wineVarietyService = new WineVarietyService();

    private Random random = new Random();


    /**
     * Calls the challengeDAO to update the database so that the user has the variety challenge as an active challenge.
     */
    public void startChallengeVariety()
    {
        ArrayList<Integer> wineids = getWinesforChallenge(varities);
        chalDao.userActivatesChallenge(User.getCurrentUser().getId(), "Variety Challenge", wineids);
    }

    /**
     * calls challengeDAO to make the time travelling challenge and active challenge.
     */
    public void startChallengeYears()
    {
        ArrayList<Integer> wineids = getWinesforChallenge(years);
        chalDao.userActivatesChallenge(User.getCurrentUser().getId(), "Time Travelling Challenge", wineids);
    }

    /**
     * calls the challengeDAO to make red challenge an active challege, sets the name.
     */
    public void startChallengeReds()
    {
        ArrayList<String> redsList = new ArrayList<>(wineVarietyService.getReds());
        for (int i = 0; i < 5; i++) {
            reds.add(redsList.get(i));
        }
        ArrayList<Integer> wineids = getWinesforChallenge(reds);
        chalDao.userActivatesChallenge(User.getCurrentUser().getId(), "Red Roulette Challenge", wineids);


    }

    /**
     * calls the challengeDAO to make white challenge an active challege, sets the name.
     */

    public void startChallengeWhites()
    {
        ArrayList<String> whitesList = new ArrayList<>(wineVarietyService.getWhites());
        for (int i = 0; i < 5; i++) {
            whites.add(whitesList.get(i));
        }
        ArrayList<Integer> wineids = getWinesforChallenge(whites);
        chalDao.userActivatesChallenge(User.getCurrentUser().getId(), "Great White Challenge", wineids);
    }

    /**
     * calls the challengeDAO to make rose challenge an active challege, sets the name.
     */

    public void startChallengeRose()
    {
        ArrayList<String> roseList = new ArrayList<>(wineVarietyService.getRose());
        for (int i = 0; i < 5; i++) {
            rose.add(roseList.get(i));
        }
        ArrayList<Integer> wineids = getWinesforChallenge(rose);
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
     * chose 5 random wines of the set type and returns there ids in an array list.
     * @param types is an array list of strings that are the type of wines for the challenge.
     * @return ArrayList<Integer> list of wine ids </Integer>
     */
    public ArrayList<Integer> getWinesforChallenge(ArrayList<String> types)
    {
        ArrayList<Integer> wines = new ArrayList<>();
        for (int i = 0; i < types.size(); i ++) {
            int wine = 0;
            boolean wineInvalid = true;
            SearchWineService.getInstance().searchWinesByTags(types.get(i), 100);
            while (wineInvalid) {
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






