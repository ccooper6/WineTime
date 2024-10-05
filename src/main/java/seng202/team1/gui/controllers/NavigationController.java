package seng202.team1.gui.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.FXWrapper;
import seng202.team1.models.User;
import seng202.team1.models.Wine;
import seng202.team1.repository.DAOs.SearchDAO;
import seng202.team1.services.SearchWineService;

import java.io.IOException;

/**
 * Controller class for the navigation.fxml page.
 * @author Elise Newman, Caleb Cooper, Lydia Jackson, Isaac Macdonald, Yuhao Zhang, Wen Sheng Thong
 */
public class NavigationController {
    @FXML
    private FontAwesomeIconView dropdownButton;
    @FXML
    private VBox userDropDownMenu;
    @FXML
    private Parent overlayContent;
    @FXML
    private StackPane rootPane;
    @FXML
    private Pane topBar;
    @FXML
    private StackPane contentHere;
    @FXML
    private TextField searchBar;
    @FXML
    private ComboBox<String> sortByComboBox;





    private Parent loadingScreen;

    boolean isFiltering = false;
    private static final Logger LOG = LogManager.getLogger(NavigationController.class);

    private Wine wine;
    private boolean dropdownLocked = false;

    private final ObservableList<String> options = FXCollections.observableArrayList();

