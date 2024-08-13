package seng202.team0.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/** A singleton class which launches the FXML pages.
 */
public class FXWrapper {
    private static FXWrapper instance = null;
    private Stage stage;
    private String currentFXML = "login";

    private FXWrapper() {}

    /** Gets the singleton.
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

    /**Stores the stage from the FXWindow.
     * @param stage is not changed.
     */
    public void init(Stage stage) {
        this.stage = stage;
    }

    /**Loads the FXML and controller listed in the fx:controller.
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
}