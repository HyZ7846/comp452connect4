# Connect4 Game

## Description

Connect4 is a classic two-player connection game in which the players take turns dropping colored chips from the top into a vertically suspended grid. The objective of the game is to connect four of one's own chips in a row, either vertically, horizontally, or diagonally, before the opponent does.

This JavaFX implementation allows a human player to compete against a computer-controlled opponent. The human player always places white chips, while the computer places black chips using an optimized Minimax algorithm with alpha-beta pruning.

## Features

- **7x7 Grid:** The game board is a 7x7 grid with the top row reserved for clickable buttons to drop chips.
- **Human vs Computer:** The human player plays with white chips, and the computer plays with black chips.
- **Minimax Algorithm:** The computer uses the Minimax algorithm with alpha-beta pruning to decide its moves.
- **Depth Cutoff:** The depth of the game tree search is limited to 8 levels to balance performance and effectiveness.
- **Winning Detection:** The game detects and announces the winner when either player connects four chips in a row.
- **Evaluation Heuristic:** The evaluation function considers sequences of 2, 3, and 4 consecutive chips, assigning different weights to them.

## How to Play

1. **Start the Game:** Launch the game by running the `Connect4` class.
2. **Make a Move:** Click on any button in the top row to drop a white chip into the corresponding column.
3. **Computer's Turn:** After your move, the computer will automatically place a black chip.
4. **Win Condition:** The game checks for a win after each move and announces the winner when four chips are connected.

## Requirements

- Java Development Kit (JDK) 8 or later
- JavaFX library

## Running the Game

1. **Compile the Code:**

```javac assign3/board/Connect4.java```

2. **Run the Game:**

```java assign3.board.Connect4```


## Code Overview

- `Connect4`: The main class that sets up the game board and handles user interactions.
- `handleButtonClick`: Handles the player's move and checks for a win.
- `computerMove`: Uses the Minimax algorithm to determine the best move for the computer.
- `minimax`: Implements the Minimax algorithm with alpha-beta pruning.
- `evaluateBoard` & `evaluatePosition`: Evaluate the board state to determine the best move for the computer.
- `checkWin`: Checks for a win condition.
- `disableButtons`: Disables all buttons when the game is won.

