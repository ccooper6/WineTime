package seng202.team1.gui.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.FXWrapper;
import seng202.team1.models.User;
import seng202.team1.models.Wine;
import seng202.team1.models.WineBuilder;
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
 */
public class PopUpController {
    @FXML
    private TextFlow valueDisplay;
    @FXML
    private TextFlow pointsDisplay;
    @FXML
    private Button helpButton;
    @FXML
    private TextFlow helpText;
    @FXML
    private Button tagsHelpButton;
    @FXML
    private TextFlow tagsHelpText;
    @FXML
    private AnchorPane linkAndIcon;
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
    @FXML
    private Tooltip reviewTooltip;
    @FXML
    private Tooltip heartTooltip;
    @FXML
    private FontAwesomeIconView wishlistIcon;

    private final ReviewService REVIEWSERVICE = new ReviewService();
    private NavigationController navigationController;
    private static final Logger LOG = LogManager.getLogger(PopUpController.class);
    private Wine wine;
    private int wineID;
    private int currentUserUid;

    /**
     * Default constructor for PopupController.
     */
    public PopUpController() {}

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        popUpCloseButton.setOnAction(actionEvent -> closePopUp());
        logWine.setOnAction(actionEvent -> loadWineLoggingPopUp());

        navigationController = FXWrapper.getInstance().getNavigationController();
        wine = navigationController.getWine();
        if (wine == null) {
            LOG.error("Error in PopUpController.initialize(): Wine is null");
            wine = WineBuilder.genericSetup(-1, "There was an error loading your wine", "Wine could not be found.", -1, -1).build();
        }

        currentUserUid = User.getCurrentUser().getId();
        wineID = wine.getID();

        setUpWishlistIcon();

        PauseTransition pause = new PauseTransition(Duration.millis(100)); // delay the hover effects to prevent wierd behaviour
        pause.setOnFinished(event -> initializeHovers());
        pause.play();

