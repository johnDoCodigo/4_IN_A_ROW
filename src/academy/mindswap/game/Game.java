package academy.mindswap.game;

/*

TO DO LIST:

* playAgain();? //JP

* GAME METHODS:
** placePiece(); //FILIPE
** checkWin(); //RUI
** checkDraw(); //SUSANA

*SERVER
** Create a game instance //SUSANA
** Communication with Server/Client (receive and send I/O to server) //RUI
** Communication with Server/Game (receive and send I/O to server) //JP

*/

public class Game {

    public static void main(String[] args) {

        Game game1 = new Game();


        //FILL BOARD WITH O's
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                game1.board[i][j] = "O";
            }
        }

        game1.updatePrettyBoard();
        System.out.println(game1.prettyBoard);

        //VERIFICATIONS

        //CHECK FOR HORIZONTAL WIN
        System.out.println("-".repeat(20) + "CHECK FOR HORIZONTAL WIN"+"-".repeat(20));

        for (int i = 0; i < 7; i++) {  //CLEAN BOARD WITH O's
            for (int j = 0; j < 6; j++) {
                game1.board[i][j] = " ";
            }
        }

        game1.board[2][3] = "@";
        game1.board[3][3] = "@";
        game1.board[4][3] = "@";
        game1.board[5][3] = "@";
        game1.checkWinner("@");
        System.out.println(game1.updatePrettyBoard());
        System.out.println("Game winner? " + game1.checkWinner("@"));

        //CHECK FOR VERTICAL WIN
        System.out.println("-".repeat(20) + "CHECK FOR VERTICAL WIN"+"-".repeat(20));
        for (int i = 0; i < 7; i++) {  //CLEAN BOARD WITH O's
            for (int j = 0; j < 6; j++) {
                game1.board[i][j] = "O";
            }
        }
        game1.board[3][2] = "@";
        game1.board[3][3] = "@";
        game1.board[3][4] = "@";
        game1.board[3][5] = "@";

        System.out.println("Game winner? " + game1.checkWinner("@"));
        System.out.println(game1.updatePrettyBoard());



        //CHECK FOR SLASH WIN /
        System.out.println("-".repeat(20) + "CHECK FOR SLASH WIN /"+"-".repeat(20));
        for (int i = 0; i < 7; i++) {  //CLEAN BOARD WITH O's
            for (int j = 0; j < 6; j++) {
                game1.board[i][j] = " ";
            }
        }
        game1.board[1][2] = "@";
        game1.board[2][3] = "@";
        game1.board[3][4] = "@";
        game1.board[4][5] = "@";

        System.out.println("Game winner? " + game1.checkWinner("@"));
        System.out.println(game1.updatePrettyBoard());

        //CHECK FOR BACKSLASH WIN \
        System.out.println("-".repeat(20) + "CHECK FOR BACKSLASH WIN "+"-".repeat(20));
        for (int i = 0; i < 7; i++) {  //CLEAN BOARD WITH O's
            for (int j = 0; j < 6; j++) {
                game1.board[i][j] = " ";
            }
        }
        game1.board[5][2] = "@";
        game1.board[4][3] = "@";
        game1.board[3][4] = "@";
        game1.board[2][5] = "@";

        System.out.println("Game winner? " + game1.checkWinner("@"));
        System.out.println(game1.updatePrettyBoard());


    }


    String player1piece = "@";
    String player2piece = "#";
    String[][] board;
    String prettyBoard;
    int numberOfPlays;

    public Game() {
        this.board = new String[7][6];
        fillEmptyBoard();
        this.numberOfPlays = 0;
    }

    private void fillEmptyBoard() {
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 6; y++) {
                this.board[x][y] = " ";
            }
        }
    }

    public void placePiece(int x) {
        //check if possition already has a String, if not, place it on first empty Y.
        //for ... board[out][i]
        //else return and player must choose another X


        updatePrettyBoard();
        //checkWinner();
        checkDraw();
    }

    private String updatePrettyBoard() {
        prettyBoard = "|---|---|---|---|---|---|---|\n" +
                "| " + board[0][5] + " | " + board[1][5] + " | " + board[2][5] + " | " + board[3][5] + " | " + board[4][5] + " | " + board[5][5] + " | " + board[6][5] + " |\n" +
                "| " + board[0][4] + " | " + board[1][4] + " | " + board[2][4] + " | " + board[3][4] + " | " + board[4][4] + " | " + board[5][4] + " | " + board[6][4] + " |\n" +
                "| " + board[0][3] + " | " + board[1][3] + " | " + board[2][3] + " | " + board[3][3] + " | " + board[4][3] + " | " + board[5][3] + " | " + board[6][3] + " |\n" +
                "| " + board[0][2] + " | " + board[1][2] + " | " + board[2][2] + " | " + board[3][2] + " | " + board[4][2] + " | " + board[5][2] + " | " + board[6][2] + " |\n" +
                "| " + board[0][1] + " | " + board[1][1] + " | " + board[2][1] + " | " + board[3][1] + " | " + board[4][1] + " | " + board[5][1] + " | " + board[6][1] + " |\n" +
                "| " + board[0][0] + " | " + board[1][0] + " | " + board[2][0] + " | " + board[3][0] + " | " + board[4][0] + " | " + board[5][0] + " | " + board[6][0] + " |\n" +
                "|---+---+---+---+---+---+---+\n" +
                "| 0   1   2   3   4   5   6 |\n";


        prettyBoard = prettyBoard.replace("W","\033[0;32mX\033[0m");
        return prettyBoard;
    }

    private void checkDraw() {
        //if
    }

    private boolean checkWinner(String playerTurn) {
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
                    return true;
                }
            }
        }
        // verticalCheck
        for (int i = 0; i < boardWidth - 3; i++) {
            for (int j = 0; j < boardHeight; j++) {
                if (this.board[i][j] == playerTurn && this.board[i + 1][j] == playerTurn && this.board[i + 2][j] == playerTurn && this.board[i + 3][j] == playerTurn) {
                    return true;
                }
            }
        }
        // slashDiagonalCheck
        for (int i = 3; i < boardWidth; i++) {
            for (int j = 0; j < boardHeight - 3; j++) {
                if (this.board[i][j] == playerTurn && this.board[i - 1][j + 1] == playerTurn && this.board[i - 2][j + 2] == playerTurn && this.board[i - 3][j + 3] == playerTurn)
                    return true;
            }
        }
        // backslashDiagonalCheck
        for (int i = 3; i < boardWidth; i++) {
            for (int j = 3; j < boardHeight; j++) {
                if (this.board[i][j] == playerTurn && this.board[i - 1][j - 1] == playerTurn && this.board[i - 2][j - 2] == playerTurn && this.board[i - 3][j - 3] == playerTurn)
                    return true;
            }
        }
        return false;
    }
}

