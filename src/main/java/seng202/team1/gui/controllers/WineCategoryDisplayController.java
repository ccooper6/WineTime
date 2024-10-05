package seng202.team1.gui.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.FXWrapper;
import seng202.team1.models.User;
import seng202.team1.models.Wine;
import seng202.team1.repository.DAOs.SearchDAO;
import seng202.team1.services.SearchWineService;
import seng202.team1.services.WineCategoryService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for the wineCategoryDisplay.fxml category displays.
 * @author Elise Newman, Caleb Cooper, Yuhao Zhang
 */
public class WineCategoryDisplayController {
    @FXML
    private Text titleText;
    @FXML
    private FontAwesomeIconView leftArrowButton;
    @FXML
    private FontAwesomeIconView rightArrowButton;
    @FXML
    private AnchorPane mainWine0;
    @FXML
    private AnchorPane mainWine1;
    @FXML
    private AnchorPane mainWine2;
    @FXML
    private AnchorPane mainWine3;
    @FXML
    private AnchorPane mainWine4;
    @FXML
    private AnchorPane mainWine5;
    @FXML
    private AnchorPane mainWine6;
    @FXML
    private Button seeMoreButton;


    private List<AnchorPane> wineViews;
    private int firstWine = 0;
    private int MAXWINES = 11;
    private int leftDisplay = 7;
    private int rightDisplay;
    private final double TRANSITIONDURATION = 0.2;
    private int DISTANCEBETWEEN = 210;

    private boolean isWishlist = false;
    private boolean isRecommendations = false;

    private final ArrayList<Parent> wineDisplays = new ArrayList<>();
    private ArrayList<Wine> displayWines;

    private static ArrayList<Wine> savedWineList;
    private static String savedSearchString;

    private String tags;

    private static final Logger LOG = LogManager.getLogger(WineCategoryDisplayController.class);

