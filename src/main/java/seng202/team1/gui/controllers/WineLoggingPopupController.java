package seng202.team1.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.FXWrapper;
import seng202.team1.models.Review;
import seng202.team1.models.User;
import seng202.team1.models.Wine;
import seng202.team1.services.ReviewService;

import java.util.ArrayList;

import static java.sql.Types.NULL;

/**
 * The controller class for the wine logging popup. Called by {@link PopUpController#loadWineLoggingPopUp()} when the
 * log wine button is pressed.
 *
 * @author Wen Sheng Thong, Caleb Cooper
 */
public class WineLoggingPopupController {
    @FXML
    private Button popUpCloseButton;
    @FXML
    private Label characterRemainingLabel;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private Slider ratingSlider;
    @FXML
    private Button submitLogButton;
    @FXML
    private FlowPane tagFlowPane;
    @FXML
    private Label likingText;
    @FXML
    private Text promptText;
    @FXML
    private Button deleteReviewButton;

    private ArrayList<CheckBox> tagCheckBoxArray;
    private ArrayList<String> tagNameArray;
    private Wine currentWine;
    private ReviewService reviewService;
    private final Logger LOG = LogManager.getLogger(WineLoggingPopupController.class);
    private final NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
    private Review existingReview;

    /**
     * Sets the functionality of the various GUI elements for the wine logging popup.
     */
    public void initialize() {
        deleteReviewButton.setOpacity(0);
        deleteReviewButton.setDisable(true);
        likingText.setTextFill(Color.GREEN);
        tagCheckBoxArray = new ArrayList<>();
        tagNameArray = new ArrayList<>();
        currentWine = FXWrapper.getInstance().getNavigationController().getWine();
        reviewService = new ReviewService();
        implementFxmlFunction();
    }

    /**
     * Calls all the function that adds functionality to the various fxml components upon initialization.
     * Calls {@link WineLoggingPopupController#addTagCheckBoxes(Wine)}, {@link WineLoggingPopupController#addDescCharLimit()},
     * {@link WineLoggingPopupController#submitLog()} and {@link WineLoggingPopupController#monitorRating()}
     * <p></p>
     * Also checks if the user has already reviewed the wine and calls {@link WineLoggingPopupController#populateReviewData(Review)}
     */
    private void implementFxmlFunction() {
        addTagCheckBoxes(currentWine);
        addDescCharLimit();
        submitLogButton.setOnAction(actionEvent -> submitLog());
        monitorRating();

        existingReview = reviewService.getReview(User.getCurrentUser().getId(), currentWine.getWineId());
        if (existingReview != null) {
            deleteReviewButton.setDisable(false);
            deleteReviewButton.setOpacity(1);
            promptText.setText("Edit your review");
            populateReviewData(existingReview);
        } else {
            promptText.setText("Review this wine!");
        }
    }

    /**
     * Deletes the review that is being edited
     */
    public void onDeleteReviewPushed() {
        LOG.info("Deleting review with ID {}", existingReview);
        reviewService.deleteReview(existingReview);
        returnToWinePopUp();
    }

    /**
     * Populates the review data of the current wine into the wine logging popup.
     * Called if the user has already previously reviewed the wine.
     * @param review The review object obtained from {@link ReviewService#getReview(int, int)}
     */
    private void populateReviewData(Review review) {
        ratingSlider.setValue(review.getRating());
        descriptionTextArea.setText(review.getReviewDescription());

        ArrayList<String> likedTags = review.getTagsSelected();
        for (int i = 0; i < tagNameArray.size(); i++) {
            if (likedTags.contains(tagNameArray.get(i))) {
                tagCheckBoxArray.get(i).setSelected(true);
            }
        }
    }

    /**
     * Takes in a wine object and adds the wine's tags as {@link CheckBox} options to
     * {@link WineLoggingPopupController#tagCheckBoxArray} to be then added as children to the
     *{@link WineLoggingPopupController#tagFlowPane}. Called upon initialization.
     * @param wine The wine object obtained from {@link NavigationController#getWine()}
     */
    private void addTagCheckBoxes(Wine wine) {
        if (wine.getVintage() != NULL) {
            CheckBox vintageCheckBox = new CheckBox(wine.getVintage() + " Vintage");
            vintageCheckBox.setFont(Font.font("Noto Serif"));
            tagCheckBoxArray.add(vintageCheckBox);
            tagNameArray.add(Integer.toString(wine.getVintage()));
        }
        if (wine.getCountry() != null) {
            CheckBox countryCheckBox = new CheckBox(wine.getCountry() + " Country");
            countryCheckBox.setFont(Font.font("Noto Serif"));
            tagCheckBoxArray.add(countryCheckBox);
            tagNameArray.add(wine.getCountry());
        }
        if (wine.getProvince() != null) {
            CheckBox provinceCheckBox = new CheckBox(wine.getProvince() + " province");
            provinceCheckBox.setFont(Font.font("Noto Serif"));
            tagCheckBoxArray.add(provinceCheckBox);
            tagNameArray.add(wine.getProvince());
        }
        if (wine.getRegion1() != null) {
            CheckBox region1CheckBox = new CheckBox(wine.getRegion1() + " region");
            region1CheckBox.setFont(Font.font("Noto Serif"));
            tagCheckBoxArray.add(region1CheckBox);
            tagNameArray.add(wine.getRegion1());
        }
        if (wine.getRegion2() != null) {
            CheckBox region2CheckBox = new CheckBox(wine.getRegion2() + " region");
            region2CheckBox.setFont(Font.font("Noto Serif"));
            tagCheckBoxArray.add(region2CheckBox);
            tagNameArray.add(wine.getRegion2());
        }
        if (wine.getVariety() != null) {
            CheckBox varietyCheckBox = new CheckBox(wine.getVariety());
            varietyCheckBox.setFont(Font.font("Noto Serif"));
            tagCheckBoxArray.add(varietyCheckBox);
            tagNameArray.add(wine.getVariety());
        }
        if (wine.getWinery() != null) {
            CheckBox wineryCheckBox = new CheckBox(wine.getWinery() + " winery");
            wineryCheckBox.setFont(Font.font("Noto Serif"));
            tagCheckBoxArray.add(wineryCheckBox);
            tagNameArray.add(wine.getWinery());
        }
        for (CheckBox checkbox : tagCheckBoxArray) {
            tagFlowPane.getChildren().add(checkbox);
        }
    }

