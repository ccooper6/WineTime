package seng202.team0.gui;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Translate;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.w3c.dom.Text;
import seng202.team0.models.Wine;
import seng202.team0.models.testWines.*;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainPageController {
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
    public String getWineInfo(Wine wine) {
        return("Name: %s\nVariety: %s\nDescription: %s".formatted(wine.getName(), wine.getVariety(), wine.getDescription()));
    }
    // ******************ENDS HERE********************

    public void initialize() {
        wineViews = List.of(mainWine1, mainWine2, mainWine3, mainWine4, mainWine5);
        List<Label> wineInfos = List.of(wineInfo1, wineInfo2, wineInfo3, wineInfo4, wineInfo5);
        List<ImageView> wineIcons = List.of(mainWineIcon1, mainWineIcon2, mainWineIcon3, mainWineIcon4, mainWineIcon5);
        displayWines(wineInfos, wineIcons);
        onRefresh(wineInfos, wineIcons);
    }

    @FXML
    public void displayWines(List<Label> wineInfo, List<ImageView> wineIcon) {
        if(winesTest.size() >= wineViews.size()) {
            for (int i = 0; i < wineViews.size(); i++) {
                wineInfo.get(i).setText(String.valueOf(getId(i)));
                //wineInfo.get(i).setText(getWineInfo(winesTest.get(i)));
                wineIcon.get(i).setImage(new Image(winesTest.get(i).getImagePath()));
            }
        } else {
            for (int i = winesTest.size(); i < wineViews.size(); i++) {
                wineInfo.get(i).setText("No wine available.");
                wineIcon.get(i).setImage(null);
            }
        }
    }

    @FXML
    public void onRefresh( List<Label> wineInfo, List<ImageView> wineIcon) {
        scrollArrow.setOnMouseClicked(event -> {
            transition();
            /*Wine firstWine = winesTest.get(first);
            winesTest.remove(0);
            winesTest.add(firstWine);*/
            displayWines(wineInfo, wineIcon);
        });
//
    }

    public int getId(int id) {
        return (firstWine + id) % wineViews.size();
    }

    public void resetFirst() {
        TranslateTransition transition1 = new TranslateTransition(Duration.seconds(1), wineViews.get(firstWine));
        transition1.setByX(-225);
        transition1.setInterpolator(Interpolator.LINEAR);
        transition1.play();
        fadeOut();
        TranslateTransition transitionReturn = new TranslateTransition(Duration.seconds(0), wineViews.get(firstWine));
        transitionReturn.setByX(900);
        transitionReturn.setInterpolator(Interpolator.LINEAR);
        transitionReturn.play();
        if (firstWine >= wineViews.size() - 1) {
            firstWine = 0;
        } else {
            firstWine ++;
        }
    }
    public void fadeOut(){
        FadeTransition  fadeTransitionOut = new FadeTransition(Duration.seconds(1), wineViews.get(firstWine));
        fadeTransitionOut.setFromValue(1);
        fadeTransitionOut.setToValue(0.5);
        fadeTransitionOut.play();
        wineViews.get(firstWine).setDisable(true);
    }
    public void fadeIn() {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), wineViews.get(getId(4)));
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
        wineViews.get(getId(4)).setDisable(false);
    }

    @FXML
    public void transition() {
        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(1), wineViews.get(getId(1)));
        TranslateTransition transition3 = new TranslateTransition(Duration.seconds(1), wineViews.get(getId(2)));
        TranslateTransition transition4 = new TranslateTransition(Duration.seconds(1), wineViews.get(getId(3)));
        TranslateTransition transition5 = new TranslateTransition(Duration.seconds(1), wineViews.get(getId(4)));
        List<TranslateTransition> wineTransitions = List.of(transition2, transition3, transition4, transition5);
        for (int i = 0; i < wineTransitions.size(); i++) {
            wineTransitions.get(i).setByX(-225);
            wineTransitions.get(i).setInterpolator(Interpolator.LINEAR);
            wineTransitions.get(i).play();
        }
        fadeIn();
        resetFirst();
    }

    @FXML
    public void onWineClicked(MouseEvent event) { // From advanced java fx tutorial
        AnchorPane pane = (AnchorPane) event.getSource();
        String[] name = pane.getId().split("");
        Integer paneNum = Integer.valueOf(name[8]);
        Wine wine = winesTest.get(paneNum - 1);

        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.initPopUp(wine);
    }

    @FXML
    public void darkenPane(MouseEvent event) {
        AnchorPane pane = (AnchorPane) event.getSource();
        pane.setStyle("-fx-background-color: #999999; -fx-background-radius: 15");
    }

    @FXML
    public void lightenPane(MouseEvent event) {
        AnchorPane pane = (AnchorPane) event.getSource();
        pane.setStyle("-fx-border-color: #d9d9d9; -fx-border-radius: 15");
    }

    @FXML
    public void scroll() {

    }
}
