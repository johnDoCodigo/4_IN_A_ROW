package academy.mindswap.game;

import academy.mindswap.server.GameServer;
import academy.mindswap.server.messages.Messages;
import academy.mindswap.server.sounds.Sound;
import academy.mindswap.server.sounds.SoundFiles;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectFourBoard {
    private String[][] board;
    private String prettyBoard;
    private int numberOfPlays;

    public ConnectFourBoard() {
        this.board = new String[7][6];
        fillEmptyBoard();
        updatePrettyBoard();
        this.numberOfPlays = 0;
    }

    public void fillEmptyBoard() {
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 6; y++) {
                this.board[x][y] = "⬤";
            }
        }
    }

    public void placePiece(String move, int playerChoiceInput) {
        if (!checkWinner(move) && numberOfPlays < 42) {
            for (int y = 0; y < 6; y++) {
                if (board[playerChoiceInput][y].equals("⬤")) {
                    if (numberOfPlays % 2 == 0) {
                        board[playerChoiceInput][y] = "R";
                        break;
                    } else {
                        board[playerChoiceInput][y] = "Y";
                        break;
                    }
                }
            }
            numberOfPlays++;
            updatePrettyBoard();
        }
    }

    public boolean checkFullColumn(int playerChoiceInput) {
        if (board[playerChoiceInput][5].equals("⬤")) {
            return false;
        }
        return true;
    }



    public String updatePrettyBoard() {
        prettyBoard = "\n" +
                CharactersAndColors.BLUE_BACKGROUND + "|----+----+----+----+----+----+----|" + CharactersAndColors.RESET + "\n" +
                CharactersAndColors.BLUE_BACKGROUND + "| " + board[0][5] + " | " + board[1][5] + " | " + board[2][5] + " | " + board[3][5] + " | " + board[4][5] + " | " + board[5][5] + " | " + board[6][5] + " |" + CharactersAndColors.RESET + "\n" +
                CharactersAndColors.BLUE_BACKGROUND + "| " + board[0][4] + " | " + board[1][4] + " | " + board[2][4] + " | " + board[3][4] + " | " + board[4][4] + " | " + board[5][4] + " | " + board[6][4] + " |" + CharactersAndColors.RESET + "\n" +
                CharactersAndColors.BLUE_BACKGROUND + "| " + board[0][3] + " | " + board[1][3] + " | " + board[2][3] + " | " + board[3][3] + " | " + board[4][3] + " | " + board[5][3] + " | " + board[6][3] + " |" + CharactersAndColors.RESET + "\n" +
                CharactersAndColors.BLUE_BACKGROUND + "| " + board[0][2] + " | " + board[1][2] + " | " + board[2][2] + " | " + board[3][2] + " | " + board[4][2] + " | " + board[5][2] + " | " + board[6][2] + " |" + CharactersAndColors.RESET + "\n" +
                CharactersAndColors.BLUE_BACKGROUND + "| " + board[0][1] + " | " + board[1][1] + " | " + board[2][1] + " | " + board[3][1] + " | " + board[4][1] + " | " + board[5][1] + " | " + board[6][1] + " |" + CharactersAndColors.RESET + "\n" +
                CharactersAndColors.BLUE_BACKGROUND + "| " + board[0][0] + " | " + board[1][0] + " | " + board[2][0] + " | " + board[3][0] + " | " + board[4][0] + " | " + board[5][0] + " | " + board[6][0] + " |" + CharactersAndColors.RESET + "\n" +
                CharactersAndColors.BLUE_BACKGROUND + "|----+----+----+----+----+----+----+" + CharactersAndColors.RESET + "\n" +
                CharactersAndColors.BLUE_BACKGROUND + "|" + CharactersAndColors.RESET + "  0    1    2    3    4    5    6 " + CharactersAndColors.BLUE_BACKGROUND + "|" + CharactersAndColors.RESET;

        prettyBoard = prettyBoard.replace("W", CharactersAndColors.CIRCLE_GREEN + CharactersAndColors.BLUE_BACKGROUND);
        prettyBoard = prettyBoard.replace("R", CharactersAndColors.CIRCLE_RED + CharactersAndColors.BLUE_BACKGROUND);
        prettyBoard = prettyBoard.replace("Y", CharactersAndColors.CIRCLE_YELLOW + CharactersAndColors.BLUE_BACKGROUND);
        return prettyBoard;
    }

    public boolean checkDraw() {
        if (numberOfPlays == 42) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkWinner(String playerTurn) {
        int boardWidth = 7;
        int boardHeight = 6;

        // horizontalCheck
        for (int j = 0; j < boardHeight - 3; j++) {
            for (int i = 0; i < boardWidth; i++) {
                if (this.board[i][j] == playerTurn && this.board[i][j + 1] == playerTurn && this.board[i][j + 2] == playerTurn && this.board[i][j + 3] == playerTurn) {
                    this.board[i][j] = "W";
                    this.board[i][j + 1] = "W";
                    this.board[i][j + 2] = "W";
                    this.board[i][j + 3] = "W";
                    updatePrettyBoard();
                    return true;
                }
            }
        }
        // verticalCheck
        for (int i = 0; i < boardWidth - 3; i++) {
            for (int j = 0; j < boardHeight; j++) {
                if (this.board[i][j] == playerTurn && this.board[i + 1][j] == playerTurn && this.board[i + 2][j] == playerTurn && this.board[i + 3][j] == playerTurn) {
                    this.board[i][j] = "W";
                    this.board[i + 1][j] = "W";
                    this.board[i + 2][j] = "W";
                    this.board[i + 3][j] = "W";
                    updatePrettyBoard();
                    return true;
                }
            }
        }
        // slashDiagonalCheck
        for (int i = 3; i < boardWidth; i++) {
            for (int j = 0; j < boardHeight - 3; j++) {
                if (this.board[i][j] == playerTurn && this.board[i - 1][j + 1] == playerTurn && this.board[i - 2][j + 2] == playerTurn && this.board[i - 3][j + 3] == playerTurn) {
                    this.board[i][j] = "W";
                    this.board[i - 1][j + 1] = "W";
                    this.board[i - 2][j + 2] = "W";
                    this.board[i - 3][j + 3] = "W";
                    updatePrettyBoard();
                    return true;
                }
            }
        }
        // backslashDiagonalCheck
        for (int i = 3; i < boardWidth; i++) {
            for (int j = 3; j < boardHeight; j++) {
                if (this.board[i][j] == playerTurn && this.board[i - 1][j - 1] == playerTurn && this.board[i - 2][j - 2] == playerTurn && this.board[i - 3][j - 3] == playerTurn) {
                    this.board[i][j] = "W";
                    this.board[i - 1][j - 1] = "W";
                    this.board[i - 2][j - 2] = "W";
                    this.board[i - 3][j - 3] = "W";
                    updatePrettyBoard();
                    return true;
                }
            }
        }
        return false;
    }

    public void gameOver(String winner) {
        Sound sound = new Sound();
        sound.getSoundClip(SoundFiles.GAME_OVER.getPath());
        if (winner == "R") {
            System.out.println(Messages.PLAYER1_WIN);
        } else {
            System.out.println(Messages.PLAYER2_WIN);
        }
    }

    public String getPrettyBoard() {
        return prettyBoard;
    }

    public void resetNumberOfPlays() {
        this.numberOfPlays = 0;
    }
}
