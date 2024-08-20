package seng202.team0.gui;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team0.App;
import seng202.team0.models.Wine;

import java.io.IOException;

/**
 * Controller class for the navigation.fxml page.
 * @author Elise
 */
public class NavigationController {
    @FXML
    public ImageView homeExampleButton;
    @FXML
    public FontAwesomeIconView savesExampleButton;
    @FXML
    public FontAwesomeIconView likesExampleButton;
    @FXML
    public FontAwesomeIconView userExampleButton;
    private Parent overlayContent;
    @FXML
    public AnchorPane mainContent;
    @FXML
    public Pane StackPanePane;
    @FXML
    private StackPane contentHere;

    private Wine wine;

    /**
     * Loads in content from desired fxml and initates a blank, invisible overlay popup.
     * @param name is the fxml main content which is loaded
     */
    public void loadPageContent(String name) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(String.format("/fxml/%s.fxml", name)));
            Parent pageContent = loader.load();
            contentHere.getChildren().clear();
            contentHere.getChildren().addFirst(pageContent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Wine getWine() { return this.wine; }

    /**
     * Creates a popup
     */
    private void loadPopUpContent() {
        try {
            FXMLLoader paneLoader = new FXMLLoader(getClass().getResource("/fxml/popup.fxml"));
            overlayContent = paneLoader.load();
            overlayContent.setVisible(true); // Initially invisible
            contentHere.getChildren().add(overlayContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initPopUp(Wine wine) {
        this.wine = wine;
        loadPopUpContent();
    }

    public void closePopUp() {
        if (overlayContent != null) {
            overlayContent.setVisible(false);
        }
    }

    public void onHomeClicked(MouseEvent actionEvent) {
        FXWrapper.getInstance().launchSubPage("mainpage");
    }

    public void onSavesClicked(ActionEvent actionEvent) {
        //example navigation subpage - to change when made
        FXWrapper.getInstance().launchSubPage("mainpage");
    }

    public void onLikesClicked(MouseEvent actionEvent) {
        //example navigation subpage - to change when made
        Logger log = LogManager.getLogger(App.class);
        log.info("Needs Implementing");
        FXWrapper.getInstance().launchSubPage("mainpage");
    }

    public void onUserClicked(MouseEvent actionEvent) {
        //example navigation subpage - to change when made
        FXWrapper.getInstance().launchSubPage("profile");
    }


}
