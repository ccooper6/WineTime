package seng202.team1.gui.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.FXWrapper;
import seng202.team1.models.Wine;
import seng202.team1.models.WineBuilder;
import seng202.team1.services.ReviewService;
import seng202.team1.services.WishlistService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private FontAwesomeIconView logWineIcon;

    private final ReviewService reviewService = new ReviewService();
    private static final Logger LOG = LogManager.getLogger(PopUpController.class);

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        popUpCloseButton.setOnAction(actionEvent -> { closePopUp(); });
        logWine.setOnAction(actionEvent -> { loadWineLoggingPopUp(); });
        Wine wine = navigationController.getWine();
        if (wine == null) {
            LOG.error("Wine is null");
            wine = WineBuilder.genericSetup(-1, "Error Wine", "Wine is null", -1).build();
        }

        int currentUserUid = WishlistService.getUserID(FXWrapper.getInstance().getCurrentUser());
        int wineID = wine.getWineId();
        boolean inWishlist = WishlistService.checkInWishlist(wineID, currentUserUid);
        FontAwesomeIconView icon = (FontAwesomeIconView) addToWishlist.getGraphic();
        if (inWishlist) {
            icon.setFill(Color.web("#70171e"));
        } else {
            icon.setFill(Color.web("#d0d0d0"));
        }

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

        int currentUserUid = WishlistService.getUserID(FXWrapper.getInstance().getCurrentUser());

        if (reviewService.reviewExists(currentUserUid, wine.getWineId())) {
            logWineIcon.setFill(Color.web("#70171e"));
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

}
