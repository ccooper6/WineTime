package seng202.team1.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.FXWrapper;
import seng202.team1.gui.controllers.NavigationController;
import seng202.team1.models.User;
import seng202.team1.models.Wine;
import seng202.team1.repository.DAOs.ChallengeDAO;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

/**
 * Service class for the challenge tracker feature.
 * @author Lydia Jackson
 */
public class ChallengeService {
    private final ChallengeDAO chalDao = new ChallengeDAO();
    private final Logger LOG = LogManager.getLogger(ChallengeService.class);

    /**
     * Calls the challengeDAO to update the database so that the user has the variety challenge as an active challenge.
     */
    public void startChallengeVariety()
    {
        ArrayList<Integer> wineids = getWinesforVarietyChallenge();
        chalDao.userActivatesChallenge(User.getCurrentUser().getId(), "Variety Challenge", wineids);
        LOG.info("User has started the variety challenge");
    }

    /**
     * calls the challenge complete method in the challenge dao.
     * @param cname the name of the challenge.
     */
    public void challengeCompleted(String cname)
    {
        chalDao.challengeCompleted(User.getCurrentUser().getId(), cname);
        LOG.info("User has completed the challenge");
    }

    /**
     * chose 5 random wines of different variety and returns there ids in an array list.
     * @return ArrayList of wine ids
     */
    public ArrayList<Integer> getWinesforVarietyChallenge() {
        ArrayList<Integer> varietyWines = new ArrayList<>();
        Random random = new Random();
        SearchWineService.getInstance().searchWinesByTags("merlot", 50);
        varietyWines.add(SearchWineService.getInstance().getWineList().get(random.nextInt(SearchWineService.getInstance().getWineList().size())).getWineId());
        SearchWineService.getInstance().searchWinesByTags("pinot gris", 50);
        varietyWines.add(SearchWineService.getInstance().getWineList().get(random.nextInt(SearchWineService.getInstance().getWineList().size())).getWineId());
        SearchWineService.getInstance().searchWinesByTags("chardonnay", 50);
        varietyWines.add(SearchWineService.getInstance().getWineList().get(random.nextInt(SearchWineService.getInstance().getWineList().size())).getWineId());
        SearchWineService.getInstance().searchWinesByTags("red blend", 50);
        varietyWines.add(SearchWineService.getInstance().getWineList().get(random.nextInt(SearchWineService.getInstance().getWineList().size())).getWineId());
        SearchWineService.getInstance().searchWinesByTags("rose", 50);
        varietyWines.add(SearchWineService.getInstance().getWineList().get(random.nextInt(SearchWineService.getInstance().getWineList().size())).getWineId());

        return varietyWines;
    }

    /**
     * Launches/resets the profile.
     */
    public void launchProfile() {
        LOG.info("Launching profile");
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.closePopUp();
        navigationController.loadPageContent("profile");
    }

    /**
     * Launches the select challenge pop up.
     */
    public void launchSelectChallenge() {
        LOG.info("Launching profile");
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
        boolean active = Objects.equals(chalDao.getChallengeForUser(User.getCurrentUser().getId()), "Variety Challenge");
        if (active) {
            LOG.info("User current has active challenge");
        } else {
            LOG.info("User does not currently have active challenge");
        }
        return active;
    }

    /**
     * Gets the wines for the challenge that the user has active.
     * @return arraylist of the wines for the challenge the user is participating in.
     */
    public ArrayList<Wine> challengeWines() {
        ArrayList<Wine> wines = chalDao.getWinesForChallenge(chalDao.getChallengeForUser(User.getCurrentUser().getId()), User.getCurrentUser().getId());
        LOG.info("Fetched " + wines.size() + " wines for the active challenge");
        return wines;
    }

    /**
     * Gets the name of the challenge the user has active.
     * @return string name of users active challenge.
     */
    public String usersChallenge() {
        String challenge = chalDao.getChallengeForUser(User.getCurrentUser().getId());
        LOG.info("User is currently participating in the " + challenge + " challenge");
        return challenge;
    }


}






