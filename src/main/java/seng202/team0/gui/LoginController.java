package seng202.team0.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import seng202.team0.services.UserLoginService;

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

    /**
     * Very simple method to handle when the login button is pressed. Validates the user account using the inputs
     * from the username and password text fields.
     */
    @FXML
    public void onLoginPressed() {
        String username = userNameTextField.getText();
        String password = passwordTextField.getText();
        UserLoginService userLoginService = new UserLoginService();
        userLoginService.validateAccount(username, password);
    }

    /**
     * Very simple method to handle when the register button is pressed. Creates a new user account using the inputs
     * from the username and password text fields.
     */
    @FXML
    public void onRegisterPressed() {
        String username = userNameTextField.getText();
        String password = passwordTextField.getText();
        UserLoginService userLoginService = new UserLoginService();
        System.out.println(userLoginService.createAccount(username, password));
    }

}
