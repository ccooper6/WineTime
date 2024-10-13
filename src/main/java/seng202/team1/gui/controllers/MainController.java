package seng202.team1.gui.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import seng202.team1.gui.FXWrapper;
import seng202.team1.models.User;
import seng202.team1.services.CategoryService;
import seng202.team1.services.RecommendWineService;

import java.time.LocalTime;
import java.util.List;

/**
 * Controller class for the mainpage.fxml page.
 */
public class MainController {
    @FXML
    private Text helloText;
    @FXML
    private GridPane contentsGrid;
    @FXML
    private AnchorPane categoryAnchorPane;
    @FXML
    private FontAwesomeIconView refreshIcon;

    /**
     * Default constructor for MainController
     */
    public MainController(){}

    /**
     * Initializes the main page view, sets the greeting message.
     */
    public void initialize() {
        CategoryService.resetCategories();

        setUpWelcomeMessage();

        displayWines();

        setUpRefreshIconAnimation();
    }

    /**
     * Sets the greeting message for the user.
     */
    private void setUpWelcomeMessage() {
        LocalTime currentTime = java.time.LocalTime.now();
        if (currentTime.isAfter(java.time.LocalTime.of(6, 0)) && currentTime.isBefore(java.time.LocalTime.of(12, 0))) {
            helloText.setText("Good morning, " + User.getCurrentUser().getName() + "!");
        } else if (currentTime.isAfter(java.time.LocalTime.of(12, 0)) && currentTime.isBefore(java.time.LocalTime.of(17, 0))) {
            helloText.setText("Good afternoon, " + User.getCurrentUser().getName() + "!");
        } else {
            helloText.setText("Good evening, " + User.getCurrentUser().getName() + "!");
        }
    }

    /**
     * Displays the wines on the main page.
     */
    private void displayWines() {
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.executeWithLoadingScreen(() -> {
            if (!CategoryService.areTagsGenerated()) {
                CategoryService.generateTags();
            }

            generateAllCategories();

            boolean hasRecommended = RecommendWineService.getInstance().hasEnoughFavouritesTag(User.getCurrentUser().getId());
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
        List<Parent> allCategories = CategoryService.getAllCategories();
        String[] tagsList = CategoryService.getGeneratedTags();

        for (int i = 0; i < tagsList.length && i < 8; i++) {
            Parent parent = WineCategoryDisplayController.createCategory(tagsList[i]);
            allCategories.add(parent);
        }
    }

    /**
     * Displays the different tag categories with recommendations. If no wines can be recommended, it doesn't display
     * the recommended category
     */
    private void displayCategoriesWithRec() {
        List<Parent> allCategories = CategoryService.getAllCategories();
        Parent reccParent = WineCategoryDisplayController.createCategory("recommend");

        Platform.runLater(() -> {
            contentsGrid.add(reccParent, 0, 0);
            for (int i = 0; i < 8 && i < allCategories.size(); i++) {
                Parent parent = allCategories.get(i);
                contentsGrid.setPrefHeight(contentsGrid.getPrefHeight() + 20);
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
            for (int i = 0; i < allCategories.size(); i++) {
                Parent parent = allCategories.get(i);
                if (i >= contentsGrid.getRowCount()) {
                    categoryAnchorPane.setPrefHeight(categoryAnchorPane.getPrefHeight() + 200);
                    contentsGrid.setPrefHeight(contentsGrid.getPrefHeight() + 200);
                    contentsGrid.addRow(contentsGrid.getRowCount());
                }
                contentsGrid.add(parent, 0, i);
            }
        });

    }

    /**
     * Sets up the refresh icon animation.
     */
    private void setUpRefreshIconAnimation() {
        RotateTransition refreshAnimation = new RotateTransition(Duration.millis(500), refreshIcon);
        refreshAnimation.setByAngle(360);
        refreshIcon.setOnMouseClicked(event -> {
            refreshAnimation.play();
            refreshAnimation.setOnFinished(e -> refreshCategories());
        });
    }

    /**
     * Refreshes the selection of wines in the main page.
     */
    @FXML
    private void refreshCategories() {
        CategoryService.resetCategories(true);
        FXWrapper.getInstance().getNavigationController().loadMainScreen();
    }
}
