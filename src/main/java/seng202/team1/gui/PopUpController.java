package seng202.team1.gui;

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
import seng202.team1.models.User;
import seng202.team1.models.Wine;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import seng202.team1.models.WineBuilder;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.services.SearchWineService;


/**
 * Controller class for the popup.fxml popup.
 * @author Caleb
 */
public class PopUpController {
    @FXML
    Button popUpCloseButton;
    @FXML
    ImageView wineImage;
    @FXML
    Text wineName;
    @FXML
    Text description;
    @FXML
    Button logWine;
    @FXML
    Button addToWishlist;
    @FXML
    Button vintageTag;
    @FXML
    Button varietyTag;
    @FXML
    Button countryTag;
    @FXML
    Button provinceTag;
    @FXML
    Button wineryTag;
    @FXML
    Button regionTag;
    @FXML
    private ScrollPane tagScrollPane;
    @FXML
    private FlowPane tagFlowPane;
    DatabaseManager databaseManager;
    Wine wine;

    private static final Logger log = LogManager.getLogger(PopUpController.class);

    //todo: fix panning of long regions and titles.
    //todo: fix selectable main page scroll pane

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        databaseManager = DatabaseManager.getInstance();

        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        popUpCloseButton.setOnAction(actionEvent -> { closePopUp(); });
        logWine.setOnAction(actionEvent -> { loadWineLoggingPopUp(); });
        wine = navigationController.getWine();
        if (wine == null) {
            log.error("Wine is null");
            wine = WineBuilder.generaicSetup(-1, "Error Wine", "Wine is null", -1).build();
        }

        //set initial colour based on state
        int currentUserUid = getUId(FXWrapper.getInstance().getCurrentUser());
        int wineID = wine.getWineId();
        boolean inWishlist = SearchWineService.getInstance().checkInWishlist(wineID, currentUserUid);
        FontAwesomeIconView icon = (FontAwesomeIconView) addToWishlist.getGraphic();
        if (inWishlist) {
            icon.setFill(Color.web("#70171e"));
        } else {
            icon.setFill(Color.web("#d0d0d0"));
        }

        addToWishlist.setOnAction(actionEvent -> {
            //checks existence in wishlist table and toggles existence
            boolean inWishlistLambda = SearchWineService.getInstance().checkInWishlist(wineID, currentUserUid);
            if (inWishlistLambda) {
                SearchWineService.getInstance().removeFromWishlist(wineID, currentUserUid);
                icon.setFill(Color.web("#d0d0d0"));
            } else {
                SearchWineService.getInstance().addToWishlist(wineID, currentUserUid);
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
        regionTag.setText(wine.getRegion1()); //todo: fix this to add region 1 and 2
        hideNullTags();
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

    public void loadWineLoggingPopUp() {
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.closePopUp();
        navigationController.loadWineLoggingPopUpContent();
    }
    /**
     * Returns the int user id of the current user. Called during initialization
     * @param currentUser the current user
     * @return int uid
     */
    private int getUId(User currentUser) {
        int uid = 0;
        String uidSql = "SELECT id FROM user WHERE username = ? AND name = ?";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement uidPs = conn.prepareStatement(uidSql)) {
                uidPs.setString(1, currentUser.getEncryptedUserName());
                uidPs.setString(2, currentUser.getName());
                uid = uidPs.executeQuery().getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return uid;
    }
}
