package academy.mindswap.game;

import academy.mindswap.server.GameServer;
import academy.mindswap.server.messages.Messages;
import academy.mindswap.server.sounds.Sound;
import academy.mindswap.server.sounds.SoundFiles;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class responsible for creating a board of the game and saves the board positions.
 * Invoking the connectFourBoard will create a matrix 7x6.
 * Contains methods to place pieces on the board.
 * Checks for a winner or a draw and keeps updating the board for display.
 */
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

    /**
     * Fills the real board with ⬤ which represents an empty position.
     */
    public void fillEmptyBoard() {
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 6; y++) {
                this.board[x][y] = "⬤";
            }
        }
    }

    /**
     * Method to place a piece "R" or "Y" into the real board.
     * Also invokes the method that updates the display board.
     * Increments the number of plays tha has already been made.
     * @param move the current player piece "R"/"Y";
     * @param playerChoiceInput the column selected by the player
     */
    public void placePiece(String move, int playerChoiceInput) {
        if (checkWinner(move) || numberOfPlays >= 42) {
            return;
        }
        String colorChar = (numberOfPlays % 2 == 0) ? "R" : "Y";
        for (int y = 0; y < 6; y++) {
            if (board[playerChoiceInput][y].equals("⬤")) {
                board[playerChoiceInput][y] = colorChar;
                numberOfPlays++;
                updatePrettyBoard();
                break;
            }
        }
    }

    /**
     * Checks if the column selected by the player is already full.
     * @param playerChoiceInput the column selected by the player.
     * @return returns true if the column is already full or false if it still has any space left.
     */
    public boolean checkFullColumn(int playerChoiceInput) {
        return !board[playerChoiceInput][5].equals("⬤");
    }


    /** Method creates a String representation of the board that is displayed to the player.
     * This is not the real board. Transforms the real board to a display and prettier one.
     * and replaces the "Y" with a Yellow ⬤, the "R" with a Red ⬤ and "W" with a Green ⬤.
     * Creates a visual board with colored circles.
     * @return returns the display board to be displayed to the players.
     */
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

        prettyBoard = prettyBoard
                .replace("W", CharactersAndColors.CIRCLE_GREEN + CharactersAndColors.BLUE_BACKGROUND)
                .replace("R", CharactersAndColors.CIRCLE_RED + CharactersAndColors.BLUE_BACKGROUND)
                .replace("Y", CharactersAndColors.CIRCLE_YELLOW + CharactersAndColors.BLUE_BACKGROUND);
        return prettyBoard;
    }

    /** Checks if the game ended as draw.
     *  A draw is when the max number of moves is played (42) and neither player won.
     * @return true if it's a draw and false if not.
     */
    public boolean checkDraw() {
        return (numberOfPlays == 42);
    }


    /**
     * Checks if a winner was found right after the player places a piece.
     * @param playerTurn the piece of the player who just played (Y/R).
     * @return true if it has a winner (4 pieces of the same colour connected in any direction) or false if no winner was found.
     */
    public boolean checkWinner(String playerTurn) {
        int boardWidth = 7;
        int boardHeight = 6;

        if (checkHorizontalWin(playerTurn, boardWidth, boardHeight)) return true;
        if (checkVerticalWin(playerTurn, boardWidth, boardHeight)) return true;
        if (checkSlashDiagonalWin(playerTurn, boardWidth, boardHeight)) return true;
        if (checkBackSlashDiagonalWin(playerTurn, boardWidth, boardHeight)) return true;

        return false;
    }

    /**
     * Checks the Slash Diagonals for four connected pieces.
     * @param playerTurn the piece of the player who just played (Y/R).
     * @param boardWidth with of the board.
     * @param boardHeight height of the board.
     * @return
     */
    private boolean checkBackSlashDiagonalWin(String playerTurn, int boardWidth, int boardHeight) {
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

    /**
     * Checks the Back Slash Diagonals for four connected pieces.
     * @param playerTurn the piece of the player who just played (Y/R).
     * @param boardWidth with of the board.
     * @param boardHeight height of the board.
     * @return
     */
    private boolean checkSlashDiagonalWin(String playerTurn, int boardWidth, int boardHeight) {
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
        return false;
    }

    /**
     * Checks the columns for four connected pieces.
     * @param playerTurn the piece of the player who just played (Y/R).
     * @param boardWidth with of the board.
     * @param boardHeight height of the board.
     * @return
     */
    private boolean checkVerticalWin(String playerTurn, int boardWidth, int boardHeight) {
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
        return false;
    }

    /**
     * Checks the rows for four connected pieces.
     * @param playerTurn the piece of the player who just played (Y/R).
     * @param boardWidth with of the board.
     * @param boardHeight height of the board.
     * @return
     */
    private boolean checkHorizontalWin(String playerTurn, int boardWidth, int boardHeight) {
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
        return false;
    }


    public String getPrettyBoard() {
        return prettyBoard;
    }

    //TODO FEATURE: (Resets the number of plays when the game starts again)
    public void resetNumberOfPlays() {
        this.numberOfPlays = 0;
    }
}
