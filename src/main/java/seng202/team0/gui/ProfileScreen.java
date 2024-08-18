package seng202.team0.gui;

import javafx.fxml.FXML;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import seng202.team0.models.User;
import seng202.team0.models.Wine;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for the profile.fxml page
 * @author Lydia Jackson
 */
public class ProfileScreen {


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
    private  ScrollPane challengePane;
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


    // ***********TEST CASE WINE OBJECTS***************
    List<Wine> favWinesTest = new ArrayList<>(List.of(new Wine("1 Fave wine 1", "great red wine", 10,
            "timbuktu", "antarctica", "the world", "grape", "winners wine", "user a", "a"),
            new Wine("2 Fave wine 2", "great red wine", 10,
                    "timbuktu", "antarctica", "the world", "grape", "winners wine", "user a", "a"),
            new Wine("3 Fave wine 3", "great red wine", 10,
                    "timbuktu", "antarctica", "the world", "grape", "winners wine", "user a", "a"),
            new Wine("4 Fave wine 4", "great red wine", 10,
                    "timbuktu", "antarctica", "the world", "grape", "winners wine", "user a", "a"),
            new Wine("5 Fave wine 5", "great red wine", 10,
                    "timbuktu", "antarctica", "the world", "grape", "winners wine", "user a", "a"),
            new Wine("6 Fave wine 6", "great red wine", 10,
                    "timbuktu", "antarctica", "the world", "grape", "winners wine", "user a", "a"),
            new Wine("7 Fave wine 7", "great red wine", 10,
                    "timbuktu", "antarctica", "the world", "grape", "winners wine", "user a", "a"),
            new Wine("8 Fave wine 8", "great red wine", 10,
                    "timbuktu", "antarctica", "the world", "grape", "winners wine", "user a", "a"),
            new Wine("9 Fave wine 9", "great red wine", 10,
                    "timbuktu", "antarctica", "the world", "grape", "winners wine", "user a", "a")));

    // consider adding a wine info aspect to wine class so u can get the string description from wines
    public String getWineInfo(Wine wine) {
        return("Name: %s\nVariety: %s\nDescription: %s".formatted(wine.getName(), wine.getVariety(), wine.getDescription()));
    }
    // ******************ENDS HERE********************




    public void initialize() {
        List<AnchorPane> wishlistWineView = List.of(wishlistWine1, wishlistWine2, wishlistWine3, wishlistWine4);
        List<Label> wishlistWineInfo = List.of(wishlistWineInfo1, wishlistWineInfo2, wishlistWineInfo3, wishlistWineInfo4);
        displayWishlist(wishlistWineView, wishlistWineInfo);
        onWishlistRefreshed(wishlistWineView,  wishlistWineInfo);

    }

//    on wineView clicked, open wine pop up, pass the relevant information from the wine. make a method to do this,
//    this functionality will be needed for mulitple screens.

    @FXML
    public void displayWishlist(List<AnchorPane> wishlistWineView, List<Label> wishlistWineInfo) {
        if(favWinesTest.size() >= wishlistWineView.size()) {
            for (int i = 0; i < wishlistWineView.size(); i++) {
                wishlistWineInfo.get(i).setText(getWineInfo(favWinesTest.get(i)));
            }
        } else {
            for (int i = favWinesTest.size(); i < wishlistWineView.size(); i++) {
                wishlistWineInfo.get(i).setText("No wine available.");
            }
        }
    }


    @FXML
    public void onWishlistRefreshed(List<AnchorPane> wishlistWineView, List<Label> wishlistWineInfo) {
        wishlistMoreArrow.setOnMouseClicked(event -> {
            Wine firtWine = favWinesTest.get(0);
            favWinesTest.remove(0);
            favWinesTest.add(firtWine);
            displayWishlist(wishlistWineView, wishlistWineInfo);
        });
//
    }
}


