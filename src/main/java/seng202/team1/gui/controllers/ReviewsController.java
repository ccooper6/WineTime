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
import seng202.team1.gui.FXWrapper;
import seng202.team1.models.Review;
import seng202.team1.services.ReviewService;
import seng202.team1.services.WishlistService;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Controller class for the reviews.fxml page.
 * Displays all the wine reviews that the user has saved.
 * @author Caleb Cooper
 */
public class ReviewsController {
    private static final Logger LOG = LogManager.getLogger(ReviewsController.class);
    private ArrayList<Review> allReviews;
    private final int MAXSIZE = 5;
    private int currentPage = 0;

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

    private final ReviewService reviewService = new ReviewService();

    /**
     * Selects all review objects from the database where the int userID matches the current user.
     */
    @FXML
    public void initialize() {
        int currentUserUid = WishlistService.getUserID(FXWrapper.getInstance().getCurrentUser());
        allReviews = ReviewService.getUserReviews(currentUserUid);
        displayCurrentPage();
    }

    /**
     * Displays all the users reviews in an easy-to-read format.
     */
    @FXML
    public void displayCurrentPage() {
        if (allReviews == null || allReviews.isEmpty()) {
            title.setText("You have no saved wine logs.\nClick the log symbol on any wine and fill out the form to save it for later!");
            pageCounterText.getParent().setVisible(false);
            LOG.error("Review list is null");
            return;
        }
        int start = currentPage * MAXSIZE;

        if (start < 0 || start > allReviews.size()) {
            pageCounterText.getParent().setVisible(false);
        } else {
            pageCounterText.getParent().setVisible(true);
        }

        if (start < 0 || start >= allReviews.size()) {
            LOG.error("Cannot display reviews out of bounds.");
            return;
        }
        reviewGrid.getChildren().clear();
        reviewGrid.getRowConstraints().clear(); // Clear any existing row constraints
        scrollPane.setVvalue(0);

        int end = Math.min(start + MAXSIZE, allReviews.size());
        reviewGrid.setMinHeight((end - start) * 200 + 50);
        scrollAnchorPane.setMinHeight((end - start) * 200 + 50);
        reviewGrid.setMaxWidth(925);

        pageCounterText.setText(currentPage + 1 + "/" + (Math.ceilDiv(allReviews.size(), MAXSIZE)));
        prevArrowButton.getParent().setVisible(start > 0);
        nextArrowButton.getParent().setVisible(end < allReviews.size());

        pageCounterText.getParent().setVisible(true);

        reviewGrid.setAlignment(Pos.TOP_LEFT);

        for (int i = 0; i < end - start; i++) {
            reviewService.setCurrentReview(allReviews.get(start + i));

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/reviewDisplay.fxml"));

                Parent parent = fxmlLoader.load();

                RowConstraints rowConstraints = new RowConstraints();
                rowConstraints.setMinHeight(200);
                rowConstraints.setPrefHeight(200);
                reviewGrid.getRowConstraints().add(rowConstraints);


                reviewGrid.add(parent, 0, i);

            } catch (IOException e) {
                e.printStackTrace();
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
