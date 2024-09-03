package seng202.team0.gui;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import seng202.team0.models.User;
import seng202.team0.services.UserLoginService;

/**
 * Controller class to look after the login.fxml page.
 * @author Isaac Macdonald, Caleb Cooper
 */
public class LoginController {
    @FXML
    Text usernameText;
    @FXML
    TextField userNameTextField;
    @FXML
    Text passwordText;
    @FXML
    PasswordField passwordField;
    @FXML
    FontAwesomeIconView passwordVisibilityToggle;
    @FXML
    TextField visiblePasswordTextField;
    @FXML
    Button registerButton;
    @FXML
    Button logInButton;
    @FXML
    Text errorText;
    @FXML
    Text nameText;
    @FXML
    TextField nameTextField;
    @FXML
    Text confirmPasswordText;
    @FXML
    PasswordField confirmPasswordField;
    @FXML
    Button goBackButton;
    @FXML
    Button createUserButton;

    @FXML
    public void initialize() {
        userNameTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (nameTextField.isVisible()) {
                    onCreateUserPressed();
                } else {
                    onLoginPressed();
                }
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

        confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(passwordField.getText())) {
                passwordField.setStyle("-fx-border-color: GREEN");
                confirmPasswordField.setStyle("-fx-border-color: GREEN");
                createUserButton.setDisable(false);
            } else {
                passwordField.setStyle("-fx-border-color: RED");
                confirmPasswordField.setStyle("-fx-border-color: RED");
                createUserButton.setDisable(true);
            }
        });

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (confirmPasswordField.isVisible()) {
                if (newValue.equals(confirmPasswordField.getText())) {
                    passwordField.setStyle("-fx-border-color: GREEN");
                    confirmPasswordField.setStyle("-fx-border-color: GREEN");
                    createUserButton.setDisable(false);
                } else {
                    passwordField.setStyle("-fx-border-color: RED");
                    confirmPasswordField.setStyle("-fx-border-color: RED");
                    createUserButton.setDisable(true);
                }
            }
        });

        visiblePasswordTextField.textProperty().bindBidirectional(passwordField.textProperty());
        visiblePasswordTextField.setVisible(false);
    }

    /**
     * Very simple method to handle when the login button is pressed. Validates the user account using the inputs
     * from the username and password text fields.
     */
    @FXML
    public void onLoginPressed() {
        clearErrors();
        String username = userNameTextField.getText().toLowerCase();
        String password = passwordField.getText();
        UserLoginService userLoginService = new UserLoginService();
        if (userLoginService.validateAccount(username, password)
                && !username.isEmpty() && !password.isEmpty()
                && username.matches(".*[a-zA-Z0-9]+.*") && password.matches(".*[a-zA-Z0-9]+.*")) {
            User user = new User(userLoginService.getName(username), userLoginService.getEncryptedName(username));
            FXWrapper.getInstance().setCurrentUser(user);
            FXWrapper.getInstance().launchSubPage("mainpage");
        } else {
            errorText.setText("Invalid username or password, please try again");
            clearFields();
            setErrorFieldBorder();
        }
    }

    /**
     * Very simple method to handle when the register button is pressed. Creates a new user account using the inputs
     * from the username and password text fields.
     */
    @FXML
    public void onRegisterPressed() {
        clearErrors();
        toggleShowCreateAccount();
    }

    /**
     * . Method to register a new user account.
     */
    @FXML
    public void onCreateUserPressed() {
        String username = userNameTextField.getText().toLowerCase();
        String password = passwordField.getText();
        String name = nameTextField.getText();
        clearErrors();
        errorText.setTranslateY(130);
        UserLoginService userLoginService = new UserLoginService();
        if (!username.isEmpty() && !password.isEmpty() && username.matches(".*[a-zA-Z0-9]+.*")
                && password.matches(".*[a-zA-Z0-9]+.*") && !name.isEmpty()) {
            int outcome = userLoginService.createAccount(name, username, password);

            if (outcome == 1) {
                User user = new User(name, username);
                FXWrapper.getInstance().setCurrentUser(user);
                FXWrapper.getInstance().launchSubPage("mainpage");
            } else {
                clearFields();
                accountCreatedSuccessfully(outcome);
            }
        } else if (name.isEmpty()) {
            errorText.setText("Please enter a name and try again");
            setErrorFieldBorder(nameTextField);
        } else if (username.isEmpty() || !username.matches(".*[a-zA-Z0-9]+.*")) {
            errorText.setText("Please enter a valid username and try again");
            setErrorFieldBorder(userNameTextField);
        } else if (password.isEmpty() || !password.matches(".*[a-zA-Z0-9]+.*")) {
            errorText.setText("Please enter a valid password and try again");
            setErrorFieldBorder(passwordField);
        } else {
            errorText.setText("Invalid username or password, please try again");
            clearFields();
            setErrorFieldBorder();
        }
    }

    @FXML
    public void onGoBackPressed() {
        FXWrapper.getInstance().launchPage("login");
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

    private void setErrorFieldBorder(TextField textField) {
        textField.setStyle("-fx-border-color: RED");
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
        } else {
            errorText.setText("An error has occurred, try again.");
            setErrorFieldBorder();
            setErrorFieldBorder(nameTextField);
        }

    }

    private void toggleShowCreateAccount() {
        nameText.setVisible(true);
        nameTextField.setVisible(true);
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
    }
}
