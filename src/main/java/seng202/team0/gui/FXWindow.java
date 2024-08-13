package seng202.team0.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Creates the window for the scene, loads the login page and initiates the FXWrapper
 */
public class FXWindow extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Parent root = loader.load();

        FXWrapper fxWrapper = FXWrapper.getInstance();
        fxWrapper.init(primaryStage);

        primaryStage.setTitle("WineTime App");
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Launches the FXWrapper class
     * @param args from Application
     */
    public static void launchWrapper(String[] args) {
        launch(args);
    }
}
