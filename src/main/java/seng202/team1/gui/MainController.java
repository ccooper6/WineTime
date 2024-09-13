package seng202.team1.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import seng202.team1.services.SearchWineService;
import seng202.team1.services.WineCategoryService;


import java.io.IOException;

/**
 * Controller class for the mainpage.fxml page
 * @author Caleb Cooper
 */
public class MainController {
    @FXML
    Text helloText;
    @FXML
    GridPane contentsGrid;

    /**
     * Initializes the controller.
     */
    public void initialize() {
        WineCategoryService.getInstance().resetCurrentCategory();
        helloText.setText("Hello, " + FXWrapper.getInstance().getCurrentUser().getName() + "!");

        String[] tags = {
                "Bordeaux, Merlot",
                "Marlborough, Sauvignon Blanc",
                "Tuscany, Sangiovese",
                "Hawke's Bay, Syrah",
                "Spain, Rioja, Tempranillo",
                "New Zealand, Gisborne, Chardonnay",
                "US, Napa Valley, Cabernet Sauvignon",
                "Central Otago, Pinot Noir, New Zealand"
        };

        try {
            for (int i = 0; i < tags.length; i++) {
                SearchWineService.getInstance().searchWinesByTags(tags[i], 10);
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/wineCategoryDisplay.fxml"));
                Parent parent = fxmlLoader.load();
                contentsGrid.add(parent, 0, i);
                WineCategoryService.getInstance().incrementCurrentCategory();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
