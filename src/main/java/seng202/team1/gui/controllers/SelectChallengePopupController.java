package seng202.team1.gui.controllers;

import javafx.application.Platform;
import seng202.team1.gui.FXWrapper;
import seng202.team1.services.ChallengeService;

/**
 * Controller class for the selectChallengePopup.fxml page.
 */
public class SelectChallengePopupController {

    private final ChallengeService challengeService = new ChallengeService();
    private final NavigationController navigationController = FXWrapper.getInstance().getNavigationController();

    /**
     * Calls challenge service method to start the variety challenge, closes select challenge popup
     * and reloads the profile screen.
     */
    public void startVarietyChallenge() {
        navigationController.executeWithLoadingScreen(() -> {
            challengeService.startChallengeVariety();
            Platform.runLater(this::launchProfile);
        });
    }

    /**
     * Calls challenge service method to start the decades challenge, closes select challenge popup
     * and reloads the profile screen.
     */
    public void startYearsChallenge() {
        navigationController.executeWithLoadingScreen(() -> {
            challengeService.startChallengeYears();
            Platform.runLater(this::launchProfile);
        });
    }

    /**
     * Calls challenge service method to start the reds challenge, closes select challenge popup
     * and reloads the profile screen.
     */
    public void startRedsChallenge() {
        navigationController.executeWithLoadingScreen(() -> {
            challengeService.startChallengeReds();
            Platform.runLater(this::launchProfile);
        });
    }

    /**
     * Calls challenge service method to start the whites challenge, closes select challenge popup
     * and reloads the profile screen.
     */
    public void startWhitesChallenge() {
        navigationController.executeWithLoadingScreen(() -> {
            challengeService.startChallengeWhites();
            Platform.runLater(this::launchProfile);
        });
    }

    /**
     * Calls challenge service method to start the rose challenge closes select challenge popup
     * and reloads the profile screen.
     */
    public void startRoseChallenge() {
        navigationController.executeWithLoadingScreen(() -> {
            challengeService.startChallengeRose();
            Platform.runLater(this::launchProfile);
        });
    }

    /**
     * Closes the select challenge popup.
     */
    public void closeChallengePopup() {
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.closePopUp();

    }

    /**
     * Launches the profile screen.
     */
    public void launchProfile() {
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.closePopUp();
        navigationController.loadPageContent("profile");
    }

}
