package seng202.team1.gui.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.FXWrapper;
import seng202.team1.models.User;
import seng202.team1.models.Wine;
import seng202.team1.models.WineBuilder;
import seng202.team1.repository.DAOs.SearchDAO;
import seng202.team1.services.ReviewService;
import seng202.team1.services.SearchWineService;
import seng202.team1.services.WishlistService;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Controller class for the popup.fxml popup.
 * @author Caleb Cooper, Elise Newman, Yuhao Zhang, Wen Sheng Thong
 */
public class PopUpController {
    @FXML
    private Button popUpCloseButton;
    @FXML
    private ImageView wineImage;
    @FXML
    private Text wineName;
    @FXML
    private Text description;
    @FXML
    private Button logWine;
    @FXML
    private Button addToWishlist;
    @FXML
    private Button vintageTag;
    @FXML
    private Button varietyTag;
    @FXML
    private Button countryTag;
    @FXML
    private Button provinceTag;
    @FXML
    private Button wineryTag;
    @FXML
    private Button regionTag;
    @FXML
    private ScrollPane tagScrollPane;
    @FXML
    private FlowPane tagFlowPane;
    @FXML
    private Button wineSearchLink;
    @FXML
    private FontAwesomeIconView logWineIcon;

    private ArrayList<Button> tagButtons;

    private final ReviewService reviewService = new ReviewService();
    private static final Logger LOG = LogManager.getLogger(PopUpController.class);

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        tagButtons = new ArrayList<>(List.of(vintageTag, varietyTag, countryTag, provinceTag, wineryTag, regionTag));

        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        popUpCloseButton.setOnAction(actionEvent -> closePopUp());
        logWine.setOnAction(actionEvent -> loadWineLoggingPopUp());
        Wine wine = navigationController.getWine();
        if (wine == null) {
            LOG.error("Wine is null");
            wine = WineBuilder.genericSetup(-1, "Error Wine", "Wine is null", -1).build();
        }

        int currentUserUid = User.getCurrentUser().getId();
        int wineID = wine.getWineId();
        boolean inWishlist = WishlistService.checkInWishlist(wineID, currentUserUid);
        FontAwesomeIconView icon = (FontAwesomeIconView) addToWishlist.getGraphic();
        if (inWishlist) {
            icon.setFill(Color.web("#70171e"));
        } else {
            icon.setFill(Color.web("#d0d0d0"));
        }

        Wine finalWine = wine;
        addToWishlist.setOnAction(actionEvent -> {
            //checks existence in wishlist table and toggles existence
            boolean inWishlistLambda = WishlistService.checkInWishlist(wineID, currentUserUid);
            if (inWishlistLambda) {
                try {
                    WishlistService.removeFromWishlist(wineID, currentUserUid);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                icon.setFill(Color.web("#d0d0d0"));
            } else {
                WishlistService.addToWishlist(wineID, currentUserUid);
                icon.setFill(Color.web("#70171e"));
            }
            if (navigationController.getCurrentPage().equals("wishlist")) { // refresh the wishlist page behind the popup
                navigationController.loadPageContent("wishlist");
                navigationController.initPopUp(finalWine);
            } else if (navigationController.getCurrentPage().equals("profile")) { // refresh the profile page behind the popup
                navigationController.loadPageContent("profile");
                navigationController.initPopUp(finalWine);
            } else if (navigationController.getCurrentPage().equals("wineReviews")) { // refresh the wine reviews page behind the popup
                navigationController.loadPageContent("wineReviews");
                navigationController.initPopUp(finalWine);
            }


        });
        populatePopup(wine);

        tagFlowPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() > 101) {
                tagScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                tagFlowPane.setPrefWidth(210);
            } else {
                tagScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            }
        });

        initialiseOnTagClicked();
    }

    private void initialiseOnTagClicked()
    {
        for (Button button : tagButtons) {
            button.setOnAction(actionEvent -> {
                String buttonName = button.getText();
                NavigationController nav = FXWrapper.getInstance().getNavigationController();
                nav.executeWithLoadingScreen(() -> {
                    SearchWineService.getInstance().searchWinesByTags(buttonName, SearchDAO.UNLIMITED);
                    SearchWineService.getInstance().setCurrentSearch(buttonName);
                    SearchWineService.getInstance().setCurrentMethod("Tags");
                    Platform.runLater(() -> FXWrapper.getInstance().launchSubPage("searchWine"));

                });

            });
        }
    }

    /**
     * Populates the text fields and image with the data of the given wine.
     * @param wine the wine object that the data is retrieved from
     */
    @FXML
    private void populatePopup(Wine wine) {
        wineImage.setImage(new Image(wine.getImagePath()));
        wineName.setText(wine.getName());
        description.setText(wine.getDescription());
        if (wine.getVintage() > 0) {
            vintageTag.setText(Integer.toString(wine.getVintage()));
        } else {
            vintageTag.setText(null);
        }
        varietyTag.setText(wine.getVariety());
        countryTag.setText(wine.getCountry());
        provinceTag.setText(wine.getProvince());
        wineryTag.setText(wine.getWinery());
        regionTag.setText(wine.getRegion1());
        hideNullTags();

        int currentUserUid = User.getCurrentUser().getId();

        if (reviewService.reviewExists(currentUserUid, wine.getWineId())) {
            logWineIcon.setFill(Color.web("#808080"));
        } else {
            logWineIcon.setFill(Color.web("#d0d0d0"));
        }
    }


    /**
     * Hides the tags that are null.
     */
    private void hideNullTags() {
        ArrayList<Button> tags = new ArrayList<>(List.of(vintageTag, varietyTag, countryTag, provinceTag, wineryTag, regionTag));
        tags.removeIf(tag -> tag.getText() == null || Objects.equals(tag.getText(), "0"));
        tagFlowPane.getChildren().setAll(tags);
    }

    /**
     * Closes the popup screen when the user clicks the close button.
     */
    public void closePopUp() {
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.closePopUp();
    }

    /**
     * Loads the wine logging popup page when the log wine button is clicked on the popup page.
     */
    public void loadWineLoggingPopUp() {
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.closePopUp();
        navigationController.loadWineLoggingPopUpContent();
    }

    /**
     * This method takes the user to a web browsers with results in wine-searcher for the wine belonging to the popup
     */
    public void onWineSearchLinkClicked() {
        try {
            java.awt.Desktop.getDesktop();
            String query = wineName.getText();
            query = Normalizer.normalize(query, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            query = pattern.matcher(query).replaceAll("");
            String googleSearchURL = "https://www.wine-searcher.com/find/" + query.replace(" ", "+");
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    // open browser with a thread
                    Runnable browseRunnable = () -> {
                        try {
                            desktop.browse(new URI(googleSearchURL));
                        } catch (URISyntaxException e) {
                            LOG.error("Error in PopupController.onWineSearchLinkClicked(): Syntax error in URL: {}", googleSearchURL);
                        } catch (IOException e) {
                            LOG.error("Error in PopupController.onWineSearchLinkClicked(): Default browser could not be launched");
                        }
                    };

                    Thread thread = new Thread(browseRunnable);
                    thread.start();
                }
            } else {
                System.out.println("Not supported");
            }
        } catch (Exception e) {
            LOG.error("Something went wrong trying to search for a wine.");
        }
    }

}
