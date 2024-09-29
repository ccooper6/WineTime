package seng202.team1.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.FXWrapper;
import seng202.team1.models.User;
import seng202.team1.models.Wine;
import seng202.team1.services.ChallengeService;
import seng202.team1.services.SearchWineService;
import seng202.team1.services.WineCategoryService;
import seng202.team1.services.WishlistService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for the profile.fxml page.
 * @author Lydia Jackson, Caleb Cooper, Elise Newman
 */
public class ProfileController {
    @FXML
    private Pane winesPane;
    @FXML
    private Pane noChallengePane;
    @FXML
    private Pane challengePane;
    @FXML
    private AnchorPane wishlistPane;
    @FXML
    private AnchorPane chal1;
    @FXML
    private AnchorPane chal2;
    @FXML
    private AnchorPane chal3;
    @FXML
    private AnchorPane chal4;
    @FXML
    private AnchorPane chal5;

    private final ChallengeService challengeService = new ChallengeService();

    private static final Logger LOG = LogManager.getLogger(ProfileController.class);

    /**
     * Initialises the controller checks if user has is participating in a challenge, calls
     * methods to appropriately alter screens.
     */
    public void initialize() {
        challengePane.setVisible(false);
        if (challengeService.activeChallenge()) {
            moveWinesPane();
            activateChallenge();
            displayChallenge();
        }
        displayWishlist();

    }

    /**
     * Displays the wishlist on the profile in the scrollable grid format, currently displays a wine category using
     * wine category display.
     */
    @FXML
    public void displayWishlist() {
        WineCategoryService.getInstance().resetCurrentCategory();
        LOG.info("Fetching wishlist.");

        try {
            Parent parent = WineCategoryDisplayController.createCategory("wishlist");
            wishlistPane.getChildren().add(parent);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends user to quiz screen.
     */
    public void onQuizClicked() {
        FXWrapper.getInstance().launchPage("quizscreen");
    }

    /**
     * Sends user to the select challenge popup.
     */
    public void onChallengeClicked() {
        challengeService.launchSelectChallenge();
    }

    /**
     * Displays the challenge wines using the wine mini displays.
     */
    public void displayChallenge() {
        List<AnchorPane> wineViews = List.of(chal1, chal2, chal3, chal4, chal5);
        ArrayList<Wine> challengeWines = challengeService.challengeWines();
        for (int i = 0; i < wineViews.size(); i++) {
            SearchWineService.getInstance().setCurrentWine(challengeWines.get(i));
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/wineMiniDisplay.fxml"));
                wineViews.get(i).getChildren().add(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Makes the challenge pane visible and disables previous one.
     */
    public void activateChallenge() {
        LOG.info("Activating challenge for user " + User.getCurrentUser().getName());
        noChallengePane.setVisible(false);
        challengePane.setVisible(true);
    }

    /**
     * Shifts the main pane to make room for challenge wines.
     */
    public void moveWinesPane() {
        winesPane.setLayoutY(190);

    }
}


