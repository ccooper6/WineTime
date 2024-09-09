package seng202.team0.gui;

import javafx.fxml.FXML;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import seng202.team0.models.Wine;
import seng202.team0.services.SearchWineService;

/**
 * Controller for displaying wine cards
 * @author yzh428
 */
public class WineDisplayController {
    @FXML
    Label wineInfo;
    @FXML
    ImageView wineImage;
    @FXML
    AnchorPane winePane;

    Wine wine;

    /**
     * Displays the wine card using SearchWineService instances' current wine
     */
    @FXML
    public void initialize()
    {
        wine = SearchWineService.getInstance().getCurrentWine();

        wineImage.setImage(new Image(wine.getImagePath()));

//        set text info
        String infoText = "Name: " + wine.getName();
        infoText += "\nVariety: " + wine.getVariety();
        wineInfo.setText(infoText);

        winePane.setOnMouseEntered(event -> darkenPane());
        winePane.setOnMouseExited(event -> lightenPane());
    }

    /**
     * Shows the wine in more detail with a popUp that overlays the screen.
     * <p>
     * Calls {@link NavigationController#initPopUp(Wine)} using this wine as the base.
     */
    @FXML
    public void popUp()
    {
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();

        navigationController.initPopUp(wine);
    }

    /**
     * Darkens the pane when the mouse enters
     */
    @FXML
    public void darkenPane()
    {
        winePane.setStyle("-fx-background-color: #999999; -fx-background-radius: 15;");
    }

    /**
     * Lightens the pane when the mouse exits
     */
    @FXML
    public void lightenPane()
    {
        winePane.setStyle("-fx-background-color: white; -fx-border-radius: 15; -fx-background-radius: 15; -fx-border-color: #d9d9d9");
    }

}
