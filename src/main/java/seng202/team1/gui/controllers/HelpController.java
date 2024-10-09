package seng202.team1.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.FXWrapper;

import java.util.HashMap;
import java.util.Objects;

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

    private static final Logger LOG = LogManager.getLogger(HelpController.class);

    public void initialize() {
        HashMap<ImageView, String> imagePaths = new HashMap<>() {{
            put(wineTimeLogo, "/images/logo.png");
            put(wineTimeText, "/images/winetime-nobg.png");
            put(loginHelp, "/images/helpScreen/loginHelp.gif");
            put(homePageHelp, "/images/helpScreen/homePageHelp.gif");
            put(profilePageHelp, "/images/helpScreen/profileHelp.gif");
            put(reviewWineHelp, "/images/helpScreen/reviewWineHelp.gif");
            put(wishListHelp, "/images/helpScreen/wishListHelp.gif");
            put(winePopUpHelp, "/images/helpScreen/winePopupHelp.gif");
            put(searchPageHelp, "/images/helpScreen/searchHelp.gif");
        }};

        FXWrapper.getInstance().getNavigationController().executeWithLoadingScreen(() -> {
            for (ImageView imageView : imagePaths.keySet()) {
                try {
                    imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePaths.get(imageView)))));
                } catch (NullPointerException e) {
                    LOG.error("Error: Could not load image {}, {}", imagePaths.get(imageView), e.getMessage());
                    // TODO maybe add text for user?
                }
            }
        });
    }
}