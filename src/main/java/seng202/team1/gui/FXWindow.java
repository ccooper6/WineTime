package seng202.team1.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Creates the window for the scene, loads the login page and initiates the FXWrapper.
 */
public class FXWindow extends Application {

    /**
     * Creates a login page and initializes fxWrapper.
     * @param primaryStage is the first stage initialised
     * @throws IOException when
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        FXWrapper fxWrapper = FXWrapper.getInstance();
        fxWrapper.init(primaryStage);

        Parent root = loader.load();
        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setTitle("Wine Time");
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/images/logo.png")).toExternalForm()));
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
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
