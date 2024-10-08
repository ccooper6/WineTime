package seng202.team1.services;

import seng202.team1.models.User;
import seng202.team1.models.Wine;
import seng202.team1.repository.DAOs.ChallengeDAO;

import java.util.*;

/**
 * Service class for the challenge tracker feature.
 * @author Lydia Jackson
 */
public class ChallengeService {

    private final ArrayList<String> varieties = new ArrayList<>(Arrays.asList("merlot", "pinot gris", "chardonnay", "red blend", "rose"));
    private final ArrayList<String> years = new ArrayList<>(Arrays.asList("1989", "1990", "2000", "2010", "2005"));
    private final ArrayList<String> reds = new ArrayList<>(Arrays.asList("red", "red", "red", "red", "red"));
    private final ArrayList<String> whites = new ArrayList<>(Arrays.asList("whites", "whites", "whites", "whites", "whites"));
    private final ArrayList<String> rose = new ArrayList<>(Arrays.asList("rose", "rose", "rose", "rose", "rose"));

    private final ChallengeDAO challengeDAO = new ChallengeDAO();

    /**
     * Calls the challengeDAO to update the database so that the user has the variety challenge as an active challenge.
     */
    public void startChallengeVariety()
    {
        ArrayList<Integer> wineIDs = getWinesForChallenge(varieties, "tags");
        challengeDAO.userActivatesChallenge(User.getCurrentUser().getId(), "Variety Challenge", wineIDs);
    }

    /**
     * calls challengeDAO to make the time travelling challenge and active challenge.
     */
    public void startChallengeYears()
    {
        ArrayList<Integer> wineIDs = getWinesForChallenge(years, "tags");
        challengeDAO.userActivatesChallenge(User.getCurrentUser().getId(), "Time Travelling Challenge", wineIDs);
    }

    /**
     * calls the challengeDAO to make red challenge an active challenge, sets the name.
     */
    public void startChallengeReds()
    {
        ArrayList<Integer> wineids = getWinesForChallenge(reds, "name");
        challengeDAO.userActivatesChallenge(User.getCurrentUser().getId(), "Red Roulette Challenge", wineids);
    }

    /**
     * calls the challengeDAO to make white challenge an active challege, sets the name.
     */

    public void startChallengeWhites()
    {
        ArrayList<Integer> wineids = getWinesForChallenge(whites, "name");
        challengeDAO.userActivatesChallenge(User.getCurrentUser().getId(), "Great White Challenge", wineids);
    }

    /**
     * calls the challengeDAO to make rose challenge an active challenge, sets the name.
     */

    public void startChallengeRose()
    {
        ArrayList<Integer> wineIDs = getWinesForChallenge(rose, "name");
        challengeDAO.userActivatesChallenge(User.getCurrentUser().getId(), "Rose challenge", wineIDs);
    }

    /**
     * calls the challenge complete method in the challenge dao.
     * @param cname the name of the challenge.
     */
    public void challengeCompleted(String cname)
    {
        challengeDAO.challengeCompleted(User.getCurrentUser().getId(), cname);
    }

    /**
     * chose 5 random wines of the set type and returns there ids in an array list.
     * @param types is an array list of strings that are the type of wines for the challenge.
     * @param searchType is a string of either tags or name, which determines weather to user search service search in name or search in tags method.
     * @return ArrayList<Integer> list of wine ids </Integer>
     */
    public ArrayList<Integer> getWinesForChallenge(ArrayList<String> types, String searchType)
    {
        ArrayList<Integer> wines = new ArrayList<>();
        if (searchType.equals("name")) {
            System.out.println(types.getFirst());
            SearchWineService.getInstance().searchWinesByName(types.getFirst(), 100);
        }
        for (String type : types) {
            int wine = 0;
            boolean wineInvalid = true;
            if (searchType.equals("tags")) {
                SearchWineService.getInstance().searchWinesByTags(type, 100);
            }
            while (wineInvalid) {
                Random random = new Random();
                System.out.println(SearchWineService.getInstance().getWineList().size());
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
     * Checks to see if user has the variety challenge as an active challenge.
     * @return true if the user has the variety challenge.
     */
    public boolean activeChallenge() {
        return challengeDAO.getChallengeForUser(User.getCurrentUser().getId()) != null;
    }

    /**
     * Gets the wines for the challenge that the user has active.
     * @return arraylist of the wines for the challenge the user is participating in.
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