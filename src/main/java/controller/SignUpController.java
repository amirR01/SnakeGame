package controller;

import com.google.gson.Gson;
import model.User;
import view.messages.SignUpAndLoginMessage;

import java.io.File;
import java.io.FileWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpController {
    private static SignUpController instance = null;

    private SignUpController() {

    }

    public static SignUpController getInstance() {
        if (instance == null)
            instance = new SignUpController();
        return instance;
    }

    public SignUpAndLoginMessage signUpUser(String username, String password) {
        if (username.equals("") || password.equals(""))
            return SignUpAndLoginMessage.INVALID_INPUT;
        if (User.getUserByUsername(username) != null)
            return SignUpAndLoginMessage.USER_EXISTS;
        Matcher passwordMatcher = getCommandMatcher
                ("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])\\S{8,}$", password);
        if (!passwordMatcher.matches())
            return SignUpAndLoginMessage.WRONG_PASSWORD_FORMAT;
        User.jsonUsers(new User(username, password));
        return SignUpAndLoginMessage.SUCCESSFUL_SIGN_UP;
    }

    public static Matcher getCommandMatcher(String patternInput, String input) {
        Pattern pattern = Pattern.compile(patternInput);
        Matcher matcher = pattern.matcher(input);
        return matcher;
    }


}
