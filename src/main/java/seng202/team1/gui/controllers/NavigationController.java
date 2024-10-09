package seng202.team1.gui.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.FXWrapper;
import seng202.team1.models.User;
import seng202.team1.models.Wine;
import seng202.team1.repository.DAOs.SearchDAO;
import seng202.team1.services.CategoryService;
import seng202.team1.services.SearchWineService;
import seng202.team1.services.WishlistService;

import java.io.IOException;

/**
 * Controller class for the navigation.fxml page.
 * @author Elise Newman, Caleb Cooper, Lydia Jackson, Isaac Macdonald, Yuhao Zhang, Wen Sheng Thong
 */
public class NavigationController {
    public FontAwesomeIconView wishlistButton;
    public FontAwesomeIconView reviewsButton;
    public ImageView homeExampleButton;
    public FontAwesomeIconView userButton;
    public ImageView logo;
    public Pane logoPane;
    public Circle circle;
    public FontAwesomeIconView closeButton;
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


    private int openPopups = 0; // Counter for open popups

    private Parent loadingScreen;

    private static final Logger LOG = LogManager.getLogger(NavigationController.class);

    private Wine wine;
    private boolean dropdownLocked = false;

     /**
     * Initializes the controller.
     */
    public void initialize() {
        initialiseSearchBar();

        topBar.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> { // Ensures that user can deselect the search bar
            if (searchBar.isFocused()) {
                searchBar.getParent().requestFocus();
            }
        });
    }


    /**
     * Sets action events to when to search. Searches by name / tag depending on combo box
     */
    private void initialiseSearchBar()
    {
        if (FXWrapper.getInstance().getCurrentPage().equals("searchWine")) {
            searchBar.setText(SearchWineService.getInstance().getCurrentSearch());
        }

        searchBar.setOnAction(e -> launchSearchWineLoadingScreen());

        wishlistButton.setOnMouseEntered(event -> {
            wishlistButton.setFill(Paint.valueOf("#A05252"));
        });
        wishlistButton.setOnMouseExited(event -> {
            wishlistButton.setFill(Paint.valueOf("#70171e"));
        });
        reviewsButton.setOnMouseEntered(event -> {
            reviewsButton.setFill(Paint.valueOf("#909090"));
        });
        reviewsButton.setOnMouseExited(event -> {
            reviewsButton.setFill(Paint.valueOf("#707070"));
        });
        userButton.setOnMouseEntered(event -> {
            userButton.setFill(Paint.valueOf("#909090"));
        });
        userButton.setOnMouseExited(event -> {
            userButton.setFill(Paint.valueOf("#707070"));
        });
        closeButton.setOnMouseEntered(event -> {
            closeButton.setFill(Paint.valueOf("#909090"));
        });
        closeButton.setOnMouseExited(event -> {
            closeButton.setFill(Paint.valueOf("#b0b0b0"));
        });
        logoPane.setOnMouseEntered(event -> { //random values
            homeExampleButton.setFitHeight(homeExampleButton.getFitHeight() + 5);
            homeExampleButton.setFitWidth(homeExampleButton.getFitWidth() + 5);
            homeExampleButton.setTranslateX(-1.25);
            homeExampleButton.setTranslateY(-1.25);
            logo.setFitWidth(logo.getFitWidth() + 5);
            logo.setFitHeight(logo.getFitHeight() + 5);
            logo.setTranslateY(-1.25);
            logo.setTranslateX(-1.25);
            circle.setRadius(circle.getRadius() + 2.5);
        });
        logoPane.setOnMouseExited(event -> {
            homeExampleButton.setFitHeight(homeExampleButton.getFitHeight() - 5);
            homeExampleButton.setFitWidth(homeExampleButton.getFitWidth() - 5);
            homeExampleButton.setTranslateX(1.25);
            homeExampleButton.setTranslateY(1.25);
            logo.setFitHeight(logo.getFitHeight() - 5);
            logo.setTranslateY(1.25);
            logo.setTranslateX(1.25);
            circle.setRadius(circle.getRadius() - 2.5);
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
    public void launchSearchWineLoadingScreen() {
        NavigationController nav = FXWrapper.getInstance().getNavigationController();
        nav.executeWithLoadingScreen(() -> {
        SearchWineService.getInstance().searchWinesByName(searchBar.getText(), SearchDAO.UNLIMITED);
        SearchWineService.getInstance().setCurrentSearch(searchBar.getText());
        Platform.runLater(() -> FXWrapper.getInstance().launchSubPage("searchWine"));
        });
        searchBar.getParent().requestFocus();
    }


    /**
     * Launches the search page when the search icon is clicked.
     */
    public void onSearchIconClicked() {
        launchSearchWineLoadingScreen();
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

    @FXML
    public void closeApp() {
        FXWrapper.getInstance().closeApplication();
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
        if (dropdownButton.getRotate() == 90) {
            dropdownButton.setRotate(0);
            dropdownButton.setTranslateX(0);
        } else {
            dropdownButton.setRotate(90);
            dropdownButton.setTranslateX(-5);
        }
    }

    /**
     * Logs out the user and returns to the login page.
     */
    @FXML
    public void onLogOutClicked() {
        LOG.info("Logging out user {}", User.getCurrentUser().getName());

        User.setCurrenUser(null);
        CategoryService.resetCategories(true);
        FXWrapper.getInstance().launchPage("login");
    }

    /**Loads in content from desired fxml and initiates a blank, invisible overlay popup.
     * @param name is the fxml main content which is loaded
     */
    public void loadPageContent(String name) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(String.format("/fxml/%s.fxml", name)));
            Parent pageContent = loader.load();
            contentHere.getChildren().clear();
            contentHere.getChildren().add(pageContent);
        } catch (IOException e) {
            LOG.error("Error in NavigationController.loadPageContent: {}", e.getMessage());
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
            LOG.error("Error in NavigationController.loadMainScreen: Could not load fxml content.");
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
        if (openPopups >= 1) {
            LOG.error("Error in NavigationController.loadPopupContent: There is already a popup open, you cannot have more than 1 at a time. Close the popup and try again.");
            return;
        }
        try {
            FXMLLoader paneLoader = new FXMLLoader(getClass().getResource("/fxml/popup.fxml"));
            overlayContent = paneLoader.load();
            overlayContent.setVisible(true); // Initially invisible
            contentHere.getChildren().add(overlayContent);
            openPopups++;
        } catch (IOException e) {
            LOG.error("Error in NavigationController.loadPopupContent: Could not load fxml content.");
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
            LOG.error("Error in NavigationController.loadWineLoggingPopupContent: Could not load fxml content.");
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
            LOG.error("Error in NavigationController.loadSelectChallengePopupContent: Could not load fxml content.");
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
            LOG.error("Error in NavigationController.showLoadingScreen: Could not load fxml content.");
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
            contentHere.getChildren().remove(overlayContent);
            openPopups--;
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
        executeWithLoadingScreen(() -> Platform.runLater(() -> FXWrapper.getInstance().launchSubPage("wineReviews")));
    }

    /**
     * Sends the user to the wishlist page when the heart icon is clicked.
     */
    public void onLikesClicked() {
        executeWithLoadingScreen(() -> Platform.runLater(() -> FXWrapper.getInstance().launchSubPage("wishlist")));
    }

    /**
     * Sends the user to the profile page when the user button is clicked.
     */
    public void onUserClicked() {
        executeWithLoadingScreen(() -> Platform.runLater(() -> FXWrapper.getInstance().launchSubPage("profile")));
    }

    /**
     * Sends the user to the help page when the help button is clicked.
     */
    public void onHelpClicked() {
        executeWithLoadingScreen(() -> Platform.runLater(() -> FXWrapper.getInstance().launchSubPage("helpScreen")));
    }
}
