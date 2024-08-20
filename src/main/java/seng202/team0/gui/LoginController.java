package seng202.team0.gui;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import seng202.team0.services.UserLoginService;

/**
 * Controller class to look after the login.fxml page.
 * @author Isaac Macdonald, Caleb Cooper
 */
public class LoginController {
    @FXML
    TextField userNameTextField;
    @FXML
    PasswordField passwordField;
    @FXML
    Button registerButton;
    @FXML
    Button logInButton;
    @FXML
    Text errorText;
    @FXML
    FontAwesomeIconView passwordVisibilityToggle;
    @FXML
    private TextField visiblePasswordTextField;

    @FXML
    public void initialize() {
        userNameTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onLoginPressed();
            }
        });

        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onLoginPressed();
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
        String username = userNameTextField.getText();
        String password = passwordField.getText();
        UserLoginService userLoginService = new UserLoginService();
        if (userLoginService.validateAccount(username, password)
                && !username.isEmpty() && !password.isEmpty()
                && username.matches(".*[a-zA-Z0-9]+.*") && password.matches(".*[a-zA-Z0-9]+.*")) {
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
        String username = userNameTextField.getText();
        String password = passwordField.getText();
        UserLoginService userLoginService = new UserLoginService();
        if (!username.isEmpty() && !password.isEmpty() && username.matches(".*[a-zA-Z0-9]+.*")
                && password.matches(".*[a-zA-Z0-9]+.*")) {
            int outcome = userLoginService.createAccount(username, password);
            clearFields();
            accountCreatedSuccessfully(outcome);
        } else {
            errorText.setText("Invalid username or password, please try again");
            clearFields();
            setErrorFieldBorder();
        }
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
        passwordField.clear();
    }

    /**
     * Sets the borders of all the possible text fields to hava a red border to indicate
     * an error.
     */
    private void setErrorFieldBorder() {
        userNameTextField.setStyle("-fx-border-color: RED");
        passwordField.setStyle("-fx-border-color: RED");
        visiblePasswordTextField.setStyle("-fx-border-color: RED");
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
    }

    /**
     * Sets the error text message based on the output that is returned from creating a new account in the database.
     * @param outcome the code of what error the sql function has returned to differentiate error messages
     */
    private void accountCreatedSuccessfully(int outcome) {
        switch (outcome) {
            case 0:
                errorText.setText("Username already exists.");
                break;
            case 1:
                errorText.setText("Account created!");
                errorText.setFill(Paint.valueOf("Green"));
                break;
            default:
                errorText.setText("An error has occurred, try again.");
        }

    }

}
