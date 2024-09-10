package seng202.team0.gui;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import seng202.team0.models.Wine;
import seng202.team0.services.SearchWineService;

import java.util.List;

/**
 * Controller class for the profile.fxml page.
 * @author Lydia Jackson
 */
public class ProfileController {


    String challengeName;
    @FXML
    public Button quizButton;
    @FXML
    public Label challengeLabeltext;
    @FXML
    public Button startChallengeButton;
    @FXML
    private Button changePasswordButton;
    @FXML
    private AnchorPane profilescrollpane;
    @FXML
    private  AnchorPane reveiwedWinePane;
    @FXML
    private  Label reviewedWineInfo;
    @FXML
    private  ImageView reviewedWineImage;
    @FXML
    private  AnchorPane wishlistWine1;
    @FXML
    private  Label wishlistWineInfo1;
    @FXML
    private  ImageView wishlistWineImage1;
    @FXML
    private  Label wishlistSeeMore;
    @FXML
    private  Label wishlistLabel;
    @FXML
    private  Label challengeLabel;

    @FXML
    private  Label reviewsLabel;
    @FXML
    private  Label reviewsSeeMore;
    @FXML
    private  AnchorPane wishlistWine2;
    @FXML
    private  Label wishlistWineInfo2;
    @FXML
    private  ImageView wishlistWineImage2;
    @FXML
    private  AnchorPane wishlistWine3;
    @FXML
    private  Label wishlistWineInfo3;
    @FXML
    private  ImageView wishlistWineImage3;
    @FXML
    private  AnchorPane wishlistWine4;
    @FXML
    private  Label wishlistWineInfo4;
    @FXML
    private  ImageView wishlistWineImage4;
    @FXML
    private  FontAwesomeIconView wishlistSC;
    @FXML
    private  FontAwesomeIconView profileSC;
    @FXML
    private  FontAwesomeIconView reviewsSC;
    @FXML
    private  ImageView logo;
    @FXML
    private  TextField searchBar;
    @FXML
    private  FontAwesomeIconView searchIcon;
    @FXML
    private  FontAwesomeIconView wishlistMoreArrow;

    @FXML
    private Pane winesPane;

    @FXML
    private Pane noChallengePane;

    @FXML
    private Pane challengePane;

    @FXML
    private Label challengeIntro;


    List<Wine> wineList;
    // consider adding a wine info aspect to wine class so u can get the string description from wines

    /**
     * Returns a formatted string with the wines name, variety and description.
     * @param wine the wine to get the info from
     * @return a formatted string
     */
    public String getWineInfo(Wine wine) {
        return ("Name: %s\nVariety: %s\nDescription: %s".formatted(wine.getName(), wine.getVariety(), wine.getDescription()));
    }
    // ******************Test stuff ENDS HERE********************

    /**
     * Initializes the profile.fxml page.
     */
    public void initialize() {
        List<AnchorPane> wishlistWineView = List.of(wishlistWine1, wishlistWine2, wishlistWine3, wishlistWine4);
        List<Label> wishlistWineInfo = List.of(wishlistWineInfo1, wishlistWineInfo2, wishlistWineInfo3,
                wishlistWineInfo4);
        displayWishlist(wishlistWineView, wishlistWineInfo);
        onWishlistRefreshed(wishlistWineView,  wishlistWineInfo);
        challengePane.setVisible(false);
//        FXWrapper.getInstance().setChallenge(1);
        System.out.println(FXWrapper.getInstance().getChallenge());
        if (FXWrapper.getInstance().getChallenge() == 1) {
            System.out.println("calls moveWine");
            moveWinesPane();
            activateChallenge();

        }

    }

//    on wineView clicked, open wine pop up, pass the relevant information from the wine. make a method to do this,
//    this functionality will be needed for mulitple screens.

    /**
     * Displays the wishlist wines on the profile page.
     * @param wishlistWineView the list of wine views
     * @param wishlistWineInfo the list of wine info labels
     */
    @FXML
    public void displayWishlist(List<AnchorPane> wishlistWineView, List<Label> wishlistWineInfo) {
        SearchWineService.getInstance().searchWinesByName("Stemmari");
        wineList = SearchWineService.getInstance().getWineList();
        if (wineList.size() >= wishlistWineView.size()) {
            for (int i = 0; i < wishlistWineView.size(); i++) {
                wishlistWineInfo.get(i).setText(getWineInfo(wineList.get(i)));
            }
        } else {
            for (int i = wineList.size(); i < wishlistWineView.size(); i++) {
                wishlistWineInfo.get(i).setText("No wine available.");
            }
        }
    }

    /**
     * Refreshes the wishlist wines on the profile page when the arrow is clicked.
     * @param wishlistWineView a list of wine views
     * @param wishlistWineInfo a list of wine info labels
     */
    @FXML
    public void onWishlistRefreshed(List<AnchorPane> wishlistWineView, List<Label> wishlistWineInfo) {
        wishlistMoreArrow.setOnMouseClicked(event -> {
            Wine firtWine = wineList.getFirst();
            wineList.removeFirst();
            wineList.add(firtWine);
            displayWishlist(wishlistWineView, wishlistWineInfo);
        });
//
    }

    /**
     * Opens the quiz screen when the quiz button is clicked.
     * @param actionEvent the event of the user clicking the quiz button
     */
    public void onQuizClicked(ActionEvent actionEvent) {
        FXWrapper.getInstance().launchSubPage("quizscreen");
    }

    /**
     * Opens the challenge menu when the challenge button is clicked.
     * @param actionEvent the event of the user clicking the challenge button
     */
    public void onChallengeClicked(ActionEvent actionEvent) {
//        launch pop up to take you to challenge menu
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.closePopUp();
        navigationController.loadSelectChallengePopUpContent();
    }

    /**
     * Activates the challenge tracker when the start challenge button is clicked.
     */
    public void activateChallenge() {
        System.out.println("challenge tracker is activated.");
        noChallengePane.setVisible(false);
        challengePane.setVisible(true);
        challengeName = "Variety challenge"; // change to relevant.
        challengeIntro.setText("you have started the " + challengeName);
    }

    /**
     * Moves the wines pane down when the challenge is active.
     */
    public void moveWinesPane() {
        System.out.println("moveWinesPane was called");
        System.out.println(winesPane.getLayoutY());
        winesPane.setLayoutY(190);

    }
}


