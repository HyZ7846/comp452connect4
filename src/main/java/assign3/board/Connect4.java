package assign3.board;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Connect4 extends Application {

    private static final int COLUMNS = 7;
    private static final int ROWS = 7; // Adjusted for 7x7 grid
    private static final int TILE_SIZE = 100;
    private static final int DEPTH_CUTOFF = 8; // Depth cutoff for minimax
    private int[] nextAvailableRow = new int[COLUMNS]; // Track the next available row for each column
    private Circle[][] grid = new Circle[ROWS][COLUMNS]; // Track the placed chips
    private GridPane gridPane = new GridPane();
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

        // Style and add win text below the grid
        winText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        winText.setFill(Color.BLACK);
        gridPane.add(winText, 0, ROWS, COLUMNS, 1);

        Scene scene = new Scene(gridPane, COLUMNS * TILE_SIZE, ROWS * TILE_SIZE + TILE_SIZE);
        primaryStage.setTitle("Connect4");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Handle the button click to place a white chip and trigger the computer's move
    private void handleButtonClick(int column) {
        int row = nextAvailableRow[column];
        if (row < 1) {
            // Column is full, do nothing
            System.out.println("Column is full");
            return;
        }

        // Create a white chip
        Circle chip = new Circle((double) TILE_SIZE / 2);
        chip.setFill(Color.WHITE);
        gridPane.add(chip, column, row);
        grid[row][column] = chip;

        // Check for a win
        if (checkWin(row, column, Color.WHITE)) {
            winText.setText("White wins the game!");
            disableButtons(); // Disable buttons after a win
            return;
        }

        // Update the next available row for this column
        nextAvailableRow[column]--;

        // Print the column and row for debugging
        System.out.println("Dropped White chip in column: " + column + ", row: " + row);

        // Computer's turn to place a black chip
        computerMove();
    }

    // Computer's move using minimax with alpha-beta pruning
    private void computerMove() {
        int bestColumn = -1;
        int bestValue = Integer.MIN_VALUE;
        for (int col = 0; col < COLUMNS; col++) {
            int row = nextAvailableRow[col];
            if (row >= 1) {
                grid[row][col] = new Circle((double) TILE_SIZE / 2, Color.BLACK);
                nextAvailableRow[col]--;
                int moveValue = minimax(0, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
                grid[row][col] = null;
                nextAvailableRow[col]++;
                if (moveValue > bestValue) {
                    bestValue = moveValue;
                    bestColumn = col;
                }
            }
        }

        if (bestColumn != -1) {
            int row = nextAvailableRow[bestColumn];
            Circle chip = new Circle((double) TILE_SIZE / 2);
            chip.setFill(Color.BLACK);
            gridPane.add(chip, bestColumn, row);
            grid[row][bestColumn] = chip;
            nextAvailableRow[bestColumn]--;

            if (checkWin(row, bestColumn, Color.BLACK)) {
                winText.setText("Black wins the game!");
                disableButtons(); // Disable buttons after a win
                return;
            }
        }
    }

    // Minimax algorithm with alpha-beta pruning
    private int minimax(int depth, boolean isMaximizing, int alpha, int beta) {
        if (depth == DEPTH_CUTOFF) {
            return evaluateBoard();
        }

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int col = 0; col < COLUMNS; col++) {
                int row = nextAvailableRow[col];
                if (row >= 1) {
                    grid[row][col] = new Circle((double) TILE_SIZE / 2, Color.BLACK);
                    nextAvailableRow[col]--;
                    int eval = minimax(depth + 1, false, alpha, beta);
                    grid[row][col] = null;
                    nextAvailableRow[col]++;
                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, eval);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int col = 0; col < COLUMNS; col++) {
                int row = nextAvailableRow[col];
                if (row >= 1) {
                    grid[row][col] = new Circle((double) TILE_SIZE / 2, Color.WHITE);
                    nextAvailableRow[col]--;
                    int eval = minimax(depth + 1, true, alpha, beta);
                    grid[row][col] = null;
                    nextAvailableRow[col]++;
                    minEval = Math.min(minEval, eval);
                    beta = Math.min(beta, eval);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return minEval;
        }
    }

    // Evaluate the board state
    private int evaluateBoard() {
        int score = 0;
        for (int row = 1; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                if (grid[row][col] != null) {
                    Color color = (Color) grid[row][col].getFill();
                    if (color == Color.BLACK) {
                        score += evaluatePosition(row, col, color);
                    } else if (color == Color.WHITE) {
                        score -= evaluatePosition(row, col, color);
                    }
                }
            }
        }
        return score;
    }

    // Evaluate the position by counting sequences of 2, 3, and 4 consecutive chips
    private int evaluatePosition(int row, int column, Color color) {
        return (countConsecutive(row, column, 1, 0, color) +
                countConsecutive(row, column, 0, 1, color) +
                countConsecutive(row, column, 1, 1, color) +
                countConsecutive(row, column, 1, -1, color));
    }

    // Count consecutive chips and assign different weights
    private int countConsecutive(int row, int column, int dRow, int dCol, Color color) {
        int score = 0;
        int count = 0;
        for (int i = -3; i <= 3; i++) {
            int r = row + i * dRow;
            int c = column + i * dCol;
            if (r >= 0 && r < ROWS && c >= 0 && c < COLUMNS && (grid[r][c] != null && grid[r][c].getFill().equals(color))) {
                count++;
            } else {
                if (count == 2) score += 10;
                else if (count == 3) score += 50;
                else if (count >= 4) score += 1000;
                count = 0;
            }
        }
        if (count == 2) score += 10;
        else if (count == 3) score += 50;
        else if (count >= 4) score += 1000;
        return score;
    }

    // Check for a win in any direction
    private boolean checkWin(int row, int column, Color color) {
        return (checkDirection(row, column, 1, 0, color) + checkDirection(row, column, -1, 0, color) >= 3 ||
                checkDirection(row, column, 0, 1, color) + checkDirection(row, column, 0, -1, color) >= 3 ||
                checkDirection(row, column, 1, 1, color) + checkDirection(row, column, -1, -1, color) >= 3 ||
                checkDirection(row, column, 1, -1, color) + checkDirection(row, column, -1, 1, color) >= 3);
    }

    // Count consecutive chips in a specific direction
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

    // Disable all buttons after a win
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
