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
import java.util.Objects;

/**
 * Controller class for the profile.fxml page.
 */
public class ProfileController {
    @FXML
    private Pane winesPane;
    @FXML
    private Pane noChallengePane;
    @FXML
    private Pane challengePane;
    @FXML
    private AnchorPane wishlistAnchorPane;
    @FXML
    private AnchorPane challengeWineAnchorPane1;
    @FXML
    private AnchorPane challengeWineAnchorPane2;
    @FXML
    private AnchorPane challengeWineAnchorPane3;
    @FXML
    private AnchorPane challengeWineAnchorPane4;
    @FXML
    private AnchorPane challengeWineAnchorPane5;
    @FXML
    private Pane completedChalPane;
    @FXML
    private Label completeChallengeLabel;
    @FXML
    private PieChart likedTagPieChart;
    @FXML
    private PieChart hateTagPieChart;
    @FXML
    private Label notEnoughDislikedLabel;
    @FXML
    private Label notEnoughLikedLabel;
    @FXML
    private Pane mainPane;
    @FXML
    private AnchorPane pieChartAnchorPane;
    @FXML
    private Label noPieChartLabel;

    int completedWineCount = 0;
    private final ChallengeService CHALLENGESERVICE = new ChallengeService();
    private final ReviewService REVIEWSERVICE = new ReviewService();
    private final TagRankingService TAGRANKINGSERVICE = new TagRankingService();

    private static final Logger LOG = LogManager.getLogger(ProfileController.class);


    /**
     * Default constructor for ProfileController.
     */
    public ProfileController() {}


    /**
     * Initializes the controller checks if user has is participating in a challenge, calls
     * methods to appropriately alter screens.
     */
    public void initialize() {
        challengePane.setVisible(false);
        if (CHALLENGESERVICE.activeChallenge()) {
            moveWinesPane();
            activateChallenge();
            displayChallenge();
        }
        displayTagRankings();
        displayWishlist();
    }

    /**
     * Shifts the profile page elements depending on if a pie chart should be displayed or not.
     */
    private void displayTagRankings() {
        int uid = User.getCurrentUser().getId();
        if (TAGRANKINGSERVICE.hasEnoughLikedTags(uid) || TAGRANKINGSERVICE.hasEnoughDislikedTags(uid)) {
            noPieChartLabel.setVisible(false);
            likedTagPieChart.setStyle("-fx-border-color: #3f0202");
            hateTagPieChart.setStyle("-fx-border-color: #3f0202");
            pieChartAnchorPane.setDisable(false);
            moveMainPane(100);
            displayPieCharts(uid);
        } else {
            makePieChartInvisible();
            moveMainPane(-210); //210 is the perfect distance to shift the main page up
        }
    }

    /**
     * Disables the pie chart anchor pane as well as making sure its elements are invisible.
     */
    private void makePieChartInvisible() {
        noPieChartLabel.setVisible(true);
        notEnoughLikedLabel.setVisible(false);
        likedTagPieChart.setStyle(null);
        hateTagPieChart.setStyle(null);
        notEnoughDislikedLabel.setVisible(false);
        pieChartAnchorPane.setDisable(true);
    }

    /**
     * Initialises and creates the pie charts.
     * @param uid is the current users id
     */
    private void displayPieCharts(int uid) {
        if (TAGRANKINGSERVICE.hasEnoughLikedTags(uid)) {
            notEnoughLikedLabel.setVisible(false);
            createPie(likedTagPieChart, TAGRANKINGSERVICE.getTopTagData(uid, 5), "Your top 5 liked tags");
        } else {
            notEnoughLikedLabel.setVisible(true);
            createEmptyPie(likedTagPieChart,"Your top 5 liked tags");
        }
        if (TAGRANKINGSERVICE.hasEnoughDislikedTags(uid)) {
            notEnoughDislikedLabel.setVisible(false);
            createPie(hateTagPieChart, TAGRANKINGSERVICE.getLowestTagData(uid, 5), "Your top 5 disliked tags");
        } else {
            notEnoughDislikedLabel.setVisible(true);
            createEmptyPie(hateTagPieChart, "Your top 5 disliked tags");
        }
    }

