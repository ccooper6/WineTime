package seng202.team1.services;

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

/**
 * todo
 * track the progress, if a challenge has been logged them it is marked as completed,
 * the if the wines displayed,
 */
public class ChallengeService {
    private final ChallengeDAO chalDao = new ChallengeDAO();

    /**
     * Calls the challengeDAO to update the database so that the user has the variety challenge as an active challenge.
     */
    public void startChallengeVariety()
    {
        ArrayList<Integer> wineids = getWinesforVarietyChallenge();
        chalDao.userActivatesChallenge(User.getCurrentUser().getId(), "Variety Challenge", wineids);
    }

    public void challengeCompleted(String cname)
    {
        chalDao.challengeCompleted(User.getCurrentUser().getId(), cname);
    }

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
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.closePopUp();
        navigationController.loadPageContent("profile");
    }

    /**
     * Launches the select challenge pop up.
     */
    public void launchSelectChallenge() {
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
//        if (chalDao.)
        System.out.println("user doing variety challenge?");
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

//    public ArrayList<Integer> getWinesforVarietyChallenge() {
//    }  retrieve 5 radom wines from the database and put them in the db,
    // make it decades
    // copy for countries.

}






