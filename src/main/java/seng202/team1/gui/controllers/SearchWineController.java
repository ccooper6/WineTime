package seng202.team1.gui.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.models.Wine;
import seng202.team1.services.SearchWineService;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Controller for the Search Wines Page.
 * @author Yuhao Zhang
 */
public class SearchWineController {
    private static final Logger LOG = LogManager.getLogger(SearchWineController.class);
    private final int MAXSIZE = 50;
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
    @FXML
    private AnchorPane gotoPane;
    @FXML
    private Text gotoTotalText;
    @FXML
    private TextField gotoTextField;
    @FXML
    private Button gotoButton;

    /**
     * Initialises the controller using wines from SearchWineService instance.
     */
    @FXML
    public void initialize()
    {

        gotoPane.setVisible(false);

        allWines = SearchWineService.getInstance().getWineList();

        if (allWines == null) {
            LOG.error("Error in SearchWineController.initialize(): The wine list is null");
            allWines = new ArrayList<>();
        }
        displayCurrentPage();

        // setup goto popup
        gotoTextField.setStyle("-fx-border-color: RED");
        gotoButton.setDisable(true);

        // make goto text field red if input is invalid
        gotoTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("\\d+") && !newValue.isEmpty() && Integer.parseInt(newValue) > 0 && Integer.parseInt(newValue) < Math.ceilDiv(allWines.size(), MAXSIZE)) {
                gotoTextField.setStyle("-fx-border-color: GREEN");
                gotoButton.setDisable(false);
            } else {
                gotoTextField.setStyle("-fx-border-color: RED");
                gotoButton.setDisable(true);
            }
        });

        gotoTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String text = gotoTextField.getText();
                if (text.matches("\\d+") && !text.isEmpty() && Integer.parseInt(text) > 0 && Integer.parseInt(text) < allWines.size()) {
                    gotoPage();
                }
            }
        });
    }

    /**
     * Displays the current page of wines in a scrollable grid format using wines from allWines.
     */
    @FXML
    public void displayCurrentPage()
    {
        if (allWines == null || allWines.isEmpty()) {
            title.setText("Sorry, your search query had no results.\n\nTry:\n    - Checking your spelling\n    - Making sure you're searching for the correct attributes (e.g\n      Tags or Title)\n    - Making sure your tags are correct (e.g Winery, Variety,\n      Vintage, Country, Region)\n    - Different Keywords");

            pageCounterText.getParent().setVisible(false);

            LOG.error("Error in SearchWineController.displayCurrentPage(): The wine list is null");
            return;
        }

        int start = currentPage * MAXSIZE;

        // disable if page out of bounds
        pageCounterText.getParent().setVisible(start >= 0 && start <= allWines.size());

        if (start < 0 || start >= allWines.size()) {
            LOG.error("Error in SearchWineController.displayCurrentPage(): Page {} is out of bounds for wine list", currentPage);
            return;
        }

        wineGrid.getChildren().clear();

        scrollPane.setVvalue(0);

        int columns = wineGrid.getColumnCount();

//        setup grid
        int end = Math.min(start + MAXSIZE, allWines.size());

        title.setText("Search Results showing " + (start + 1) + "-" + end + " of " + allWines.size());

        int gridRows = Math.ceilDiv(end - start, columns);
        wineGrid.setMinHeight(gridRows * 130 - 10);
        scrollAnchorPane.setMinHeight(gridRows * 130 - 10);

//        page navigation management at bottom
        pageCounterText.setText(currentPage + 1 + "/" + (Math.ceilDiv(allWines.size(), MAXSIZE)));
        prevArrowButton.getParent().setVisible(start > 0);
        nextArrowButton.getParent().setVisible(end < allWines.size());

        pageCounterText.getParent().setVisible(true);

        gotoTotalText.setText("/" + (Math.ceilDiv(allWines.size(), MAXSIZE)));


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
                LOG.error("Error in SearchWineController.displayCurrentPage(): Could not load fxml content for wine ID {}.", allWines.get(start + i).getWineId());
            }
        }
    }

    /**
     * Set current page to 0 and display the page.
     */
    @FXML
    public void pageStart()
    {
        currentPage = 0;
        displayCurrentPage();
    }

    /**
     * Decrement the current page number and display the page.
     */
    @FXML
    public void pagePrev()
    {
        currentPage--;
        displayCurrentPage();
    }

    /**
     * Increment the current page number and display the page.
     */
    @FXML
    public void pageNext()
    {
        currentPage++;
        displayCurrentPage();
    }

    /**
     * Set current page to last page and display the page.
     */
    @FXML
    public void pageEnd()
    {
        currentPage = Math.ceilDiv(allWines.size() - 1, MAXSIZE) - 1;
        displayCurrentPage();
    }

    /**
     * Sets the current page to the page defined by the user.
     */
    @FXML
    public void gotoPage()
    {
        int pageNumber = Integer.parseInt(gotoTextField.getText());

        currentPage = pageNumber - 1;
        displayCurrentPage();

        gotoPane.setVisible(false);
    }

    /**
     * Opens up the Goto Page Popup
     */
    @FXML
    public void openGotoPopup()
    {
        gotoPane.setVisible(true);
    }
}
