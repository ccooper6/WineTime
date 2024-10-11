package seng202.team1.gui.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.models.User;
import seng202.team1.models.Wine;
import seng202.team1.services.SearchWineService;
import seng202.team1.services.WishlistService;

import java.io.IOException;
import java.util.ArrayList;

/**
 * controller for wishlist.fxml
 * Uses methods in SearchWineService to call WishlistDAO to query database.
 */
public class WishlistController {
    private static final Logger LOG = LogManager.getLogger(WishlistController.class);
    private ArrayList<Wine> allWines;
    private final int MAXSIZE = 60;
    private int currentPage = 0;

    @FXML
    private GridPane wineGrid;
    @FXML
    private AnchorPane scrollAnchorPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private FontAwesomeIconView prevArrowButton;
    @FXML
    private Text pageCounterText;
    @FXML
    private FontAwesomeIconView nextArrowButton;
    @FXML
    private Text title;

    /**
     * initializes the controller
     * Selects all wine objects from the database where the int userID matches the current user.
     */
    @FXML
    public void initialize() {
        int currentUserUid = User.getCurrentUser().getId();
        allWines = WishlistService.getWishlistWines(currentUserUid);
        displayCurrentPage();
    }

    /**
     * Displays the wine objects in a grid form like SearchWineController.
     */
    @FXML
    public void displayCurrentPage() {
        if (allWines == null || allWines.isEmpty()) {
            title.setText("You have no wines saved in your wishlist.\nClick the heart symbol on any wine to save it for later!");
            pageCounterText.getParent().setVisible(false);
            LOG.error("Wine list is null");
            return;
        }
        int start = currentPage * MAXSIZE;

        pageCounterText.getParent().setVisible(start >= 0 && start <= allWines.size());

        if (start < 0 || start >= allWines.size()) {
            LOG.error("Cannot display wines out of bounds.");
            return;
        }

        wineGrid.getChildren().clear();
        scrollPane.setVvalue(0);

        int columns = wineGrid.getColumnCount();
        int end = Math.min(start + MAXSIZE, allWines.size());
        int gridRows = Math.ceilDiv(end - start, columns);
        wineGrid.setMinHeight(gridRows * (135 + 10) - 10);
        scrollAnchorPane.setMinHeight(gridRows * (135 + 10) - 10);

        pageCounterText.setText(currentPage + 1 + "/" + (Math.ceilDiv(allWines.size(), MAXSIZE)));
        prevArrowButton.getParent().setVisible(start > 0);
        nextArrowButton.getParent().setVisible(end < allWines.size());

        pageCounterText.getParent().setVisible(true);

        for (int i = 0; i < end - start; i++) {
            SearchWineService.getInstance().setCurrentWine(allWines.get(start + i));

            int currentRow = i / columns;
            int currentCol = i % columns;

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/wineMiniDisplay.fxml"));

                Parent parent = fxmlLoader.load();

                wineGrid.add(parent, currentCol, currentRow);

            } catch (IOException e) {
                LOG.error("Error in WishlistController.displayCurrentPage(): Could not load fxml content for wine ID {}.", allWines.get(start + i));
            }
        }
    }

    /**
     * Set current page to 0 and calls display page method.
     */
    @FXML
    public void pageStart() {
        currentPage = 0;
        displayCurrentPage();
    }

    /**
     * Decrement the current page number and calls display page method.
     */
    @FXML
    public void pagePrev() {
        currentPage--;
        displayCurrentPage();
    }

    /**
     * Increment the current page number and calls display page method.
     */
    @FXML
    public void pageNext() {
        currentPage++;
        displayCurrentPage();
    }

    /**
     * Set current page to last page and calls display page method.
     */
    @FXML
    public void pageEnd() {
        currentPage = Math.ceilDiv(allWines.size(), MAXSIZE) - 1;
        displayCurrentPage();
    }
}
