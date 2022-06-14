package view.game;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import model.Cell;

import java.util.Objects;
import java.util.Random;

public class Fruit {
    private Cell position;
    private Random random = new Random();
    private GridPane gridPane;
    private final Image apple = new
            Image(Objects.requireNonNull(getClass().getResource("/pics/apple.png")).toString());
    private final Image banana = new
            Image(Objects.requireNonNull(getClass().getResource("/pics/banana.png")).toString());
    private final Image orange = new
            Image(Objects.requireNonNull(getClass().getResource("/pics/orange.png")).toString());
    private ImageView imageView;


    public Fruit(int xPos, int yPos, GridPane gridPane, String fruitType) {
        this.gridPane = gridPane;
        position = new Cell(xPos, yPos);

        if (fruitType == "apple") {
            imageView = new ImageView(apple);
            imageView.setFitWidth(30);
            imageView.setFitHeight(30);
            getPaneInMap(gridPane, position.getRow(), position.getColumn()).getChildren().add(imageView);
        }
        if (fruitType == "banana") {
            imageView = new ImageView(banana);
            imageView.setFitWidth(30);
            imageView.setFitHeight(30);
            getPaneInMap(gridPane, position.getRow(), position.getColumn()).getChildren().add(imageView);
        }
        if (fruitType == "orange") {
            imageView = new ImageView(orange);
            imageView.setFitWidth(30);
            imageView.setFitHeight(30);
            getPaneInMap(gridPane, position.getRow(), position.getColumn()).getChildren().add(imageView);
        }

    }

    public Cell getCell() {
        return position;
    }

    public void moveFruit() {
        removeFruit();
        int positionX = random.nextInt(20);
        int positionY = random.nextInt(20);
        position.setColumn(positionX);
        position.setRow(positionY);
        getPaneInMap(gridPane, position.getRow(), position.getColumn()).getChildren().add(imageView);
    }

    public void removeFruit() {
        getPaneInMap(gridPane, position.getRow(), position.getColumn()).getChildren().remove(imageView);
    }

    private Pane getPaneInMap(GridPane gridPane, int row, int column) {
        for (Node child : gridPane.getChildren()) {
            if (GridPane.getRowIndex(child) == row && GridPane.getColumnIndex(child) == column)
                return (Pane) child;
        }
        return null;
    }
}
