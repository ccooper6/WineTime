package seng202.team0.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;

import java.io.IOException;

public class NavigationController {
    @FXML
    public AnchorPane mainContent;

    public void loadPageContent(String name) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(String.format("/fxml/%s.fxml", name)));
            Parent pageContent = loader.load();
            mainContent.getChildren().clear();
            mainContent.getChildren().add(pageContent);
            AnchorPane.setTopAnchor(pageContent, 0.0);
            AnchorPane.setBottomAnchor(pageContent, 0.0);
            AnchorPane.setLeftAnchor(pageContent, 0.0);
            AnchorPane.setRightAnchor(pageContent, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onHomeClicked(ActionEvent actionEvent) {
        FXWrapper.getInstance().launchSubPage("mainpage");
    }

    public void onSavesClicked(ActionEvent actionEvent) {
        //example navigation subpage - to change when made
        FXWrapper.getInstance().launchSubPage("mainpage");
    }

    public void onLikesClicked(ActionEvent actionEvent) {
        //example navigation subpage - to change when made
        FXWrapper.getInstance().launchSubPage("main");
    }

    public void onUserClicked(ActionEvent actionEvent) {
        //example navigation subpage - to change when made
        FXWrapper.getInstance().launchPage("login");
    }
}
