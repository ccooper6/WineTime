package seng202.team1.gui;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import seng202.team1.models.User;
import seng202.team1.repository.ChallengeDAO;
import seng202.team1.repository.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SelectChallengePopupController {

    private int currentUserID;

    private DatabaseManager databaseManager;
    private ChallengeDAO chalDao;



    public void startVarietyChallenge(ActionEvent actionEvent) {
//        close popup
//        launch profile
//move wine pane dow
        databaseManager = DatabaseManager.getInstance();
//        currentUserID = getUId(FXWrapper.getInstance().getCurrentUser());
        chalDao = new ChallengeDAO();
        currentUserID = chalDao.getUId(FXWrapper.getInstance().getCurrentUser());
        chalDao.userActivatesChallenge(currentUserID, "Variety Challenge");
        System.out.println("started challenge");
//        FXWrapper.getInstance().setChallenge(1);
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
