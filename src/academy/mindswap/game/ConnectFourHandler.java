package academy.mindswap.game;

import academy.mindswap.server.GameServer;
import academy.mindswap.server.commands.Command;
import academy.mindswap.server.messages.Messages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectFourHandler implements Runnable {
    private final GameServer.PlayerConnectionHandler player1;
    private final GameServer.PlayerConnectionHandler player2;
    private final ConnectFourBoard board;
    private String move;
    public static volatile boolean isGameEnded;
    private volatile boolean isGameStarted;
    private boolean checkIfGameCanStart = false;

    private List<GameServer.PlayerConnectionHandler> listOfBoardPlayers = new ArrayList<>();

    public ConnectFourHandler(GameServer.PlayerConnectionHandler player1, GameServer.PlayerConnectionHandler player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = new ConnectFourBoard();
        this.move = "R";
        isGameEnded = false;
        isGameStarted = false;
        listOfBoardPlayers.add(player1);
        listOfBoardPlayers.add(player2);
    }

    @Override
    public void run() {
        GameServer.PlayerConnectionHandler currentPlayer = player1;
        GameServer.PlayerConnectionHandler notPlayingPlayer = player2;

        ReentrantLock lockNotYourTurnInput = new ReentrantLock();

        //Waits a bit so that players can read the instructions.
        try {
            Thread.sleep(1500);
            broadcast("GAME STARTS IN: " + 3);
            Thread.sleep(1500);
            broadcast("GAME STARTS IN: " + 2);
            Thread.sleep(1500);
            broadcast("GAME STARTS IN: " + 1);
            Thread.sleep(1500);
            broadcast(Messages.START_GAME);
            Thread.sleep(3000);


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //GAME  LOGIC
        while (!isGameEnded) {
            if (checkIfGameCanStart() && !isGameStarted) {
                startGame();
            }

            if (isGameStarted && !isGameEnded) {
                //Broadcasts current player turn
                lockNotYourTurnInput.lock();
                currentPlayer.send(currentPlayer.getName() + ", it's your turn!");
                notPlayingPlayer.send(notPlayingPlayer.getName() + ", you must wait for your turn.");
                lockNotYourTurnInput.unlock();

                //Get player turn input and checks from valid input
                Integer playerMove = null;
                while (playerMove == null || board.checkFullColumn(playerMove)) {
                    currentPlayer.send("Enter a number between 0 and 6:");
                    String input = null;
                    try {
                        input = currentPlayer.in.readLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    if (input.matches("[0-6]")) {
                        playerMove = Integer.parseInt(input);

                        if (board.checkFullColumn(playerMove)) {
                            currentPlayer.send(Messages.COLUMN_FULL);
                        }

                    } else {
                        currentPlayer.send("Invalid input.");
                    }
                }

                //Place current player move
                board.placePiece(getMove(), playerMove);

                //Checks for winner
                if (board.checkWinner(getMove())) {
                    broadcast(currentPlayer.getName() + " HAS WON THE GAME!");
                    broadcast(board.getPrettyBoard());
                    if (currentPlayer == player1) {
                        broadcast(Messages.PLAYER1_WIN);
                    } else {
                        broadcast(Messages.PLAYER2_WIN);
                    }
                    break;
                }

                //Checks for draw
                if (board.checkDraw()) {
                    broadcast(board.getPrettyBoard());
                    broadcast(Messages.CHECK_DRAW);
                    break;
                }

                //Broadcasts the board with the new move
                broadcast(board.getPrettyBoard());

                //Switch move and player.
                switchMove();
                currentPlayer = (currentPlayer == player1) ? player2 : player1;
                notPlayingPlayer = (notPlayingPlayer == player2) ? player1 : player2;
            }
        }
        broadcast(Messages.PLAY_AGAIN_OR_QUIT);
        //TODO FEATURE: Play again or quit
        /*
        while (true) {
            String firstPlayer = null;
            String secondPlayer = null;

            try {
                firstPlayer = currentPlayer.in.readLine();
                secondPlayer = currentPlayer.in.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (firstPlayer.equals("/playagain") && secondPlayer.equals("/playagain")) {
                playAgain();
                broadcast("A new game is about to start...");
                startGame();
                break;
            } else {
                break;
            }
        }
         */
    }

    //TODO FEATURE: Play again
    public void playAgain() {
        board.fillEmptyBoard();
        board.updatePrettyBoard();
        board.resetNumberOfPlays();
        isGameStarted = true;
        isGameEnded = false;
    }

    public void startGame() {
        isGameStarted = true;
        broadcast(board.getPrettyBoard());
    }

    public synchronized void broadcast(String message) {
        listOfBoardPlayers.stream()
                .forEach(player -> player.send(message));
    }

    private synchronized boolean checkIfGameCanStart() {
        return checkIfGameCanStart;
    }

    public synchronized boolean theGameCanStart() {
        this.checkIfGameCanStart = true;
        return true;
    }

    public String getMove() {
        return move;
    }

    public void switchMove() {
        move = (move == "R") ? "Y" : "R";
    }
}
