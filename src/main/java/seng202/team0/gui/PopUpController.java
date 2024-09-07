package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team0.models.Wine;
import seng202.team0.models.WineBuilder;


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
    Text province;
    @FXML
    Text region1;
    @FXML
    Text variety;
    @FXML
    Text winery;
    @FXML
    Text region2;

    private static final Logger log = LogManager.getLogger(PopUpController.class);

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        Wine wine = navigationController.getWine();
        if (wine == null) {
            log.error("Wine is null");
            wine = WineBuilder.generaicSetup(-1, "Error Wine", "Wine is null", -1).build();
        }
        populatePopup(wine);
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
        province.setText(wine.getProvince());
        region1.setText(wine.getRegion1());
        region2.setText(wine.getRegion2());
        winery.setText(wine.getWinery());
        variety.setText(wine.getVariety());
    }

    /**
     * Closes the popup screen when the user clicks the close button.
     */
    public void closePopUp() {
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.closePopUp();
    }
}
