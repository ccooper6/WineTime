package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainPageController {
    @FXML
    Button wineButton;

    @FXML
    public void onWineButtonClicked() { // From advanced java fx tutorial
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.initPopUp("name", "wine");

//            FXMLLoader winePopupLoader = new FXMLLoader(getClass().getResource("/fxml/popup.fxml"));
//            VBox root = winePopupLoader.load();
//            Scene modalScene = new Scene(root);
//            Stage modal = new Stage();
//            modal.setScene(modalScene);
//            modal.setWidth(500);
//            modal.setHeight(400);
//            modal.setResizable(false);
//            modal.setTitle("Info");
//            modal.initModality(Modality.WINDOW_MODAL);
//            modal.showAndWait();
    }

    @FXML
    public void darkenButton(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #999999");
    }

    @FXML
    public void lightenButton(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("");
    }
}
