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
import seng202.team1.services.SearchWineService;
import seng202.team1.repository.WineCategoryService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WineCategoryDisplayController {
    @FXML
    Text titleText;

    @FXML
    GridPane wineGrid;

    @FXML
    FontAwesomeIconView leftArrowButton;
    @FXML
    FontAwesomeIconView rightArrowButton;

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
    int MAXWINES = 5;
    double TRANSDURATION = 0.2;
    int DISTANCEBETWEEN = 200;

    ArrayList<Parent> wineDisplays;

    @FXML
    public void initialize()
    {
        wineViews = List.of(mainWine1, mainWine2, mainWine3, mainWine4, mainWine5);
        //List<Label> wineInfos = List.of(wineInfo1, wineInfo2, wineInfo3, wineInfo4, wineInfo5);
        //List<ImageView> wineIcons = List.of(mainWineIcon1, mainWineIcon2, mainWineIcon3, mainWineIcon4, mainWineIcon5);
        //displayWines(wineInfos, wineIcons);
        onRefresh();

        ArrayList<Wine> displayWines = SearchWineService.getInstance().getWineList();

        if (displayWines == null || displayWines.size() < 5) {
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
        mainWine1.getChildren().add(wineDisplays.get(0));
        mainWine2.getChildren().add(wineDisplays.get(1));
        mainWine3.getChildren().add(wineDisplays.get(2));
        mainWine4.getChildren().add(wineDisplays.get(3));
        mainWine5.getChildren().add(wineDisplays.get(4));
    }

    /**
     * Refreshes the wine list in preparation to be displayed on the main page.
     */
    @FXML
    public void onRefresh(/*List<Label> wineInfo, List<ImageView> wineIcon*/) {
        rightArrowButton.setOnMouseClicked(event -> {
            transitionRight();
            //displayWines(wineInfo, wineIcon);
        });
//
//        leftArrowButton.setOnMouseClicked(event -> {
//            transitionRight();
//        });
    }

    /**
    * @param id is the position of the view pane.
    * @return id of the wineView which should be displayed at that position.
     */
    public int getId(int id) {
        return (firstWine + id) % wineViews.size();
    }

    /**
     * The indexing of the wine views is reset
     */
    public void resetFirst() {
        if (firstWine >= wineViews.size() - 1) {
            firstWine = 0;
        } else {
            firstWine ++;
        }
        rightArrowButton.setDisable(false);
        leftArrowButton.setDisable(false);
    }
//    public void resetFirstLeft() {
//        if (firstWine <= 0) {
//            firstWine = wineViews.size() -1;
//        } else {
//            firstWine --;
//        }
//        rightArrowButton.setDisable(false);
//        leftArrowButton.setDisable(false);
//    }

    /**
     * As the first wine moves to the left, it decreases in opacity, is disabled and then teleports to the right;
     */
    public void fadeOutRight() {
        FadeTransition fadeTransitionOut = new FadeTransition(Duration.seconds(TRANSDURATION), wineViews.get(firstWine));
        fadeTransitionOut.setFromValue(1);
        fadeTransitionOut.setToValue(0);
        fadeTransitionOut.play();
        wineViews.get(firstWine).setDisable(true);
        TranslateTransition transitionReturn = new TranslateTransition(Duration.seconds(0.1), wineViews.get(firstWine));
        transitionReturn.setByX(DISTANCEBETWEEN * 5);
        transitionReturn.setInterpolator(Interpolator.DISCRETE);
        fadeTransitionOut.setOnFinished(event -> transitionReturn.play());
        transitionReturn.setOnFinished(event -> resetFirst());
    }

    /**
     * As the last wine moves in from the right, it increases in opacity and is enabled.
     */
    public void fadeInRight() {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(TRANSDURATION), wineViews.get(getId(4)));
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
        fadeTransition.setOnFinished(event -> wineViews.get(getId(4)).setDisable(false));
    }

    @FXML
    public void transitionRight() {
        rightArrowButton.setDisable(true);
        leftArrowButton.setDisable(true);
        TranslateTransition transition1 = new TranslateTransition(Duration.seconds(TRANSDURATION), wineViews.get(getId(1)));
        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(TRANSDURATION), wineViews.get(getId(2)));
        TranslateTransition transition3 = new TranslateTransition(Duration.seconds(TRANSDURATION), wineViews.get(getId(3)));
        TranslateTransition transition4 = new TranslateTransition(Duration.seconds(TRANSDURATION), wineViews.get(getId(4)));
        TranslateTransition transition5 = new TranslateTransition(Duration.seconds(TRANSDURATION), wineViews.get(getId(5)));
        List<TranslateTransition> wineTransitions = List.of(transition1, transition2, transition3, transition4, transition5);
        for (int i = 0; i < wineTransitions.size(); i++) {
            wineTransitions.get(i).setByX(-DISTANCEBETWEEN);
            wineTransitions.get(i).setInterpolator(Interpolator.LINEAR);
            wineTransitions.get(i).play();
        }
        //below all occur at the same time as wineTransitions
        fadeInRight();
        fadeOutRight();
    }
}