    /**
     * Only initialises on login.
     * Creates an array of the anchor panes (len = 6)
     * Fetches the number of wine objects from the database and stores them in another array (len = MAXWINES)
     */
    @FXML
    public void initialize() {
        wineViews = List.of(mainWine0, mainWine1, mainWine2, mainWine3, mainWine4, mainWine5, mainWine6);

        onRefresh();
        displayWines = savedWineList;
        tags = savedSearchString;

        isWishlist = tags.equalsIgnoreCase("wishlist");
        isRecommendations = tags.equalsIgnoreCase("recommend");

        if (isWishlist) {
            titleText.setText("Your Wishlist: ");
        } else if (isRecommendations) {
            titleText.setText("Recommendations for you: ");
            if (displayWines.size() < 10) {
                seeMoreButton.setDisable(true);
                seeMoreButton.setVisible(false);
            }
        } else {
            titleText.setText(WineCategoryService.getInstance().getCategoryTitles().get(WineCategoryService.getInstance().getCurrentCategory()));
        }
        if (displayWines.isEmpty()) {
            if (isWishlist) {
                titleText.setText("Your Wishlist: \n\nYou have no saved wines...\nGo to home or search pages to discover new wines!");
            } else if (isRecommendations) {
                titleText.setText("Recommendations for you: \n\nThere are currently no recommendations for you...");
            } else {
                titleText.setText(WineCategoryService.getInstance().getCategoryTitles().get(WineCategoryService.getInstance().getCurrentCategory()) +
                        "\n\nThere was an issue fetching wines. \nPlease try restarting the app!");
            }
            leftArrowButton.setDisable(true);
            leftArrowButton.setVisible(false);
            rightArrowButton.setDisable(true);
            rightArrowButton.setVisible(false);
        } else if (displayWines.size() <= 5) {
            fiveOrLess();
        } else {
            if (displayWines.size() == 6) {
                for (int i = 0; i < 6; i++) {
                    displayWines.addLast(displayWines.get(i));
                }
            }
            if (displayWines.size() == 7) {
                leftDisplay = 0;
            }
            if (displayWines.size() < MAXWINES) {
                MAXWINES = displayWines.size();
                rightDisplay = MAXWINES - 1;
            } else {
                rightDisplay = 6;
            }
            for (int i = 0; i < MAXWINES; i++) {
                SearchWineService.getInstance().setCurrentWine(displayWines.get(i));
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/wineMiniDisplay.fxml"));
                    wineDisplays.add(loader.load());
                } catch (IOException e) {
                    LOG.error("Error in WineCategoryDisplayController.initialize: Could not load fxml content for wine ID {}.", displayWines.get(i).getWineId());
                }
            }
            mainWine0.getChildren().add(wineDisplays.get(0));
            mainWine1.getChildren().add(wineDisplays.get(1));
            mainWine2.getChildren().add(wineDisplays.get(2));
            mainWine3.getChildren().add(wineDisplays.get(3));
            mainWine4.getChildren().add(wineDisplays.get(4));
            mainWine5.getChildren().add(wineDisplays.get(5));
            mainWine6.getChildren().add(wineDisplays.get(6));
        }
    }

    /**
     * Controls the arrow buttons.
     */
    @FXML
    public void onRefresh() {
        rightArrowButton.setOnMouseClicked(event -> {
            transitionRight();
        });

        leftArrowButton.setOnMouseClicked(event -> {
            transitionLeft();
        });
    }

    /**
     * Returns the id of the wineView which should be displayed at that position.
     * @param id is the position of the view pane.
     * @return id of the wineView which should be displayed at that position.
     */
    public int getId(int id) {
        return (firstWine + id) % wineViews.size();
    }

    /**
     * Controls all the left or right translations.
     * @param posOrNeg is the direction of the translation (right = positive)
     */
    public void shift(int posOrNeg) {
        for (Parent wineDisplay : wineDisplays) { // Temporarily disables each wine tile
            wineDisplay.setDisable(true);
        }

        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(TRANSITIONDURATION), wineViews.get(getId(2)));
        TranslateTransition transition1 = new TranslateTransition(Duration.seconds(TRANSITIONDURATION), wineViews.get(getId(1)));
        TranslateTransition transition3 = new TranslateTransition(Duration.seconds(TRANSITIONDURATION), wineViews.get(getId(3)));
        TranslateTransition transition4 = new TranslateTransition(Duration.seconds(TRANSITIONDURATION), wineViews.get(getId(4)));
        TranslateTransition transition5 = new TranslateTransition(Duration.seconds(TRANSITIONDURATION), wineViews.get(getId(5)));
        TranslateTransition transition6;
        if (posOrNeg == 1) {
            transition6 = new TranslateTransition(Duration.seconds(TRANSITIONDURATION), wineViews.get(getId(0)));
        } else {
            transition6 = new TranslateTransition(Duration.seconds(TRANSITIONDURATION), wineViews.get(getId(6)));
        }
        List<TranslateTransition> wineTransitions = List.of(transition1, transition2, transition3, transition4, transition5, transition6);
        for (int i = 0; i < wineTransitions.size(); i++) {
            wineTransitions.get(i).setByX(posOrNeg * DISTANCEBETWEEN);
            wineTransitions.get(i).setInterpolator(Interpolator.LINEAR);
            wineTransitions.get(i).play();
        }

        wineTransitions.get(wineTransitions.size() - 1).setOnFinished(event -> { // Un-disables the wine tiles
            for (Parent wineDisplay : wineDisplays) {
                wineDisplay.setDisable(false);
            }
        });
    }

    /**
     * The moving frame (0 or 6) increases in opacity, the button is enabled once visible.
     * @param movingFrame is the relative id of the anchor pane moving.
     */
    public void fadeIn(int movingFrame) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(TRANSITIONDURATION), wineViews.get(getId(movingFrame)));
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
        wineViews.get(getId(movingFrame)).setDisable(false);
    }

    /**
     * The moving frame (1 or 4) decreases in opacity, the button is disabled.
     * @param movingFrame is the relative id of the anchor pane moving.
     */
    public void fadeOut(int movingFrame) {
        FadeTransition fadeTransitionOut = new FadeTransition(Duration.seconds(TRANSITIONDURATION), wineViews.get(getId(movingFrame)));
        fadeTransitionOut.setFromValue(1);
        fadeTransitionOut.setToValue(0);
        fadeTransitionOut.play();
        wineViews.get(getId(movingFrame)).setDisable(true);
    }

    /**
     * Translates the end frame to the location of the opposite end.
     * The arrows are re-enabled
     * @param movingFrame is the end frame moving (0 or 5)
     * @param posOrNeg is the direction of the translation (right = positive)
     */
    public void teleportEnd(int movingFrame, int posOrNeg) {
        TranslateTransition transitionReturn = new TranslateTransition(Duration.seconds(TRANSITIONDURATION), wineViews.get(getId(movingFrame)));
        transitionReturn.setByX(posOrNeg * DISTANCEBETWEEN * 6);
        transitionReturn.setInterpolator(Interpolator.DISCRETE);
        transitionReturn.play();
        transitionReturn.setOnFinished(event -> {
            if (posOrNeg == -1) {
                resetFirstLeft(movingFrame);
            } else {
                resetFirstRight(movingFrame);
            }
            rightArrowButton.setDisable(false);
            leftArrowButton.setDisable(false);
        });
    }

    /**
     * Changes the content within the teleporting frame.
     * Updates the first wine index
     * @param frame is the teleporting frame (0 or 5)
     */
    public void resetFirstRight(int frame) {
        wineViews.get(getId(frame)).getChildren().set(0, wineDisplays.get(leftDisplay));
        leftDisplay = (leftDisplay + 1) % MAXWINES;
        rightDisplay = (rightDisplay + 1) % MAXWINES;

        if (firstWine >= wineViews.size() - 1) {
            firstWine = 0;
        } else {
            firstWine ++;
        }
    }

    /**
     * Changes the content within the teleporting frame.
     * Updates the firstwine index
     * @param frame is the teleporting frame (0 or 5)
     */
    public void resetFirstLeft(int frame) {
        wineViews.get(getId(frame)).getChildren().set(0, wineDisplays.get(rightDisplay));
        if (leftDisplay <= 0) {
            leftDisplay = MAXWINES - 1;
        } else {
            leftDisplay --;
        }
        if (rightDisplay == 0) {
            rightDisplay = MAXWINES - 1;
        } else {
            rightDisplay --;
        }

        if (firstWine <= 0) {
            firstWine = wineViews.size() - 1;
        } else {
            firstWine --;
        }
    }

    /**
     * Triggered by right button clicked.
     * Controls movement of the anchor panes
     */
    @FXML
    public void transitionRight() {
        rightArrowButton.setDisable(true);
        leftArrowButton.setDisable(true);
        shift(-1);
        fadeIn(6);
        fadeOut(1);
        teleportEnd(0, 1);
    }

    /**
     * Triggered by left button clicked.
     * Controls movement of the anchor panes
     */
    @FXML
    public void transitionLeft() {
        rightArrowButton.setDisable(true);
        leftArrowButton.setDisable(true);
        shift(1);
        fadeIn(0);
        fadeOut(5);
        teleportEnd(6, -1);
    }

    /**
     * Takes the user to the search page with search parameters of {@link WineCategoryDisplayController#tags}.
     */
    @FXML
    public void seeMore()
    {
        if (isWishlist) {
            FXWrapper.getInstance().launchSubPage("wishlist");
        } else if (isRecommendations) {
            SearchWineService.getInstance().searchWinesByRecommend(120);
            FXWrapper.getInstance().launchSubPage("searchWine");
        } else {
            SearchWineService.getInstance().searchWinesByTags(tags, SearchDAO.UNLIMITED);
            FXWrapper.getInstance().launchSubPage("searchWine");
        }
    }

    /**
     * Displays the wines in the category if there are 4 or fewer wines.
     */
    public void fiveOrLess() {
        for (Wine displaywine : displayWines) {
            SearchWineService.getInstance().setCurrentWine(displaywine);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/wineMiniDisplay.fxml"));
                wineDisplays.add(loader.load());
            } catch (IOException e) {
                LOG.error("Error in WineCategoryDisplayController.fourOrLess: Could not load fxml content for wine ID {}.", displaywine.getWineId());
            }
        }
        mainWine1.getChildren().add(wineDisplays.get(0));
        if (displayWines.size() >= 2) {
            mainWine2.getChildren().add(wineDisplays.get(1));
        }
        if (displayWines.size() >= 3) {
            mainWine3.getChildren().add(wineDisplays.get(2));
        }
        if (displayWines.size() >= 4) {
            mainWine4.getChildren().add(wineDisplays.get(3));
        }
        if (displayWines.size() == 5) {
            mainWine5.getChildren().add(wineDisplays.get(4));
        }
        leftArrowButton.setDisable(true);
        leftArrowButton.setVisible(false);
        rightArrowButton.setDisable(true);
        rightArrowButton.setVisible(false);
    }

    /**
     * Create new category display using searchString as the search parameter
     *
     * @param searchString A String that contains the tags to search by seperated by commas
     *                     If string is "wishlist" or "recommend", the corresponding search will be done instead
     * @throws IOException if fxmlLoader cannot load the display
     * @return the parent of the new category
     */
    public static Parent createCategory(String searchString) throws IOException {

        if (searchString.equalsIgnoreCase("wishlist")) {
            SearchWineService.getInstance().searchWinesByWishlist(User.getCurrentUser().getId());
        } else if (searchString.equalsIgnoreCase("recommend")) {
            SearchWineService.getInstance().searchWinesByRecommend(10);
        } else {
            SearchWineService.getInstance().searchWinesByTags(searchString, 10);
        }

        savedWineList = SearchWineService.getInstance().getWineList();
        savedSearchString = searchString;


        FXMLLoader fxmlLoader = new FXMLLoader(WineCategoryDisplayController.class
                .getResource("/fxml/wineCategoryDisplay.fxml"));
        Parent parent = fxmlLoader.load();
        // Have to do this as it requires multiple loops to finish completely
        // - need to use for "A Task Which Returns Partial Results", from the Task documentation
        if (!searchString.equalsIgnoreCase("recommend")) {
            WineCategoryService.getInstance().incrementCurrentCategory();
        }
        return parent;
    }
}
