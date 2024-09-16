package seng202.team1.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.apache.commons.beanutils.LazyDynaClass;
import org.apache.commons.collections.functors.FalsePredicate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.models.Wine;
import seng202.team1.repository.ChallengeDAO;
import seng202.team1.repository.DatabaseManager;
import seng202.team1.services.SearchWineService;
import seng202.team1.services.WineCategoryService;
import seng202.team1.services.WishlistService;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Controller class for the profile.fxml page.
 * @author Lydia Jackson
 */
public class ProfileController {


    String challengeName;
    public Button quizButton;
    public Label challengeLabeltext;
    public Button StartChallengeButton;
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
    private  FontAwesomeIconView ReviewsSC;
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
    private AnchorPane wishlistPane;

    @FXML
    AnchorPane chal1;
    @FXML
    AnchorPane chal2;
    @FXML
    AnchorPane chal3;
    @FXML
    AnchorPane chal4;
    @FXML
    AnchorPane chal5;

    List<AnchorPane> wineViews;

    private DatabaseManager databaseManager;
    private ChallengeDAO chalDao;

    private int currentUserID;

    private static final Logger log = LogManager.getLogger(ProfileController.class);

    List<Wine> wineList;
    // consider adding a wine info aspect to wine class so u can get the string description from wines
    public String getWineInfo(Wine wine) {
        return("Name: %s\nVariety: %s\nDescription: %s".formatted(wine.getName(), wine.getVariety(), wine.getDescription()));
    }
    // ******************Test stuff ENDS HERE********************


    /**
     * Initialises the controller checks if user has is participating in a challenge, calls methods to appropriately alter
     * screens.
     */
    public void initialize() {
        // the users id needs to be refreshed, registered user thinks its 0
        challengePane.setVisible(false);

        databaseManager = DatabaseManager.getInstance();
        chalDao = new ChallengeDAO();
        currentUserID = chalDao.getUId(FXWrapper.getInstance().getCurrentUser());
        System.out.println("user id: " + currentUserID);
        System.out.println(chalDao.getChallengeForUser(currentUserID));
        if (Objects.equals(chalDao.getChallengeForUser(currentUserID), "Variety Challenge")) {
            System.out.println("user has this challenge");
            System.out.println("calls moveWine");
            System.out.println("wine ids for chal ");
            System.out.println("wine list " + chalDao.getWineIdsFromChallenge(chalDao.getChallengeForUser(currentUserID)));
            for(int i =0; i<5; i++){
                System.out.println("wine id: " + chalDao.getWineIdsFromChallenge(chalDao.getChallengeForUser(currentUserID)).get(i));
                System.out.println("wines " + chalDao.getWinesForChallenge(chalDao.getChallengeForUser(currentUserID)).get(i).getName());
            }
            moveWinesPane();
            activateChallenge();
            displayChallenge(chalDao.getChallengeForUser(currentUserID));
        }

        displayWishlist();

    }

    /**
     * will display the wishlist on the profile in the scrollable grid format, currently displays a wine catergory using
     * wine catergory display.
     */
//    @FXML
//    public void displayWishlist(List<AnchorPane> wishlistWineView, List<Label> wishlistWineInfo) {
//        SearchWineService.getInstance().searchWinesByName("Stemmari", 10);
//        wineList = SearchWineService.getInstance().getWineList();
//        if (wineList.size() >= wishlistWineView.size()) {
//            for (int i = 0; i < wishlistWineView.size(); i++) {
//                wishlistWineInfo.get(i).setText(getWineInfo(wineList.get(i)));
//            }
//        } else {
//            for (int i = wineList.size(); i < wishlistWineView.size(); i++) {
//                wishlistWineInfo.get(i).setText("No wine available.");
//            }
//        }
//    }

    @FXML
    public void displayWishlist() {
        WineCategoryService.getInstance().resetCurrentCategory();
        int currentUserUid = WishlistService.getUserID(FXWrapper.getInstance().getCurrentUser());
        System.out.println(currentUserUid);

        try {
            SearchWineService.getInstance().searchWinesByWishlist(currentUserUid);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/wineCategoryDisplay.fxml"));
            Parent parent = fxmlLoader.load();
            wishlistPane.getChildren().add(parent);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

        /*
         * try {
         *             FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("/fxml/wineCategoryDisplay.fxml"));
         *             Parent parent1 = fxmlLoader1.load();
         *             wishlistPane.getChildren().add(parent1);
         *         } catch (IOException e) {
         *             throw new RuntimeException(e);
         *         }
         */



//    @FXML
//    public void onWishlistRefreshed(List<AnchorPane> wishlistWineView, List<Label> wishlistWineInfo) {
//        wishlistMoreArrow.setOnMouseClicked(event -> {
//            Wine firtWine = wineList.getFirst();
//            wineList.removeFirst();
//            wineList.add(firtWine);
////            displayWishlist(wishlistWineView, wishlistWineInfo);
//        });
////
//    }

    /**
     * opens the quiz screen.
     * @param actionEvent
     */

    public void onQuizClicked(ActionEvent actionEvent) { FXWrapper.getInstance().launchSubPage("quizscreen");}


    /**
     * launches the select challenge popup.
     * @param actionEvent
     */
    public void onChallengeClicked(ActionEvent actionEvent) {
//        launch pop up to take you to challenge menu
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.closePopUp();
        navigationController.loadSelectChallengePopUpContent();

    }

    /**
     * displays the challenge wines using the wine mini displays
     * @param cname
     */
    public void displayChallenge(String cname) {
        wineViews = List.of(chal1, chal2, chal3, chal4, chal5);
        for (int i = 0; i < wineViews.size(); i++) {
//            SearchWineService.getInstance().setCurrentWine();
            SearchWineService.getInstance().setCurrentWine(chalDao.getWinesForChallenge(chalDao.getChallengeForUser(currentUserID)).get(i));
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/wineMiniDisplay.fxml"));
//                wineDisplays.add(loader.load());
                wineViews.get(i).getChildren().add(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }




    }

    /**
     * makes the challenge pane visible and disables previous one.
     */

    public void activateChallenge() {
        System.out.println("challenge tracker is activated.");
        noChallengePane.setVisible(false);
        challengePane.setVisible(true);
        challengeName = "Variety challenge"; // change to relevant.

    }

    /**
     * shifts the main pane to make room for challenge wines.
     */

    public void moveWinesPane() {
        System.out.println("moveWinesPane was called");
        System.out.println(winesPane.getLayoutY());
        winesPane.setLayoutY(190);

    }
}


