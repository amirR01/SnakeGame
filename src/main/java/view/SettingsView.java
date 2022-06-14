package view;

import controller.MainMenuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SettingsView {
    public ProgressBar snakeHealth;
    public Label healthLabel;
    public Label speedLabel;

    @FXML
    public void initialize() {
        snakeHealth.setStyle("-fx-accent: lightgreen;");
        snakeHealth.setProgress(MainMenuController.getInstance().getLoggedInUser().getSettings().getSnakeHealth() / 5);
        healthLabel.setText("Snake Health : " + MainMenuController.getInstance().getLoggedInUser().getSettings().getSnakeHealth());
        speedLabel.setText("Game Speed: ");

    }

    public void decreaseHealth(ActionEvent actionEvent) {
        int health = (int) (snakeHealth.getProgress() * 5);
        if (health == 2)
            return;
        health -= 1;
        snakeHealth.setProgress((double) health / 5);
        MainMenuController.getInstance().getLoggedInUser().getSettings().setSnakeHealth(health);
        healthLabel.setText("Snake Health : " + MainMenuController.getInstance().getLoggedInUser().getSettings().getSnakeHealth());
    }

    public void increaseHealth(ActionEvent actionEvent) {
        int health = (int) (snakeHealth.getProgress() * 5);
        if (health == 5)
            return;
        health += 1;
        snakeHealth.setProgress((double) health / 5);
        MainMenuController.getInstance().getLoggedInUser().getSettings().setSnakeHealth(health);
        healthLabel.setText("Snake Health : " + MainMenuController.getInstance().getLoggedInUser().getSettings().getSnakeHealth());
    }

    public void backToMain(ActionEvent actionEvent) throws IOException {
        Parent url = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/MainMenu.fxml")));
        Scene scene = new Scene(url);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void makeSpeedFast(ActionEvent actionEvent) {
        speedLabel.setText("Game Speed: FAST");
        MainMenuController.getInstance().getLoggedInUser().getSettings().setGameSpeed(0.08);
    }

    public void makeSpeedNormal(ActionEvent actionEvent) {
        speedLabel.setText("Game Speed: NORMAL");
        MainMenuController.getInstance().getLoggedInUser().getSettings().setGameSpeed(0.2);
    }

    public void makeSpeedSlow(ActionEvent actionEvent) {
        speedLabel.setText("Game Speed: SLOW");
        MainMenuController.getInstance().getLoggedInUser().getSettings().setGameSpeed(0.5);
    }
}
