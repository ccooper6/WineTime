package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team0.models.testWines.wine1;
import seng202.team0.services.CounterService;


/**
 * Controller for the main.fxml window
 * @author seng202 teaching team
 */
public class MainController{

    private static final Logger log = LogManager.getLogger(MainController.class);

    @FXML
    private Label defaultLabel;

    @FXML
    private Button defaultButton;
    @FXML
    private Button defaultButton1;

    private CounterService counterService;

    /**
     * Initialize the window
     *
     * @param stage Top level container for this window
     */
    public void init(Stage stage) {
        counterService = new CounterService();
    }

    /**
     * Method to call when our counter button is clicked
     *
     */
    @FXML
    public void onButton1Clicked() {
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.initPopUp(new wine1());
    }

    @FXML
    public void onButtonClicked() {
        FXWrapper.getInstance().launchSubPage("mainpage");
    }


}
