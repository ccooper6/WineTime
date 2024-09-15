package seng202.team1.gui;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.App;
import seng202.team1.models.Wine;
import seng202.team1.services.SearchWineService;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

import java.io.IOException;

/**
 * Controller class for the navigation.fxml page.
 * @author Elise
 */
public class NavigationController {
    @FXML
    public ImageView homeExampleButton;
    @FXML
    public FontAwesomeIconView savesExampleButton;
    @FXML
    public FontAwesomeIconView likesExampleButton;
    @FXML
    public FontAwesomeIconView userExampleButton;
    @FXML
    private VBox userDropDownMenu;
    @FXML
    private Button logOutButton;
    @FXML
    private Parent overlayContent;
    @FXML
    public AnchorPane mainContent;
    @FXML
    public Pane StackPanePane;
    @FXML
    Pane topBar;
    @FXML
    private StackPane contentHere;
    @FXML
    TextField searchBar;

    @FXML
    ComboBox<String> sortByComboBox;

    private static final Logger log = LogManager.getLogger(NavigationController.class);


    private Wine wine;
    //private WineService wineService = new WineService();

    /**
     * Loads in content from desired fxml and initates a blank, invisible overlay popup.
    /**
     * Initializes the controller
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
     * Inserts options into sort by combo box and selects first
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
                //searchForWine(searchBar.getText());

                if (sortByComboBox.getValue().equals("In Name")) {
                    SearchWineService.getInstance().searchWinesByName(searchBar.getText());
                } else {
                    SearchWineService.getInstance().searchWinesByTags(searchBar.getText());
                }

                SearchWineService.getInstance().setCurrentSearch(searchBar.getText());
                SearchWineService.getInstance().setCurrentMethod(sortByComboBox.getValue());
                FXWrapper.getInstance().launchSubPage("searchWine");
//                searchBar.clear();
                searchBar.getParent().requestFocus();
            }
        });

        sortByComboBox.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER && !searchBar.getText().isEmpty()) {

                //searchForWine(searchBar.getText());

                if (sortByComboBox.getValue().equals("In Name")) {
                    SearchWineService.getInstance().searchWinesByName(searchBar.getText());
                } else {
                    SearchWineService.getInstance().searchWinesByTags(searchBar.getText());
                }

                SearchWineService.getInstance().setCurrentSearch(searchBar.getText());
                SearchWineService.getInstance().setCurrentMethod(sortByComboBox.getValue());
                FXWrapper.getInstance().launchSubPage("searchWine");
//                searchBar.clear();
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

    @FXML
    public void onLogOutClicked(ActionEvent actionEvent) {
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
            contentHere.getChildren().addFirst(pageContent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /*private void searchForWine(String wineName) {
        List<Wine> wines = wineService.searchWineByName(wineName);
        if (!wines.isEmpty()) {
            initPopUp(wines.get(0));
        }
    }*/

    /**
     * Returns the currently viewed wine object
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
            log.error("Failed to load wine pop up\n" + e.getMessage());
        }
    }

    /**
     * Loads the wine logging popup page when the log wine button is clicked in the wine popup page
     */
    public void loadWineLoggingPopUpContent() {
        try {
            FXMLLoader paneLoader = new FXMLLoader(getClass().getResource("/fxml/wineLoggingPopup.fxml"));
            overlayContent = paneLoader.load();
            overlayContent.setVisible(true);
            contentHere.getChildren().add(overlayContent);
        } catch (IOException e) {
            log.error("Failed to load wine logging pop up\n" + e.getMessage());
        }
    }

    public void loadSelectChallengePopUpContent() {
        try {
            FXMLLoader paneLoader = new FXMLLoader(getClass().getResource("/fxml/selectChallengePopup.fxml"));
            overlayContent = paneLoader.load();
            overlayContent.setVisible(true);
            contentHere.getChildren().add(overlayContent);
        } catch (IOException e) {
            log.error("Failed to load select challenge pop up\n" + e.getMessage());
        }
    }

    public void initPopUp(Wine wine) {
        this.wine = wine;
        loadPopUpContent();
    }

    public void closePopUp() {
        if (overlayContent != null) {
            overlayContent.setVisible(false);
        }
//        refresh the page under it,

    }

    public void onHomeClicked(MouseEvent actionEvent) {
        FXWrapper.getInstance().launchSubPage("mainpage");
    }

    public void onSavesClicked(ActionEvent actionEvent) {
        FXWrapper.getInstance().launchSubPage("mainpage");
    }

    public void onLikesClicked(MouseEvent actionEvent) {
        Logger log = LogManager.getLogger(App.class);
        FXWrapper.getInstance().launchSubPage("wishlist");
    }

    public void onUserClicked(MouseEvent actionEvent) {
        //example navigation subpage - to change when made
        FXWrapper.getInstance().launchSubPage("profile");
    }


}