    /**
     * Adds the character limit to the {@link WineLoggingPopupController#descriptionTextArea} as well as make sure the
     * {@link WineLoggingPopupController#characterRemainingLabel} properly reflects the number of characters remaining.
     */
    private void addDescCharLimit() {
        int maxLength = 160;
        descriptionTextArea.setWrapText(true);
        descriptionTextArea.textProperty().addListener((observableValue, oldValue, newValue) -> {
            String string = "";
            int textLength = descriptionTextArea.getText().length();
            if (textLength <= 20) {
                string = "";
            } else if (textLength <= 110) {
                string = "  ";
            } else {
                string = "    ";
            }
            characterRemainingLabel.setText(string + (maxLength - textLength) + " characters remaining");
            if (textLength > maxLength) {
                descriptionTextArea.setText(descriptionTextArea.getText().substring(0, maxLength));
            }
        });
    }

    /**
     * Ensures that {@link WineLoggingPopupController#likingText} changes according to the value of
     * {@link WineLoggingPopupController#ratingSlider}.
     */
    private void monitorRating() {
        ratingSlider.valueProperty().addListener((observableValue, number, t1) -> {
            if (ratingSlider.getValue() < 3) {
                likingText.setText(" dislike?");
                likingText.setTextFill(Color.RED);
            } else {
                likingText.setText(" like?");
                likingText.setTextFill(Color.GREEN);
            }
        });
    }

    /**
     * Submits the liked tags and review to the database. It then calls
     * {@link WineLoggingPopupController#returnToWinePopUp()} to return to the wine pop up screen
     * <p></p>
     * If no tags have been selected, it will add all the tags to the 'Likes' table. A rating of 1-2 will add a negative
     * value to the tag, whilst a 4-5 will add a positive value to the tag.
     * <p></p>
     * Also updates the likes of the tags in the database depending on whether the user has changed their rating or
     * selected different tags.
     */
    private void submitLog() { // TODO: Need to clean up method, do we really need both tags selected and liked if we track none selected anyways?
        int rating = (int) ratingSlider.getValue();
        int currentUserUid = User.getCurrentUser().getId();
        int currentWineId = currentWine.getWineId();
        String description = descriptionTextArea.getText();
        ArrayList<String> selectedTags = new ArrayList<>();
        ArrayList<String> tagsToLike = new ArrayList<>();
        if (hasClickedTag()) {
            for (int i = 0; i < tagNameArray.size(); i++) {
                if (tagCheckBoxArray.get(i).isSelected()) {
                    selectedTags.add(tagNameArray.get(i));
                    tagsToLike.add(tagNameArray.get(i));
                }
            }
        } else {
            tagsToLike = tagNameArray;
        }
        boolean noneSelected = selectedTags.isEmpty();

        Review existingReview = reviewService.getReview(currentUserUid, currentWineId);
        ArrayList<String> finalTagsToLike = tagsToLike;
        navigationController.executeWithLoadingScreen(() -> {
            if (existingReview != null) {
                ArrayList<String> oldTags = existingReview.getTagsLiked();
                int oldRating = existingReview.getRating();
                reviewService.updateTagLikes(currentUserUid, finalTagsToLike, oldTags, rating, oldRating);
            } else {
                reviewService.updateTagLikes(currentUserUid, finalTagsToLike, new ArrayList<>(), rating, 0);
            }

            reviewService.submitLog(rating, currentUserUid, currentWineId, selectedTags, finalTagsToLike, noneSelected, description);
            returnToWinePopUp();
        });
    }

    /**
     * Returns a boolean value if a tag check box has been selected or not.
     * @return True if a tag check box has been selected, false otherwise.
     */
    private Boolean hasClickedTag() {
        for (CheckBox checkBox : tagCheckBoxArray) {
            if (checkBox.isSelected()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns to the wine pop up screen.
     */
    @FXML
    private void returnToWinePopUp() {
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.closePopUp();
        if (navigationController.getCurrentPage().equals("wineReviews")) {
            navigationController.loadPageContent("wineReviews");
        } else if (navigationController.getCurrentPage().equals("profile")) {
            navigationController.loadPageContent("profile");
        }
        navigationController.initPopUp(currentWine);
    }
}
