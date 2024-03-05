package Controllers;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Random;

public class TicTacToeGame extends Application {

    private static final int SIZE = 3;
    private static final String EMPTY = " ";
    private static final String PLAYER_X = "X";
    private static final String PLAYER_O = "O";
    private String currentPlayer = PLAYER_X;
    private Button[][] buttons = new Button[SIZE][SIZE];

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        GridPane gridPane = new GridPane();
        root.getChildren().add(gridPane);

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Button button = new Button(EMPTY);
                button.setMinSize(100, 100);
                button.setOnAction(event -> {
                    if (button.getText().equals(EMPTY)) {
                        button.setText(PLAYER_X);
                        if (isWinner(PLAYER_X)) {
                            showAlert("Félicitations !", "Vous avez gagné !");
                            resetGame();
                        } else if (isBoardFull()) {
                            showAlert("Match nul !", "La grille est pleine. Match nul !");
                            resetGame();
                        } else {
                            computerMove();
                            if (isWinner(PLAYER_O)) {
                                showAlert("Dommage !", "L'ordinateur a gagné !");
                                resetGame();
                            } else if (isBoardFull()) {
                                showAlert("Match nul !", "La grille est pleine. Match nul !");
                                resetGame();
                            }
                        }
                    }
                });
                buttons[row][col] = button;
                gridPane.add(button, col, row);
            }
        }

        gridPane.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tic Tac Toe");
        primaryStage.show();
    }

    private boolean isWinner(String player) {
        String[][] board = new String[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                board[row][col] = buttons[row][col].getText();
            }
        }

        // Vérifier les lignes
        for (int i = 0; i < SIZE; i++) {
            if (!board[i][0].equals(EMPTY) && board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2]) && board[i][0].equals(player)) {
                return true;
            }
        }

        // Vérifier les colonnes
        for (int i = 0; i < SIZE; i++) {
            if (!board[0][i].equals(EMPTY) && board[0][i].equals(board[1][i]) && board[1][i].equals(board[2][i]) && board[0][i].equals(player)) {
                return true;
            }
        }

        // Vérifier les diagonales
        if (!board[0][0].equals(EMPTY) && board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2]) && board[0][0].equals(player)) {
            return true;
        }
        if (!board[0][2].equals(EMPTY) && board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0]) && board[0][2].equals(player)) {
            return true;
        }

        return false;
    }

    private boolean isBoardFull() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (buttons[row][col].getText().equals(EMPTY)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void resetGame() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                buttons[row][col].setText(EMPTY);
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void computerMove() {
        Random rand = new Random();
        int row, col;
        do {
            row = rand.nextInt(SIZE);
            col = rand.nextInt(SIZE);
        } while (!buttons[row][col].getText().equals(EMPTY));
        buttons[row][col].setText(PLAYER_O);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
