package controller;

import com.google.gson.Gson;
import model.User;
import view.messages.ProfileMenuMessage;
import view.messages.SignUpAndLoginMessage;

import java.io.File;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileMenuController {
    private static ProfileMenuController instance = null;
    private static final String userDirectoryPath = System.getProperty("user.dir") +
            File.separator + "database" + File.separator + "users";

    private ProfileMenuController() {

    }

    public static ProfileMenuController getInstance() {
        if (instance == null)
            instance = new ProfileMenuController();
        return instance;
    }

    public ProfileMenuMessage changeUsername(String newUsername) {
        if (newUsername.equals(""))
            return ProfileMenuMessage.INVALID_INPUT;
        if (User.getUserByUsername(newUsername) != null)
            return ProfileMenuMessage.USERNAME_TAKEN;
        User user = MainMenuController.getInstance().getLoggedInUser();
        String previousUsername = user.getUsername();
        File file = new File(Paths.get("database" + File.separator + "users" + File.separator + previousUsername + ".json").toAbsolutePath().toString());
        if (file.delete()) {
            System.out.println("done");
        } else System.out.println("failed");
        user.changeUsername(newUsername);
        User.jsonUsers(user);
        return ProfileMenuMessage.CHANGED_SUCCESSFULLY;
    }

    public ProfileMenuMessage changePassword(String newPassword) {
        if (newPassword.equals(""))
            return ProfileMenuMessage.INVALID_INPUT;
        Matcher passwordMatcher = getCommandMatcher
                ("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])\\S{8,}$", newPassword);
        if (!passwordMatcher.matches())
            return ProfileMenuMessage.WRONG_PASSWORD_FORMAT;
        User user = MainMenuController.getInstance().getLoggedInUser();
        user.changePassword(newPassword);
        User.jsonUsers(MainMenuController.getInstance().getLoggedInUser());
        return ProfileMenuMessage.PASSWORD_CHANGED;
    }

    public void logout() {
        MainMenuController.getInstance().setLoggedInUser(null);
    }

    public static Matcher getCommandMatcher(String patternInput, String input) {
        Pattern pattern = Pattern.compile(patternInput);
        Matcher matcher = pattern.matcher(input);
        return matcher;
    }
}