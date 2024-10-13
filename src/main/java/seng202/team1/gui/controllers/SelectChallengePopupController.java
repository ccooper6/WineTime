package seng202.team1.gui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import seng202.team1.gui.FXWrapper;
import seng202.team1.services.ChallengeService;

/**
 * Controller class for the selectChallengePopup.fxml page.
 */
public class SelectChallengePopupController {

    private final ChallengeService CHALLENGESERVICE = new ChallengeService();
    private final NavigationController NAVIGATIONCONTROLLER = FXWrapper.getInstance().getNavigationController();

    /**
     * Default constructor for SelectChallengePopupController.
     */
    public SelectChallengePopupController() {}

    /**
     * Calls challenge service method to start the variety challenge, closes select challenge popup
     * and reloads the profile screen.
     */
    @FXML
    public void startVarietyChallenge() {
        NAVIGATIONCONTROLLER.executeWithLoadingScreen(() -> {
            CHALLENGESERVICE.startChallengeVariety();
            Platform.runLater(this::launchProfile);
        });
    }

    /**
     * Calls challenge service method to start the decades challenge, closes select challenge popup
     * and reloads the profile screen.
     */
    @FXML
    public void startYearsChallenge() {
        NAVIGATIONCONTROLLER.executeWithLoadingScreen(() -> {
            CHALLENGESERVICE.startChallengeYears();
            Platform.runLater(this::launchProfile);
        });
    }

    /**
     * Calls challenge service method to start the reds challenge, closes select challenge popup
     * and reloads the profile screen.
     */
    @FXML
    public void startRedsChallenge() {
        NAVIGATIONCONTROLLER.executeWithLoadingScreen(() -> {
            CHALLENGESERVICE.startChallengeReds();
            Platform.runLater(this::launchProfile);
        });
    }

    /**
     * Calls challenge service method to start the whites challenge, closes select challenge popup
     * and reloads the profile screen.
     */
    @FXML
    public void startWhitesChallenge() {
        NAVIGATIONCONTROLLER.executeWithLoadingScreen(() -> {
            CHALLENGESERVICE.startChallengeWhites();
            Platform.runLater(this::launchProfile);
        });
    }

    /**
     * Calls challenge service method to start the rose challenge closes select challenge popup
     * and reloads the profile screen.
     */
    @FXML
    public void startRoseChallenge() {
        NAVIGATIONCONTROLLER.executeWithLoadingScreen(() -> {
            CHALLENGESERVICE.startChallengeRose();
            Platform.runLater(this::launchProfile);
        });
    }

    /**
     * Closes the select challenge popup.
     */
    @FXML
    public void closeChallengePopup() {
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.closePopUp();

    }

    /**
     * Launches the profile screen.
     */
    private void launchProfile() {
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.closePopUp();
        navigationController.loadPageContent("profile");
    }

}
