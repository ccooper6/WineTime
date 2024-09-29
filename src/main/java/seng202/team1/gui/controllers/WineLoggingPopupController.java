package seng202.team1.gui.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import static java.sql.Types.NULL;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import seng202.team1.gui.FXWrapper;
import seng202.team1.models.User;
import seng202.team1.models.Review;
import seng202.team1.models.Wine;
import seng202.team1.repository.DAOs.UserDAO;
import seng202.team1.services.WineLoggingPopupService;

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

    private ArrayList<CheckBox> tagCheckBoxArray;
    private ArrayList<String> tagNameArray;
    private Wine currentWine;
    private WineLoggingPopupService wineLoggingPopupService;

    /**
     * Sets the functionality of the various GUI elements for the wine logging popup.
     */
    public void initialize() {
        tagCheckBoxArray = new ArrayList<>();
        tagNameArray = new ArrayList<>();
        currentWine = FXWrapper.getInstance().getNavigationController().getWine();
        wineLoggingPopupService = new WineLoggingPopupService();
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

        Review existingReview = wineLoggingPopupService.getReview(User.getCurrentUser().getId(), currentWine.getWineId());
        if (existingReview != null) {
            promptText.setText("Edit your review");
            populateReviewData(existingReview);
        } else {
            promptText.setText("Review this wine!");
        }
    }

    /**
     * Populates the review data of the current wine into the wine logging popup.
     * Called if the user has already previously reviewed the wine.
     * @param review The review object obtained from {@link WineLoggingPopupService#getReview(int, int)}
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
            tagCheckBoxArray.add(new CheckBox(wine.getVintage() + " Vintage"));
            tagNameArray.add(Integer.toString(wine.getVintage()));
        }
        if (wine.getCountry() != null) {
            tagCheckBoxArray.add(new CheckBox(wine.getCountry() + " Country"));
            tagNameArray.add(wine.getCountry());
        }
        if (wine.getProvince() != null) {
            tagCheckBoxArray.add(new CheckBox(wine.getProvince() + " province"));
            tagNameArray.add(wine.getProvince());
        }
        if (wine.getRegion1() != null) {
            tagCheckBoxArray.add(new CheckBox(wine.getRegion1() + " region"));
            tagNameArray.add(wine.getRegion1());
        }
        if (wine.getRegion2() != null) {
            tagCheckBoxArray.add(new CheckBox(wine.getRegion2() + " region"));
            tagNameArray.add(wine.getRegion2());
        }
        if (wine.getVariety() != null) {
            tagCheckBoxArray.add(new CheckBox(wine.getVariety()));
            tagNameArray.add(wine.getVariety());
        }
        if (wine.getWinery() != null) {
            tagCheckBoxArray.add(new CheckBox(wine.getWinery() + " winery"));
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
        int maxLength = 120;
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
                likingText.setText("Which of the following parts of the wine did you dislike? (Optional)");
            } else {
                likingText.setText("Which of the following parts of the wine did you like? (Optional)");
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
    private void submitLog() {
        int newRating = (int) ratingSlider.getValue();
        boolean noneSelected = false;
        ArrayList<String> selectedTags = new ArrayList<>();
        if (hasClickedTag()) {
            for (int i = 0; i < tagNameArray.size(); i++) {
                if (tagCheckBoxArray.get(i).isSelected()) {
                    selectedTags.add(tagNameArray.get(i));
                }
            }
        } else {
            selectedTags = tagNameArray;
            noneSelected = true;
        }

        Review existingReview = wineLoggingPopupService.getReview(User.getCurrentUser().getId(), currentWine.getWineId());

        ArrayList<String> existingTags;
        if (existingReview != null) {
            existingTags = existingReview.getTagsSelected();
        } else {
            existingTags = new ArrayList<>();
        }

        int oldRating;
        if (existingReview != null) {
            oldRating = existingReview.getRating();
        } else {
            oldRating = 0;
        }

        ArrayList<String> tagsToAdd = new ArrayList<>(selectedTags);
        tagsToAdd.removeAll(existingTags);

        ArrayList<String> tagsToRemove = new ArrayList<>(existingTags);
        tagsToRemove.removeAll(selectedTags);

        wineLoggingPopupService.updateTagLikes(User.getCurrentUser().getId(), tagsToAdd, tagsToRemove, existingTags, newRating, oldRating);


        wineLoggingPopupService.submitLog(newRating, User.getCurrentUser().getId(), currentWine.getWineId(), selectedTags, noneSelected, descriptionTextArea.getText());
        returnToWinePopUp();
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
