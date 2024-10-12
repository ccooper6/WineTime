package seng202.team1.gui.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.RangeSlider;
import org.controlsfx.control.SearchableComboBox;
import seng202.team1.gui.FXWrapper;
import seng202.team1.models.Wine;
import seng202.team1.repository.DAOs.TagDAO;
import seng202.team1.services.SearchWineService;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Controller for the SearchWines.fxml Page.
 */
// TODO: If time, try make the validation into a single modular method
public class SearchWineController {
    public Pane greyScreen;
    @FXML
    private FontAwesomeIconView sortDirection;
    @FXML
    private ComboBox<String> sortDropDown;
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
    @FXML
    private TextField minPriceTextField;
    @FXML
    private TextField maxPriceTextField;
    @FXML
    private Label pricePlusLabel;
    @FXML
    private TextField minPointsTextField;
    @FXML
    private TextField maxPointsTextField;
    @FXML
    private TextField minYearTextField;
    @FXML
    private TextField maxYearTextField;
    @FXML
    private Button resetCountry;
    @FXML
    private Button resetWinery;
    @FXML
    private Button resetVariety;
    @FXML
    private Text resetVintage;
    @FXML
    private Text resetPoints;
    @FXML
    private Text resetPrice;

    private final int MAXSIZE = 60;
    private ArrayList<Wine> allWines;
    private int currentPage = 0;
    private static final Logger LOG = LogManager.getLogger(SearchWineController.class);
    private static final NavigationController navigationController = new NavigationController();

    /**
     * Initializes the controller using wines from SearchWineService instance.
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
            setUpFilterValues();
        } else {
            resetFilters();
        }

        displayCurrentPage();
        setupGotoPopup();
    }

    /**
     * Sets up the goto page popup.
     */
    private void setupGotoPopup() {
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

    /**
     * Initializes the variety combo box and its listeners.
     */
    private void initializeVarietyComboBox()
    {
        varietyComboBox.getItems().addAll(TagDAO.getInstance().getVarieties());

        varietyComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            SearchWineService.getInstance().setCurrentVarietyFilter(newValue);
            resetVariety.setDisable(!(newValue != null && !newValue.isEmpty()));
        });

