package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import seng202.team0.models.Wine;
import seng202.team0.models.testWines.wine1;

public class PopUpController {
    public Button popUpCloseButton;
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

    @FXML
    public void initialize() {
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        Wine wine = navigationController.getWine();
        if (wine == null) {
            wine = new wine1();
        }
        populatePopup(wine);
    }

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

    public void closePopUp() {
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.closePopUp();
    }
}
