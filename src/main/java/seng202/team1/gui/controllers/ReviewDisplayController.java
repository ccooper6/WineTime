package seng202.team1.gui.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.FXWrapper;
import seng202.team1.models.Review;
import seng202.team1.models.Wine;
import seng202.team1.services.ReviewService;
import seng202.team1.services.SearchWineService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for the reviewDisplay.fxml page.
 * Displays the details of a single review.
 * @author Caleb Cooper
 */
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

    private final Logger LOG = LogManager.getLogger(ReviewDisplayController.class);

    /**
     * Initialises the controller.
     * Sets the text of the review date, description, stars representing the users rating
     * of the wine and tags liked/disliked.
     */
    @FXML
    public void initialize() {
        Review review = ReviewService.getCurrentReview();
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

        Wine currentWine = ReviewService.getCurrentWine();
        if (currentWine == null) {
            LOG.error("Error in ReviewDisplay.initialize(): Current wine is null for review with ID {}", review.getUid());
        } else {
            SearchWineService.getInstance().setCurrentWine(currentWine);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/wineMiniDisplay.fxml"));
                reviewedWineTile.getChildren().add(loader.load());
            } catch (IOException e) {
                LOG.error("Error in ReviewDisplay.initialize(): Could not load fxml content for review with ID {}", review.getUid());
            }
        }

        tagsLiked.setText("");

        ArrayList<String> tags = review.getTagsSelected();
        if (tags == null || tags.isEmpty()) {
            tagsLiked.setText("No tags liked");
        } else if (rating < 3) {
            tagsLiked.setText("Tags disliked: " + String.join(", ", tags));
        } else {
            tagsLiked.setText("Tags liked: " + String.join(", ", tags));
        }
    }

    /**
     * Deletes the selected review from the database and refreshes the wine reviews page.
     */
    @FXML
    public void onDeletePressed() {
        LOG.info("Deleting review with ID {}", ReviewService.getCurrentReview().getUid());
        ReviewService.deleteReview(ReviewService.getCurrentReview().getRating());
        FXWrapper.getInstance().launchSubPage("wineReviews");
    }

}
