package seng202.team1.gui;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

public class SelectChallengePopupController {

    public void startVarietyChallenge(ActionEvent actionEvent) {
//        close popup
//        launch profile
//move wine pane down
        FXWrapper.getInstance().setChallenge(1);
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.closePopUp();
        navigationController.loadPageContent("profile");

    }


    public void closeChallengePopup(MouseEvent mouseEvent) {
//        close popup
//        launch profile
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.closePopUp();

    }
}
