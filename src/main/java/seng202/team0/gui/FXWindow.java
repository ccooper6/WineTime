package seng202.team0.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Creates the window for the scene, loads the login page and initiates the FXWrapper.
 * @author Elise
 */
public class FXWindow extends Application {

    /**
     * Creates a login page and initialises fxWrapper.
     * @param primaryStage is the first stage initialised
     * @throws IOException when
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/profile.fxml"));   // change this back to login when done
        Parent root = loader.load();

        FXWrapper fxWrapper = FXWrapper.getInstance();
        fxWrapper.init(primaryStage);

        primaryStage.setTitle("Wine Time");
        primaryStage.getIcons().add(new Image(getClass().getResource("/images/logo.png").toExternalForm()));
        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Launches the FXWrapper class.
     * @param args from Application
     */
    public static void launchWrapper(String[] args) {
        launch(args);
    }
}
