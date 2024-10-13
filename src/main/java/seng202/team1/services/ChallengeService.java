package seng202.team1.services;

import seng202.team1.models.User;
import seng202.team1.models.Wine;
import seng202.team1.repository.DAOs.ChallengeDAO;

import java.util.*;

/**
 * Service class for the challenge tracker feature.
 */
public class ChallengeService {

    private ArrayList<String> varieties = new ArrayList<>(Arrays.asList("merlot", "pinot gris", "chardonnay", "red blend", "rose"));
    private ArrayList<String> years = new ArrayList<>(Arrays.asList("1989", "1990", "2000", "2010", "2005"));
    private ArrayList<String> reds = new ArrayList<>();
    private ArrayList<String> whites = new ArrayList<>();
    private ArrayList<String> rose = new ArrayList<>();

    private final ChallengeDAO challengeDAO = new ChallengeDAO();
    private WineVarietyService wineVarietyService = new WineVarietyService();

    private Random random = new Random();

    /**
     * Default constructor for ChallengeService
     */
    public ChallengeService(){}

    /**
     * Calls the challengeDAO to update the database so that the user has the variety challenge as an active challenge.
     */
    public void startChallengeVariety()
    {
        ArrayList<Integer> wineIDs = getWinesforChallenge(varieties);
        challengeDAO.userActivatesChallenge(User.getCurrentUser().getId(), "Variety Challenge", wineIDs);
    }

    /**
     * calls challengeDAO to make the time travelling challenge and active challenge.
     */
    public void startChallengeYears()
    {
        ArrayList<Integer> wineIDs = getWinesforChallenge(years);
        challengeDAO.userActivatesChallenge(User.getCurrentUser().getId(), "Time Travelling Challenge", wineIDs);
    }

    /**
     * calls the challengeDAO to make red challenge an active challenge, sets the name.
     */
    public void startChallengeReds()
    {
        reds.clear();
        ArrayList<String> redsList = new ArrayList<>(wineVarietyService.getReds());
        for (int i = 0; i < 5; i++) {
            reds.add(redsList.get(i));
        }
        ArrayList<Integer> wineIDs = getWinesforChallenge(reds);
        challengeDAO.userActivatesChallenge(User.getCurrentUser().getId(), "Red Roulette Challenge", wineIDs);


    }

    /**
     * calls the challengeDAO to make white challenge an active challenge, sets the name.
     */

    public void startChallengeWhites()
    {
        whites.clear();
        ArrayList<String> whitesList = new ArrayList<>(wineVarietyService.getWhites());
        for (int i = 0; i < 5; i++) {
            whites.add(whitesList.get(i));
        }
        ArrayList<Integer> wineIDs = getWinesforChallenge(whites);
        challengeDAO.userActivatesChallenge(User.getCurrentUser().getId(), "Great White Challenge", wineIDs);
    }

    /**
     * calls the challengeDAO to make rose challenge an active challenge, sets the name.
     */

    public void startChallengeRose()
    {
        rose.clear();
        ArrayList<String> roseList = new ArrayList<>(wineVarietyService.getRose());
        for (int i = 0; i < 5; i++) {
            rose.add(roseList.get(i));
        }
        ArrayList<Integer> wineIDs = getWinesforChallenge(rose);
        challengeDAO.userActivatesChallenge(User.getCurrentUser().getId(), "Rosè challenge", wineIDs);
    }

    /**
     * calls the challenge complete method in the challenge dao.
     * @param cname the name of the challenge completed.
     */
    public void challengeCompleted(String cname)
    {
        challengeDAO.challengeCompleted(User.getCurrentUser().getId(), cname);
    }

    /**
     * chose 5 random wines of the set type and returns there ids in an array list.
     * @param types is an {@link ArrayList<String>} that are the type of wines for the challenge.
     * @return {@link ArrayList<Integer>} of wine ids
     */
    public ArrayList<Integer> getWinesforChallenge(ArrayList<String> types)
    {
        ArrayList<Integer> wines = new ArrayList<>();
        for (int i = 0; i < types.size(); i ++) {
            int wine = 0;
            boolean wineInvalid = true;
            SearchWineService.getInstance().searchWinesByTags(types.get(i), 100);
            while (wineInvalid) {
                Random random = new Random();
                wine = SearchWineService.getInstance().getWineList().get(random.nextInt(SearchWineService.getInstance().getWineList().size())).getWineId();
                boolean winefound = false;
                for (Integer integer : wines) {
                    if (wine == integer) {
                        winefound = true;
                        break;
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
     * Checks to see if user has an active challenge.
     * @return boolean, true if the user has an active challenge.
     */
    public boolean activeChallenge() {
        return challengeDAO.getChallengeForUser(User.getCurrentUser().getId()) != null;
    }

    /**
     * Gets the wines for the challenge that the user has active.
     * @return {@link ArrayList<Wine>} of the wines for the challenge the user is participating in.
     */
    public ArrayList<Wine> challengeWines() {
        return challengeDAO.getWinesInChallenge(challengeDAO.getChallengeForUser(User.getCurrentUser().getId()), User.getCurrentUser().getId());
    }

    /**
     * Gets the name of the challenge the user has active.
     * @return string name of users active challenge.
     */
    public String usersChallenge() {
        return challengeDAO.getChallengeForUser(User.getCurrentUser().getId());
    }
}