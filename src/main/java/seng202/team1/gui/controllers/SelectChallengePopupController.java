package seng202.team1.gui.controllers;

import seng202.team1.gui.FXWrapper;
import seng202.team1.services.ChallengeService;

/**
 * Controller class for the selectChallengePopup.fxml page.
 * @author Lydia Jackson
 */
public class SelectChallengePopupController {

    private final ChallengeService challengeService = new ChallengeService();

    /**
     * calls challenge service method to start the variety challenge and closes popup and reloads the profile screen on start challenge clicked.
     */
    public void startVarietyChallenge() {
        challengeService.startChallengeVariety();
        launchProfile();
    }

    /**
     * calls challenge service method to start the decades challenge and closes popup and reloads the profile screen on start challenge clicked.
     */
    public void startYearsChallenge() {
        challengeService.startChallengeYears();
        launchProfile();
    }

    /**
     * calls challenge service method to start the reds challenge and closes popup and reloads the profile screen on start challenge clicked.
     */
    public void startRedsChallenge() {
        challengeService.startChallengeReds();;
        launchProfile();
    }

    /**
     * calls challenge service method to start the whites challenge and closes popup and reloads the profile screen on start challenge clicked.
     */
    public void startWhitesChallenge() {
        challengeService.startChallengeWhites();
        launchProfile();
    }

    /**
     * calls challenge service method to start the rose challenge and closes popup and reloads the profile screen on start challenge clicked.
     */
    public void startRoseChallenge() {
        challengeService.startChallengeRose();
        launchProfile();
    }

    /**
     * Closes the popup.
     */
    public void closeChallengePopup() {
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.closePopUp();

    }

    /**
     * launches the profile screen and closes the popup
     */
    public void launchProfile() {
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.closePopUp();
        navigationController.loadPageContent("profile");
    }

}
