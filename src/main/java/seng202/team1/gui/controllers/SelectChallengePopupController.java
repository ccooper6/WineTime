package seng202.team1.gui.controllers;

import seng202.team1.services.ChallengeService;

/**
 * Controller class for the selectChallengePopup.fxml page.
 * @author Lydia Jackson
 */
public class SelectChallengePopupController {

    private final ChallengeService challengeService = new ChallengeService();

    /**
     * Updates the database and closes popup and reloads the profile screen on start challenge clicked.
     */
    public void startVarietyChallenge() {
        challengeService.startChallengeVariety();
        challengeService.launchProfile();
    }


    /**
     * Closes the popup.
     */
    public void closeChallengePopup() {
        challengeService.closeSelectChallenge();

    }

}
