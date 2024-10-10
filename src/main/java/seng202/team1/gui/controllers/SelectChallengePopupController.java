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
     * Calls challenge service method to start the variety challenge and closes popup and reloads the profile screen on start challenge clicked.
     */
    public void startVarietyChallenge() {
        navigationController.executeWithLoadingScreen(() -> {
            challengeService.startChallengeVariety();
            Platform.runLater(this::launchProfile);
        });
    }

    /**
     * Calls challenge service method to start the decades challenge and closes popup and reloads the profile screen on start challenge clicked.
     */
    public void startYearsChallenge() {
        navigationController.executeWithLoadingScreen(() -> {
            challengeService.startChallengeYears();
            Platform.runLater(this::launchProfile);
        });
    }

    /**
     * Calls challenge service method to start the reds challenge and closes popup and reloads the profile screen on start challenge clicked.
     */
    public void startRedsChallenge() {
        navigationController.executeWithLoadingScreen(() -> {
            challengeService.startChallengeReds();
            Platform.runLater(this::launchProfile);
        });
    }

    /**
     * Calls challenge service method to start the whites challenge and closes popup and reloads the profile screen on start challenge clicked.
     */
    public void startWhitesChallenge() {
        navigationController.executeWithLoadingScreen(() -> {
            challengeService.startChallengeWhites();
            Platform.runLater(this::launchProfile);
        });
    }

    /**
     * Calls challenge service method to start the rose challenge and closes popup and reloads the profile screen on start challenge clicked.
     */
    public void startRoseChallenge() {
        navigationController.executeWithLoadingScreen(() -> {
            challengeService.startChallengeRose();
            Platform.runLater(this::launchProfile);
        });
    }

    /**
     * Closes the popup.
     */
    public void closeChallengePopup() {
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.closePopUp();

    }

    /**
     * Launches the profile screen and closes the popup.
     */
    public void launchProfile() {
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.closePopUp();
        navigationController.loadPageContent("profile");
    }

}
