package view.game;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Cell;
import model.Game;
import model.User;

import java.io.IOException;
import java.util.*;


// learning source : https://www.youtube.com/watch?v=AYeFmhQALqM
public class GameController {
    // Image importing
    private final Image snakeHeadRight = new
            Image(Objects.requireNonNull(getClass().getResource("/pics/snakeHeadRight.png")).toString());
    private final ImageView snakeHeadRightView = new ImageView(snakeHeadRight);
    private final Image snakeHeadLeft = new
            Image(Objects.requireNonNull(getClass().getResource("/pics/snakeHeadLeft.png")).toString());
    private final ImageView snakeHeadLeftView = new ImageView(snakeHeadLeft);
    private final Image snakeHeadUp = new
            Image(Objects.requireNonNull(getClass().getResource("/pics/snakeHeadUp.png")).toString());
    private final ImageView snakeHeadUpView = new ImageView(snakeHeadUp);
    private final Image snakeHeadDown = new
            Image(Objects.requireNonNull(getClass().getResource("/pics/snakeHeadDown.png")).toString());
    private final ImageView snakeHeadDownView = new ImageView(snakeHeadDown);
    private final Image snakeBodyImage = new
            Image(Objects.requireNonNull(getClass().getResource("/pics/snakeBody.png")).toString());
    private final ImageView snakeBodyView = new ImageView(snakeBodyImage);


    public enum Direction {
        UP, DOWN, RIGHT, LEFT
    }

    private Stage stage;
    private Game game;
    public Label usernameLabel;
    public Label scoreLabel;
    public Label lifeLabel;
    private Cell snakeHead;
    private final List<Cell> Cells = new ArrayList<>();
    private final ArrayList<Cell> snakeBody = new ArrayList<>();
    Fruit apple;
    Fruit banana = null;
    Fruit orange = null;
    private Direction direction;
    private int gameTicks;
    private double seconds = 0.18;
    Timeline mainTimeline;
    Timeline bananaSpawnTimeLine;
    Timeline bananaDeleteTimeLine;
    Timeline orangeSpawnTimeLine;
    Timeline orangeDeleteTimeLine;
    private boolean canChangeDirection;
    @FXML
    public GridPane gridPane;

    @FXML
    void start(MouseEvent event) {
        resetData();
        setFit();
        createFirstSnake();
        createFruit();
        playTimelines();

    }

