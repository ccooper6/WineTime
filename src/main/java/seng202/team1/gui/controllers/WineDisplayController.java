package seng202.team1.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import seng202.team1.gui.FXWrapper;
import seng202.team1.models.Wine;
import seng202.team1.services.SearchWineService;

/**
 * Controller for displaying wine cards.
 * @author Yuhao Zhang, Caleb Cooper
 */
public class WineDisplayController {
    @FXML
    private Label wineInfo;
    @FXML
    private ImageView wineImage;
    @FXML
    private AnchorPane winePane;
    @FXML
    private AnchorPane wineCompleted;

    private Wine wine;

    /**
     * Displays the wine card using SearchWineService instances' current wine.
     */
    @FXML
    public void initialize()
    {
        wine = SearchWineService.getInstance().getCurrentWine();
        wineCompleted.setVisible(false);
        wineImage.setImage(new Image(wine.getImagePath()));
        String infoText = "Name: " + wine.getName();
        infoText += "\nVariety: " + wine.getVariety();
        wineInfo.setText(infoText);

        winePane.setOnMouseEntered(event -> darkenPane());
        winePane.setOnMouseExited(event -> lightenPane());
    }

    /**
     * Shows the wine in more detail with a popUp that overlays the screen.
     * Calls {@link NavigationController#initPopUp(Wine)} using this wine as the base.
     */
    @FXML
    public void popUp()
    {
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();

        navigationController.initPopUp(wine);
    }

    /**
     * Darkens the pane when the mouse enters.
     */
    @FXML
    public void darkenPane()
    {
        winePane.setStyle("-fx-background-color: #999999; -fx-background-radius: 15;");
    }

    /**
     * Lightens the pane when the mouse exits.
     */
    @FXML
    public void lightenPane()
    {
        winePane.setStyle("-fx-background-color: white; -fx-border-radius: 15; -fx-background-radius: 15; -fx-border-color: #d9d9d9");
    }

    @FXML
    public void completedChallengeWine()
    {
//        winePane.setStyle("-fx-background-color:  #008000; -fx-border-radius: 15; -fx-background-radius: 15; -fx-border-color: #d9d9d9");
        wineCompleted.setVisible(true);
        wineCompleted.setMouseTransparent(true);
    }

}
