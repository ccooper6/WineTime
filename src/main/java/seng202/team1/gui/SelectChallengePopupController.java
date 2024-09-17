package seng202.team1.gui;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import seng202.team1.models.User;
import seng202.team1.repository.ChallengeDAO;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.services.ChallengeService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SelectChallengePopupController {

    ChallengeService challengeService = new ChallengeService();


    /**
     * updates the database and closes popup and reloads the profile screen on start challenge clicked.
     * @param actionEvent
     */

    public void startVarietyChallenge(ActionEvent actionEvent) {
        challengeService.startChallengeVariety();
        challengeService.launchProfile();
    }


    /**
     * closes the popup.
     * @param mouseEvent
     */
    public void closeChallengePopup(MouseEvent mouseEvent) {
        challengeService.closeSelectChallenge();

    }

}
