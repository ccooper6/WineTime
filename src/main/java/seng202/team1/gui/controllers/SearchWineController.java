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
    @FXML
    private Pane greyScreen;
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
     * Default constructor for SearchWineController
     */
    public SearchWineController(){}

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
        initializeComboBox(
                varietyComboBox,
                TagDAO.getInstance().getVarieties(),
                "variety",
                resetVariety
        );
    }

    /**
     * Initializes the country combo box and its listeners.
     */
    private void initializeCountryComboBox()
    {
        initializeComboBox(
                countryComboBox,
                TagDAO.getInstance().getCountries(),
                "country",
                resetCountry
        );
    }

    /**
     * Initializes the winery combo box and its listeners.
     */
    private void initializeWineryComboBox()
    {
        initializeComboBox(
                wineryComboBox,
                TagDAO.getInstance().getWineries(),
                "winery",
                resetWinery
        );
    }

    /**
     * Initializes the year/vintage range slider and also its related text fields, sets up the listeners to make
     * this work.
     */
    private void initializeVintageRangeSlider() {
        initializeSlider(
                vintageSlider,
                minYearTextField,
                maxYearTextField,
                resetVintage,
                TagDAO.getInstance().getMinVintage(),
                TagDAO.getInstance().getMaxVintage(),
                "vintage"
        );
    }

    /**
     * Initializes the points range slider and also its related text fields, sets up the listeners to make
     * this work.
     */
    private void initializePointsRangeSlider()
    {
        initializeSlider(
                pointsSlider,
                minPointsTextField,
                maxPointsTextField,
                resetPoints,
                TagDAO.getInstance().getMinPoints(),
                TagDAO.getInstance().getMaxPoints(),
                "points"
        );
    }

    /**
     * Initializes the price range slider and also its related text fields, sets up the listeners to make
     * this work.
     */
    private void initializePriceRangeSlider() {
        initializeSlider(
                priceSlider,
                minPriceTextField,
                maxPriceTextField,
                resetPrice,
                4, // We set minimum and max to specific values to avoid slider from being unnecessarily large, 200 represents 200+
                200,
                "price"
        );
    }

    /**
     * Initializes the combo box with the given items and sets up the listeners to make this work.
     * @param comboBox the combo box to initialize
     * @param items the items to add to the combo box
     * @param filterType the type of filter (country, variety or winery)
     * @param resetButton the button that resets the filter
     */
    private void initializeComboBox(SearchableComboBox<String> comboBox, ArrayList<String> items, String filterType, Button resetButton) {
        comboBox.getItems().addAll(items);

        comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateSearchWineService(filterType, newValue);
            resetButton.setDisable(!(newValue != null && !newValue.isEmpty()));
        });

        comboBox.setValue(getCurrentFilterValue(filterType));
        resetButton.setDisable(!(comboBox.getValue() != null && !comboBox.getValue().isEmpty()));
    }

    /**
     * Initializes the slider with the given parameters. This keeps the code from being unnecessarily repeated and
     * hard to read.
     * @param slider the slider to initialize
     * @param minTextField the text field for the minimum value
     * @param maxTextField the text field for the maximum value
     * @param resetText the text to show when the slider is not at its default value
     * @param min the minimum value of the slider
     * @param max the maximum value of the slider
     * @param sliderType what type of slider the slider is (price, points or vintage)
     */
    private void initializeSlider(RangeSlider slider, TextField minTextField, TextField maxTextField, Text resetText, int min, int max, String sliderType) {
        // Initialize slider
        slider.setMin(min);
        slider.setMax(max);
        slider.setLowValue((int) slider.getMin());
        slider.setHighValue((int) slider.getMax());

        // Initialize text fields
        minTextField.setText(String.valueOf((int) slider.getMin()));
        maxTextField.setText(String.valueOf((int) slider.getMax()));

        slider.setLabelFormatter(new StringConverter<>() {
            // Show only specific labels for min, max, and every 5 years (or any desired interval)
            public String toString(Number value) {
                int interval = value.intValue() % 50; // Default case (price)
                if (sliderType.equals("vintage")) {
                    interval = (value.intValue() - 21) % 50;
                } else if (sliderType.equals("points")) {
                    interval = value.intValue() % 5;
                }
                if (value.intValue() == slider.getMin() || value.intValue() == slider.getMax() || interval == 0) {
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

        // Slider -> TextFields
        slider.lowValueProperty().addListener((observable, oldValue, newValue) -> {
            minTextField.setText(String.valueOf(newValue.intValue()));
            updateSearchWineService(sliderType, "min", newValue.intValue());
            resetText.setVisible(newValue.intValue() != min || slider.getHighValue() != max);
        });


        slider.highValueProperty().addListener((observable, oldValue, newValue) -> {
            maxTextField.setText(String.valueOf(newValue.intValue()));
            updateSearchWineService(sliderType, "max", newValue.intValue());
            resetText.setVisible(slider.getLowValue() != min || newValue.intValue() != max);
            if (sliderType.equals("price")) {
                pricePlusLabel.setVisible(newValue.intValue() == max);
            }
        });

        // TextFields -> Slider (on Enter key or focus loss)
        minTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                validateAndSetSliderLowValue(slider, minTextField, min, max, sliderType);
            }
        });

        minTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Focus lost
                validateAndSetSliderLowValue(slider, minTextField, min, max, sliderType);
            }
        });

        maxTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                validateAndSetSliderHighValue(slider, maxTextField, min, max);
            }
        });

        maxTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Focus lost
                validateAndSetSliderHighValue(slider, maxTextField, min, max);
            }
        });

        resetText.setVisible(slider.getLowValue() != min || slider.getHighValue() != max);
    }

    /**
     * Validates the low value of the slider and sets it to be the current value in the SearchWineService
     * if it is valid.
     * @param slider the slider to validate the value of
     * @param textField the text field to validate the value of
     * @param min the minimum value of the slider
     * @param max the maximum value of the slider
     */
    private void validateAndSetSliderLowValue(RangeSlider slider, TextField textField, int min, int max, String sliderType) {
        try {
            int value = Integer.parseInt(textField.getText());
            if (value >= min && value <= max) {
                slider.setLowValue(value);
                updateSearchWineService(sliderType, "min", value);
            } else {
                textField.setText(String.valueOf((int) slider.getLowValue()));
            }
        } catch (NumberFormatException e) {
            textField.setText(String.valueOf((int) slider.getLowValue()));
        }
    }

    /**
     * Validates the high value of the slider and sets it to be the current value in the SearchWineService
     * if it is valid.
     * @param slider the slider to validate the value of
     * @param textField the text field to validate the value of
     * @param min the minimum value of the slider
     * @param max the maximum value of the slider
     */
    private void validateAndSetSliderHighValue(RangeSlider slider, TextField textField, int min, int max) {
        try {
            int value = Integer.parseInt(textField.getText());
            if (value >= min && value <= max) {
                slider.setHighValue(value);
                updateSearchWineService("price", "max", value);
            } else {
                textField.setText(String.valueOf((int) slider.getHighValue()));
            }
        } catch (NumberFormatException e) {
            textField.setText(String.valueOf((int) slider.getHighValue()));
        }
    }

    /**
     * Updates the SearchWineService with the new values of the combobox according to what type
     * of combobox dropdown it is.
     * Note: This is used for searchable combo boxes
     *
     * @param filterType the type of filter (country, variety or winery)
     * @param value the value to set the current SearchWineService value to
     */
    private void updateSearchWineService(String filterType, String value) {
        SearchWineService searchWineService = SearchWineService.getInstance();
        switch (filterType) {
            case "country":
                searchWineService.setCurrentCountryFilter(value);
                break;
            case "variety":
                searchWineService.setCurrentVarietyFilter(value);
                break;
            case "winery":
                searchWineService.setCurrentWineryFilter(value);
                break;
        }
    }

    /**
     * Updates the SearchWineService with the new values of the sliders according to what type
     * of slider it is.
     * Note: This is used for sliders
     *
     * @param sliderType the type of slider (price, points or vintage)
     * @param valueType the type of value (min or max)
     * @param value the value to set the current SearchWineService value to
     */
    private void updateSearchWineService(String sliderType, String valueType, int value) {
        SearchWineService searchWineService = SearchWineService.getInstance();
        switch (sliderType) {
            case "price":
                if (valueType.equals("min")) {
                    searchWineService.setCurrentMinPrice(value);
                } else {
                    searchWineService.setCurrentMaxPrice(value);
                }
                break;
            case "points":
                if (valueType.equals("min")) {
                    searchWineService.setCurrentMinPoints(value);
                } else {
                    searchWineService.setCurrentMaxPoints(value);
                }
                break;
            case "vintage":
                if (valueType.equals("min")) {
                    searchWineService.setCurrentMinYear(value);
                } else {
                    searchWineService.setCurrentMaxYear(value);
                }
                break;
        }
    }

    /**
     * Gets the current filter value depending on what filter type is passed through.
     * @param filterType the type of filter to get the current value of
     * @return the current value of the filter
     */
    private String getCurrentFilterValue(String filterType) {
        SearchWineService service = SearchWineService.getInstance();
        return switch (filterType) {
            case "country" -> service.getCurrentCountryFilter();
            case "variety" -> service.getCurrentVarietyFilter();
            case "winery" -> service.getCurrentWineryFilter();
            default -> null;
        };
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
                LOG.error("Error in SearchWineController.displayCurrentPage(): Could not load fxml content for wine ID {}.", allWines.get(start + i).getID());
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
