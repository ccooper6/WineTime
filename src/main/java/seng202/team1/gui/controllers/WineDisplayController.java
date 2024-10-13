package seng202.team1.gui.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import seng202.team1.gui.FXWrapper;
import seng202.team1.models.User;
import seng202.team1.models.Wine;
import seng202.team1.services.ReviewService;
import seng202.team1.services.SearchWineService;
import seng202.team1.services.WishlistService;

/**
 * Controller class for wineMiniDisplay.fxml.
 * Displays a wine card with the wine's information and image.
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
    @FXML
    private FontAwesomeIconView wineLiked;
    @FXML
    private FontAwesomeIconView wineReviewed;

    private Wine wine;
    private final ReviewService REVIEWSERVICE = new ReviewService();

    /**
     * Default constructor for WineDisplayController
     */
    public WineDisplayController(){}

    /**
     * Initializes the controller and displays the wine card using SearchWineService instances' current wine.
     */
    @FXML
    public void initialize()
    {
        wine = SearchWineService.getInstance().getCurrentWine();
        wineCompleted.setVisible(false);
        wineImage.setImage(new Image(wine.getImagePath()));
        wineInfo.setText(wine.getName());
        wineInfo.setWrapText(true);
        winePane.setOnMouseEntered(event -> darkenPane());
        winePane.setOnMouseExited(event -> lightenPane());

        wineReviewed.setVisible(REVIEWSERVICE.reviewExists(User.getCurrentUser().getId(), wine.getWineId()));
        wineLiked.setVisible(WishlistService.checkInWishlist(wine.getWineId(), User.getCurrentUser().getId()));
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
        winePane.setStyle("-fx-background-color: #d6d6d6; -fx-background-radius: 15;");
    }

    /**
     * Lightens the pane when the mouse exits.
     */
    @FXML
    public void lightenPane()
    {
        winePane.setStyle("-fx-background-color: white; -fx-border-radius: 15; -fx-background-radius: 15; -fx-border-color: #d9d9d9");
    }

    /**
     * makes overlay pane visible to highlight the tile green with a tick.
     */
    @FXML
    public void completedChallengeWine()
    {
        wineCompleted.setVisible(true);
        wineCompleted.setMouseTransparent(true);
    }

}
