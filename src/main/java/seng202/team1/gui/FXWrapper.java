package seng202.team1.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.controllers.NavigationController;

import java.io.IOException;

/**
 * A singleton class which launches the FXML pages.
 */
public class FXWrapper {
    private static FXWrapper instance = null;
    private Stage stage;
    private NavigationController navigationController;
    private String currentPage = "init";
    private String previousPage = "init";

    private static final Logger LOG = LogManager.getLogger(FXWrapper.class);

    /**
     * Default constructor for FXWrapper
     */
    public FXWrapper(){}

    /**
     * Gets the singleton.
     * @return the FXWrapper singleton
     */
    public static FXWrapper getInstance() {
        if (instance == null) {
            synchronized (FXWrapper.class) {
                instance = new FXWrapper();
            }
        }
        return instance;
    }

    /**
     * Stores the stage from the FXWindow.
     * @param stage is the stage from the FXWindow and is not changed.
     */
    public void init(Stage stage) {
        this.stage = stage;
    }

    /**
     * Loads the FXML and controller listed in the fx:controller.
     * This page type cannot launch popUps.
     * @param name is the name of the fxml in lowercase with no type.
     */
    public void launchPage(String name) {
        try {
            FXWrapper.getInstance().setCurrentPage(name);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(String.format("/fxml/%s.fxml", name)));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
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
            FXWrapper.getInstance().setCurrentPage(name);
            FXMLLoader navigationLoader = new FXMLLoader(getClass().getResource("/fxml/navigation.fxml"));
            Parent navigationRoot = navigationLoader.load();
            navigationController = navigationLoader.getController();
            if (name.equals("mainpage")) {
                navigationController.loadMainScreen();
            } else {
                navigationController.loadPageContent(name);
            }
            Scene scene = new Scene(navigationRoot);
            stage.setScene(scene);
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


    /**
     * Gets the current page that is being shown.
     * @return current page name
     */
    public String getCurrentPage() {
        return currentPage;
    }

    /**
     * Gets the previous page that was being shown.
     * @return previous page name
     */
    public String getPreviousPage() {
        return previousPage;
    }

    /**
     * Sets the current page that is being shown.
     * @param page the current page name
     */
    public void setCurrentPage(String page) {
        previousPage = currentPage;
        currentPage = page;
    }

    /**
     * Closes the application by calling system exit.
     */
    public void closeApplication() {
        System.exit(0);
    }

}