        setUpPriceText();
        setUpPointsText();
        setUpHelpDisplays();
        addToWishlistRefreshListener();
        populatePopup(wine);
        correctlyFormatTags();
        initialiseOnTagClicked();
    }

    /**
     * Sets up the wishlist icon.
     */
    private void setUpWishlistIcon() {
        boolean inWishlist = WishlistService.checkInWishlist(wineID, currentUserUid);
        wishlistIcon = (FontAwesomeIconView) addToWishlist.getGraphic();
        if (inWishlist) {
            wishlistIcon.setFill(Color.web("#70171e"));
            heartTooltip.setText("Remove this wine from your likes");
        } else {
            wishlistIcon.setFill(Color.web("#c0c0c0"));
            heartTooltip.setText("Add this wine to your likes");
        }
    }

    /**
     * Sets up the price text for the popup.
     */
    private void setUpPriceText() {
        Text dollarSign = new Text("$");
        dollarSign.setStyle("-fx-font-family: 'Noto Serif'; -fx-font-size: 18;");
        Text value = new Text(String.valueOf(wine.getPrice()));
        if (!"0".equals(value.getText())) {
            value.setStyle("-fx-font-family: 'Noto Serif'; -fx-font-size: 18;");
            Text currency = new Text(" USD");
            currency.setStyle("-fx-font-family: 'Noto Serif'; -fx-font-size: 10;");
            valueDisplay.getChildren().addAll(dollarSign, value, currency);
        } else {
            Text buffer = new Text(" ");
            buffer.setStyle("-fx-font-family: 'Noto Serif'; -fx-font-size: 18;");
            Text unknown = new Text("Price Unknown");
            unknown.setStyle("-fx-font-family: 'Noto Serif'; -fx-font-size: 10;");
            valueDisplay.getChildren().addAll(buffer, unknown);
        }
    }

    /**
     * Sets up the points text for the popup.
     */
    private void setUpPointsText() {
        Text points = new Text(String.valueOf(wine.getPoints()));
        points.setStyle("-fx-font-family: 'Noto Serif'; -fx-font-size: 18;");
        Text range = new Text(" / 100");
        range.setStyle("-fx-font-family: 'Noto Serif'; -fx-font-size: 12;");
        pointsDisplay.getChildren().addAll(points, range);
    }

    /**
     * Sets up the text for the help displays in the popup.
     */
    private void setUpHelpDisplays() {
        Text firstLine = new Text("\nBased scores from WineEnthusiast:\n\n");
        firstLine.setStyle("-fx-font-family: 'Noto Serif'; -fx-font-size: 12; -fx-font-weight: bold; -fx-text-fill: #f0f0f0");
        Text secondLine = new Text("98-100 = Classic\n94-97 = Superb\n90-93 = Excellent\n87-89 = Very Good\n83-86 = Good\n80-82 = Acceptable\nMore info on help page\n");
        secondLine.setStyle("-fx-font-family: 'Noto Serif'; -fx-font-size: 12; -fx-text-fill: #f0f0f0");
        helpText.getChildren().addAll(firstLine, secondLine);

        firstLine = new Text("\nTags categorize the wine and allow users to find similar wines.\n\nTry clicking the tags to discover more wines with these attributes!\n");
        secondLine.setStyle("-fx-font-family: 'Noto Serif'; -fx-font-size: 12; -fx-text-fill: #f0f0f0");
        tagsHelpText.getChildren().add(firstLine);
    }

    /**
     * Adds a listener to the add to wishlist button that toggles the existence of the wine in the wishlist.
     * Also refreshes the page behind the popup if needed.
     */
    private void addToWishlistRefreshListener() {
        addToWishlist.setOnAction(actionEvent -> {
            //checks existence in wishlist table and toggles existence
            boolean inWishlistLambda = WishlistService.checkInWishlist(wineID, currentUserUid);
            if (inWishlistLambda) {
                try {
                    WishlistService.removeFromWishlist(wineID, currentUserUid);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                wishlistIcon.setFill(Color.web("#c0c0c0"));
                heartTooltip.setText("Add this wine to your likes");
            } else {
                WishlistService.addToWishlist(wineID, currentUserUid);
                wishlistIcon.setFill(Color.web("#70171e"));
                heartTooltip.setText("Remove this wine from your likes");
            }
            if (navigationController.getCurrentPage().equals("wishlist")) { // refresh the wishlist page behind the popup
                navigationController.closePopUp();
                navigationController.loadPageContent("wishlist");
                navigationController.initPopUp(wine);
            } else if (navigationController.getCurrentPage().equals("profile")) { // refresh the profile page behind the popup
                navigationController.closePopUp();
                navigationController.loadPageContent("profile");
                navigationController.initPopUp(wine);
            } else if (navigationController.getCurrentPage().equals("wineReviews")) { // refresh the wine reviews page behind the popup
                navigationController.closePopUp();
                navigationController.loadPageContent("wineReviews");
                navigationController.initPopUp(wine);
            }
        });
    }

    /**
     * Correctly formats the tags in the popup using a scroll pane.
     */
    private void correctlyFormatTags() {
        tagFlowPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() > 101) {
                tagScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                tagFlowPane.setPrefWidth(210);
            } else {
                tagScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            }
        });
    }

    /**
     * Initialises the on tag clicked functionality for the tags in the popup.
     */
    private void initialiseOnTagClicked()
    {
        ArrayList<Button> tagButtons = new ArrayList<>(List.of(vintageTag, varietyTag, countryTag, provinceTag, wineryTag, regionTag));

        for (Button button : tagButtons) {
            button.setOnAction(actionEvent -> {
                String buttonName = button.getText();
                NavigationController nav = FXWrapper.getInstance().getNavigationController();
                nav.executeWithLoadingScreen(() -> {
                    int unlimited = -1;

                    SearchWineService.getInstance().searchWinesByTags(buttonName, unlimited);
                    SearchWineService.getInstance().setCurrentSearch(buttonName);
                    SearchWineService.getInstance().setCurrentMethod("Tags");
                    Platform.runLater(() -> FXWrapper.getInstance().launchSubPage("searchWine"));

                });
            });
        }
    }

    /**
     * Initializes the hover effects for the buttons on the popup.
     */
    private void initializeHovers() {
        wineSearchLink.setOnMouseEntered(event -> showWineSearchLink());
        wineSearchLink.setOnMouseExited(event -> hideWineSearchLink());
        linkAndIcon.setOnMouseEntered(event -> showWineSearchLink());
        linkAndIcon.setOnMouseExited(event -> hideWineSearchLink());

        addToWishlist.setOnMouseEntered(event -> ((FontAwesomeIconView) addToWishlist.getGraphic()).setFill(Paint.valueOf("#A05252")));
        addToWishlist.setOnMouseExited(event -> {
            Paint paint;
            if (WishlistService.checkInWishlist(wineID, currentUserUid)) {
                paint = Paint.valueOf("#70171e");
            } else {
                paint = Paint.valueOf("#c0c0c0");
            }
            ((FontAwesomeIconView) addToWishlist.getGraphic()).setFill(paint);
        });

        logWine.setOnMouseEntered(event -> ((FontAwesomeIconView) logWine.getGraphic()).setFill(Paint.valueOf("#808080")));

        logWine.setOnMouseExited(event -> {
            if (REVIEWSERVICE.reviewExists(currentUserUid, wine.getID())) {
                logWineIcon.setFill(Color.web("#333333"));
            } else {
                logWineIcon.setFill(Color.web("#c0c0c0"));
            }
        });
        helpButton.setOnMouseEntered(event -> helpText.setVisible(true));
        helpButton.setOnMouseExited(event -> helpText.setVisible(false));

        tagsHelpButton.setOnMouseEntered(event -> tagsHelpText.setVisible(true));
        tagsHelpButton.setOnMouseExited(event -> tagsHelpText.setVisible(false));
    }

    /**
     * Shows the wine search link.
     */
    private void showWineSearchLink() {
        wineSearchLink.setTextFill(Paint.valueOf("#808080"));
        wineSearchLink.setUnderline(true);
        ((FontAwesomeIconView) wineSearchLink.getGraphic()).setFill(Paint.valueOf("#808080"));
        linkAndIcon.setVisible(true);
    }

    /**
     * Hides the wine search link.
     */
    private void hideWineSearchLink() {
        wineSearchLink.setTextFill(Paint.valueOf("#c0c0c0"));
        wineSearchLink.setUnderline(false);
        ((FontAwesomeIconView) wineSearchLink.getGraphic()).setFill(Paint.valueOf("#c0c0c0"));
        linkAndIcon.setVisible(false);
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

        if (REVIEWSERVICE.reviewExists(currentUserUid, wine.getID())) {
            logWineIcon.setFill(Color.web("#333333"));
            reviewTooltip.setText("Edit your review");

        } else {
            logWineIcon.setFill(Color.web("#c0c0c0"));
            reviewTooltip.setText("Write a review for this wine");
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
     * This method takes the user to a web browsers with results in wine-searcher for the wine displayed on the popup
     * when the link is clicked.
     */
    public void onWineSearchLinkClicked() {
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
            LOG.error("Error in PopupController.onWIneSearchLinkClicked(): Desktop does not support wine link function");
        }
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
}
