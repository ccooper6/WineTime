package seng202.team0.gui;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import seng202.team0.models.Wine;
import seng202.team0.services.SearchWineService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for the mainpage.fxml page
 * @author Caleb Cooper
 */
public class MainController {
    @FXML
    GridPane contentsGrid;

    /**
     * Initializes the controller.
     */
    public void initialize() {
        SearchWineService.getInstance().searchWinesByName("Rainstorm");

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/wineCategoryDisplay.fxml"));
            Parent parent = fxmlLoader.load();

            contentsGrid.add(parent, 0, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
