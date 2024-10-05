package seng202.team1.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.controllers.NavigationController;
import seng202.team1.models.User;

import java.io.IOException;

/**
 * A singleton class which launches the FXML pages.
 * @author Elise Newman, Caleb Cooper
 */
public class FXWrapper {
    private static FXWrapper instance = null;
    private Stage stage;
    private NavigationController navigationController;
    private int challenge = 0;

    private static final Logger LOG = LogManager.getLogger(FXWrapper.class);

    /**
     * Gets the singleton.
     * @return the FXWrapper singleton
     */
    public static FXWrapper getInstance() {
        // TODO why check for null twice?
        if (instance == null) {
            synchronized (FXWrapper.class) {
                if (instance == null) {
                    instance = new FXWrapper();
                }
            }
        }
        return instance;
    }

    /**
     * Stores the stage from the FXWindow.
     * @param stage is not changed.
     */
    public void init(Stage stage) {
        this.stage = stage;
    }

    /**
     * Loads the FXML and controller listed in the fx:controller.
     * This page type cannot launch popUps yet.
     * @param name is the name of the fxml in lowercase with no type.
     */
    public void launchPage(String name) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(String.format("/fxml/%s.fxml", name)));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(name);
            stage.show();
        } catch (IOException e) {
            LOG.error("Error in FXWrapper.launchPage: Could not load fxml content for {}.", name);
        }
    }

    /**
     * Used for all pages with a navigation bar, navigation fxml is a parent to the variable fxml.
     * This page layout can launch popUps.
     * @param name is the name of the inner fxml in lowercase (no type)
     *
     */
    public void launchSubPage(String name) {
        try {
            FXMLLoader navigationLoader = new FXMLLoader(getClass().getResource("/fxml/navigation.fxml"));
            Parent navigationRoot = navigationLoader.load();
            navigationController = navigationLoader.getController();
            // TODO should this be .equals?
            if (name == "mainpage") {
                navigationController.loadMainScreen();
            } else {
                navigationController.loadPageContent(name);
            }
            Scene scene = new Scene(navigationRoot);
            stage.setScene(scene);
            stage.setTitle(name);
            stage.show();
        } catch (IOException e) {
            LOG.error("Error in FXWrapper.launchSubPage: Could not load fxml content for {}.", name);
        }
    }

    /**
     * Getter function for the navigation controller.
     * @return the current navigation controller
     */
    public NavigationController getNavigationController() {
        return navigationController;
    }
}