package view.messages;

import javafx.scene.control.Alert;

public enum SignUpAndLoginMessage {
    USER_EXISTS("User already exists", Alert.AlertType.ERROR),
    INVALID_INPUT("Fill the fields!", Alert.AlertType.ERROR),
    NO_USER_FOUND("User not found!", Alert.AlertType.ERROR),
    WRONG_PASSWORD_FORMAT("wrong password format!",Alert.AlertType.ERROR),
    SUCCESSFUL_SIGN_UP("Sign Up Successful", Alert.AlertType.INFORMATION),
    WRONG_PASSWORD("Password and Username dont match", Alert.AlertType.ERROR),
    SUCCESSFUL_LOGIN("", null);

    private final String label;
    private final Alert.AlertType alertType;

    SignUpAndLoginMessage(String label, Alert.AlertType alertType) {
        this.label = label;
        this.alertType = alertType;
    }

    public Alert.AlertType getAlertType() {
        return alertType;
    }

    public String getLabel() {
        return label;
    }
}

