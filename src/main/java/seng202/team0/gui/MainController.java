package seng202.team0.gui;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import seng202.team0.models.Wine;
import seng202.team0.services.SearchWineService;
import seng202.team0.services.WineCategoryService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Controller class for the mainpage.fxml page
 * @author Caleb Cooper
 */
public class MainController {
    @FXML
    GridPane contentsGrid;

    /**
     * Initializes the controller.
     */
    public void initialize() {
        SearchWineService.getInstance().searchWinesByTags("Bordeaux, Merlot");

        try {
            FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("/fxml/wineCategoryDisplay.fxml"));
            Parent parent1 = fxmlLoader1.load();
            contentsGrid.add(parent1, 0, 0);
            WineCategoryService.getInstance().incrementCurrentCategory();
            SearchWineService.getInstance().searchWinesByTags("Marlborough, Sauvignon Blanc");
            FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("/fxml/wineCategoryDisplay.fxml"));
            Parent parent2 = fxmlLoader2.load();
            contentsGrid.add(parent2, 0, 1);
            WineCategoryService.getInstance().incrementCurrentCategory();
            SearchWineService.getInstance().searchWinesByTags("Tuscany, Sangiovese");
            FXMLLoader fxmlLoader3 = new FXMLLoader(getClass().getResource("/fxml/wineCategoryDisplay.fxml"));
            Parent parent3 = fxmlLoader3.load();
            contentsGrid.add(parent3, 0, 2);
            WineCategoryService.getInstance().incrementCurrentCategory();
            SearchWineService.getInstance().searchWinesByTags("Hawke's Bay, Syrah");
            FXMLLoader fxmlLoader4 = new FXMLLoader(getClass().getResource("/fxml/wineCategoryDisplay.fxml"));
            Parent parent4 = fxmlLoader4.load();
            contentsGrid.add(parent4, 0, 3);
            WineCategoryService.getInstance().incrementCurrentCategory();
            SearchWineService.getInstance().searchWinesByTags("Spain, Rioja, Tempranillo");
            FXMLLoader fxmlLoader5 = new FXMLLoader(getClass().getResource("/fxml/wineCategoryDisplay.fxml"));
            Parent parent5 = fxmlLoader5.load();
            contentsGrid.add(parent5, 0, 4);
            WineCategoryService.getInstance().incrementCurrentCategory();
            SearchWineService.getInstance().searchWinesByTags("New Zealand, Gisborne, Chardonnay");
            FXMLLoader fxmlLoader6 = new FXMLLoader(getClass().getResource("/fxml/wineCategoryDisplay.fxml"));
            Parent parent6 = fxmlLoader6.load();
            contentsGrid.add(parent6, 0, 5);
            WineCategoryService.getInstance().incrementCurrentCategory();
            SearchWineService.getInstance().searchWinesByTags("US, Napa Valley, Cabernet Sauvignon");
            FXMLLoader fxmlLoader7 = new FXMLLoader(getClass().getResource("/fxml/wineCategoryDisplay.fxml"));
            Parent parent7 = fxmlLoader7.load();
            contentsGrid.add(parent7, 0, 6);
            WineCategoryService.getInstance().incrementCurrentCategory();
            SearchWineService.getInstance().searchWinesByTags("Central Otago, Pinot Noir, New Zealand");
            FXMLLoader fxmlLoader8 = new FXMLLoader(getClass().getResource("/fxml/wineCategoryDisplay.fxml"));
            Parent parent8 = fxmlLoader8.load();
            contentsGrid.add(parent8, 0, 7);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Calls the initialize popup function from navigation controller to display the data of the
     * corresponding wine.
     * @param event the anchor pane of the wine that was clicked
     */
//    @FXML
//    public void onWineClicked(MouseEvent event) { // From advanced java fx tutorial
//        AnchorPane pane = (AnchorPane) event.getSource();
//        String[] name = pane.getId().split("");
//        Integer paneNum = Integer.valueOf(name[8]);
//        Wine wine = wineList.get(paneNum - 1);
//
//        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
//        navigationController.initPopUp(wine);
//    }

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
