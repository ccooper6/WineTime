package seng202.team1.gui;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.models.User;
import seng202.team1.models.Wine;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.services.SearchWineService;
import seng202.team1.services.WishlistService;
//import seng202.team1.services.WishlistService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Uses methods in SearchWineService to call WishlistDAO to query database
 * Author @Elise
 */
public class WishlistController {
    private static final Logger log = LogManager.getLogger(SearchWineController.class);
    ArrayList<Wine> allWines;

    private final int MAXSIZE = 50;

    private int currentPage = 0;

    //    scroll panel
    @FXML
    GridPane wineGrid;
    @FXML
    AnchorPane scrollAnchorPane;
    @FXML
    ScrollPane scrollPane;

    //    page navigation
    @FXML
    FontAwesomeIconView startArrowButton;
    @FXML
    FontAwesomeIconView prevArrowButton;
    @FXML
    Button prevTextButton;
    @FXML
    Text pageCounterText;
    @FXML
    Button nextTextButton;
    @FXML
    FontAwesomeIconView nextArrowButton;
    @FXML
    FontAwesomeIconView endArrowButton;
    @FXML
    Text Title;
    int currentUserUid;

    /**
     * Selects all wine objects from the database where the int userID matches the current user
     */
    @FXML
    public void initialize() {
        currentUserUid = WishlistService.getUserID(FXWrapper.getInstance().getCurrentUser());
        allWines = WishlistService.getWishlistWines(currentUserUid);
        displayCurrentPage();
    }

    /**
     * Displays the wine objects in a grid form like SearchWineController
     */
    @FXML
    public void displayCurrentPage() {
        if (allWines == null || allWines.isEmpty()) {
            Title.setText("You have no wines saved in your wishlist.\nClick the heart symbol on any wine to save it for later!");
            pageCounterText.getParent().setVisible(false);
            log.error("Wine list is null");
            return;
        }
        int start = currentPage * MAXSIZE;

        if (start < 0 || start > allWines.size()) {
            pageCounterText.getParent().setVisible(false);
        } else {
            pageCounterText.getParent().setVisible(true);
        }

        if (start < 0 || start >= allWines.size()) {
            log.error("Cannot display wines out of bounds.");
            return;
        }

        wineGrid.getChildren().clear();
        scrollPane.setVvalue(0);

        int columns = wineGrid.getColumnCount();

//        setup grid
        int end = Math.min(start + MAXSIZE, allWines.size());

        int gridRows = Math.ceilDiv(end - start, columns);
        wineGrid.setMinHeight(gridRows * 130 - 10);
        scrollAnchorPane.setMinHeight(gridRows * 130 - 10);

//        page navigation management at bottom
        pageCounterText.setText(currentPage + 1 + "/" + (Math.ceilDiv(allWines.size(), MAXSIZE)));
        prevArrowButton.getParent().setVisible(start > 0);
        nextArrowButton.getParent().setVisible(end < allWines.size());

        pageCounterText.getParent().setVisible(true);


//        add wines
        for (int i = 0; i < end - start; i++) {
            SearchWineService.getInstance().setCurrentWine(allWines.get(start + i));

            int currentRow = i / columns;
            int currentCol = i % columns;

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/wineMiniDisplay.fxml"));

                Parent parent = fxmlLoader.load();

                wineGrid.add(parent, currentCol, currentRow);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Set current page to 0 and display the page
     */
    @FXML
    public void pageStart() {
        currentPage = 0;
        displayCurrentPage();
    }

    /**
     * Decrement the current page number and display the page
     */
    @FXML
    public void pagePrev() {
        currentPage--;
        displayCurrentPage();
    }

    /**
     * Increment the current page number and display the page
     */
    @FXML
    public void pageNext() {
        currentPage++;
        displayCurrentPage();
    }

    /**
     * Set current page to last page and display the page
     */
    @FXML
    public void pageEnd() {
        currentPage = Math.ceilDiv(allWines.size() - 1, MAXSIZE) - 1;
        displayCurrentPage();
    }
}
