package seng202.team1.gui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.FXWrapper;
import seng202.team1.models.User;
import seng202.team1.services.CategoryService;
import seng202.team1.services.RecommendWineService;
import seng202.team1.services.WineCategoryService;

import java.io.IOException;
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

    private static final Logger LOG = LogManager.getLogger(MainController.class);

    /**
     * Initializes the main page view.
     */
    public void initialize() {
        WineCategoryService.getInstance().resetCurrentCategory();
        helloText.setText("Hello, " + User.getCurrentUser().getName() + "!");

        if (!CategoryService.isCategoriesGenerated()) {
            generateAllCategories();
        }

        Boolean hasRecommended = RecommendWineService.getInstance().hasEnoughFavouritesTag(User.getCurrentUser().getId());
        if (hasRecommended) {
            displayCategoriesWithRec();
        } else {
            displayCategoriesNoRec();
        }
    }


    /**
     * Generates all the wine category displays for the main page.
     */
    private void generateAllCategories() {
        try {
            String[] tags = { // TODO: Current problem lies in generation of the titles, instead just remove comma from tag
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
                    "Bordeaux, Cabernet Franc",
                    "Hunter Valley, Semillon",
                    "Willamette Valley, Pinot Gris",
                    "Burgundy, Chardonnay",
                    "Sonoma, Coast Zinfandel",
                    "Mosel, Riesling",
                    "Puglia, Primitivo",
                    "South Africa, Chenin Blanc",
                    "Rheingau, Silvaner",
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
                System.out.println("Generating category: " + tags[i]);
                Parent parent = WineCategoryDisplayController.createCategory(tags[i]);
                allCategories.add(parent);
            }

            CategoryService.setCategoriesGenerated(true);
        } catch (IOException e) {
            LOG.error("An error has occurred while generating categories", e);
        }
    }

    /**
     * Displays the different tag categories with recommendations. If no wines can be recommended, it doesn't display
     * the recommended category
     */
    private void displayCategoriesWithRec() {
        List<Parent> allCategories = CategoryService.getAllCategories();
        Parent reccParent;
        try {
            reccParent = WineCategoryDisplayController.createCategory("recommend");
        } catch (IOException e) {
            LOG.error("An error has occurred while generating the recommendation category", e);
            return;
        }

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
