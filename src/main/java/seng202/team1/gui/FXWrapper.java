package seng202.team1.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import seng202.team1.models.User;

import java.io.IOException;

/**
 * A singleton class which launches the FXML pages.
 * @author Elise
 */
public class FXWrapper {
    private static FXWrapper instance = null;
    private Stage stage;
    private NavigationController navigationController;
    private User currentUser;
    private int challenge = 0;

    /**
     * Gets the singleton.
     * @return the FXWrapper singleton
     */
    public static FXWrapper getInstance() {
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
            e.printStackTrace();
        }
    }

    /**
     * Used for all pages with a navigation bar, navigation fxml is a parent to the variable fxml.
     * This page layout can launch popUps.
     * @param name is the name of the inner fxml in lowercase (no type)
     */
    public void launchSubPage(String name) {
        try {
            FXMLLoader navigationLoader = new FXMLLoader(getClass().getResource("/fxml/navigation.fxml"));
            Parent navigationRoot = navigationLoader.load();
            navigationController = navigationLoader.getController();
            navigationController.loadPageContent(name);

            Scene scene = new Scene(navigationRoot);
            stage.setScene(scene);
            stage.setTitle(name);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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
     * Sets the current user.
     * @param user the current user
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    /**
     * Gets the current user.
     * @return the current user
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the challenge number.
     * @param chalNum the challenge number.
     */
    public void setChallenge(int chalNum) {
        System.out.println("updated challenge number");
        challenge = chalNum;
    }

    /**
     * Gets the challenge number.
     * @return the challenge number.
     */
    public int getChallenge() {
        return challenge;
    }
}