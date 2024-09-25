package seng202.team1.gui.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import seng202.team1.gui.FXWrapper;
import seng202.team1.models.User;
import seng202.team1.models.Wine;
import seng202.team1.repository.DAOs.UserDAO;
import seng202.team1.services.WineLoggingPopupService;

import java.util.ArrayList;

import static java.sql.Types.NULL;

/**
 * The controller class for the wine logging popup. Called by {@link PopUpController#loadWineLoggingPopUp()} when the
 * log wine button is pressed.
 *
 * @author Wen Sheng Thong
 */
public class WineLoggingPopupController {
    @FXML
    public Button popUpCloseButton;
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

    private ArrayList<CheckBox> tagCheckBoxArray;
    private ArrayList<String> tagNameArray;
    private Wine currentWine;
    private WineLoggingPopupService wineLoggingPopupService;

    /**
     * Sets the functionality of the various GUI elements for the wine logging popup.
     */
    public void initialize() {
        tagCheckBoxArray = new ArrayList<CheckBox>();
        tagNameArray = new ArrayList<String>();
        currentWine = FXWrapper.getInstance().getNavigationController().getWine();
        wineLoggingPopupService = new WineLoggingPopupService();
        implementFxmlFunction();
    }

    /**
     * Calls all the function that adds functionality to the various fxml components upon initialization.
     * Calls {@link WineLoggingPopupController#addTagCheckBoxes(Wine)}, {@link WineLoggingPopupController#addDescCharLimit()},
     * {@link WineLoggingPopupController#submitLog()} and {@link WineLoggingPopupController#monitorRating()}
     */
    private void implementFxmlFunction() {
        addTagCheckBoxes(currentWine);
        addDescCharLimit();
        submitLogButton.setOnAction(actionEvent -> { submitLog(); });
        monitorRating();
    }

    /**
     * Takes in a wine object and adds the wine's tags as {@link CheckBox} options to
     * {@link WineLoggingPopupController#tagCheckBoxArray} to be then added as children to the
     *{@link WineLoggingPopupController#tagFlowPane}. Called upon initialization.
     * @param wine The wine object obtained from {@link NavigationController#getWine()}
     */
    private void addTagCheckBoxes(Wine wine) {
        if (wine.getVintage() != NULL) {
            tagCheckBoxArray.add(new CheckBox(Integer.toString(wine.getVintage()) + " Vintage"));
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
        descriptionTextArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                String string = "";
                int textLength = descriptionTextArea.getText().length();
                if (textLength <= 20) {
                    string = "";
                } else if (textLength <= 110) {
                    string = "  ";
                } else {
                    string = "    ";
                }
                characterRemainingLabel.setText(string + Integer.toString(maxLength - textLength) + " characters remaining");
                if (textLength > maxLength) {
                    descriptionTextArea.setText(descriptionTextArea.getText().substring(0, maxLength));
                }
            }
        });
    }

    /**
     * Ensures that {@link WineLoggingPopupController#likingText} changes according to the value of
     * {@link WineLoggingPopupController#ratingSlider}.
     */
    private void monitorRating() {
        ratingSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if (ratingSlider.getValue() < 3) {
                    likingText.setText("Which of the following parts of the wine did you dislike? (Optional)");
                } else {
                    likingText.setText("Which of the following parts of the wine did you like? (Optional)");
                }
            }
        });
    }

    /**
     * Uses {@link WineLoggingPopupService#submitLog(int, int, int, ArrayList, String)}
     * to submit the liked tags and review to the database. It then calls
     * {@link WineLoggingPopupController#returnToWinePopUp()} to return to the wine pop up screen
     * <p></p>
     * If no tags have been selected, it will add all the tags to the 'Likes' table. A rating of 1-2 will add a negative
     * value to the tag, whilst a 4-5 will add a positive value to the tag.
     */
    private void submitLog() {
        int rating = (int) ratingSlider.getValue();
        ArrayList<String> selectedTags = new ArrayList<>();
        if (hasClickedTag()) {
            for (int i = 0; i < tagNameArray.size(); i++) {
                if (tagCheckBoxArray.get(i).isSelected()) {
                    selectedTags.add(tagNameArray.get(i));
                }
            }
        } else {
            selectedTags = tagNameArray;
        }
        wineLoggingPopupService.submitLog(rating, User.getCurrentUser().getId(), currentWine.getWineId(), selectedTags,descriptionTextArea.getText());
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
        navigationController.initPopUp(currentWine);
    }
}
