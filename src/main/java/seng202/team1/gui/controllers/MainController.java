package seng202.team1.gui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.FXWrapper;
import seng202.team1.models.User;
import seng202.team1.services.CategoryService;
import seng202.team1.services.RecommendWineService;
import seng202.team1.services.WineCategoryService;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Controller class for the mainpage.fxml page.
 * @author Caleb Cooper, Yuhao Zhang, Isaac Macdonald
 */
public class MainController {
    @FXML
    private Text helloText;
    @FXML
    private GridPane contentsGrid;
    @FXML
    private AnchorPane categoryAnchorPane;

    /**
     * Initializes the main page view.
     */
    public void initialize() {
        LocalTime currentTime = java.time.LocalTime.now();
        if (currentTime.isAfter(java.time.LocalTime.of(6, 0)) && currentTime.isBefore(java.time.LocalTime.of(12, 0))) {
            helloText.setText("Good morning, " + User.getCurrentUser().getName() + "!");
        } else if (currentTime.isAfter(java.time.LocalTime.of(12, 0)) && currentTime.isBefore(java.time.LocalTime.of(5, 0))) {
            helloText.setText("Good afternoon, " + User.getCurrentUser().getName() + "!");
        } else {
            helloText.setText("Good evening, " + User.getCurrentUser().getName() + "!");
        }

        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();

        navigationController.executeWithLoadingScreen(() -> {
            if (!CategoryService.isCategoriesGenerated()) {
                generateAllCategories();
            }

            Boolean hasRecommended = RecommendWineService.getInstance().hasEnoughFavouritesTag(User.getCurrentUser().getId());
            if (hasRecommended) {
                displayCategoriesWithRec();
            } else {
                displayCategoriesNoRec();
            }
        });
    }


    /**
     * Generates all the wine category displays for the main page.
     */
    private void generateAllCategories() {
        String[] tags = {
                "Bordeaux, Merlot",
                "Marlborough, Sauvignon Blanc",
                "Tuscany, Sangiovese",
                "Hawke's Bay, Syrah",
                "Spain, Rioja, Tempranillo",
                "Mendoza, Malbec",
                "US, Napa Valley, Cabernet Sauvignon",
                "Central Otago, Pinot Noir, New Zealand",
                "Chianti Classico, Sangiovese",
                "Champagne, Pinot Meunier",
                "Provence, Rosé",
                "Veneto, Prosecco",
                "Hunter Valley, Semillon",
                "Willamette Valley, Pinot Gris",
                "Burgundy, Chardonnay",
                "Mosel, Riesling",
                "Puglia, Primitivo",
                "South Africa, Chenin Blanc",
                "Alsace, Gewurztraminer",
                "Tuscany, Vermentino",
                "Loire Valley, Sauvignon Blanc",
                "Languedoc, Grenache",
                "Wairarapa, Pinot Noir"
        };

        List<String> tagsList = Arrays.asList(tags);
        Collections.shuffle(tagsList);
        tags = tagsList.toArray(new String[0]);

        List<Parent> allCategories = CategoryService.getAllCategories();

        for (int i = 0; i < tags.length && i < 8; i++) {
            Parent parent = WineCategoryDisplayController.createCategory(tags[i]);
            allCategories.add(parent);
        }

        CategoryService.setCategoriesGenerated(true);

    }

    /**
     * Displays the different tag categories with recommendations. If no wines can be recommended, it doesn't display
     * the recommended category
     */
    private void displayCategoriesWithRec() {
        List<Parent> allCategories = CategoryService.getAllCategories();
        Parent reccParent;
        reccParent = WineCategoryDisplayController.createCategory("recommend");

        Platform.runLater(() -> {
            contentsGrid.add(reccParent, 0, 0);
            for (int i = 0; i < allCategories.size(); i++) {
                Parent parent = allCategories.get(i);
                if (i >= contentsGrid.getRowCount()) {
                    categoryAnchorPane.setPrefHeight(categoryAnchorPane.getPrefHeight() + 200);
                    contentsGrid.setPrefHeight(contentsGrid.getPrefHeight() + 200);
                    contentsGrid.addRow(contentsGrid.getRowCount());
                }
                contentsGrid.add(parent, 0, i + 1);
            }
        });
    }

    /**
     * Displays the different tag categories without recommendations.
     */
    private void displayCategoriesNoRec() {
        Platform.runLater(() -> {
            List<Parent> allCategories = CategoryService.getAllCategories();
            for (int i = 1; i < allCategories.size(); i++) {
                Parent parent = allCategories.get(i);
                contentsGrid.add(parent, 0, i - 1);
            }
        });

    }

    /**
     * Refreshes the selection of wines in the main page.
     */
    @FXML
    private void refreshCategories() {
        CategoryService.resetCategories();
        FXWrapper.getInstance().getNavigationController().loadMainScreen();
    }
}
