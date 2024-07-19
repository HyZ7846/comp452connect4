package assign3.board;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.text.Text;

public class Connect4 extends Application {

    private static final int COLUMNS = 7;
    private static final int ROWS = 7; // Adjusted for 7x7 grid
    private static final int TILE_SIZE = 100;
    private int[] nextAvailableRow = new int[COLUMNS]; // Track the next available row for each column
    private Circle[][] grid = new Circle[ROWS][COLUMNS]; // Track the placed chips
    private GridPane gridPane = new GridPane();
    private boolean isPlayerOneTurn = true; // Track the current player
    private Text winText = new Text(); // Display the win message

    @Override
    public void start(Stage primaryStage) {
        // Initialize the next available row array
        for (int i = 0; i < COLUMNS; i++) {
            nextAvailableRow[i] = ROWS - 1; // Start from the bottom row
        }

        // Create the first row with clickable buttons
        for (int col = 0; col < COLUMNS; col++) {
            Button button = new Button(" ");
            button.setPrefSize(TILE_SIZE, TILE_SIZE);
            int finalCol = col; // Need a final variable for lambda expression
            button.setOnAction(event -> handleButtonClick(finalCol));
            gridPane.add(button, col, 0);
        }

        // Create the rest of the grid with rectangles
        for (int row = 1; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);
                tile.setFill(Color.LIGHTGRAY);
                tile.setStroke(Color.BLACK);
                gridPane.add(tile, col, row);
            }
        }

        gridPane.add(winText, 0, ROWS, COLUMNS, 1); // Add win text below the grid

        Scene scene = new Scene(gridPane, COLUMNS * TILE_SIZE, ROWS * TILE_SIZE + TILE_SIZE);
        primaryStage.setTitle("Connect4");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleButtonClick(int column) {
        int row = nextAvailableRow[column];
        if (row < 1) {
            // Column is full, do nothing
            System.out.println("Column is full");
            return;
        }

        // Create a chip for the current player
        Circle chip = new Circle(TILE_SIZE / 2 - 5);
        chip.setFill(isPlayerOneTurn ? Color.WHITE : Color.RED);
        gridPane.add(chip, column, row);
        grid[row][column] = chip;

        // Check for a win
        if (checkWin(row, column)) {
            String winningColor = isPlayerOneTurn ? "White" : "Red";
            winText.setText(winningColor + " wins the game!");
            winText.setFill(isPlayerOneTurn ? Color.WHITE : Color.RED);
            disableButtons(); // Disable buttons after a win
            return;
        }

        // Update the next available row for this column
        nextAvailableRow[column]--;

        // Switch turn
        isPlayerOneTurn = !isPlayerOneTurn;

        // Print the column and row for debugging
        System.out.println("Dropped " + (isPlayerOneTurn ? "White" : "Red") + " chip in column: " + column + ", row: " + row);
    }

    private boolean checkWin(int row, int column) {
        Color color = (Color) grid[row][column].getFill();
        return (checkDirection(row, column, 1, 0, color) + checkDirection(row, column, -1, 0, color) >= 3 ||
                checkDirection(row, column, 0, 1, color) + checkDirection(row, column, 0, -1, color) >= 3 ||
                checkDirection(row, column, 1, 1, color) + checkDirection(row, column, -1, -1, color) >= 3 ||
                checkDirection(row, column, 1, -1, color) + checkDirection(row, column, -1, 1, color) >= 3);
    }

    private int checkDirection(int row, int column, int dRow, int dCol, Color color) {
        int count = 0;
        int r = row + dRow;
        int c = column + dCol;
        while (r >= 0 && r < ROWS && c >= 0 && c < COLUMNS && grid[r][c] != null && grid[r][c].getFill().equals(color)) {
            count++;
            r += dRow;
            c += dCol;
        }
        return count;
    }

    private void disableButtons() {
        for (int i = 0; i < COLUMNS; i++) {
            Button button = (Button) gridPane.getChildren().get(i);
            button.setDisable(true);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
