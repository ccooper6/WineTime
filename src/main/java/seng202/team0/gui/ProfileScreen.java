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

import java.util.List;

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


    public void initialize() {
        List<AnchorPane> wishlistWineView = List.of(wishlistWine1, wishlistWine2, wishlistWine3, wishlistWine4);
    }

//    on wineView clicked, open wine pop up, pass the relevant information from the wine. make a method to do this,
//    this functionality will be needed for mulitple screens.


    @FXML
    public void onWishlistRefreshed() {
//        wishlistMoreArrow.setOnMouseClicked(event -> );  need to pull favourite wines from user
//        user.getFavouritedWines()
    }
}


