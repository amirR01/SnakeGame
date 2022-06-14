package model;

import controller.MainMenuController;

public class GameSettings {
    private int snakeHealth;
    private double gameSpeed;

    public GameSettings() {
        this.snakeHealth = 5;
        this.gameSpeed = 0.2;
    }

    public double getSnakeHealth() {
        return snakeHealth;
    }

    public double getGameSpeed() {
        return gameSpeed;
    }

    public void setGameSpeed(double gameSpeed) {
        this.gameSpeed = gameSpeed;
    }

    public void setSnakeHealth(int snakeHealth) {
        this.snakeHealth = snakeHealth;
        User.jsonUsers(MainMenuController.getInstance().getLoggedInUser());
    }
}
