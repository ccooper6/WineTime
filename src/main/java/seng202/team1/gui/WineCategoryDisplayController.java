package seng202.team1.gui;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import seng202.team1.models.Wine;
import seng202.team1.repository.SearchDAO;
import seng202.team1.services.SearchWineService;
import seng202.team1.services.WineCategoryService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WineCategoryDisplayController {
    @FXML
    Text titleText;

    @FXML
    FontAwesomeIconView leftArrowButton;
    @FXML
    FontAwesomeIconView rightArrowButton;
    @FXML
    AnchorPane mainWine0;
    @FXML
    AnchorPane mainWine1;
    @FXML
    AnchorPane mainWine2;
    @FXML
    AnchorPane mainWine3;
    @FXML
    AnchorPane mainWine4;
    @FXML
    AnchorPane mainWine5;
    List<AnchorPane> wineViews;
    int firstWine = 0;
    int MAXWINES = 10;
    int leftDisplay = 6;
    int rightDisplay = MAXWINES -1;
    double TRANSDURATION = 0.2;
    int DISTANCEBETWEEN = 200;

    ArrayList<Parent> wineDisplays;

    String tags;

    @FXML
    public void initialize()
    {
        wineViews = List.of(mainWine0, mainWine1, mainWine2, mainWine3, mainWine4, mainWine5);

        onRefresh();
        ArrayList<Wine> displayWines = SearchWineService.getInstance().getWineList();
        tags = SearchWineService.getInstance().getCurrentTags();

        if (displayWines == null || displayWines.size() < 6) {
            System.out.println("Wine list too short");
            return;
        }

        wineDisplays = new ArrayList<>();
        titleText.setText(WineCategoryService.getInstance().getCategoryTitles().get(WineCategoryService.getInstance().getCurrentCategory()));
        for (int i = 0; i < Math.min(displayWines.size(), MAXWINES); i++) {
            SearchWineService.getInstance().setCurrentWine(displayWines.get(i));
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/wineMiniDisplay.fxml"));
                wineDisplays.add(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mainWine0.getChildren().add(wineDisplays.get(0));
        mainWine1.getChildren().add(wineDisplays.get(1));
        mainWine2.getChildren().add(wineDisplays.get(2));
        mainWine3.getChildren().add(wineDisplays.get(3));
        mainWine4.getChildren().add(wineDisplays.get(4));
        mainWine5.getChildren().add(wineDisplays.get(5));
        System.out.println(wineDisplays);
    }

    /**
     * Refreshes the wine list in preparation to be displayed on the main page.
     */
    @FXML
    public void onRefresh(/*List<Label> wineInfo, List<ImageView> wineIcon*/) {
        rightArrowButton.setOnMouseClicked(event -> {
            transitionRight();
        });

        leftArrowButton.setOnMouseClicked(event -> {
            transitionLeft();
        });
    }

    /**
    * @param id is the position of the view pane.
    * @return id of the wineView which should be displayed at that position.
     */
    public int getId(int id) {
        return (firstWine + id) % wineViews.size();
    }

    public void shift(int posOrNeg) {
        TranslateTransition transition1 = new TranslateTransition(Duration.seconds(TRANSDURATION), wineViews.get(getId(1)));
        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(TRANSDURATION), wineViews.get(getId(2)));
        TranslateTransition transition3 = new TranslateTransition(Duration.seconds(TRANSDURATION), wineViews.get(getId(3)));
        TranslateTransition transition4 = new TranslateTransition(Duration.seconds(TRANSDURATION), wineViews.get(getId(4)));
        TranslateTransition transition5;
        if (posOrNeg == 1) {
            transition5 = new TranslateTransition(Duration.seconds(TRANSDURATION), wineViews.get(getId(0)));
        } else {
            transition5 = new TranslateTransition(Duration.seconds(TRANSDURATION), wineViews.get(getId(5)));
        }
        List<TranslateTransition> wineTransitions = List.of(transition1, transition2, transition3, transition4, transition5);
        for (int i = 0; i < wineTransitions.size(); i++) {
            wineTransitions.get(i).setByX(posOrNeg * DISTANCEBETWEEN);
            wineTransitions.get(i).setInterpolator(Interpolator.LINEAR);
            wineTransitions.get(i).play();
        }
    }

    /**
     * The moving frame (0 or 5) increases in opacity, the button is enabled once visible.
     * @param movingFrame is the relative id of the anchor pane moving.
     */
    public void fadeIn(int movingFrame) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(TRANSDURATION), wineViews.get(getId(movingFrame)));
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
        wineViews.get(getId(movingFrame)).setDisable(false);
        //fadeTransition.setOnFinished(event -> );
        System.out.println("enabled " + getId(movingFrame));
    }

    /**
     * The moving frame (1 or 4) decreases in opacity, the button is disabled
     * @param movingFrame is the relative id of the anchor pane moving.
     */
    public void fadeOut(int movingFrame) {
        FadeTransition fadeTransitionOut = new FadeTransition(Duration.seconds(TRANSDURATION), wineViews.get(getId(movingFrame)));
        fadeTransitionOut.setFromValue(1);
        fadeTransitionOut.setToValue(0);
        fadeTransitionOut.play();
        wineViews.get(getId(movingFrame)).setDisable(true);
        //fadeTransitionOut.setOnFinished(event -> wineViews.get(getId(movingFrame)).setDisable(true));
        System.out.println("disabled " + getId(movingFrame));
    }

    public void teleportEnd(int movingFrame, int posOrNeg) {
        TranslateTransition transitionReturn = new TranslateTransition(Duration.seconds(0.1), wineViews.get(getId(movingFrame)));
        transitionReturn.setByX(posOrNeg * DISTANCEBETWEEN * 5);
        transitionReturn.setInterpolator(Interpolator.DISCRETE);
        transitionReturn.play();
        transitionReturn.setOnFinished(event -> {
            if(posOrNeg == -1) {
                resetFirstLeft(movingFrame);
            } else {
                resetFirstRight(movingFrame);
            }
            rightArrowButton.setDisable(false);
            leftArrowButton.setDisable(false);
        });
    }

    /**
     * The indexing of the wine views is reset
     */
    public void resetFirstRight(int frame) {
        wineViews.get(getId(frame)).getChildren().set(0, wineDisplays.get(leftDisplay));
        leftDisplay = (leftDisplay + 1) % MAXWINES;
        rightDisplay = (rightDisplay + 1) % MAXWINES;
        System.out.println(leftDisplay + " "+ rightDisplay);

        if (firstWine >= wineViews.size() - 1) {
            firstWine = 0;
        } else {
            firstWine ++;
        }
    }
    public void resetFirstLeft(int frame) {
        wineViews.get(getId(frame)).getChildren().set(0, wineDisplays.get(rightDisplay));
        if(leftDisplay <= 0) {
            leftDisplay = MAXWINES -1;
        } else {
            leftDisplay --;
        }
        if (rightDisplay == 0) {
            rightDisplay = MAXWINES -1;
        } else {
            rightDisplay --;
        }
        System.out.println(leftDisplay + " "+  rightDisplay);

        if (firstWine <= 0) {
            firstWine = wineViews.size() -1;
        } else {
            firstWine --;
        }
    }
//    private void getLeftNewDisplay(int frame) {
//        wineViews.get(getId(frame)).getChildren().set(0, wineDisplays.get(newDisplay));
//        newDisplay = (newDisplay + 1) % MAXWINES;
//    }

    @FXML
    public void transitionRight() {
        rightArrowButton.setDisable(true);
        leftArrowButton.setDisable(true);
        shift(-1);
        fadeIn(5);
        fadeOut(1);
        teleportEnd(0, 1);
    }
    @FXML
    public void transitionLeft() {
        rightArrowButton.setDisable(true);
        leftArrowButton.setDisable(true);
        shift(1);
        fadeIn(0);
        fadeOut(4);
        teleportEnd(5, -1);
    }

    /**
     * Takes the user to the search page with search parameters of {@link WineCategoryDisplayController#tags}
     */
    @FXML
    public void seeMore()
    {
        SearchWineService.getInstance().searchWinesByTags(tags, SearchDAO.UNLIMITED);
        FXWrapper.getInstance().launchSubPage("searchWine");
    }
}
