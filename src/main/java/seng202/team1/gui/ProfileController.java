package seng202.team1.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.apache.commons.beanutils.LazyDynaClass;
import org.apache.commons.collections.functors.FalsePredicate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.models.Wine;
import seng202.team1.repository.ChallengeDAO;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.services.ChallengeService;
import seng202.team1.services.SearchWineService;
import seng202.team1.services.WineCategoryService;
import seng202.team1.services.WishlistService;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Controller class for the profile.fxml page.
 * @author Lydia Jackson
 */
public class ProfileController {
    public Button quizButton;
    public Label challengeLabeltext;
    public Button StartChallengeButton;

    @FXML
    private Pane winesPane;

    @FXML
    private Pane noChallengePane;

    @FXML
    private Pane challengePane;

    @FXML
    private AnchorPane wishlistPane;

    @FXML
    AnchorPane chal1;
    @FXML
    AnchorPane chal2;
    @FXML
    AnchorPane chal3;
    @FXML
    AnchorPane chal4;
    @FXML
    AnchorPane chal5;

    List<AnchorPane> wineViews;

    private static final Logger log = LogManager.getLogger(ProfileController.class);

    ChallengeService challengeService = new ChallengeService();


    /**
     * Initialises the controller checks if user has is participating in a challenge, calls methods to appropriately alter
     * screens.
     */
    public void initialize() {
        challengePane.setVisible(false);
        if (challengeService.activeChallenge()) {
            moveWinesPane();
            activateChallenge();
            String cname = challengeService.usersChallenge();
            displayChallenge(cname);
        }
        displayWishlist();

    }

    /**
     * will display the wishlist on the profile in the scrollable grid format, currently displays a wine catergory using
     * wine catergory display.
     */

    @FXML
    public void displayWishlist() {
        WineCategoryService.getInstance().resetCurrentCategory();
        int currentUserUid = WishlistService.getUserID(FXWrapper.getInstance().getCurrentUser());
        System.out.println(currentUserUid);

        try {
            SearchWineService.getInstance().searchWinesByWishlist(currentUserUid);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/wineCategoryDisplay.fxml"));
            Parent parent = fxmlLoader.load();
            wishlistPane.getChildren().add(parent);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * opens the quiz screen.
     * @param actionEvent
     */

    public void onQuizClicked(ActionEvent actionEvent) { FXWrapper.getInstance().launchSubPage("quizscreen");}


    /**
     * launches the select challenge popup.
     * @param actionEvent
     */
    public void onChallengeClicked(ActionEvent actionEvent) {
        challengeService.launchSelectChallenge();
    }

    /**
     * displays the challenge wines using the wine mini displays
     * @param cname
     */
    public void displayChallenge(String cname) {
        wineViews = List.of(chal1, chal2, chal3, chal4, chal5);
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
     * makes the challenge pane visible and disables previous one.
     */

    public void activateChallenge() {
        noChallengePane.setVisible(false);
        challengePane.setVisible(true);
    }

    /**
     * shifts the main pane to make room for challenge wines.
     */

    public void moveWinesPane() {
        winesPane.setLayoutY(190);

    }
}


