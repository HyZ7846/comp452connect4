module assign3.board {
    requires javafx.controls;
    requires javafx.fxml;


    opens assign3.board to javafx.fxml;
    exports assign3.board;
}