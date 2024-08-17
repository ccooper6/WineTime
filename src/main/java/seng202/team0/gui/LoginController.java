package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import seng202.team0.services.UserLoginService;

import java.util.Objects;

/**
 * Controller class to look after the login.fxml page
 * @author Isaac Macdonald, Caleb Cooper
 */
public class LoginController {
    @FXML
    TextField userNameTextField;
    @FXML
    TextField passwordTextField;
    @FXML
    Button registerButton;
    @FXML
    Button logInButton;
    @FXML
    Text errorText;

    /**
     * Very simple method to handle when the login button is pressed. Validates the user account using the inputs
     * from the username and password text fields.
     */
    @FXML
    public void onLoginPressed() {
        clearErrors();
        String username = userNameTextField.getText();
        String password = passwordTextField.getText();
        UserLoginService userLoginService = new UserLoginService();
        if (userLoginService.validateAccount(username, password)
                && !username.isEmpty() && !password.isEmpty()
                && username.matches(".*[a-zA-Z0-9]+.*") && password.matches(".*[a-zA-Z0-9]+.*")) {
            FXWrapper.getInstance().launchPage("mainpage");
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
        String password = passwordTextField.getText();
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

    private void clearFields() {
        userNameTextField.clear();
        passwordTextField.clear();
    }

    private void setErrorFieldBorder() {
        userNameTextField.setStyle("-fx-border-color: RED");
        passwordTextField.setStyle("-fx-border-color: RED");
    }

    private void clearErrors() {
        errorText.setText("");
        errorText.setFill(Paint.valueOf("Red"));
        userNameTextField.setStyle("-fx-border-color: None");
        passwordTextField.setStyle("-fx-border-color: None");
    }

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
