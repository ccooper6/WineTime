package seng202.team1.gui.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.models.Review;
import seng202.team1.models.Wine;
import seng202.team1.services.LogsService;
import seng202.team1.services.SearchWineService;

import java.io.IOException;
import java.util.List;

public class ReviewDisplayController {
    @FXML
    private AnchorPane reviewedWineTile;
    @FXML
    private Text reviewDate;
    @FXML
    private Text reviewDescription;
    @FXML
    private FontAwesomeIconView star1;
    @FXML
    private FontAwesomeIconView star2;
    @FXML
    private FontAwesomeIconView star3;
    @FXML
    private FontAwesomeIconView star4;
    @FXML
    private FontAwesomeIconView star5;
    @FXML
    private Text tagsLiked;
    @FXML
    private Button editButton;

    @FXML
    public void initialize() {
        Logger LOG = LogManager.getLogger(ReviewDisplayController.class);
        Review review = LogsService.getCurrentReview();
        reviewDate.setText(review.getReviewDate());
        reviewDescription.setText(review.getReviewDescription());
        List<FontAwesomeIconView> stars = List.of(star1, star2, star3, star4, star5);
        for (int i = 0; i < review.getRating(); i++) {
            stars.get(i).setGlyphName("STAR");
        }
        Wine currentWine = LogsService.getCurrentWine();
        if (currentWine == null) {
            LOG.error("Current wine is null for review with ID: " + review.getUid());
        } else {
            SearchWineService.getInstance().setCurrentWine(currentWine);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/wineMiniDisplay.fxml"));
                reviewedWineTile.getChildren().add(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
