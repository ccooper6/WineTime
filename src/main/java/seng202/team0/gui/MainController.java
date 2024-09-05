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

import java.io.IOException;
import java.util.ArrayList;
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
        SearchWineService.getInstance().searchWinesByName("Rainstorm");

        try {
            FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("/fxml/wineCategoryDisplay.fxml"));
            Parent parent1 = fxmlLoader1.load();
            contentsGrid.add(parent1, 0, 0);
//            SearchWineService.getInstance().searchWinesByName("New Zealand, Sauvignon Blanc");
//            FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("/fxml/wineCategoryDisplay.fxml"));
//            Parent parent2 = fxmlLoader2.load();
//            contentsGrid.add(parent2, 1, 1);
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
