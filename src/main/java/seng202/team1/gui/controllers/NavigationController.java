package seng202.team1.gui.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
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
    private FontAwesomeIconView userExampleButton;
    @FXML
    private VBox userDropDownMenu;
    @FXML
    private Parent overlayContent;
    @FXML
    private Pane topBar;
    @FXML
    private StackPane contentHere;
    @FXML
    private TextField searchBar;
    @FXML
    private ComboBox<String> sortByComboBox;

    private static final Logger LOG = LogManager.getLogger(NavigationController.class);

    private Wine wine;

     /**
     * Initializes the controller.
     */
    public void initialize() {
        initialseSortByComboBox();

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
    private void initialseSortByComboBox()
    {
        sortByComboBox.getItems().add("In Name");
        sortByComboBox.getItems().add("In Tags");
        if (SearchWineService.getInstance().getCurrentMethod() == null) {
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
        searchBar.setText(SearchWineService.getInstance().getCurrentSearch());
        searchBar.setOnAction(e -> {
            if (!searchBar.getText().isEmpty()) {

                if (sortByComboBox.getValue().equals("In Name")) {
                    SearchWineService.getInstance().searchWinesByName(searchBar.getText(), SearchDAO.UNLIMITED);
                } else {
                    SearchWineService.getInstance().searchWinesByTags(searchBar.getText(), SearchDAO.UNLIMITED);
                }

                SearchWineService.getInstance().setCurrentSearch(searchBar.getText());
                SearchWineService.getInstance().setCurrentMethod(sortByComboBox.getValue());
                FXWrapper.getInstance().launchSubPage("searchWine");
                searchBar.getParent().requestFocus();
            }
        });

        sortByComboBox.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER && !searchBar.getText().isEmpty()) {

                if (sortByComboBox.getValue().equals("In Name")) {
                    SearchWineService.getInstance().searchWinesByName(searchBar.getText(), SearchDAO.UNLIMITED);
                } else {
                    SearchWineService.getInstance().searchWinesByTags(searchBar.getText(), SearchDAO.UNLIMITED);
                }

                SearchWineService.getInstance().setCurrentSearch(searchBar.getText());
                SearchWineService.getInstance().setCurrentMethod(sortByComboBox.getValue());
                FXWrapper.getInstance().launchSubPage("searchWine");
                searchBar.getParent().requestFocus();
            }
        });

        topBar.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> { // Ensures that user can deselect the search bar
            if (searchBar.isFocused()) {
                searchBar.getParent().requestFocus();
            }
        });

        userExampleButton.setOnMouseEntered(event -> userDropDownMenu.setVisible(true));
        userExampleButton.setOnMouseExited(event -> {
            if (!userDropDownMenu.isHover()) {
                userDropDownMenu.setVisible(false);
            }
        });
        userDropDownMenu.setOnMouseExited(event -> {
            if (!userExampleButton.isHover()) {
                userDropDownMenu.setVisible(false);
            }
        });
        userDropDownMenu.setOnMouseEntered(event -> userDropDownMenu.setVisible(true));
    }

    /**
     * Logs out the user and returns to the login page.
     */
    @FXML
    public void onLogOutClicked() {
        FXWrapper.getInstance().setCurrentUser(null);
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
Smal        FXWrapper.getInstance().launchSubPage("wineReviews");
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
}
