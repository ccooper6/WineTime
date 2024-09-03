package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import seng202.team0.models.Wine;
import seng202.team0.models.testWines.wine1;
import seng202.team0.services.WineService;

import java.util.ArrayList;
import java.util.List;

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
    Text winery;
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

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        Wine wine = navigationController.getWine();
        if (wine == null) {
            wine = new wine1();
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
        winery.setText(wine.getWinery());
        vintageTag.setText(Integer.toString(wine.getVintage()));
        varietyTag.setText(wine.getVariety());
        countryTag.setText(wine.getCountry());
        provinceTag.setText(wine.getProvince());
        wineryTag.setText(wine.getWinery());
        regionTag.setText(wine.getRegion());
    }

    /**
     * Closes the popup screen when the user clicks the close button.
     */
    public void closePopUp() {
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.closePopUp();
    }
}
