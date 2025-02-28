package seng202.team1.gui.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team1.gui.FXWrapper;
import seng202.team1.services.UserLoginService;

import java.util.regex.*;

/**
 * Controller class for user login and register. Handles getting user input and displaying information to application.
 */
public class LoginController {
    @FXML
    private FontAwesomeIconView closeButton;
    @FXML
    private Text usernameText;
    @FXML
    private TextField userNameTextField;
    @FXML
    private Text passwordText;
    @FXML
    private PasswordField passwordField;
    @FXML
    private FontAwesomeIconView passwordVisibilityToggle;
    @FXML
    private TextField visiblePasswordTextField;
    @FXML
    private Button registerButton;
    @FXML
    private Button logInButton;
    @FXML
    private Text errorText;
    @FXML
    private Text nameText;
    @FXML
    private TextField nameTextField;
    @FXML
    private Text passwordInfoText;
    @FXML
    private Text confirmPasswordText;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Button goBackButton;
    @FXML
    private Button createUserButton;

    private final Pattern PASSWORDPATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d\\W]{8,}$");

    private static final Logger LOG = LogManager.getLogger(LoginController.class);

    /**
     * Default constructor for LoginController.
     */
    public LoginController() {}

    /**
     * Initialises the login page by adding observers to text fields.
     */
    @FXML
    public void initialize() {
        // allow enter key to be used to log in/register
        setCheckOnEnterListeners();

        // check on text update if the register button should be enabled
        setupTextFieldListeners();

        bindPasswordFields();

        setupCloseButtonHoverEffect();
    }

    /**
     * Sets up the listeners for the text fields to check if the create user button should be available
     * to click.
     */
    private void setupTextFieldListeners() {
        userNameTextField.textProperty().addListener((observable, oldValue, newValue) -> setRegisterButton());
        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> setRegisterButton());
    }

    /**
     * Binds the password fields to ensure that the values of the two fields are the same.
     * This allows for the show/hide password functionality to work.
     */
    private void bindPasswordFields() {
        visiblePasswordTextField.textProperty().bindBidirectional(passwordField.textProperty());
        visiblePasswordTextField.setVisible(false);
    }

    /**
     * Sets up the hover effect for the close button.
     */
    private void setupCloseButtonHoverEffect() {
        closeButton.setOnMouseEntered(event -> closeButton.setFill(Paint.valueOf("#909090")));
        closeButton.setOnMouseExited(event -> closeButton.setFill(Paint.valueOf("#b0b0b0")));
    }

    /**
     * Sets the listeners for the fields that appear when a user wants to create an
     * account to indicate what info they are required to fill in.
     */
    private void setRegisterFieldListeners() {
        // check passwords match
        confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            checkPassword(newValue, passwordField.getText());
        });

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (confirmPasswordField.isVisible()) {
                checkPassword(newValue, confirmPasswordField.getText());
            }
        });

        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                nameTextField.setStyle("-fx-border-color: RED");
            } else {
                nameTextField.setStyle("-fx-border-color: None");
            }
        });

        userNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                userNameTextField.setStyle("-fx-border-color: RED");
            } else {
                userNameTextField.setStyle("-fx-border-color: None");
            }
        });
    }

    /**
     * Checks to ensure the password the user has entered is accepted and matches the other password field
     * If password is acceptable, highlight both fields green. Otherwise, highlight them red.
     * Then checks to set the register button enabled or disabled
     *
     * @param newValue The new updated value that has just been inputted by the user
     * @param otherValue The value in the other password field
     */
    private void checkPassword(String newValue, String otherValue) {
        if (newValue.equals(otherValue) && !newValue.isEmpty() && PASSWORDPATTERN.matcher(newValue).matches()) {
            passwordField.setStyle("-fx-border-color: GREEN");
            confirmPasswordField.setStyle("-fx-border-color: GREEN");
        } else {
            passwordField.setStyle("-fx-border-color: RED");
            confirmPasswordField.setStyle("-fx-border-color: RED");
        }
        setRegisterButton();
    }

    /**
     * Sets the listeners for the text fields to check if the enter key is pressed.
     */
    private void setCheckOnEnterListeners() {
        userNameTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                // name text field is only visible if on register screen
                if (nameTextField.isVisible()) {
                    onCreateUserPressed();
                } else {
                    onLoginPressed();
                }
            }
            setRegisterButton();
        });

        nameTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onCreateUserPressed();
            } else {
                setRegisterButton();
            }
        });

        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (nameTextField.isVisible()) {
                    onCreateUserPressed();
                } else {
                    onLoginPressed();
                }
            }
        });

        visiblePasswordTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (nameTextField.isVisible()) {
                    onCreateUserPressed();
                } else {
                    onLoginPressed();
                }
            }
        });

        confirmPasswordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onCreateUserPressed();
            }
        });
    }

    /**
     * Checks if register user fields have been filled and controls whether the register button can be clicked or not.
     * Is clickable if no fields are empty and password matches confirm password
     */
    @FXML
    private void setRegisterButton()
    {
        boolean isNameEmpty = nameTextField.getText().trim().isEmpty();
        boolean isUsernameEmpty = userNameTextField.getText().trim().isEmpty();
        boolean isPasswordGood = !PASSWORDPATTERN.matcher(passwordField.getText()).matches();
        boolean doesPasswordMatch = passwordField.getText().equals(confirmPasswordField.getText());

        boolean isRegisterEnabled = !isNameEmpty && !isUsernameEmpty && !isPasswordGood && doesPasswordMatch;

        createUserButton.setDisable(!isRegisterEnabled);
    }

    /**
     * Method that is used to toggle between the password being visible and hidden.
     */
    @FXML
    public void toggleShowPassword() {
        if (passwordField.isVisible()) {
            passwordField.setVisible(false);
            visiblePasswordTextField.setVisible(true);
            visiblePasswordTextField.setManaged(true);
            passwordVisibilityToggle.setGlyphName("EYE");
        } else {
            passwordField.setVisible(true);
            visiblePasswordTextField.setVisible(false);
            visiblePasswordTextField.setManaged(false);
            passwordVisibilityToggle.setGlyphName("EYE_SLASH");
        }
    }

    /**
     * Method that clears the username and password text fields.
     */
    private void clearFields() {
        userNameTextField.clear();
        passwordField.clear(); // Linked to visiblePasswordTextField, so we don't need to clear both
        confirmPasswordField.clear();
        if (!nameTextField.isVisible()) { // Only clear the name field if it is hidden
            nameTextField.clear();
        }
    }

    /**
     * Sets the borders of all the possible text fields to hava a red border to indicate
     * an error.
     */
    private void setErrorFieldBorder() {
        userNameTextField.setStyle("-fx-border-color: RED");
        passwordField.setStyle("-fx-border-color: RED");
        visiblePasswordTextField.setStyle("-fx-border-color: RED");
        if (confirmPasswordField.isVisible()) {
            confirmPasswordField.setStyle("-fx-border-color: RED");
        }
    }

    /**
     * Method that sets the border of a text field to have a red border to indicate an error.
     * @param textField the text field that will have its border set to red
     */
    private void setErrorFieldBorder(TextField textField) {
        textField.setStyle("-fx-border-color: RED");
    }

    /**
     * Resets the borders of the password and confirm password text fields to not have a red border.
     */
    private void resetPasswordBorders() {
        passwordField.setStyle("-fx-border-color: None");
        confirmPasswordField.setStyle("-fx-border-color: None");
    }

    /**
     * Clears the error text, sets the error text to be coloured red and sets the border of the text
     * fields to not have a red border anymore.
     */
    private void clearErrors() {
        errorText.setText("");
        errorText.setFill(Paint.valueOf("Red"));
        userNameTextField.setStyle("-fx-border-color: None");
        passwordField.setStyle("-fx-border-color: None");
        visiblePasswordTextField.setStyle("-fx-border-color: None");
        if (nameTextField.isVisible()) {
            nameTextField.setStyle("-fx-border-color: None");
        }
        if (confirmPasswordField.isVisible()) {
            confirmPasswordField.setStyle("-fx-border-color: None");
        }
    }

    /**
     * Sets the error text message based on the output that is returned from creating a new account in the database.
     * @param outcome the code of what error the sql function has returned to differentiate error messages
     */
    private void accountCreatedSuccessfully(int outcome) {
        if (outcome == 0) {
            errorText.setText("Username already exists.");
            setErrorFieldBorder(userNameTextField);
            resetPasswordBorders();
        } else {
            errorText.setText("An error has occurred, try again.");
            setErrorFieldBorder();
            setErrorFieldBorder(nameTextField);
        }
    }

    /**
     * Method that toggles the visibility of the fields and buttons needed to create account.
     */
    private void toggleShowCreateAccount() {
        nameText.setVisible(true);
        nameTextField.setVisible(true);
        passwordText.setText("Password:");
        passwordInfoText.setVisible(true);
        confirmPasswordText.setVisible(true);
        confirmPasswordField.setVisible(true);
        goBackButton.setVisible(true);
        createUserButton.setVisible(true);
        registerButton.setVisible(false);
        logInButton.setVisible(false);

        if (visiblePasswordTextField.isVisible()) {
            toggleShowPassword();
        }
        passwordVisibilityToggle.setVisible(false);

        usernameText.setTranslateY(62);
        userNameTextField.setTranslateY(62);
        passwordText.setTranslateY(62);
        passwordField.setTranslateY(62);
        visiblePasswordTextField.setTranslateY(62);
        passwordVisibilityToggle.setTranslateY(62);
        nameText.setTranslateY(-124);
        nameTextField.setTranslateY(-124);

        nameTextField.setStyle("-fx-border-color: RED");
        if (userNameTextField.getText().isEmpty()) {
            userNameTextField.setStyle("-fx-border-color: RED");
        }
        passwordField.setStyle("-fx-border-color: RED");
        confirmPasswordField.setStyle("-fx-border-color: RED");

        // Call setRegisterFieldListeners to handle further changes
        setRegisterFieldListeners();
    }

    /**
     * Simple method to handle when the login button is pressed. Validates the user account using the inputs
     * from the username and password text fields.
     */
    @FXML
    public void onLoginPressed() {
        clearErrors();
        String username = userNameTextField.getText().toLowerCase();
        String password = passwordField.getText();
        UserLoginService userLoginService = new UserLoginService();
        if (userLoginService.checkLogin(username, password)
                && !username.isEmpty() && !password.isEmpty()) {
            LOG.info("Logged in as {}", username);
            FXWrapper.getInstance().launchSubPage("mainpage");
        } else {
            errorText.setText("Invalid username or password, please try again");
            clearFields();
            setErrorFieldBorder();
        }
    }

    /**
     * Method used to register a new user account. Contains checks to ensure that the user has entered all the
     * required information and that the password and confirm password fields match.
     */
    @FXML
    public void onCreateUserPressed() {
        clearErrors();
        errorText.setTranslateX(-85);
        errorText.setTranslateY(130);

        UserLoginService userLoginService = new UserLoginService();
        String username = userNameTextField.getText().toLowerCase();
        String password = passwordField.getText();
        String name = nameTextField.getText();
        if (!username.isEmpty() && PASSWORDPATTERN.matcher(password).matches() && !name.isEmpty() && password.equals(confirmPasswordField.getText())) {
            int outcome = userLoginService.storeLogin(name, username, password);
            if (outcome == 1) {
                LOG.info("Account created for user{}", username);
                userLoginService.checkLogin(username, password);
                LOG.info("Logged in as {}", username);
                FXWrapper.getInstance().launchSubPage("mainpage");
            } else {
                accountCreatedSuccessfully(outcome);
            }
        }
        checkPassword(password, confirmPasswordField.getText());
    }

    /**
     * Simple method to handle when the register button is pressed. Creates a new user account using the inputs
     * from the username and password text fields.
     */
    @FXML
    public void onRegisterPressed() {
        clearErrors();
        toggleShowCreateAccount();
    }

    /**
     * Method that is used to go back to the login screen.
     */
    @FXML
    public void onGoBackPressed() {
        FXWrapper.getInstance().launchPage("login");
    }

    /**
     * Method that is called when the close button is pressed. Closes the application.
     */
    @FXML
    public void closeApp() {
        FXWrapper.getInstance().closeApplication();
    }
}
