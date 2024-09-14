package seng202.team1.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.models.Wine;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import seng202.team1.models.WineBuilder;


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

    private static final Logger log = LogManager.getLogger(PopUpController.class);

    //todo: fix panning of long regions and titles.
    //todo: fix selectable main page scroll pane

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
            log.error("Wine is null");
            wine = WineBuilder.genericSetup(-1, "Error Wine", "Wine is null", -1).build();
        }
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
}
