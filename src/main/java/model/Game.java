package model;

public class Game {
    private String username;
    private int score = 0;
    private int snakeHealth;
    private double gameSpeed;

    public Game(String username, int snakeHealth, double gameSpeed) {
        this.username = username;
        this.snakeHealth = snakeHealth;
        this.gameSpeed = gameSpeed;
    }

    public void addScoreOfFruit(int increment) {
        score += increment;
    }

    public int getScore() {
        return score;
    }

    public String getUsername() {
        return username;
    }

    public int getSnakeHealth() {
        return snakeHealth;
    }

    public double getGameSpeed() {
        return gameSpeed;
    }

    public void decreaseSnakeHealth() {
        snakeHealth -= 1;
    }
}
