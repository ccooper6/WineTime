package seng202.team1.gui.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.models.Review;
import seng202.team1.models.User;
import seng202.team1.services.ReviewService;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Controller class for the reviews.fxml page.
 * Displays all the wine reviews that the user has saved.
 */
public class ReviewsController {
    @FXML
    private GridPane reviewGrid;
    @FXML
    private AnchorPane scrollAnchorPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private FontAwesomeIconView prevArrowButton;
    @FXML
    private Text pageCounterText;
    @FXML
    private FontAwesomeIconView nextArrowButton;
    @FXML
    private Text title;

    private ArrayList<Review> allReviews;
    private final int MAXSIZE = 5;
    private int currentPage = 0;

    private static final Logger LOG = LogManager.getLogger(ReviewsController.class);

    /**
     * Selects all review objects from the database where the int userID matches the current user.
     */
    @FXML
    public void initialize() {
        int currentUserUid = User.getCurrentUser().getId();
        allReviews = ReviewService.getUserReviews(currentUserUid);
        displayCurrentPage();
    }

    /**
     * Display the current page of reviews.
     */
    @FXML
    public void displayCurrentPage() {
        if (allReviews == null || allReviews.isEmpty()) {
            handleEmptyReviews();
            return;
        }
        int start = currentPage * MAXSIZE;
        int end = Math.min(start + MAXSIZE, allReviews.size());

        if (start < 0 || start >= allReviews.size()) {
            LOG.error("Cannot display reviews out of bounds.");
            return;
        }

        setupPageDisplay(start, end);
        loadReviews(start, end);
    }

    /**
     * Display a message if the user has no saved reviews.
     */
    private void handleEmptyReviews() {
        title.setText("You have no saved wine reviews.\nClick the log symbol on any wine and fill out the form to save it for later!");
        pageCounterText.getParent().setVisible(false);
        if (allReviews == null) {
            LOG.error("Error: Review list is null");
        }
    }

    /**
     * Set up the display for the current page of reviews.
     * @param start Start index of the reviews to display
     * @param end End index of the reviews to display
     */
    private void setupPageDisplay(int start, int end) {
        pageCounterText.getParent().setVisible(start >= 0 && start <= allReviews.size());
        reviewGrid.getChildren().clear();
        reviewGrid.getRowConstraints().clear(); // Clear any existing row constraints
        scrollPane.setVvalue(0);

        reviewGrid.setMinHeight((end - start) * 200 + 50);
        scrollAnchorPane.setMinHeight((end - start) * 200 + 50);
        reviewGrid.setMaxWidth(925);

        pageCounterText.setText(currentPage + 1 + "/" + (Math.ceilDiv(allReviews.size(), MAXSIZE)));
        prevArrowButton.getParent().setVisible(start > 0);
        nextArrowButton.getParent().setVisible(end < allReviews.size());

        pageCounterText.getParent().setVisible(true);
        reviewGrid.setAlignment(Pos.TOP_LEFT);
    }

    /**
     * Load the reviews to display on the current page.
     * @param start Start index of the reviews to display
     * @param end End index of the reviews to display
     */
    private void loadReviews(int start, int end) {
        for (int i = 0; i < end - start; i++) {
            ReviewService reviewService = new ReviewService();
            reviewService.setCurrentReview(allReviews.get(start + i));

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/reviewDisplay.fxml"));
                Parent parent = fxmlLoader.load();

                ReviewDisplayController controller = fxmlLoader.getController();
                controller.setReview(allReviews.get(start + i));

                RowConstraints rowConstraints = new RowConstraints();
                rowConstraints.setMinHeight(200);
                rowConstraints.setPrefHeight(200);
                reviewGrid.getRowConstraints().add(rowConstraints);

                reviewGrid.add(parent, 0, i);

            } catch (IOException e) {
                LOG.error("Error: Could not load Reviews page.");
            }
        }
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
    }

    /**
     * Set current page to 0 and display the page.
     */
    @FXML
    public void pageStart() {
        currentPage = 0;
        displayCurrentPage();
    }

    /**
     * Decrement the current page number and display the page.
     */
    @FXML
    public void pagePrev() {
        currentPage--;
        displayCurrentPage();
    }

    /**
     * Increment the current page number and display the page.
     */
    @FXML
    public void pageNext() {
        currentPage++;
        displayCurrentPage();
    }

    /**
     * Set current page to last page and display the page.
     */
    @FXML
    public void pageEnd() {
        currentPage = Math.ceilDiv(allReviews.size() - 1, MAXSIZE) - 1;
        displayCurrentPage();
    }
}