    /**
     * Creates and sets the styling of the pie chart.
     * @param pie {@link PieChart} PieChart object
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
        try {
            pie.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style/tagPieChart.css")).toExternalForm());
        } catch (NullPointerException e) {
            LOG.error("Error: Could not load pie chart stylesheet.");
        } // do nothing if the pie chart could not be created
    }

    /**
     * Creates and sets the styling of an empty pie chart.
     * @param pie {@link PieChart} PieChart object
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
        try {
            pie.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style/greyTagPieChart.css")).toExternalForm());
        } catch (NullPointerException e) {
            LOG.error("Error: Could not load empty pie chart stylesheet.");
        } // do nothing if pie chart could not be created
    }

    /**
     * Displays the wishlist on the profile using category display.
     */
    @FXML
    public void displayWishlist() {
        SearchWineService.getInstance().setCurrentMethod("notRecommended");
        Parent parent = WineCategoryDisplayController.createCategory("wishlist");
        wishlistAnchorPane.getChildren().add(parent);

    }

    /**
     * Displays the challenge wines using the wine mini displays.
     */
    public void displayChallenge() {
        List<AnchorPane> wineViews = List.of(challengeWineAnchorPane1, challengeWineAnchorPane2, challengeWineAnchorPane3, challengeWineAnchorPane4, challengeWineAnchorPane5);
        ArrayList<Wine> challengeWines = CHALLENGESERVICE.challengeWines();
        completedWineCount = 0;
        int currentUserUid = User.getCurrentUser().getId();
        String cname = CHALLENGESERVICE.usersChallenge();
        for (int i = 0; i < wineViews.size(); i++) {
            SearchWineService.getInstance().setCurrentWine(challengeWines.get(i));
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/wineMiniDisplay.fxml"));
                wineViews.get(i).getChildren().add(loader.load());
                WineDisplayController wineDisplayController = loader.getController();
                if (REVIEWSERVICE.reviewExists(currentUserUid, challengeWines.get(i).getID())) {
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
     * Makes the challenge pane visible and disables other panes.
     */
    public void activateChallenge() {
        LOG.info("Activating challenge for user {}", User.getCurrentUser().getName());
        noChallengePane.setVisible(false);
        challengePane.setVisible(true);
    }

    /**
     * Shifts the wishlist wine pane by 90 to make room for challenge wines.
     */
    public void moveWinesPane() {
        winesPane.setLayoutY(winesPane.getLayoutY() + 90);
    }

    /**
     * Shifts the main pane to make room for pie charts.
     * @param moveDistance how much to move the Y distance by
     */
    public void moveMainPane(int moveDistance) {
        mainPane.setLayoutY(mainPane.getLayoutY() + moveDistance);
    }


    /**
     * Moves the wishlist back up as well as displaying a congratulatory text for completing the challenge.
     * @param cname the name of the users challenge
     */
    public void challengeCompleted(String cname) {
        winesPane.setLayoutY(winesPane.getLayoutY() - 90);
        challengePane.setVisible(false);
        completedChalPane.setVisible(true);
        completeChallengeLabel.setText("Congratulations you completed the " + CHALLENGESERVICE.usersChallenge() + "!");
        CHALLENGESERVICE.challengeCompleted(cname);
    }

    /**
     * Quits the challenge and displays a message.
     */
    @FXML
    public void quitChallenge() {
        winesPane.setLayoutY(winesPane.getLayoutY() - 90);
        challengePane.setVisible(false);
        completedChalPane.setVisible(true);
        completeChallengeLabel.setText("You quit the " + CHALLENGESERVICE.usersChallenge() + ".");
        CHALLENGESERVICE.challengeCompleted(CHALLENGESERVICE.usersChallenge());
    }

    /**
     * Sends user to quiz screen when quiz button clicked.
     */
    public void onQuizClicked() {
        FXWrapper.getInstance().launchPage("quizscreen");
    }

    /**
     * Opens the select challenge popup when select challenge clicked.
     */
    public void onChallengeClicked() {
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.closePopUp();
        navigationController.loadSelectChallengePopUpContent();
    }

    /**
     * Logs the current user out, launches login page.
     */
    @FXML
    public void logOutButton() {
        User.setCurrentUser(null);
        FXWrapper.getInstance().launchPage("login");
    }
}


