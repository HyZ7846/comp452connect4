module assign3.board {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens assign3.board to javafx.fxml;
    exports assign3.board;
}