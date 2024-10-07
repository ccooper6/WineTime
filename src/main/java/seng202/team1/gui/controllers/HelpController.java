package seng202.team1.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HelpController {

    @FXML
    private ImageView homePageHelp;

    @FXML
    private ImageView loginHelp;

    @FXML
    private ImageView profilePageHelp;

    @FXML
    private ImageView reviewWineHelp;

    @FXML
    private ImageView wishListHelp;

    @FXML
    private ImageView searchPageHelp;

    @FXML
    private ImageView winePopUpHelp;
    @FXML
    private ImageView wineTimeText;
    @FXML
    private ImageView wineTimeLogo;

    public void initialize() {
        homePageHelp.setImage(new Image(getClass().getResourceAsStream("/images/helpScreen/homePageHelp.gif")));
        loginHelp.setImage(new Image(getClass().getResourceAsStream("/images/helpScreen/loginHelp.gif")));
        profilePageHelp.setImage(new Image(getClass().getResourceAsStream("/images/helpScreen/profileHelp.gif")));
        reviewWineHelp.setImage(new Image(getClass().getResourceAsStream("/images/helpScreen/reviewWineHelp.gif")));
        wishListHelp.setImage(new Image(getClass().getResourceAsStream("/images/helpScreen/wishListHelp.gif")));
        winePopUpHelp.setImage(new Image(getClass().getResourceAsStream("/images/helpScreen/winePopupHelp.gif")));
        searchPageHelp.setImage(new Image(getClass().getResourceAsStream("/images/helpScreen/searchHelp.gif")));
        wineTimeLogo.setImage(new Image(getClass().getResourceAsStream("/images/logo.png")));
        wineTimeText.setImage(new Image(getClass().getResourceAsStream("/images/winetime-nobg.png")));
    }
}