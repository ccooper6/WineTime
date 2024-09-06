package seng202.team0.gui;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import seng202.team0.models.Wine;
import seng202.team0.models.testWines.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for the mainpage.fxml page
 * @author Caleb Cooper and Elise Newman
 */
public class MainController {
    @FXML
    Text helloText;
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
    @FXML
    Label wineInfo1;
    @FXML
    Label wineInfo2;
    @FXML
    Label wineInfo3;
    @FXML
    Label wineInfo4;
    @FXML
    Label wineInfo5;
    @FXML
    ImageView mainWineIcon1;
    @FXML
    ImageView mainWineIcon2;
    @FXML
    ImageView mainWineIcon3;
    @FXML
    ImageView mainWineIcon4;
    @FXML
    ImageView mainWineIcon5;

    @FXML
    FontAwesomeIconView scrollArrow;
    int firstWine = 0;
    List<AnchorPane> wineViews;

    // ***********TEST CASE WINE OBJECTS***************
    List<Wine> winesTest = new ArrayList<>(List.of(new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6()));
    // consider adding a wine info aspect to wine class so u can get the string description from wines

    /**
     * Class to format wine information to display on main page.
     * @param wine wine to get data of
     * @return formatted wine data string
     */
    public String getWineInfo(Wine wine) {
        return ("Name: %s\nVariety: %s\nDescription: %s".formatted(wine.getName(), wine.getVariety(), wine.getDescription()));
    }
    // ******************ENDS HERE********************

    /**
     * Initializes the controller.
     */
    public void initialize() {
        wineViews = List.of(mainWine1, mainWine2, mainWine3, mainWine4, mainWine5);
        List<Label> wineInfos = List.of(wineInfo1, wineInfo2, wineInfo3, wineInfo4, wineInfo5);
        List<ImageView> wineIcons = List.of(mainWineIcon1, mainWineIcon2, mainWineIcon3, mainWineIcon4, mainWineIcon5);
        displayWines(wineInfos, wineIcons);
        onRefresh(wineInfos, wineIcons);
    }

    /**
     * Sets each anchor pane to include the wine data of the corresponding wine object.
     * @param wineInfo list text boxes for the wine data
     * @param wineIcon list of wine icons corresponding to each wine
     */
    @FXML
    public void displayWines(List<Label> wineInfo, List<ImageView> wineIcon) {
        if(winesTest.size() >= wineViews.size()) {
            for (int i = 0; i < wineViews.size(); i++) {
                wineInfo.get(getId(i)).setText(String.valueOf(getId(i)));
                //wineInfo.get(i).setText(getWineInfo(winesTest.get(i)));
                wineIcon.get(getId(i)).setImage(new Image(winesTest.get(getId(i)).getImagePath()));
            }
        } else {
            for (int i = winesTest.size(); i < wineViews.size(); i++) {
                wineInfo.get(i).setText("No wine available.");
                wineIcon.get(i).setImage(null);
            }
        }
    }

    /**
     * Refreshes the wine list in preparation to be displayed on the main page.
     * @param wineInfo list text boxes for the wine data
     * @param wineIcon list of wine icons corresponding to each wine
     */
    @FXML
    public void onRefresh( List<Label> wineInfo, List<ImageView> wineIcon) {
        scrollArrow.setOnMouseClicked(event -> {
            transition();
            displayWines(wineInfo, wineIcon);
        });
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
        scrollArrow.setDisable(false);
    }

    /**
     * As the first wine moves to the left, it decreases in opacity, is disabled and then teleports to the right;
     */
    public void fadeOut() {
        FadeTransition fadeTransitionOut = new FadeTransition(Duration.seconds(0.5), wineViews.get(firstWine));
        fadeTransitionOut.setFromValue(1);
        fadeTransitionOut.setToValue(0);
        fadeTransitionOut.play();
        wineViews.get(firstWine).setDisable(true);
        TranslateTransition transitionReturn = new TranslateTransition(Duration.seconds(0.1), wineViews.get(firstWine));
        transitionReturn.setByX(1125);
        transitionReturn.setInterpolator(Interpolator.DISCRETE);
        fadeTransitionOut.setOnFinished(event -> transitionReturn.play());
        transitionReturn.setOnFinished(event -> resetFirst());
    }

    /**
     * As the last wine moves in from the right, it increases in opacity and is enabled.
     */
    public void fadeIn() {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), wineViews.get(getId(4)));
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
        fadeTransition.setOnFinished(event -> wineViews.get(getId(4)).setDisable(false));
    }

    /**
     * Controls all the horizontal transitions of each category, triggered by onRefresh
     */
    @FXML
    public void transition() {
        scrollArrow.setDisable(true);
        TranslateTransition transition1 = new TranslateTransition(Duration.seconds(0.5), wineViews.get(getId(0)));
        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(0.5), wineViews.get(getId(1)));
        TranslateTransition transition3 = new TranslateTransition(Duration.seconds(0.5), wineViews.get(getId(2)));
        TranslateTransition transition4 = new TranslateTransition(Duration.seconds(0.5), wineViews.get(getId(3)));
        TranslateTransition transition5 = new TranslateTransition(Duration.seconds(0.5), wineViews.get(getId(4)));
        List<TranslateTransition> wineTransitions = List.of(transition1, transition2, transition3, transition4, transition5);
        for (int i = 0; i < wineTransitions.size(); i++) {
            wineTransitions.get(i).setByX(-225);
            wineTransitions.get(i).setInterpolator(Interpolator.LINEAR);
            wineTransitions.get(i).play();
        }
        //below all occur at the same time as wineTransitions
        fadeIn();
        fadeOut();
    }

    /**
     * Calls the initialize popup function from navigation controller to display the data of the
     * corresponding wine.
     * @param event the anchor pane of the wine that was clicked
     */
    @FXML
    public void onWineClicked(MouseEvent event) { // From advanced java fx tutorial
        AnchorPane pane = (AnchorPane) event.getSource();
        String[] name = pane.getId().split("");
        Integer paneNum = Integer.valueOf(name[8]);
        Wine wine = winesTest.get(paneNum - 1);

        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.initPopUp(wine);
    }

    /**
     * Darkens the anchor pane to indicate the cursor is hovering.
     * @param event the anchor pane of the wine that was hovered over
     */
    @FXML
    public void darkenPane(MouseEvent event) {
        AnchorPane pane = (AnchorPane) event.getSource();
        pane.setStyle("-fx-background-color: #999999; -fx-background-radius: 15");
    }

    /**
     * Lightens the anchor pane to indicate the cursor is no longer hovering.
     * @param event the anchor pane of the wine that was hovered over
     */
    @FXML
    public void lightenPane(MouseEvent event) {
        AnchorPane pane = (AnchorPane) event.getSource();
        pane.setStyle("-fx-border-color: #d9d9d9; -fx-border-radius: 15");
    }
}
