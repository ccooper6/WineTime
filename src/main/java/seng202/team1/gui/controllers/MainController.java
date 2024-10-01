package seng202.team1.gui.controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import seng202.team1.gui.FXWrapper;
import seng202.team1.models.User;
import seng202.team1.services.RecommendWineService;
import seng202.team1.services.SearchWineService;
import seng202.team1.services.WineCategoryService;

import java.io.IOException;

/**
 * Controller class for the mainpage.fxml page.
 * @author Caleb Cooper, Yuhao Zhang, Isaac Macdonald
 */
public class MainController {
    @FXML
    Text helloText;
    @FXML
    GridPane contentsGrid;
    @FXML
    AnchorPane categoryAnchorPane;
    /**
     * Initializes the main page view.
     */
    public void initialize() {
        NavigationController nav = FXWrapper.getInstance().getNavigationController();
        nav.executeWithLoadingScreen(() -> {
            WineCategoryService.getInstance().resetCurrentCategory();
            helloText.setText("Hello, " + User.getCurrentUser().getName() + "!");

            String[] tags = {
                    "Bordeaux, Merlot",
                    "Marlborough, Sauvignon Blanc",
                    "Tuscany, Sangiovese",
                    "Hawke's Bay, Syrah",
                    "Spain, Rioja, Tempranillo",
                    "Mendoza, Malbec",
                    "US, Napa Valley, Cabernet Sauvignon",
                    "Central Otago, Pinot Noir, New Zealand"
            };

            try {
                Boolean hasRecommended = RecommendWineService.getInstance().hasEnoughFavouritesTag(User.getCurrentUser().getId());
                if (hasRecommended) {
                    displayCategoryWithRec(tags);
                } else {
                    displayCategoryNoRecc(tags);
                }
            } catch (IOException e) { e.printStackTrace(); }
        });
    }

    /**
     * Displays the different tag categories with recommendations. If no wines can be recommended, it doesn't display
     * the recommended category
     * @param tags an Array of tag string for each category to add
     * @throws IOException
     */
    private void displayCategoryWithRec(String[] tags) throws IOException {
        Parent reccParent = WineCategoryDisplayController.createCategory("recommend");
        if (SearchWineService.getInstance().getWineList().isEmpty()) {
            displayCategoryNoRecc(tags);
        } else {
            for (int i = 0; i <= tags.length; i++) {
                Parent parent;
                if (i == 0) {
                    parent = reccParent;
                    if (SearchWineService.getInstance().getWineList().isEmpty()) {
                        break;
                    }
                } else {
                    parent = WineCategoryDisplayController.createCategory(tags[i - 1]);
                }
                int finalI = i;
                Platform.runLater(() -> {
                    if (finalI >= contentsGrid.getRowCount()) {
                        categoryAnchorPane.setPrefHeight(categoryAnchorPane.getPrefHeight() + 200);
                        contentsGrid.setPrefHeight(contentsGrid.getPrefHeight() + 200);
                        contentsGrid.addRow(contentsGrid.getRowCount());
                    }
                    contentsGrid.add(parent, 0, finalI);
                });
            }
        }
    }

    /**
     * Displays the different tag categories without recommendations
     * @param tags an Array of tag string for each category to add
     * @throws IOException
     */
    private void displayCategoryNoRecc(String[] tags) throws IOException {
        for (int i = 0; i < tags.length; i++) {
            Parent parent = WineCategoryDisplayController.createCategory(tags[i]);
            int finalI = i;
            Platform.runLater(() -> contentsGrid.add(parent, 0, finalI));
            // Have to do this as it requires multiple loops to finish completely
            // - need to use for "A Task Which Returns Partial Results", from the Task documentation
        }
    }
}
