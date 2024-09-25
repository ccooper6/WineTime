package seng202.team1.gui.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.models.Wine;
import seng202.team1.services.SearchWineService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Controller for the Search Wines Page.
 * @author Yuhao Zhang
 */
public class SearchWineController {
    private static final Logger LOG = LogManager.getLogger(SearchWineController.class);
    private final int MAXSIZE = 50;
    public FontAwesomeIconView sortDirection;
    public ComboBox sortDropDown;
    private ArrayList<Wine> allWines;
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
     * Initialises the controller using wines from SearchWineService instance.
     */
    @FXML
    public void initialize() {
        initSortByOptions();

        allWines = SearchWineService.getInstance().getWineList();

        if (allWines == null) {
            LOG.error("Wine list is null");
            allWines = new ArrayList<>();
        }
        displayCurrentPage();
    }

    /**
     * Displays the current page of wines in a scrollable grid format using wines from allWines.
     */
    @FXML
    public void displayCurrentPage() {
        if (allWines == null || allWines.size() == 0) {
            title.setText("Sorry, your search query had no results.\n\nTry:\n    - Checking your spelling\n    - Making sure you're searching for the correct attributes (e.g\n      Tags or Title)\n    - Making sure your tags are correct (e.g Winery, Variety,\n      Vintage, Country, Region)\n    - Different Keywords");

            pageCounterText.getParent().setVisible(false);

            LOG.error("Wine list is null");
            return;
        }

        int start = currentPage * MAXSIZE;

        if (allWines.isEmpty() || start < 0 || start > allWines.size()) {
            pageCounterText.getParent().setVisible(false);
        } else {
            pageCounterText.getParent().setVisible(true);
        }

        if (start < 0 || start >= allWines.size()) {
            LOG.error("Cannot display wines out of bounds.");
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
            if (SearchWineService.getInstance().getSortDirection()) {
                SearchWineService.getInstance().setCurrentWine(allWines.get(start + i));
            } else {
                SearchWineService.getInstance().setCurrentWine(allWines.get(allWines.size() - start - i -1));
            }

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
     * Sets the dropdown options for the sorting
     */
    public void initSortByOptions() {
        sortDropDown.getItems().add("Recommended");
        sortDropDown.getItems().add("Price");
        sortDropDown.getItems().add("Rating");
        sortDropDown.getItems().add("Vintage");
    }

    /**
     * Set current page to 0 and display the page.
     */
    @FXML
    public void pageStart() {
        currentPage = 0;
        displayCurrentPage();
    }

    /**
     * Decrement the current page number and display the page.
     */
    @FXML
    public void pagePrev() {
        currentPage--;
        displayCurrentPage();
    }

    /**
     * Increment the current page number and display the page.
     */
    @FXML
    public void pageNext() {
        currentPage++;
        displayCurrentPage();
    }

    /**
     * Set current page to last page and display the page.
     */
    @FXML
    public void pageEnd() {
        currentPage = Math.ceilDiv(allWines.size() - 1, MAXSIZE) - 1;
        displayCurrentPage();
    }

    public void changeIcon() {
        System.out.println(SearchWineService.getInstance().getSortDirection());
        if (SearchWineService.getInstance().getSortDirection()) {
            sortDirection.setIcon(FontAwesomeIcon.valueOf("ARROW_DOWN"));
            SearchWineService.getInstance().setSortDirection(false);
            displayCurrentPage();
        } else {
            sortDirection.setIcon(FontAwesomeIcon.valueOf("ARROW_UP"));
            SearchWineService.getInstance().setSortDirection(true);
            displayCurrentPage();
        }
    }
}
