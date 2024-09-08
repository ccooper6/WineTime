package seng202.team0.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import seng202.team0.models.User;
import seng202.team0.models.Wine;
import seng202.team0.repository.DatabaseManager;
import seng202.team0.repository.LogWineDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static java.sql.Types.NULL;

/**
 * The controller class for the wine logging popup. Called by {@link PopUpController#loadWineLoggingPopUp()} when the
 * log wine button is pressed.
 */
public class WineLoggingPopupController {
    /**
     * The label which shows how many characters are left in the character limit of the description text area
     */
    @FXML
    private Label characterRemainingLabel;
    /**
     * The text area where the user inputs the description of the wine
     */
    @FXML
    private TextArea descriptionTextArea;
    /**
     * The slider which determines the rating based off a int value of 1-5
     */
    @FXML
    private Slider ratingSlider;
    /**
     * The submit log buttons which calls {@link WineLoggingPopupController#submitLog()} when pressed
     */
    @FXML
    private Button submitLogButton;
    /**
     * The flow pane which is used to contain the tag check boxes
     */
    @FXML
    private FlowPane tagFlowPane;
    /**
     * The label which displays the text prompt for the tag flow pane
     */
    @FXML
    private Label likingText;
    /**
     * An array containing the tag checkboxes. Populated upon initialization
     */
    private ArrayList<CheckBox> tagCheckBoxArray;
    /**
     * An array containing the actual tag names. Populated along side {@link WineLoggingPopupController#tagCheckBoxArray}
     */
    private ArrayList<String> tagNameArray;
    /**
     * The {@link DatabaseManager} to handle connections to the database
     */
    private DatabaseManager databaseManager;
    /**
     * The current wine being logged
     */
    private Wine currentWine;
    /**
     * The int uid of the current user
     */
    private int currentUserUid;
    /**
     * A {@link LogWineDao} to handle reviews and likes entries to the database
     */
    private LogWineDao logWineDao;

    public void initialize() {
        tagCheckBoxArray = new ArrayList<CheckBox>();
        tagNameArray = new ArrayList<String>();
        databaseManager = DatabaseManager.getInstance();
        currentWine = FXWrapper.getInstance().getNavigationController().getWine();
        currentUserUid = getUId(FXWrapper.getInstance().getCurrentUser());
        logWineDao = new LogWineDao();
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
        if (wine.getVintage() != NULL) { //TODO double confirm the default values of empty wine tag values
            tagCheckBoxArray.add(new CheckBox(Integer.toString(wine.getVintage()) + " Vintage"));
            tagNameArray.add(Integer.toString(wine.getVintage()));
        }
        if (!wine.getProvince().isBlank()) {
            tagCheckBoxArray.add(new CheckBox(wine.getProvince() + " province"));
            tagNameArray.add(wine.getProvince());
        }
        if (!wine.getRegion1().isBlank()) {
            tagCheckBoxArray.add(new CheckBox(wine.getRegion1() + " region"));
            tagNameArray.add(wine.getRegion1());
        }
        if (!wine.getRegion2().isBlank()) {
            tagCheckBoxArray.add(new CheckBox(wine.getRegion2() + " region"));
            tagNameArray.add(wine.getRegion2());
        }
        if (!wine.getVariety().isBlank()) {
            tagCheckBoxArray.add(new CheckBox(wine.getVariety()));
            tagNameArray.add(wine.getVariety());
        }
        if (!wine.getWinery().isBlank()) {
            tagCheckBoxArray.add(new CheckBox(wine.getWinery() + " winery"));
            tagNameArray.add(wine.getWinery());
        }
        for (CheckBox checkbox : tagCheckBoxArray) {
            tagFlowPane.getChildren().add(checkbox);
        }
    }

    /**
     * Adds the character limit to the {@link WineLoggingPopupController#descriptionTextArea} as well as make sure the
     * {@link WineLoggingPopupController#characterRemainingLabel} properly reflects the number of characters remaining
     */
    private void addDescCharLimit() {
        int maxLength = 120;
        descriptionTextArea.setWrapText(true);
        descriptionTextArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                String s = "";
                int textLength = descriptionTextArea.getText().length();
                if (textLength <= 20) {
                    s = "";
                } else if (textLength <= 110) {
                    s = "  ";
                } else {
                    s = "    ";
                }
                characterRemainingLabel.setText(s + Integer.toString(maxLength - textLength) + " characters remaining");
                if (textLength > maxLength) {
                    descriptionTextArea.setText(descriptionTextArea.getText().substring(0, maxLength));
                }
            }
        });
    }

    /**
     * Ensures that {@link WineLoggingPopupController#likingText} changes according to the value of
     * {@link WineLoggingPopupController#ratingSlider}
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
     * Uses {@link LogWineDao} to submit the liked tags and review to the database. It then calls
     * {@link WineLoggingPopupController#returnToWinePopUp()} to return to the wine pop up screen
     * <p></p>
     * If no tags have been selected, it will add all the tags to the 'Likes' table. A rating of 1-2 will add a negative
     * value to the tag, whilst a 4-5 will add a positive value to the tag.
     */
    private void submitLog() {
        int rating = (int) ratingSlider.getValue();
        if (hasClickedTag()) {
            ArrayList<String> selectedTags = new ArrayList<String>() ;
            for (int i = 0; i < tagNameArray.size(); i++) {
                if (tagCheckBoxArray.get(i).isSelected()) {
                    selectedTags.add(tagNameArray.get(i));
                }
            }
            for (String tag : selectedTags) {
                logWineDao.likes(currentUserUid, tag, rating - 3);
            }
        } else {
            for (String tag : tagNameArray) {
                logWineDao.likes(currentUserUid, tag, rating - 3);
            }
        }
        if (!descriptionTextArea.getText().isBlank()) {
            String desc = descriptionTextArea.getText().replaceAll("\\s+", " ");
            logWineDao.reviews(currentUserUid, currentWine.getWineId(), rating, desc, getCurrentTimeStamp());
        } else {
            logWineDao.reviews(currentUserUid, currentWine.getWineId(), rating, "", getCurrentTimeStamp());
        }
        returnToWinePopUp();
    }

    /**
     * Returns a boolean value if a tag check box has been selected or not
     * @return
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
     * Returns the int user id of the current user. Called during initialization
     * @param currentUser the current user
     * @return int uid
     */
    private int getUId(User currentUser) {
        int uid = 0;
        String uidSql = "SELECT id FROM user WHERE username = ? AND name = ?";
        try (Connection conn = databaseManager.connect()) {
            try (PreparedStatement uidPs = conn.prepareStatement(uidSql)) {
                uidPs.setString(1, currentUser.getEncryptedUserName());
                uidPs.setString(2, currentUser.getName());
                uid = uidPs.executeQuery().getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return uid;
    }

    /**
     * Called by {@link WineLoggingPopupController#submitLog()} to obtain the date time stamp of the review in
     * "YYYY-MM-DD HH:mm:SS" format
     * @return the string date time stamp in "YYYY-MM-DD HH:mm:SS" format
     */
    private String getCurrentTimeStamp() {
        return ZonedDateTime.now( ZoneId.systemDefault() ).format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss"));
    }

    /**
     * Returns to the wine pop up screen.
     */
    private void returnToWinePopUp() {
        NavigationController navigationController = FXWrapper.getInstance().getNavigationController();
        navigationController.closePopUp();
        navigationController.initPopUp(currentWine);
    }

}
