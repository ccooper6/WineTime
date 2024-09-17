package seng202.team1.services;

import seng202.team1.gui.FXWrapper;
import seng202.team1.gui.NavigationController;
import seng202.team1.models.Wine;
import seng202.team1.repository.ChallengeDAO;
import seng202.team1.repository.DatabaseManager;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Service class for the challenge tracker feature.
 * @author Lydia Jackson
 */

public class ChallengeService {
    private DatabaseManager databaseManager = DatabaseManager.getInstance();
    private ChallengeDAO chalDao = new ChallengeDAO();
    private int currentUserID = chalDao.getUId(FXWrapper.getInstance().getCurrentUser());


    /**
     * calls the cahllengeDAO to update the database so that the user has the variety challenge as an active challenge.
     */
    public void startChallengeVariety() {
        chalDao.userActivatesChallenge(currentUserID, "Variety Challenge");
        chalDao.wineInChallenge();
    }

    /**
     * launches/resets the profile.
     */
    public void launchProfile() {
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.closePopUp();
        navigationController.loadPageContent("profile");
    }

    /**
     * launches the select challenge pop up.
     */
    public void launchSelectChallenge() {
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.closePopUp();
        navigationController.loadSelectChallengePopUpContent();
    }

    /**
     * closes the select challenge pop up.
     */
    public void closeSelectChallenge() {
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.closePopUp();
    }


    /**
     * checks to see if user has the variety challenge as an active challenge.
     * @return true if the user has the variety challenge.
     */
    public boolean activeChallenge() {
        return Objects.equals(chalDao.getChallengeForUser(currentUserID), "Variety Challenge");
    }

    /**
     * gets the wines for the challenge that the user has active.
     * @return arraylist of the wines for the challenge the user is participating in.
     */
    public ArrayList<Wine> challengeWines() {
        return chalDao.getWinesForChallenge(chalDao.getChallengeForUser(currentUserID));
    }

    /**
     * gets the name of the challenge the user has active.
     * @return string name of users active challenge.
     */
    public String usersChallenge() {
        return chalDao.getChallengeForUser(currentUserID);
    }

}