    @FXML
    private void exit() {
        checkScore();
        Parent url = null;
        try {
            url = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Scene scene = new Scene(url);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    public void initialize() {
        drawBackGround();
        mainTimeline = new Timeline(new KeyFrame(Duration.seconds(seconds), e -> {
            Cells.add(new Cell(snakeHead.getColumn(), snakeHead.getRow()));
            moveSnakeHead(snakeHead);
            if (checkIfGameIsOver(snakeHead)) {
                game.decreaseSnakeHealth();
                lifeLabel.setText("LIFE: " + game.getSnakeHealth());
                stopTimelines();
                if (game.getSnakeHealth() == 0) {
                    exit();
                }

            } else {
                for (int i = 1; i < snakeBody.size(); i++) {
                    moveSnakeTail(snakeBody.get(i), i);
                }
                canChangeDirection = true;
                eatFruit();
                gameTicks++;
            }
        }));
        bananaSpawnTimeLine = new Timeline(new KeyFrame(Duration.seconds(45), e -> {
            if (banana == null) {
                banana = new Fruit(15, 15, gridPane, "banana");
                newFruitPosition(banana);
                bananaDeleteTimeLine.play();
                bananaSpawnTimeLine.stop();
            }
        }));
        bananaDeleteTimeLine = new Timeline(new KeyFrame(Duration.seconds(10), e -> {
            if (banana != null) {
                banana.removeFruit();
                banana = null;
                bananaSpawnTimeLine.play();
                bananaDeleteTimeLine.stop();
            }
        }));
        orangeSpawnTimeLine = new Timeline(new KeyFrame(Duration.seconds(30), e -> {
            if (orange == null) {
                orange = new Fruit(15, 15, gridPane, "orange");
                newFruitPosition(orange);
                orangeDeleteTimeLine.play();
                orangeSpawnTimeLine.stop();
            }
        }));
        orangeDeleteTimeLine = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
            if (orange != null) {
                orange.removeFruit();
                orange = null;
                orangeSpawnTimeLine.play();
                orangeDeleteTimeLine.stop();
            }
        }));

    }

    private void createFruit() {
        apple = new Fruit(2, 13, gridPane, "apple");
        apple.moveFruit();
        banana = null;
        orange = null;
    }

    private void createFirstSnake() {
        snakeHead = new Cell(10, 10);
        getPaneInMap(gridPane, snakeHead.getRow(), snakeHead.getColumn()).getChildren().add(snakeHeadRightView);
        Cell snakeTail = new Cell(snakeHead.getColumn() - 1, snakeHead.getRow());
        getPaneInMap(gridPane, snakeTail.getRow(), snakeTail.getColumn()).getChildren().add(snakeBodyView);
        snakeBody.add(snakeHead);
        snakeBody.add(snakeTail);
    }

    private void resetData() {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                Pane pane = getPaneInMap(gridPane, i, j);
                pane.getChildren().clear();
            }
        }
        gameTicks = 0;
        Cells.clear();
        snakeBody.clear();
        direction = Direction.RIGHT;
        canChangeDirection = true;
        seconds = game.getGameSpeed();
    }

    private void setFit() {
        snakeHeadRightView.setFitWidth(30);
        snakeHeadRightView.setFitHeight(30);
        snakeHeadLeftView.setFitWidth(30);
        snakeHeadLeftView.setFitHeight(30);
        snakeHeadUpView.setFitWidth(30);
        snakeHeadUpView.setFitHeight(30);
        snakeHeadDownView.setFitWidth(30);
        snakeHeadDownView.setFitHeight(30);
        snakeBodyView.setFitWidth(30);
        snakeBodyView.setFitHeight(30);
    }

    private void playTimelines() {
        mainTimeline.setCycleCount(Animation.INDEFINITE);
        mainTimeline.play();
        bananaSpawnTimeLine.setCycleCount(Animation.INDEFINITE);
        bananaSpawnTimeLine.play();
        bananaDeleteTimeLine.setCycleCount(Animation.INDEFINITE);
//        bananaDeleteTimeLine.play();
        orangeSpawnTimeLine.setCycleCount(Animation.INDEFINITE);
        orangeSpawnTimeLine.play();
        orangeDeleteTimeLine.setCycleCount(Animation.INDEFINITE);
//        orangeDeleteTimeLine.play();

    }

    private void stopTimelines() {
        mainTimeline.stop();
        bananaSpawnTimeLine.stop();
        bananaDeleteTimeLine.stop();
        orangeSpawnTimeLine.stop();
        orangeDeleteTimeLine.stop();
    }

    private void drawBackGround() {
        gridPane.getChildren().clear();
        gridPane.setGridLinesVisible(false);
        ArrayList<Pane> panes = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                Pane pane = new Pane();
                panes.add(pane);
                Pane pane2 = new Pane();
                pane2.setPrefWidth(15);
                pane2.setPrefHeight(15);
                pane2.setStyle("-fx-background-color: #a9d651;-fx-border-color: #a1d049; -fx-border-radius: 2;");
                gridPane.add(pane2, i, j);
            }
        }
    }

    private Pane getPaneInMap(GridPane gridPane, int row, int column) {
        for (Node child : gridPane.getChildren()) {
            if (GridPane.getRowIndex(child) == row && GridPane.getColumnIndex(child) == column)
                return (Pane) child;
        }
        return null;
    }

    @FXML
    void moveKeyPressed(KeyEvent event) {
        if (canChangeDirection) {
            if (event.getCode().equals(KeyCode.UP) && direction != Direction.DOWN) {
                direction = Direction.UP;
            } else if (event.getCode().equals(KeyCode.DOWN) && direction != Direction.UP) {
                direction = Direction.DOWN;
            } else if (event.getCode().equals(KeyCode.LEFT) && direction != Direction.RIGHT) {
                direction = Direction.LEFT;
            } else if (event.getCode().equals(KeyCode.RIGHT) && direction != Direction.LEFT) {
                direction = Direction.RIGHT;
            }
            canChangeDirection = false;
        }
    }

    private void moveSnakeHead(Cell snakeHead) {
        if (direction.equals(Direction.RIGHT)) {
            snakeHead.setColumn(snakeHead.getColumn() + 1);
            if (!checkIfGameIsOver(snakeHead)) {
                removeLastHead(snakeHead);
                getPaneInMap(gridPane, snakeHead.getRow(), snakeHead.getColumn()).getChildren().add(snakeHeadRightView);
            }
        } else if (direction.equals(Direction.LEFT)) {
            snakeHead.setColumn(snakeHead.getColumn() - 1);
            if (!checkIfGameIsOver(snakeHead)) {
                removeLastHead(snakeHead);
                getPaneInMap(gridPane, snakeHead.getRow(), snakeHead.getColumn()).getChildren().add(snakeHeadLeftView);
            }
        } else if (direction.equals(Direction.UP)) {
            snakeHead.setRow(snakeHead.getRow() - 1);
            if (!checkIfGameIsOver(snakeHead)) {
                removeLastHead(snakeHead);
                getPaneInMap(gridPane, snakeHead.getRow(), snakeHead.getColumn()).getChildren().add(snakeHeadUpView);
            }
        } else if (direction.equals(Direction.DOWN)) {
            snakeHead.setRow(snakeHead.getRow() + 1);
            if (!checkIfGameIsOver(snakeHead)) {
                removeLastHead(snakeHead);
                getPaneInMap(gridPane, snakeHead.getRow(), snakeHead.getColumn()).getChildren().add(snakeHeadDownView);
            }
        }
    }

    private void removeLastHead(Cell snakeHead) {
        getPaneInMap(gridPane, snakeHead.getRow(), snakeHead.getColumn()).getChildren().clear();
    }

    private void moveSnakeTail(Cell snakeTail, int tailNumber) {
        if (snakeTail.getRow() != -1)
            getPaneInMap(gridPane, snakeTail.getRow(), snakeTail.getColumn()).getChildren().clear();
        int passedCells = gameTicks - tailNumber + 1;
        if (passedCells < 0) passedCells = 0;
        int row = Cells.get(passedCells).getRow();
        int column = Cells.get(passedCells).getColumn();
        snakeTail.setRow(row);
        snakeTail.setColumn(column);
        ImageView snakeBodyView = new ImageView(snakeBodyImage);
        snakeBodyView.setFitWidth(30);
        snakeBodyView.setFitHeight(30);
        getPaneInMap(gridPane, row, column).getChildren().add(snakeBodyView);
    }

    private void addSnakeTail() {
        int column = -1;
        int row = -1;
        Cell snakeTail = new Cell(column, row);
        snakeBody.add(snakeTail);
    }

    public boolean checkIfGameIsOver(Cell snakeHead) {
        if (snakeHead.getColumn() > 19 || snakeHead.getColumn() < 0
                || snakeHead.getRow() < 0 || snakeHead.getRow() > 19) {
            return true;
        } else if (snakeHitItSelf()) {
            return true;
        }
        return false;
    }

    public boolean snakeHitItSelf() {
        int size = Cells.size() - 1;
        if (size > 3) {
            for (int i = size - snakeBody.size(); i < size; i++) {
                if (Cells.get(size).getColumn() == (Cells.get(i).getColumn())
                        && Cells.get(size).getRow() == (Cells.get(i).getRow())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void eatFruit() {
        if (snakeHead.getColumn() == apple.getCell().getColumn() && snakeHead.getRow() == apple.getCell().getRow()) {
            game.addScoreOfFruit(2);
            scoreLabel.setText("SCORE: " + game.getScore());
            newFruitPosition(apple);
            addSnakeTail();
        }
        if (banana != null)
            if (snakeHead.getColumn() == banana.getCell().getColumn() && snakeHead.getRow() == banana.getCell().getRow()) {
                game.addScoreOfFruit(-10);
                scoreLabel.setText("SCORE: " + game.getScore());
                banana.removeFruit();
                banana = null;
                bananaDeleteTimeLine.stop();
                bananaSpawnTimeLine.play();
                if (snakeBody.size() > 5) {
                    removeTail();
                }
            }
        if (orange != null)
            if (snakeHead.getColumn() == orange.getCell().getColumn() && snakeHead.getRow() == orange.getCell().getRow()) {
                game.addScoreOfFruit(5);
                scoreLabel.setText("SCORE: " + game.getScore());
                orange.removeFruit();
                orange = null;
                orangeDeleteTimeLine.stop();
                orangeSpawnTimeLine.play();
                addSnakeTail();
                addSnakeTail();
            }
    }

    private void removeTail() {
        ArrayList<Cell> removedCells = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Cell cell = snakeBody.get(snakeBody.size() - i);
            getPaneInMap(gridPane, cell.getRow(), cell.getColumn()).getChildren().clear();
            removedCells.add(cell);
        }
        for (Cell cell : removedCells) {
            snakeBody.remove(cell);
        }
    }

    private void newFruitPosition(Fruit fruit) {
        fruit.moveFruit();
        while (isFruitInsideSnake(fruit) || isFruitInsideOtherFruit(fruit)) {
            fruit.moveFruit();
        }
    }

    private boolean isFruitInsideSnake(Fruit fruit) {
        int size = Cells.size();
        if (size > 2) {
            for (int i = size - snakeBody.size(); i < size; i++) {
                if (fruit.getCell().getColumn() == (Cells.get(i).getColumn())
                        && fruit.getCell().getRow() == (Cells.get(i).getRow())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isFruitInsideOtherFruit(Fruit fruit) {
        if (banana != null && fruit != banana) {
            if (fruit.getCell().getColumn() == banana.getCell().getColumn()
                    && fruit.getCell().getRow() == banana.getCell().getRow())
                return true;
        }
        if (orange != null && fruit != orange) {
            if (fruit.getCell().getColumn() == orange.getCell().getColumn()
                    && fruit.getCell().getRow() == orange.getCell().getRow())
                return true;
        }
        if (fruit != apple) {
            if (fruit.getCell().getColumn() == apple.getCell().getColumn()
                    && fruit.getCell().getRow() == apple.getCell().getRow())
                return true;

        }

        return false;
    }

    public void checkScore() {
        if (game.getUsername().equals(""))
            return;
        User user = User.getUserByUsername(game.getUsername());

        if (game.getScore() > user.getScore()) {
            user.setScore(game.getScore());
            User.jsonUsers(user);
        }
    }

    public void setGame(Game game) {
        this.game = game;
        seconds = game.getGameSpeed();
        usernameLabel.setText("USERNAME: " + game.getUsername());
        scoreLabel.setText("SCORE: " + game.getScore());
        lifeLabel.setText("LIFE: " + game.getSnakeHealth());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