     /**
     * Initializes the controller.
     */
    public void initialize() {
        initializeSortByComboBox();
        initialiseSearchBar();

        topBar.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> { // Ensures that user can deselect the search bar
            if (searchBar.isFocused()) {
                searchBar.getParent().requestFocus();
            }
        });
    }

    /**
     * Inserts options into sort by combo box and selects first.
     */
    private void initializeSortByComboBox()
    {
        sortByComboBox.getItems().add("In Name");
        sortByComboBox.getItems().add("In Tags");
        if (SearchWineService.getInstance().getCurrentMethod() == null || !FXWrapper.getInstance().getCurrentPage().equals("searchWine")) {
            sortByComboBox.getSelectionModel().selectFirst();
        } else {
            sortByComboBox.getSelectionModel().select(SearchWineService.getInstance().getCurrentMethod());
        }
    }

    /**
     * Sets action events to when to search. Searches by name / tag depending on combo box
     */
    private void initialiseSearchBar()
    {
        if (FXWrapper.getInstance().getCurrentPage().equals("searchWine")) {
            searchBar.setText(SearchWineService.getInstance().getCurrentSearch());
        }

        searchBar.setOnAction(e -> {
            launchSearchWineLoadingScreen();
        });

        sortByComboBox.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                launchSearchWineLoadingScreen();
            }
        });

        topBar.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> { // Ensures that user can deselect the search bar
            if (searchBar.isFocused()) {
                searchBar.getParent().requestFocus();
            } else if (userDropDownMenu.isVisible() && !dropdownButton.isHover()) {
                closeDropDown();
                rotateDropdownButton();
            }
        });

        contentHere.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (userDropDownMenu.isVisible() && !dropdownButton.isHover()) {
                closeDropDown();
                rotateDropdownButton();
            }
        });

        dropdownButton.setOnMouseEntered(event -> openDropDown());
        dropdownButton.setOnMouseExited(event -> {
            if (!userDropDownMenu.isHover() && !dropdownLocked) {
                closeDropDown();
            }
        });

        userDropDownMenu.setOnMouseExited(event -> {
            if (!dropdownButton.isHover() && !dropdownLocked) {
                closeDropDown();
            }
        });
        userDropDownMenu.setOnMouseEntered(event -> openDropDown());
    }

    /**
     * Launches the search wine loading screen by running it as a thread and launching the loading screen
     * to indicate to the user that there are searches being made behind the scenes.
     */
    private void launchSearchWineLoadingScreen() {
        NavigationController nav = FXWrapper.getInstance().getNavigationController();
        nav.executeWithLoadingScreen(() -> {
            if (sortByComboBox.getValue().equals("In Name")) {
                SearchWineService.getInstance().searchWinesByName(searchBar.getText(), SearchDAO.UNLIMITED);
            } else {
                SearchWineService.getInstance().searchWinesByTags(searchBar.getText(), SearchDAO.UNLIMITED);
            }
            SearchWineService.getInstance().setCurrentSearch(searchBar.getText());
            SearchWineService.getInstance().setCurrentMethod(sortByComboBox.getValue());

            Platform.runLater(() -> FXWrapper.getInstance().launchSubPage("searchWine"));
        });
        searchBar.getParent().requestFocus();
    }

    /**
     * Opens the dropdown menu.
     */
    private void openDropDown() {
        userDropDownMenu.setVisible(true);
    }

    /**
     * Closes the dropdown menu.
     */
    private void closeDropDown() {
        dropdownLocked = false;
        userDropDownMenu.setVisible(false);
    }

    /**
     * Toggles the dropdown menu open and closed.
     */
    @FXML
    public void toggleDropdownOpen() {
        if (!userDropDownMenu.isVisible() || (userDropDownMenu.isVisible() && !dropdownLocked)) {
            dropdownLocked = true;
            userDropDownMenu.setVisible(true);
            rotateDropdownButton();
        } else {
            dropdownLocked = false;
            userDropDownMenu.setVisible(false);
            rotateDropdownButton();
        }
    }

    /**
     * Rotates the dropdown button to indicate that the dropdown menu is toggled.
     */
    private void rotateDropdownButton() {
        dropdownButton.setRotate(dropdownButton.getRotate() + 270);
    }

    /**
     * Logs out the user and returns to the login page.
     */
    @FXML
    public void onLogOutClicked() {
        User.setCurrenUser(null);
        FXWrapper.getInstance().launchPage("login");
    }

    /**Loads in content from desired fxml and initates a blank, invisible overlay popup.
     * @param name is the fxml main content which is loaded
     */
    public void loadPageContent(String name) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(String.format("/fxml/%s.fxml", name)));
            Parent pageContent = loader.load();
            contentHere.getChildren().clear();
            contentHere.getChildren().add(pageContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the current fxml page name.
     * @return the current page name
     */
    public String getCurrentPage() {
        return FXWrapper.getInstance().getCurrentPage();
    }

    /**
     * Sends user to the main page.
     */
    public void loadMainScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainpage.fxml"));
            // mainPage is running underneath the other screens
            Parent mainPage = loader.load();
            contentHere.getChildren().add(mainPage); // Add the main page to the stack
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the currently viewed wine object.
     * @return Wine object
     */
    public Wine getWine() {
        return this.wine;
    }

    /**
     * Loads the wine popup when a wine is clicked by the user.
     */
    private void loadPopUpContent() {
        try {
            FXMLLoader paneLoader = new FXMLLoader(getClass().getResource("/fxml/popup.fxml"));
            overlayContent = paneLoader.load();
            overlayContent.setVisible(true); // Initially invisible
            contentHere.getChildren().add(overlayContent);
        } catch (IOException e) {
            LOG.error("Failed to load wine pop up\n" + e.getMessage());
        }
    }

    /**
     * Loads the wine logging popup page when the log wine button is clicked in the wine popup page.
     */
    public void loadWineLoggingPopUpContent() {
        try {
            FXMLLoader paneLoader = new FXMLLoader(getClass().getResource("/fxml/wineLoggingPopup.fxml"));
            overlayContent = paneLoader.load();
            overlayContent.setVisible(true);
            contentHere.getChildren().add(overlayContent);
        } catch (IOException e) {
            LOG.error("Failed to load wine logging pop up\n" + e.getMessage());
        }
    }

    /**
     * Loads the select challenge popup page when the select challenge button is clicked on the profile page.
     */
    public void loadSelectChallengePopUpContent() {
        try {
            FXMLLoader paneLoader = new FXMLLoader(getClass().getResource("/fxml/selectChallengePopup.fxml"));
            overlayContent = paneLoader.load();
            overlayContent.setVisible(true);
            contentHere.getChildren().add(overlayContent);
        } catch (IOException e) {
            LOG.error("Failed to load select challenge pop up\n" + e.getMessage());
        }
    }

    /**
     * The method allows blocks of logic to be executed with a loading screen. It also forces them to be run with
     * dedicated resources which is good when trying to reduce wait times (e.g. search wine service calls from the nav bar)
     * @param searchLogic the logic to be executed behind a loading screen
     */
    public void executeWithLoadingScreen(Runnable searchLogic) {
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        Platform.runLater(navigationController::showLoadingScreen);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                searchLogic.run();
                return null;
            }

            @Override
            protected void succeeded() {
                Platform.runLater(navigationController::hideLoadingScreen);
            }

            @Override
            protected void failed() {
                Platform.runLater(navigationController::hideLoadingScreen);
            }
        };

        new Thread(task).start();
    }

    /**
     * Shows the loading screen by adding it to the stack pane.
     */
    public void showLoadingScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/loadingScreen.fxml"));
            loadingScreen = loader.load();
            rootPane.getChildren().add(loadingScreen);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Hides the loading screen.
     */
    public void hideLoadingScreen() {
        rootPane.getChildren().remove(loadingScreen);
    }

    /**
     * Initializes the wine popup page with the wine that was clicked.
     * @param wine the wine that was clicked
     */
    public void initPopUp(Wine wine) {
        this.wine = wine;
        loadPopUpContent();
    }

    /**
     * Closes the popup screen when the user clicks the close button.
     */
    public void closePopUp() {
        if (overlayContent != null) {
            overlayContent.setVisible(false);
        }
    }

    /**
     * Sends the user to the home page when the home button is clicked.
     */
    public void onHomeClicked() {
        FXWrapper.getInstance().launchSubPage("mainpage");
    }

    /**
     * Sends the user to the wine log page when the log button is clicked.
     */
    public void onLogsClicked() {
        FXWrapper.getInstance().launchSubPage("wineReviews");
    }

    /**
     * Sends the user to the wishlist page when the heart icon is clicked.
     */
    public void onLikesClicked() {
        FXWrapper.getInstance().launchSubPage("wishlist");
    }

    /**
     * Sends the user to the profile page when the user button is clicked.
     */
    public void onUserClicked() {
        FXWrapper.getInstance().launchSubPage("profile");
    }

    /**
     * Sends the user to the help page when the help button is clicked.
     */
    public void onHelpClicked() {
        FXWrapper.getInstance().launchSubPage("helpScreen");
    }
}