        varietyComboBox.setValue(SearchWineService.getInstance().getCurrentVarietyFilter());
        resetVariety.setDisable(!(varietyComboBox.getValue() != null && !varietyComboBox.getValue().isEmpty()));
    }

    /**
     * Initializes the country combo box and its listeners.
     */
    private void initializeCountryComboBox()
    {

        countryComboBox.getItems().addAll(TagDAO.getInstance().getCountries());

        countryComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            SearchWineService.getInstance().setCurrentCountryFilter(newValue);
            resetCountry.setDisable(!(newValue != null && !newValue.isEmpty()));
        });

        countryComboBox.setValue(SearchWineService.getInstance().getCurrentCountryFilter());
        resetCountry.setDisable(!(countryComboBox.getValue() != null && !countryComboBox.getValue().isEmpty()));
    }

    /**
     * Initializes the winery combo box and its listeners
     */
    private void initializeWineryComboBox()
    {
        wineryComboBox.getItems().addAll(TagDAO.getInstance().getWineries());

        wineryComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            SearchWineService.getInstance().setCurrentWineryFilter(newValue);
            resetWinery.setDisable(!(newValue != null && !newValue.isEmpty()));
        });

        wineryComboBox.setValue(SearchWineService.getInstance().getCurrentWineryFilter());
        resetWinery.setDisable(!(wineryComboBox.getValue() != null && !wineryComboBox.getValue().isEmpty()));
    }

    /**
     * Initializes the year/vintage range slider and also its related text fields, sets up the listeners to make
     * this work.
     */
    private void initializeVintageRangeSlider() {
        vintageSlider.setMin(TagDAO.getInstance().getMinVintage());
        vintageSlider.setMax(TagDAO.getInstance().getMaxVintage());
        vintageSlider.setLowValue(vintageSlider.getMin());
        vintageSlider.setHighValue(vintageSlider.getMax());

        minYearTextField.setText(String.valueOf((int) vintageSlider.getMin()));
        maxYearTextField.setText(String.valueOf((int) vintageSlider.getMax()));

        vintageSlider.setLabelFormatter(new StringConverter<>() {
            public String toString(Number value) {
                // Show only specific labels for min, max, and every 5 years (or any desired interval)
                if (value.intValue() == vintageSlider.getMin() || value.intValue() == vintageSlider.getMax() || (value.intValue() - 21) % 50 == 0) {
                    return String.valueOf(value.intValue());
                }
                return ""; // Hide other labels
            }

            @Override
            public Number fromString(String string) {
                // Not used, only here for the StringConverted to be fully implemented.
                return Integer.parseInt(string);
            }
        });

        vintageSlider.lowValueProperty().addListener((observable, oldValue, newValue) -> {
            minYearTextField.setText(String.valueOf(newValue.intValue()));
            SearchWineService.getInstance().setCurrentMinYear(newValue.intValue());
            resetVintage.setVisible(newValue.intValue() != vintageSlider.getMin() || vintageSlider.getHighValue() != vintageSlider.getMax());
        });

        vintageSlider.highValueProperty().addListener((observable, oldValue, newValue) -> {
            maxYearTextField.setText(String.valueOf(newValue.intValue()));
            SearchWineService.getInstance().setCurrentMaxYear(newValue.intValue());
            resetVintage.setVisible(vintageSlider.getLowValue() != vintageSlider.getMin() || newValue.intValue() != vintageSlider.getMax());
        });

        // TextFields -> Slider (on Enter key or focus loss)
        minYearTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                validateAndSetSliderLowValueVintage();
            }
        });

        minYearTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Focus lost
                validateAndSetSliderLowValueVintage();
            }
        });

        maxYearTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                validateAndSetSliderHighValueVintage();
            }
        });

        maxYearTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Focus lost
                validateAndSetSliderHighValueVintage();
            }
        });

        vintageSlider.setLowValue(SearchWineService.getInstance().getCurrentMinYear());
        vintageSlider.setHighValue(SearchWineService.getInstance().getCurrentMaxYear());
        resetVintage.setVisible(vintageSlider.getLowValue() != vintageSlider.getMin() || vintageSlider.getHighValue() != vintageSlider.getMax());
    }

    /**
     * Initializes the points range slider and also its related text fields, sets up the listeners to make
     * this work.
     */
    private void initializePointsRangeSlider()
    {
        pointsSlider.setMin(TagDAO.getInstance().getMinPoints());
        pointsSlider.setMax(TagDAO.getInstance().getMaxPoints());
        pointsSlider.setLowValue(pointsSlider.getMin());
        pointsSlider.setHighValue(pointsSlider.getMax());

        pointsSlider.setLabelFormatter(new StringConverter<>() {
            public String toString(Number value) {
                // Show only specific labels for min, max, and every 5 years (or any desired interval)
                if (value.intValue() == pointsSlider.getMin() || value.intValue() == pointsSlider.getMax() || value.intValue() % 5 == 0) {
                    return String.valueOf(value.intValue());
                }
                return ""; // Hide other labels
            }

            @Override
            public Number fromString(String string) {
                // Not used, only here for the StringConverted to be fully implemented.
                return Integer.parseInt(string);
            }
        });

        // Initialize text fields
        minPointsTextField.setText(String.valueOf((int) pointsSlider.getMin()));
        maxPointsTextField.setText(String.valueOf((int) pointsSlider.getMax()));

        pointsSlider.lowValueProperty().addListener((observable, oldValue, newValue) -> {
            minPointsTextField.setText(String.valueOf(newValue.intValue()));
            SearchWineService.getInstance().setCurrentMinPoints(newValue.intValue());
            resetPoints.setVisible(newValue.intValue() != pointsSlider.getMin() || pointsSlider.getHighValue() != pointsSlider.getMax());
        });

        pointsSlider.highValueProperty().addListener((observable, oldValue, newValue) -> {
            maxPointsTextField.setText(String.valueOf(newValue.intValue()));
            SearchWineService.getInstance().setCurrentMaxPoints(newValue.intValue());
            resetPoints.setVisible(pointsSlider.getLowValue() != pointsSlider.getMin() || newValue.intValue() != pointsSlider.getMax());
        });

        // TextFields -> Slider (on Enter key or focus loss)
        minPointsTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                validateAndSetSliderLowValuePoints();
            }
        });

        minPointsTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Focus lost
                validateAndSetSliderLowValuePoints();
            }
        });

        maxPointsTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                validateAndSetSliderHighValuePoints();
            }
        });

        maxPointsTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Focus lost
                validateAndSetSliderHighValuePoints();
            }
        });

        pointsSlider.setLowValue(SearchWineService.getInstance().getCurrentMinPoints());
        pointsSlider.setHighValue(SearchWineService.getInstance().getCurrentMaxPoints());
        resetPoints.setVisible(pointsSlider.getLowValue() != pointsSlider.getMin() || pointsSlider.getHighValue() != pointsSlider.getMax());
    }

    /**
     * Initializes the price range slider and also its related text fields, sets up the listeners to make
     * this work.
     */
    private void initializePriceRangeSlider() {
        // Initialize slider
        priceSlider.setMin(4);
        priceSlider.setMax(200);
        priceSlider.setLowValue(priceSlider.getMin());
        priceSlider.setHighValue(priceSlider.getMax());

        priceSlider.setLabelFormatter(new StringConverter<>() {
            public String toString(Number value) {
                // Show only specific labels for min, max, and every 5 years (or any desired interval)
                if (value.intValue() == priceSlider.getMin() || value.intValue() == priceSlider.getMax() || value.intValue() % 25 == 0) {
                    return String.valueOf(value.intValue());
                }
                return ""; // Hide other labels
            }

            @Override
            public Number fromString(String string) {
                // Not used, only here for the StringConverted to be fully implemented.
                return Integer.parseInt(string);
            }
        });

        // Initialize text fields
        minPriceTextField.setText(String.valueOf((int) priceSlider.getMin()));
        maxPriceTextField.setText(String.valueOf((int) priceSlider.getMax()));

        // Slider -> TextFields
        priceSlider.lowValueProperty().addListener((observable, oldValue, newValue) -> {
            minPriceTextField.setText(String.valueOf(newValue.intValue()));
            SearchWineService.getInstance().setCurrentMinPrice(newValue.intValue());
            resetPrice.setVisible(newValue.intValue() != priceSlider.getMin() || priceSlider.getHighValue() != priceSlider.getMax());
        });

        priceSlider.highValueProperty().addListener((observable, oldValue, newValue) -> {
            maxPriceTextField.setText(String.valueOf(newValue.intValue()));
            SearchWineService.getInstance().setCurrentMaxPrice(newValue.intValue());
            resetPrice.setVisible(priceSlider.getLowValue() != priceSlider.getMin() || newValue.intValue() != priceSlider.getMax());
            pricePlusLabel.setVisible(newValue.intValue() == 200);
        });

        // TextFields -> Slider (on Enter key or focus loss)
        minPriceTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                validateAndSetSliderLowValuePrice();
            }
        });

        minPriceTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Focus lost
                validateAndSetSliderLowValuePrice();
            }
        });

        maxPriceTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                validateAndSetSliderHighValuePrice();
            }
        });

        maxPriceTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Focus lost
                validateAndSetSliderHighValuePrice();
            }
        });

        priceSlider.setLowValue(SearchWineService.getInstance().getCurrentMinPrice());
        priceSlider.setHighValue(SearchWineService.getInstance().getCurrentMaxPrice());
        resetPrice.setVisible(priceSlider.getLowValue() != priceSlider.getMin() || priceSlider.getHighValue() != priceSlider.getMax());
    }

    /**
     * Handles the verification of the low value of the price slider.
     */
    private void validateAndSetSliderLowValuePrice() {
        try {
            double minValue = Double.parseDouble(minPriceTextField.getText());
            if (minValue >= priceSlider.getMin() && minValue <= priceSlider.getHighValue()) {
                priceSlider.setLowValue(minValue);
                SearchWineService.getInstance().setCurrentMinPrice((int) minValue);
            } else {
                minPriceTextField.setText(String.valueOf((int) priceSlider.getLowValue()));
            }
        } catch (NumberFormatException e) {
            minPriceTextField.setText(String.valueOf((int) priceSlider.getLowValue()));
        }
    }

    /**
     * Handles the verification of the high value of the price slider.
     */
    private void validateAndSetSliderHighValuePrice() {
        try {
            double maxValue = Double.parseDouble(maxPriceTextField.getText());
            if (maxValue <= priceSlider.getMax() && maxValue >= priceSlider.getLowValue()) {
                priceSlider.setHighValue(maxValue);
                SearchWineService.getInstance().setCurrentMaxPrice((int) maxValue);
            } else {
                maxPriceTextField.setText(String.valueOf((int) priceSlider.getHighValue()));
            }
        } catch (NumberFormatException e) {
            maxPriceTextField.setText(String.valueOf((int) priceSlider.getHighValue()));
        }
    }

    /**
     * Handles the verification of the low value of the points' slider.
     */
    private void validateAndSetSliderLowValuePoints() {
        try {
            double minValue = Double.parseDouble(minPointsTextField.getText());
            if (minValue >= pointsSlider.getMin() && minValue <= pointsSlider.getHighValue()) {
                pointsSlider.setLowValue(minValue);
                SearchWineService.getInstance().setCurrentMinPoints((int) minValue);
            } else {
                minPointsTextField.setText(String.valueOf((int) pointsSlider.getLowValue()));
            }
        } catch (NumberFormatException e) {
            minPointsTextField.setText(String.valueOf((int) pointsSlider.getLowValue()));
        }
    }

    /**
     * Handles the verification of the high value of the points' slider.
     */
    private void validateAndSetSliderHighValuePoints() {
        try {
            double maxValue = Double.parseDouble(maxPointsTextField.getText());
            if (maxValue <= pointsSlider.getMax() && maxValue >= pointsSlider.getLowValue()) {
                pointsSlider.setHighValue(maxValue);
                SearchWineService.getInstance().setCurrentMaxPoints((int) maxValue);
            } else {
                maxPointsTextField.setText(String.valueOf((int) pointsSlider.getHighValue()));
            }
        } catch (NumberFormatException e) {
            maxPointsTextField.setText(String.valueOf((int) pointsSlider.getHighValue()));
        }
    }

    /**
     * Handles the verification of the low value of the vintage/year slider.
     */
    private void validateAndSetSliderLowValueVintage() {
        try {
            double minValue = Double.parseDouble(minYearTextField.getText());
            if (minValue >= vintageSlider.getMin() && minValue <= vintageSlider.getHighValue()) {
                vintageSlider.setLowValue(minValue);
                SearchWineService.getInstance().setCurrentMinYear((int) minValue);
            } else {
                minYearTextField.setText(String.valueOf((int) vintageSlider.getLowValue()));
            }
        } catch (NumberFormatException e) {
            minYearTextField.setText(String.valueOf((int) vintageSlider.getLowValue()));
        }
    }

    /**
     * Handles the verification of the high value of the year/vintage slider.
     */
    private void validateAndSetSliderHighValueVintage() {
        try {
            double maxValue = Double.parseDouble(maxYearTextField.getText());
            if (maxValue <= vintageSlider.getMax() && maxValue >= vintageSlider.getLowValue()) {
                vintageSlider.setHighValue(maxValue);
                SearchWineService.getInstance().setCurrentMaxYear((int) maxValue);
            } else {
                maxYearTextField.setText(String.valueOf((int)vintageSlider.getHighValue()));
            }
        } catch (NumberFormatException e) {
            maxYearTextField.setText(String.valueOf((int) vintageSlider.getHighValue()));
        }
    }

    /**
     * Apply the selected filters and refreshes the page to display updates.
     */
    public void onApplyFiltersButtonPushed() {
        FXWrapper.getInstance().getNavigationController().launchSearchWineLoadingScreen();
    }


    /**
     * Displays the current page of wines in a scrollable grid format using wines from allWines.
     */
    @FXML
    public void displayCurrentPage()
    {
        allWines = SearchWineService.getInstance().getWineList();

        if (allWines == null) {
            LOG.error("Error in SearchWineController.initialize(): The wine list is null");
            allWines = new ArrayList<>();
        }

        if (allWines.isEmpty()) {
            title.setText("\n\n\nSorry, there were no results for your search.\n\nTry:\n    - Checking your spelling\n    - A different combination of filters");

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
        wineGrid.getRowConstraints().clear();

        scrollPane.setVvalue(0);

        int columns = wineGrid.getColumnCount();

        // setup grid
        int end = Math.min(start + MAXSIZE, allWines.size());

        title.setText("Search Results showing " + (start + 1) + "-" + end + " of " + allWines.size());

        int gridRows = Math.ceilDiv(end - start, columns);
        wineGrid.setMinHeight(gridRows * (135 + 10) + 10); // rows * (height of mini display + padding) + padding
        scrollAnchorPane.setMinHeight(gridRows * (135 + 10) + 10);

        // page navigation management at bottom
        pageCounterText.setText(currentPage + 1 + "/" + (Math.ceilDiv(allWines.size(), MAXSIZE)));
        prevArrowButton.getParent().setVisible(start > 0);
        nextArrowButton.getParent().setVisible(end < allWines.size());

        pageCounterText.getParent().setVisible(true);

        gotoTotalText.setText("/" + (Math.ceilDiv(allWines.size(), MAXSIZE)));


        // add wines
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
                LOG.error("Error in SearchWineController.displayCurrentPage(): Could not load fxml content for wine ID {}.", allWines.get(start + i).getWineId());
            }
        }
    }

    /**
     * Sets the dropdown options for the sorting.
     */
    public void initSortByOptions() {
        sortDropDown.getItems().add("Name");
        sortDropDown.getItems().add("Price");
        sortDropDown.getItems().add("Points");
        sortDropDown.getItems().add("Vintage");
        sortDropDown.setValue(SearchWineService.getInstance().getPrevDropDown());
        sortDirection.setIcon(FontAwesomeIcon.valueOf("ARROW_UP"));
        SearchWineService.getInstance().setSortDirection(true);
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
        currentPage = Math.ceilDiv(allWines.size(), MAXSIZE) - 1;
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

    /**
     * When the sort arrow is clicked, the direction is toggled.
     * The display then resets the order of the stored wine elements
     */
    public void changeIcon() {
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
     * clears the filters set when button pressed.
     */
    public void onClearFiltersPushed() {
        resetFilters();
    }

    /**
     * Sets up the filter values according to previously saved ones
     */
    private void setUpFilterValues() {
        countryComboBox.setValue(SearchWineService.getInstance().getCurrentCountryFilter());
        varietyComboBox.setValue(SearchWineService.getInstance().getCurrentVarietyFilter());
        wineryComboBox.setValue(SearchWineService.getInstance().getCurrentWineryFilter());
        vintageSlider.setLowValue(SearchWineService.getInstance().getCurrentMinYear());
        vintageSlider.setHighValue(SearchWineService.getInstance().getCurrentMaxYear());
        pointsSlider.setLowValue(SearchWineService.getInstance().getCurrentMinPoints());
        pointsSlider.setHighValue(SearchWineService.getInstance().getCurrentMaxPoints());
        priceSlider.setLowValue(SearchWineService.getInstance().getCurrentMinPrice());
        priceSlider.setHighValue(SearchWineService.getInstance().getCurrentMaxPrice());
    }

    /**
     * Resets the filters to default values.
     */
    public void resetFilters() {
        countryComboBox.setValue(null);
        varietyComboBox.setValue(null);
        wineryComboBox.setValue(null);

        priceSlider.setLowValue(priceSlider.getMin());
        vintageSlider.setLowValue(vintageSlider.getMin());
        pointsSlider.setLowValue(pointsSlider.getMin());
        pointsSlider.setHighValue(pointsSlider.getMax());
        vintageSlider.setHighValue(vintageSlider.getMax());
        priceSlider.setHighValue(priceSlider.getMax());

        SearchWineService.getInstance().setCurrentCountryFilter(null);
        SearchWineService.getInstance().setCurrentWineryFilter(null);
        SearchWineService.getInstance().setCurrentVarietyFilter(null);
        SearchWineService.getInstance().setCurrentMinYear((int) vintageSlider.getMin());
        SearchWineService.getInstance().setCurrentMaxYear((int) vintageSlider.getMax());
        SearchWineService.getInstance().setCurrentMinPoints((int) pointsSlider.getMin());
        SearchWineService.getInstance().setCurrentMaxPoints((int) pointsSlider.getMax());
        SearchWineService.getInstance().setCurrentMinPrice((int) priceSlider.getMin());
        SearchWineService.getInstance().setCurrentMaxPrice((int) priceSlider.getMax());
    }

    /**
     * Sort by options trigger this function when they're clicked.
     * Re-queries the database with different ORDER BY parameter, then reloads
     */
    public void dropDownClicked() {
        String columnName = null;
        if (sortDropDown.getValue() != null) {
            switch (sortDropDown.getValue()) {
                case "Name" -> {
                    columnName = "wine_name";
                    sortDirection.setIcon(FontAwesomeIcon.valueOf("ARROW_UP"));
                    SearchWineService.getInstance().setSortDirection(true);
                }
                case "Price" -> {
                    columnName = "price";
                    sortDirection.setIcon(FontAwesomeIcon.valueOf("ARROW_UP"));
                    SearchWineService.getInstance().setSortDirection(true);
                }
                case "Points" -> {
                    columnName = "points";
                    sortDirection.setIcon(FontAwesomeIcon.valueOf("ARROW_DOWN"));
                    SearchWineService.getInstance().setSortDirection(false);
                }
                case "Vintage" -> {
                    columnName = "vintage";
                    sortDirection.setIcon(FontAwesomeIcon.valueOf("ARROW_UP"));
                    SearchWineService.getInstance().setSortDirection(true);
                }
            }
            String finalColumnName = columnName;
            navigationController.executeWithLoadingScreen(() -> {
                SearchWineService.getInstance().setSearchOrder(finalColumnName);
                SearchWineService.getInstance().setDropDown(sortDropDown.getValue());
                Platform.runLater(this::displayCurrentPage);
            });
        }
    }

    /**
     * Resets the country filter both in the gui and in the SearchWineService.
     */
    @FXML
    public void onResetCountryClicked() {

        countryComboBox.setValue(null);
        resetCountry.setDisable(true);

    }

    /**
     * Resets the winery filter both in the gui and in the SearchWineService.
     */
    @FXML
    public void onResetWineryClicked() {
        wineryComboBox.setValue(null);
        resetWinery.setDisable(true);
    }

    /**
     * Resets the variety filter both in the gui and in the SearchWineService.
     */
    @FXML
    public void onResetVarietyClicked() {
        varietyComboBox.setValue(null);
        resetVariety.setDisable(true);
    }

    /**
     * Resets the vintage slider both in the gui and in the SearchWineService.
     */
    @FXML
    public void onResetVintageClicked() {
        vintageSlider.setLowValue(vintageSlider.getMin());
        vintageSlider.setHighValue(vintageSlider.getMax());
        resetVintage.setVisible(false);
    }

    /**
     * Resets the points slider both in the gui and in the SearchWineService.
     */
    @FXML
    public void onResetPointsClicked() {
        pointsSlider.setLowValue(pointsSlider.getMin());
        pointsSlider.setHighValue(pointsSlider.getMax());
        resetPoints.setVisible(false);
    }

    /**
     * Resets the price slider both in the gui and in the SearchWineService.
     */
    @FXML
    public void onResetPriceClicked() {
        priceSlider.setLowValue(priceSlider.getMin());
        priceSlider.setHighValue(priceSlider.getMax());
        resetPrice.setVisible(false);
    }
}
