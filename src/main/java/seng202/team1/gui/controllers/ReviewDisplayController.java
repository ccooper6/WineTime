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

    private Review review;
    private final ReviewService reviewService = new ReviewService();
    private final Logger LOG = LogManager.getLogger(ReviewDisplayController.class);

    /**
     * Initialises the controller.
     * Sets the text of the review date, description, stars representing the users rating
     * of the wine and tags liked/disliked.
     */
    @FXML
    public void initialize() {
        Review review = ReviewService.getCurrentReview();
        reviewDate.setText(review.getReviewDate());

        setReviewDescription(review);
        setStarRatings(review);
        setTags(review);

        Wine currentWine = ReviewService.getCurrentWine();
        displayWine(currentWine);
    }

    /**
     * Sets the review description text to the review description if it is not empty, otherwise
     * sets the text to "No Description".
     * @param review The review object to get the description from.
     */
    private void setReviewDescription(Review review) {
        if (!review.getReviewDescription().isEmpty()) {
            reviewDescription.setText('"' + " " + review.getReviewDescription() + " " + '"');
        } else {
            reviewDescription.setText("No Description");
        }
    }

    /**
     * Sets the star ratings of the review to the stars in the review display.
     * @param review The review object to get the rating from.
     */
    private void setStarRatings(Review review) {
        List<FontAwesomeIconView> stars = List.of(star1, star2, star3, star4, star5);
        int rating = review.getRating();

        for (int i = 0; i < rating; i++) {
            stars.get(i).setGlyphName("STAR");
        }
    }

    /**
     * Sets the tags liked/disliked text to the tags in the review object. If there are no tags
     * liked or disliked, sets the text to "No tags liked".
     * @param review The review object to get the tags from.
     */
    private void setTags(Review review) {
        tagsLiked.setText("");

        ArrayList<String> tags = review.getTagsSelected();
        if (tags == null || tags.isEmpty()) {
            tagsLiked.setText("No tags liked");
        } else if (review.getRating() < 3) {
            tagsLiked.setText("Tags disliked: " + String.join(", ", tags));
        } else {
            tagsLiked.setText("Tags liked: " + String.join(", ", tags));
        }
    }

    /**
     * Handles the wine object by setting the current wine in the search wine service if it is not null and
     * tries to load the wineMiniDisplay.fxml content.
     * @param wine The wine object to be handled.
     */
    private void displayWine(Wine wine) {
        if (wine == null) {
            LOG.error("Error in ReviewDisplay.initialize(): Current wine is null for review with ID {}", review.getUid());
        } else {
            SearchWineService.getInstance().setCurrentWine(wine);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/wineMiniDisplay.fxml"));
                reviewedWineTile.getChildren().add(loader.load());
            } catch (IOException e) {
                LOG.error("Error in ReviewDisplay.initialize(): Could not load fxml content for review with ID {}", review.getUid());
            }
        }
    }

    /**
     * Deletes the selected review from the database and refreshes the wine reviews page.
     */
    @FXML
    public void onDeletePressed() {
        reviewService.deleteReview(review);
        FXWrapper.getInstance().launchSubPage("wineReviews");
    }

    /**
     * Sets the review to be displayed and refreshes the page.
     * @param review The review to be displayed.
     */
    public void setReview(Review review) {
        this.review = review;
        initialize();
    }
}