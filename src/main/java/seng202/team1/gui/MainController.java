package seng202.team1.gui;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import seng202.team1.services.SearchWineService;
import seng202.team1.services.WineCategoryService;

import java.io.IOException;

/**
 * Controller class for the mainpage.fxml page.
 * @author Caleb Cooper, Yuhao Zhang, Isaac Macdonald
 */
public class MainController {
    @FXML
    Text helloText;
    @FXML
    GridPane contentsGrid;
    private Stage loadingStage;

    /**
     * Initializes the main page view.
     */
    public void initialize() {
        showLoadingScreen();
        Task<Void> task = new Task<>() {
            // "The call method must be overridden and implemented by subclasses.
            // The call method actually performs the background thread logic" - From the documentation
            @Override
            protected Void call() {
                // Code as usual
                WineCategoryService.getInstance().resetCurrentCategory();
                helloText.setText("Hello, " + FXWrapper.getInstance().getCurrentUser().getName() + "!");

                String[] tags = {
                        "Bordeaux, Merlot",
                        "Marlborough, Sauvignon Blanc",
                        "Tuscany, Sangiovese",
                        "Hawke's Bay, Syrah",
                        "Spain, Rioja, Tempranillo",
                        "Mendoza, Malbec",
                        "US, Napa Valley, Cabernet Sauvignon",
                        "Central Otago, Pinot Noir, New Zealand"
                };

                try {
                    for (int i = 0; i < tags.length; i++) {
                        SearchWineService.getInstance().searchWinesByTags(tags[i], 10);
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                                .getResource("/fxml/wineCategoryDisplay.fxml"));
                        Parent parent = fxmlLoader.load();
                        int finalI = i;
                        Platform.runLater(() -> contentsGrid.add(parent, 0, finalI));
                        // Have to do this as it requires multiple loops to finish completely
                        // - need to use for "A Task Which Returns Partial Results", from the Task documentation
                        WineCategoryService.getInstance().incrementCurrentCategory();
                        System.out.println(i + 1 + "/" + tags.length);
                    }
                } catch (IOException e) { e.printStackTrace(); }
                return null;
            }

            @Override // I think it needs to be overridden from looking at the intellij information? Seems to work with either though
            protected void succeeded() { // Called if the task is successful in running completely through call()
                hideLoadingScreen();
            }
        };

        new Thread(task).start(); // Starts the thread to start behind the scenes (calls call())

    }

    /**
     * Shows the loading screen if it is needed.
     */
    private void showLoadingScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/loadingScreen.fxml"));
            Parent root = loader.load();
            loadingStage = new Stage();
            loadingStage.setScene(new Scene(root));
            loadingStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Hides the loading screen if it is currently shown.
     */
    private void hideLoadingScreen() {
        if (loadingStage != null) {
            loadingStage.close();
        }
    }
}
