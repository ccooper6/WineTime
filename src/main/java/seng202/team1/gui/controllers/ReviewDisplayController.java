package seng202.team1.gui.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.FXWrapper;
import seng202.team1.models.Review;
import seng202.team1.models.Wine;
import seng202.team1.services.LogsService;
import seng202.team1.services.SearchWineService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Types.NULL;

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
        reviewDate.setText("Date Reviewed: " + review.getReviewDate());

        if (!review.getReviewDescription().isEmpty()) {
            reviewDescription.setText("Description: " + review.getReviewDescription());
        } else {
            reviewDescription.setText("No description provided");
        }

        List<FontAwesomeIconView> stars = List.of(star1, star2, star3, star4, star5);
        int rating = review.getRating();

        for (int i = 0; i < rating; i++) {
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

        tagsLiked.setText("");

        ArrayList<String> tags = LogsService.getSelectedTags();
        if (tags.isEmpty()) {
            tagsLiked.setText("No tags liked");
        } else if (rating < 3) {
            tagsLiked.setText("Tags disliked: " + String.join(", ", tags));
        } else {
            tagsLiked.setText("Tags liked: " + String.join(", ", tags));
        }
    }

    @FXML
    public void onDeletePressed() {
        LogsService.deleteReview(LogsService.getCurrentReview().getRating());
        FXWrapper.getInstance().launchSubPage("logWine");
    }

}
