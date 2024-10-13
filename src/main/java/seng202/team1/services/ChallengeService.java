package seng202.team1.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.models.User;
import seng202.team1.models.Wine;
import seng202.team1.repository.DAOs.ChallengeDAO;

import java.util.*;

/**
 * Service class for the challenge tracker feature.
 */
public class ChallengeService {

    private final ArrayList<String> VARIETIES = new ArrayList<>(Arrays.asList("merlot", "pinot gris", "chardonnay", "red blend", "rose"));
    private final ArrayList<String> YEARS = new ArrayList<>(Arrays.asList("1989", "1990", "2000", "2010", "2005"));
    private final ArrayList<String> REDS = new ArrayList<>();
    private final ArrayList<String> WHITES = new ArrayList<>();
    private final ArrayList<String> ROSE = new ArrayList<>();

    private final ChallengeDAO CHALLENGEDAO = new ChallengeDAO();
    private final WineVarietyService WINEVARIETYSERVICE = new WineVarietyService();
    private static final Logger LOG = LogManager.getLogger(ChallengeService.class);


    /**
     * Default constructor for ChallengeService
     */
    public ChallengeService(){}

    /**
     * Calls the challengeDAO to update the database so that the user has the variety challenge as an active challenge.
     */
    public void startChallengeVariety()
    {
        ArrayList<Integer> wineIDs = getWinesForChallenge(VARIETIES);
        userActivatesChallenge(User.getCurrentUser().getId(), "Variety Challenge", wineIDs);
    }

    /**
     * calls challengeDAO to make the time travelling challenge and active challenge.
     */
    public void startChallengeYears()
    {
        ArrayList<Integer> wineIDs = getWinesForChallenge(YEARS);
        userActivatesChallenge(User.getCurrentUser().getId(), "Time Travelling Challenge", wineIDs);
    }

    /**
     * calls the challengeDAO to make red challenge an active challenge, sets the name.
     */
    public void startChallengeReds()
    {
        REDS.clear();
        ArrayList<String> redsList = new ArrayList<>(WINEVARIETYSERVICE.getReds());
        for (int i = 0; i < 5; i++) {
            REDS.add(redsList.get(i));
        }
        ArrayList<Integer> wineIDs = getWinesForChallenge(REDS);
        userActivatesChallenge(User.getCurrentUser().getId(), "Red Roulette Challenge", wineIDs);


    }

    /**
     * calls the challengeDAO to make white challenge an active challenge, sets the name.
     */

    public void startChallengeWhites()
    {
        WHITES.clear();
        ArrayList<String> whitesList = new ArrayList<>(WINEVARIETYSERVICE.getWhites());
        for (int i = 0; i < 5; i++) {
            WHITES.add(whitesList.get(i));
        }
        ArrayList<Integer> wineIDs = getWinesForChallenge(WHITES);
        userActivatesChallenge(User.getCurrentUser().getId(), "Great White Challenge", wineIDs);
    }

    /**
     * calls the challengeDAO to make rose challenge an active challenge, sets the name.
     */

    public void startChallengeRose()
    {
        ROSE.clear();
        ArrayList<String> roseList = new ArrayList<>(WINEVARIETYSERVICE.getRose());
        for (int i = 0; i < 5; i++) {
            ROSE.add(roseList.get(i));
        }
        ArrayList<Integer> wineIDs = getWinesForChallenge(ROSE);
        userActivatesChallenge(User.getCurrentUser().getId(), "Rosè challenge", wineIDs);
    }

    /**
     * calls the challenge complete method in the challenge dao.
     * @param cname the name of the challenge completed.
     */
    public void challengeCompleted(String cname)
    {
        CHALLENGEDAO.challengeCompleted(User.getCurrentUser().getId(), cname);
    }

    /**
     * chose 5 random wines of the set type and returns there ids in an array list.
     * @param types is an ArrayList&lt;String&gt; that are the type of wines for the challenge.
     * @return ArrayList&lt;Integer&gt; of wine ids
     */
    public ArrayList<Integer> getWinesForChallenge(ArrayList<String> types)
    {
        ArrayList<Integer> wines = new ArrayList<>();
        for (String type : types) {
            int wine = 0;
            boolean wineInvalid = true;
            SearchWineService.getInstance().searchWinesByTags(type, 100);
            while (wineInvalid) {
                Random random = new Random();
                wine = SearchWineService.getInstance().getWineList().get(random.nextInt(SearchWineService.getInstance().getWineList().size())).getID();
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
     * Inserts the wine into the challenge.
     * @param wineIds an array list of integer wine id
     * @param challengeName the name of the challenge
     * @param uid current user uid
     */
    public void addWineToChallenge(ArrayList<Integer> wineIds, int uid, String challengeName) {
        if (!CHALLENGEDAO.challengeHasWines(challengeName, uid)) {
            for (Integer wineId : wineIds) {
                CHALLENGEDAO.insertChallenge(wineId, uid, challengeName);
            }
        }
    }

    /**
     * Checks the user already has the challenge active, if not calls a method to update the database.
     * @param uid user id
     * @param cname challenge name
     * @param wineIds arraylist of integer wine ids
     */
    public void userActivatesChallenge(int uid, String cname, ArrayList<Integer> wineIds) {
        // add challenge if user does not already have this challenge already
        if (!CHALLENGEDAO.userHasChallenge(uid, cname)) {
            CHALLENGEDAO.startChallenge(uid, cname);
            addWineToChallenge(wineIds, uid, cname);
        } else {
            LOG.error("Error: Could not activate challenge for user since user has already started that challenge");
        }
    }



    /**
     * Checks to see if user has an active challenge.
     * @return boolean, true if the user has an active challenge.
     */
    public boolean activeChallenge() {
        return CHALLENGEDAO.getChallengeForUser(User.getCurrentUser().getId()) != null;
    }

    /**
     * Gets the wines for the challenge that the user has active.
     * @return ArrayList&lt;Wine&gt; of the wines for the challenge the user is participating in.
     */
    public ArrayList<Wine> challengeWines() {
        return CHALLENGEDAO.getWinesInChallenge(CHALLENGEDAO.getChallengeForUser(User.getCurrentUser().getId()), User.getCurrentUser().getId());
    }

    /**
     * Gets the name of the challenge the user has active.
     * @return string name of users active challenge.
     */
    public String usersChallenge() {
        return CHALLENGEDAO.getChallengeForUser(User.getCurrentUser().getId());
    }
}