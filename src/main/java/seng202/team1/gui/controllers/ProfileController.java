package seng202.team1.gui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.FXWrapper;
import seng202.team1.models.User;
import seng202.team1.models.Wine;
import seng202.team1.services.*;

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
    @FXML
    private Pane completedChalPane;
    @FXML
    private Label completedChallMessage;
    @FXML
    private PieChart likedTagPie;
    @FXML
    private PieChart hateTagPie;
    @FXML
    private Label notEnoughDisliked;
    @FXML
    private Label notEnoughLiked;
    @FXML
    private Pane mainPane;
    @FXML
    private AnchorPane pieChartAnchorPane;
    @FXML
    private Label noPieChartLabel;
    private static final Logger LOG = LogManager.getLogger(ProfileController.class);
    private final ChallengeService challengeService = new ChallengeService();

    private final ReviewService reviewService = new ReviewService();

    private final TagRankingService tagRankingService = new TagRankingService();

    int completedWineCount = 0;

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
        displayTagRankings();
        displayWishlist();

    }

    /**
     * Responsible for shifting the profile page elements depending if a pie chart should be displayed or not
     */
    private void displayTagRankings() {
        int uid = User.getCurrentUser().getId();
        if (tagRankingService.hasEnoughLikedTags(uid) || tagRankingService.hasEnoughDislikedTags(uid)) {
            noPieChartLabel.setVisible(false);
            likedTagPie.setStyle("-fx-border-color: #3f0202");
            hateTagPie.setStyle("-fx-border-color: #3f0202");
            pieChartAnchorPane.setDisable(false);
            moveMainPane(100);
            displayPieCharts(uid);
        } else {
            makePieChartInvisible();
            moveMainPane(- 210); //210 is the perfect distance to shift the main page up
        }
    }

    /**
     * Disables the pie chart anchor pane as well as making sure its elements are invisible
     */
    private void makePieChartInvisible() {
        noPieChartLabel.setVisible(true);
        notEnoughLiked.setVisible(false);
        likedTagPie.setStyle(null);
        hateTagPie.setStyle(null);
        notEnoughDisliked.setVisible(false);
        pieChartAnchorPane.setDisable(true);
    }

    /**
     * Initialises and creates the pie charts.
     * @param uid user id
     */
    private void displayPieCharts(int uid) {
        if (tagRankingService.hasEnoughLikedTags(uid)) {
            notEnoughLiked.setVisible(false);
            createPie(likedTagPie, tagRankingService.getTopTagData(uid, 5), "Your top 5 liked tags");
        } else {
            notEnoughLiked.setVisible(true);
            createEmptyPie(likedTagPie,"Your top 5 liked tags");
        }
        if (tagRankingService.hasEnoughDislikedTags(uid)) {
            notEnoughDisliked.setVisible(false);
            createPie(hateTagPie, tagRankingService.getLowestTagData(uid, 5), "Your top 5 liked tags");
        } else {
            notEnoughDisliked.setVisible(true);
            createEmptyPie(hateTagPie, "Your top 5 disliked tags");
        }
    }

    /**
     * Creates and sets the styling of the pie chart
     * @param pie {@link PieChart}
     * @param pieChartData an ObservableList of {@link PieChart.Data}
     * @param title the string title of the pie chart
     */
    private void createPie(PieChart pie, ObservableList<PieChart.Data> pieChartData, String title) {
        pie.setData(pieChartData);
        pie.setClockwise(true);
        pie.setTitle(title);
        pie.setLabelsVisible(true);
        pie.setLegendVisible(false);
        pie.getStylesheets().clear();
        pie.getStylesheets().add(getClass().getResource("/style/tagPieChart.css").toExternalForm());
    }

    /**
     * Creates and sets the styling of an empty pie chart
     * @param pie {@link PieChart}
     * @param title the string title of the pie chart
     */
    private void createEmptyPie(PieChart pie, String title) {
        ObservableList<PieChart.Data> nullData = FXCollections.observableArrayList();
        nullData.add(new PieChart.Data("filler", 1));
        pie.setData(nullData);
        pie.setClockwise(true);
        pie.setTitle(title);
        pie.setLabelsVisible(false);
        pie.setLegendVisible(false);
        pie.getStylesheets().clear();
        pie.getStylesheets().add(getClass().getResource("/style/greyTagPieChart.css").toExternalForm());
    }

    /**
     * Displays the wishlist on the profile in the scrollable grid format, currently displays a wine category using
     * wine category display.
     */
    @FXML
    public void displayWishlist() {
        LOG.info("Fetching wishlist.");

        Parent parent = WineCategoryDisplayController.createCategory("wishlist");
        wishlistPane.getChildren().add(parent);

    }

    /**
     * Sends user to quiz screen.
     */
    public void onQuizClicked() {
        FXWrapper.getInstance().launchPage("quizscreen");
    }

    /**
     * Sends user to the select challenge popup.
     * launches the select challenge popup.
     */
    public void onChallengeClicked() {
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.closePopUp();
        navigationController.loadSelectChallengePopUpContent();
    }

    /**
     * Displays the challenge wines using the wine mini displays.
     */
    public void displayChallenge() {
        List<AnchorPane> wineViews = List.of(chal1, chal2, chal3, chal4, chal5);
        ArrayList<Wine> challengeWines = challengeService.challengeWines();
        completedWineCount = 0;
        int currentUserUid = User.getCurrentUser().getId();
        String cname = challengeService.usersChallenge();
        for (int i = 0; i < wineViews.size(); i++) {
            SearchWineService.getInstance().setCurrentWine(challengeWines.get(i));
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/wineMiniDisplay.fxml"));
                wineViews.get(i).getChildren().add(loader.load());
                WineDisplayController wineDisplayController = loader.getController();
                if (reviewService.reviewExists(currentUserUid, challengeWines.get(i).getWineId())) {
                    wineDisplayController.completedChallengeWine();
                    completedWineCount += 1;
                }
            } catch (IOException e) {
                LOG.error(e.getMessage());
            }
        }
        if (completedWineCount == 5) {
            challengeCompleted(cname);
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
     * Shifts the wine pane to make room for challenge wines.
     */
    public void moveWinesPane() {
        winesPane.setLayoutY(winesPane.getLayoutY()+ 90);
    }
    /**
     * Shifts the main pane to make room for pie charts.
     * @param moveDistance how much to move the Y distance by
     */
    public void moveMainPane(int moveDistance) {
        mainPane.setLayoutY(mainPane.getLayoutY() + moveDistance);
    }


    /**
     * moves the wishlist back up as well as displaying a congratulatory text for completing the challenge
     * @param cname the challenge name
     */
    public void challengeCompleted(String cname) {
        winesPane.setLayoutY(winesPane.getLayoutY() - 90);
        challengePane.setVisible(false);
        completedChalPane.setVisible(true);
        completedChallMessage.setText("Congratulations you completed the " + challengeService.usersChallenge() + "!");
        challengeService.challengeCompleted(cname);
    }
}


