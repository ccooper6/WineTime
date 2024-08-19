package seng202.team0.gui;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
    Label wineInfo1;
    @FXML
    Label wineInfo2;
    @FXML
    Label wineInfo3;
    @FXML
    Label wineInfo4;
    @FXML
    ImageView mainWineIcon1;
    @FXML
    ImageView mainWineIcon2;
    @FXML
    ImageView mainWineIcon3;
    @FXML
    ImageView mainWineIcon4;
    @FXML
    FontAwesomeIconView scrollArrow;

    // ***********TEST CASE WINE OBJECTS***************
    List<Wine> winesTest = new ArrayList<>(List.of(new wine1(), new wine2(), new wine3(), new wine4(), new wine5(), new wine6()));
    // consider adding a wine info aspect to wine class so u can get the string description from wines
    public String getWineInfo(Wine wine) {
        return("Name: %s\nVariety: %s\nDescription: %s".formatted(wine.getName(), wine.getVariety(), wine.getDescription()));
    }
    // ******************ENDS HERE********************

    public void initialize() {
        List<AnchorPane> wineViews = List.of(mainWine1, mainWine2, mainWine3, mainWine4);
        List<Label> wineInfos = List.of(wineInfo1, wineInfo2, wineInfo3, wineInfo4);
        List<ImageView> wineIcons = List.of(mainWineIcon1, mainWineIcon2, mainWineIcon3, mainWineIcon4);
        displayWines(wineViews, wineInfos, wineIcons);
        onRefresh(wineViews, wineInfos, wineIcons);
    }

    @FXML
    public void displayWines(List<AnchorPane> wineView, List<Label> wineInfo, List<ImageView> wineIcon) {
        if(winesTest.size() >= wineView.size()) {
            for (int i = 0; i < wineView.size(); i++) {
                wineInfo.get(i).setText(getWineInfo(winesTest.get(i)));
                wineIcon.get(i).setImage(new Image(winesTest.get(i).getImagePath()));
            }
        } else {
            for (int i = winesTest.size(); i < wineView.size(); i++) {
                wineInfo.get(i).setText("No wine available.");
                wineIcon.get(i).setImage(null);
            }
        }
    }


    @FXML
    public void onRefresh(List<AnchorPane> wineView, List<Label> wineInfo, List<ImageView> wineIcon) {
        scrollArrow.setOnMouseClicked(event -> {
            Wine firstWine = winesTest.get(0);
            winesTest.remove(0);
            winesTest.add(firstWine);
            displayWines(wineView, wineInfo, wineIcon);
        });
//
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
        pane.setStyle("-fx-background-color: #999999");
    }

    @FXML
    public void lightenPane(MouseEvent event) {
        AnchorPane pane = (AnchorPane) event.getSource();
        pane.setStyle("-fx-border-color: #d9d9d9");
    }

    @FXML
    public void scroll() {

    }
}
