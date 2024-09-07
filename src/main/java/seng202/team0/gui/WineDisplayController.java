package seng202.team0.gui;

import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import seng202.team0.models.Wine;
import seng202.team0.services.SearchWineService;

public class WineDisplayController {
    @FXML
    Label wineInfo;

    @FXML
    ImageView wineImage;

    Wine wine;

    /**
     * Displays the wine card using SearchWineService instances' current wine
     */
    @FXML
    public void initialize()
    {
        wine = SearchWineService.getInstance().getCurrentWine();

        wineImage.setImage(new Image(wine.getImagePath()));

//        set text info
        String infoText = "Name: " + wine.getName();
        infoText += "\nVariety: " + wine.getVariety();
        wineInfo.setText(infoText);

    }

    @FXML
    public void popUp()
    {
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();

        navigationController.initPopUp(wine);
    }

}
