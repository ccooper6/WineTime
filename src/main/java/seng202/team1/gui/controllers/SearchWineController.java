package seng202.team1.gui.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.RangeSlider;
import seng202.team1.gui.FXWrapper;
import seng202.team1.models.Wine;
import seng202.team1.repository.DAOs.TagDAO;
import seng202.team1.services.SearchWineService;
import org.controlsfx.control.SearchableComboBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Controller for the Search Wines Page.
 * @author Yuhao Zhang
 */
public class SearchWineController {
    private static final Logger LOG = LogManager.getLogger(SearchWineController.class);
    private final int MAXSIZE = 60;
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
    @FXML
    private AnchorPane gotoPane;
    @FXML
    private Text gotoTotalText;
    @FXML
    private TextField gotoTextField;
    @FXML
    private Button gotoButton;
    @FXML
    private Button clearFiltersButton;

    @FXML
    private SearchableComboBox<String> varietyComboBox;
    @FXML
    private SearchableComboBox<String> countryComboBox;
    @FXML
    private SearchableComboBox<String> wineryComboBox;
    @FXML
    private RangeSlider vintageSlider;
    @FXML
    private RangeSlider priceSlider;
    @FXML
    private RangeSlider pointsSlider;


    /**
     * Initialises the controller using wines from SearchWineService instance.
     */
    @FXML
    public void initialize()
    {
        initializeCountryComboBox();
        initializeVarietyComboBox();
        initializeWineryComboBox();
        initializePointsRangeSlider();
        initializeVintageRangeSlider();
        initializePriceRangeSlider();
        initSortByOptions();
        gotoPane.setVisible(false);

        if (FXWrapper.getInstance().getPreviousPage().equals("searchWine")) {
            countryComboBox.setValue(SearchWineService.getInstance().getCurrentCountryFilter());
            varietyComboBox.setValue(SearchWineService.getInstance().getCurrentVarietyFilter());
            wineryComboBox.setValue(SearchWineService.getInstance().getCurrentWineryFilter());
            vintageSlider.setLowValue(SearchWineService.getInstance().getCurrentMinYear());
            vintageSlider.setHighValue(SearchWineService.getInstance().getCurrentMaxYear());
            pointsSlider.setLowValue(SearchWineService.getInstance().getCurrentMinPoints());
            pointsSlider.setHighValue(SearchWineService.getInstance().getCurrentMaxPoints());
            priceSlider.setLowValue(SearchWineService.getInstance().getCurrentMinPrice());
            priceSlider.setHighValue(SearchWineService.getInstance().getCurrentMaxPrice());
            System.out.println("from search page");
        } else {
            resetFilters();
            System.out.println("not from search page");
        }

        allWines = SearchWineService.getInstance().getWineList();

        if (allWines == null) {
            LOG.error("Wine list is null");
            allWines = new ArrayList<>();
        }
        
        displayCurrentPage();

        // setup goto popup
        gotoTextField.setStyle("-fx-border-color: RED");
        gotoButton.setDisable(true);

        // make goto text field red if input is invalid
        gotoTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("\\d+") && !newValue.isEmpty()) {
                int pageNumber = Integer.parseInt(newValue);
                if (pageNumber > 0 && pageNumber <= Math.ceil((double) allWines.size() / MAXSIZE)) { // check if page number is valid
                    gotoTextField.setStyle("-fx-border-color: GREEN");
                    gotoButton.setDisable(false);
                } else {
                    gotoTextField.setStyle("-fx-border-color: RED");
                    gotoButton.setDisable(true);
                }
            } else {
                gotoTextField.setStyle("-fx-border-color: RED"); // if input is not a number
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

    private void initializeVarietyComboBox()
    {
        varietyComboBox.getItems().addAll(TagDAO.getInstance().getVarieties());

        varietyComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            SearchWineService.getInstance().setCurrentVarietyFilter(newValue);
        });

    }
    private void initializeCountryComboBox()
    {

        countryComboBox.getItems().addAll(TagDAO.getInstance().getCountries());

        countryComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            SearchWineService.getInstance().setCurrentCountryFilter(newValue);
        });

    }
    private void initializeWineryComboBox()
    {
        wineryComboBox.getItems().addAll(TagDAO.getInstance().getWineries());

        wineryComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            SearchWineService.getInstance().setCurrentWineryFilter(newValue);
        });

    }

    private void initializeVintageRangeSlider()
    {
        vintageSlider.setMin(TagDAO.getInstance().getMinVintage());
        vintageSlider.setMax(TagDAO.getInstance().getMaxVintage());
        vintageSlider.setLowValue(vintageSlider.getMin());
        vintageSlider.setHighValue(vintageSlider.getMax());

        vintageSlider.lowValueProperty().addListener((observable, oldValue, newValue) -> {
            SearchWineService.getInstance().setCurrentMinYear(newValue.intValue());
        });

        vintageSlider.highValueProperty().addListener((observable, oldValue, newValue) -> {
            SearchWineService.getInstance().setCurrentMaxYear(newValue.intValue());
        });

    }

    private void initializePointsRangeSlider()
    {
        pointsSlider.setMin(TagDAO.getInstance().getMinPoints());
        pointsSlider.setMax(TagDAO.getInstance().getMaxPoints());
        pointsSlider.setLowValue(pointsSlider.getMin());
        pointsSlider.setHighValue(pointsSlider.getMax());

        pointsSlider.lowValueProperty().addListener((observable, oldValue, newValue) -> {
            SearchWineService.getInstance().setCurrentMinPoints(newValue.intValue());
        });

        pointsSlider.highValueProperty().addListener((observable, oldValue, newValue) -> {
            SearchWineService.getInstance().setCurrentMaxPoints(newValue.intValue());
        });

    }

    private void initializePriceRangeSlider()
    {
        priceSlider.setMin(4);
        priceSlider.setMax(200);
        priceSlider.setLowValue(priceSlider.getMin());
        priceSlider.setHighValue(priceSlider.getMax());

        priceSlider.lowValueProperty().addListener((observable, oldValue, newValue) -> {
            SearchWineService.getInstance().setCurrentMinPrice(newValue.intValue());
        });

        priceSlider.highValueProperty().addListener((observable, oldValue, newValue) -> {
            SearchWineService.getInstance().setCurrentMaxPrice(newValue.intValue());
        });

    }



    /**
     * Displays the current page of wines in a scrollable grid format using wines from allWines.
     */
    @FXML
    public void displayCurrentPage()
    {
        if (allWines == null || allWines.size() == 0) {
            title.setText("\n\n\nSorry, your search query had no results.\n\nTry:\n    - Checking your spelling\n    - Making sure you're searching for the correct attributes (e.g\n      Tags or Title)\n    - Making sure your tags are correct (e.g Winery, Variety,\n      Vintage, Country, Region)\n    - Different Keywords");

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
        wineGrid.getRowConstraints().clear();

        scrollPane.setVvalue(0);

        int columns = wineGrid.getColumnCount();

//        setup grid
        int end = Math.min(start + MAXSIZE, allWines.size());

        title.setText("Search Results showing " + (start + 1) + "-" + end + " of " + allWines.size());

        int gridRows = Math.ceilDiv(end - start, columns);
        wineGrid.setMinHeight(gridRows * (135 + 10) + 10); // rows * (height of mini display + padding) + padding
        scrollAnchorPane.setMinHeight(gridRows * (135 + 10) + 10);

//        page navigation management at bottom
        pageCounterText.setText(currentPage + 1 + "/" + (Math.ceilDiv(allWines.size(), MAXSIZE)));
        prevArrowButton.getParent().setVisible(start > 0);
        nextArrowButton.getParent().setVisible(end < allWines.size());

        pageCounterText.getParent().setVisible(true);

        gotoTotalText.setText("/" + (Math.ceilDiv(allWines.size(), MAXSIZE)));


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
    public void gotoPage() {
        String text = gotoTextField.getText();
        if (text.matches("\\d+") && !text.isEmpty()) {
            int pageNumber = Integer.parseInt(text);
            int totalPages = (int) Math.ceil((double) allWines.size() / MAXSIZE);
            if (pageNumber > 0 && pageNumber <= totalPages) {
                currentPage = pageNumber - 1;
                displayCurrentPage();
                gotoPane.setVisible(false);
                gotoTextField.clear();
            } else {
                gotoTextField.setStyle("-fx-border-color: RED");
            }
        }
    }

    /**
     * Opens up the Goto Page Popup.
     */
    @FXML
    public void openGotoPopup()
    {
        gotoPane.setVisible(true);
    }

    /**
     * Closes the Goto Page Popup.
     */
    @FXML
    public void closeGotoPopup()
    {
        gotoTextField.clear();
        gotoPane.setVisible(false);
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

    /**
     * Returns the selected string filters in format (Country, Winery, Variety)
     * If a filter hasn't been selected it will be null
     */
    public ArrayList<String> getStringFilters() {

        String country = countryComboBox.getValue();
        String winery = wineryComboBox.getValue();
        String variety = varietyComboBox.getValue();
        return new ArrayList<>(Arrays.asList(country, winery, variety));

    }

    /**
     * Returns the selected integer filters in format (min vintage, max vintage, min points, max points, min price, max price)
     */
    public ArrayList<Integer> getIntegerFilters() {
        Integer minVintage = (int) vintageSlider.getLowValue();
        Integer maxVintage = (int) vintageSlider.getHighValue();
        Integer minPoints = (int) pointsSlider.getLowValue();
        Integer maxPoints = (int) pointsSlider.getHighValue();
        Integer minPrice = (int) priceSlider.getLowValue();
        Integer maxPrice = (int) priceSlider.getHighValue();
        return new ArrayList<>(Arrays.asList(minVintage, maxVintage, minPoints, maxPoints, minPrice, maxPrice));

    }

    /**
     * Handles the clear filters presses
     */
    public void onClearFiltersPushed() {
        resetFilters();
    }

    /**
     * Resets the filters to default values
     */
    public void resetFilters() {

        countryComboBox.setValue(null);
        countryComboBox.setPromptText("Country");

        varietyComboBox.setValue(null);
        varietyComboBox.setPromptText("Variety");


        wineryComboBox.setValue(null);
        wineryComboBox.setPromptText("Winery");


        priceSlider.setLowValue(priceSlider.getMin());
        vintageSlider.setLowValue(vintageSlider.getMin());
        pointsSlider.setLowValue(pointsSlider.getMin());
        pointsSlider.setHighValue(pointsSlider.getMax());
        vintageSlider.setHighValue(vintageSlider.getMax());
        priceSlider.setHighValue(priceSlider.getMax());
    }
}
