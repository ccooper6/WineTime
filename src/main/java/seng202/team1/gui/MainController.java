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
        SearchWineService.getInstance().searchWinesByTags("Bordeaux, Merlot", 10);
//        System.out.println("searched for : " + SearchWineService.getInstance().getWineList().size());
        helloText.setText("Hello, " + FXWrapper.getInstance().getCurrentUser().getName() + "!");
        try {
            FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("/fxml/wineCategoryDisplay.fxml"));
            Parent parent1 = fxmlLoader1.load();
            contentsGrid.add(parent1, 0, 0);
            WineCategoryService.getInstance().incrementCurrentCategory();
            SearchWineService.getInstance().searchWinesByTags("Marlborough, Sauvignon Blanc", 10);
            FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("/fxml/wineCategoryDisplay.fxml"));
            Parent parent2 = fxmlLoader2.load();
            contentsGrid.add(parent2, 0, 1);
            WineCategoryService.getInstance().incrementCurrentCategory();
            SearchWineService.getInstance().searchWinesByTags("Tuscany, Sangiovese", 10);
            FXMLLoader fxmlLoader3 = new FXMLLoader(getClass().getResource("/fxml/wineCategoryDisplay.fxml"));
            Parent parent3 = fxmlLoader3.load();
            contentsGrid.add(parent3, 0, 2);
            WineCategoryService.getInstance().incrementCurrentCategory();
            SearchWineService.getInstance().searchWinesByTags("Hawke's Bay, Syrah", 10);
            FXMLLoader fxmlLoader4 = new FXMLLoader(getClass().getResource("/fxml/wineCategoryDisplay.fxml"));
            Parent parent4 = fxmlLoader4.load();
            contentsGrid.add(parent4, 0, 3);
            WineCategoryService.getInstance().incrementCurrentCategory();
            SearchWineService.getInstance().searchWinesByTags("Spain, Rioja, Tempranillo", 10);
            FXMLLoader fxmlLoader5 = new FXMLLoader(getClass().getResource("/fxml/wineCategoryDisplay.fxml"));
            Parent parent5 = fxmlLoader5.load();
            contentsGrid.add(parent5, 0, 4);
            WineCategoryService.getInstance().incrementCurrentCategory();
            SearchWineService.getInstance().searchWinesByTags("New Zealand, Gisborne, Chardonnay", 10);
            FXMLLoader fxmlLoader6 = new FXMLLoader(getClass().getResource("/fxml/wineCategoryDisplay.fxml"));
            Parent parent6 = fxmlLoader6.load();
            contentsGrid.add(parent6, 0, 5);
            WineCategoryService.getInstance().incrementCurrentCategory();
            SearchWineService.getInstance().searchWinesByTags("US, Napa Valley, Cabernet Sauvignon", 10);
            FXMLLoader fxmlLoader7 = new FXMLLoader(getClass().getResource("/fxml/wineCategoryDisplay.fxml"));
            Parent parent7 = fxmlLoader7.load();
            contentsGrid.add(parent7, 0, 6);
            WineCategoryService.getInstance().incrementCurrentCategory();
            SearchWineService.getInstance().searchWinesByTags("Central Otago, Pinot Noir, New Zealand", 10);
            FXMLLoader fxmlLoader8 = new FXMLLoader(getClass().getResource("/fxml/wineCategoryDisplay.fxml"));
            Parent parent8 = fxmlLoader8.load();
            contentsGrid.add(parent8, 0, 7);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
